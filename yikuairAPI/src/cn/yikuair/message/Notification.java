package cn.yikuair.message;

import java.util.HashMap;
import java.util.Map;

import javapns.back.PushNotificationManager;
import javapns.back.SSLConnectionHelper;
import javapns.data.Device;
import javapns.data.PayLoad;

import org.apache.log4j.Logger;

import cn.yikuair.base.Log;
import cn.yikuair.beans.NotificationBean;
import cn.yikuair.utils.DataUtil;
import cn.yikuair.utils.JsonUtil;

import com.baidu.yun.channel.auth.ChannelKeyPair;
import com.baidu.yun.channel.client.BaiduChannelClient;
import com.baidu.yun.channel.model.PushUnicastMessageRequest;
import com.baidu.yun.channel.model.PushUnicastMessageResponse;
import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;

public class Notification {
	private static Logger logger = Log.getLogger(Notification.class);
	public static void sendIosNotification(String deviceToken,String message,int badge){
		if(null==deviceToken||deviceToken.equals("")) return;
		 try{
			 logger.debug("send offline");
			 PayLoad payLoad = new PayLoad();
			 payLoad.addAlert(message);
			 payLoad.addBadge(badge);
			 payLoad.addSound("default");
			 
			 byte []b = DataUtil.decodeBase64(deviceToken);
			 deviceToken = DataUtil.Bytes2HexString(b);
			 
			 PushNotificationManager pushManager = PushNotificationManager.getInstance();
			 pushManager.addDevice("iPhone", deviceToken);
			 
			 //String host= "gateway.sandbox.push.apple.com";
			 String host = "gateway.push.apple.com";
			 int port = 2195;
			 String certificatePath= Notification.class.getResource("/conf/push.p12").getPath();
			 logger.debug("certificatePath:"+certificatePath);
			 String certificatePassword= "12345";
			 pushManager.initializeConnection(host,port, certificatePath,certificatePassword, SSLConnectionHelper.KEYSTORE_TYPE_PKCS12);
			 
			 //push  i phone
			 Device client = pushManager.getDevice("iPhone");
			 pushManager.sendNotification(client, payLoad);
			 pushManager.stopConnection();
			 pushManager.removeDevice("iPhone");
			 
		 } catch(Exception ex){
			 ex.printStackTrace();
		 }
	}
	
//	public static  void pushAndroidNotification(String deviceToken,String message,int badge){
//		
//		logger.debug("android push data:"+message+"...  deviceToken::"+deviceToken);
//		
//		String apiKey = "lR2FnRxnxhLfbhYD0bCOF6qA";//"SZVBSQIfxjOUfUt8pQRH0KBx";
//		String secretKey = "MGumIT399c3c17GSOQNAfMO1X8AeGN7e";//"OsVf9HDTSuPa6PTs8rGXCKN4oPEySxGG";
//		ChannelKeyPair pair = new ChannelKeyPair(apiKey, secretKey);
//		
//		BaiduChannelClient channelClient = new BaiduChannelClient(pair);
//		channelClient.setChannelLogHandler(new YunLogHandler() {
//			@Override
//			public void onHandle(YunLogEvent event) {
//				logger.debug("t:"+event.getMessage());
//			}
//		});
//		
//		try{
//			//PushBroadcastMessageRequest request = new PushBroadcastMessageRequest();
//			PushUnicastMessageRequest request = new PushUnicastMessageRequest();
//			request.setDeviceType(3);	// device_type => 1: web 2: pc 3 :android 4:ios 5:wp
//			request.setMessage(message);
//			request.setUserId(deviceToken);	
//			
//			
//			PushUnicastMessageResponse response = channelClient.pushUnicastMessage(request);
//			
//			logger.debug("push amount : " + response.getSuccessAmount()); 
//		} catch(Exception ex){
//			ex.printStackTrace();
//		}
//	}
	
	public static void pushAndroidNotification(String deviceToken,String message,int badge,String from,String type,String messageType){
		if(null==deviceToken||deviceToken.equals("")) return;
		String apiKey = "lR2FnRxnxhLfbhYD0bCOF6qA";//"SZVBSQIfxjOUfUt8pQRH0KBx";
		String secretKey = "MGumIT399c3c17GSOQNAfMO1X8AeGN7e";//"OsVf9HDTSuPa6PTs8rGXCKN4oPEySxGG";
		ChannelKeyPair pair = new ChannelKeyPair(apiKey, secretKey);
		BaiduChannelClient channelClient = new BaiduChannelClient(pair);
		channelClient.setChannelLogHandler(new YunLogHandler() {
			@Override
			public void onHandle(YunLogEvent event) {
				System.out.println("event::"+event.getMessage());
			}
		});
		
		try{
			logger.debug("deviceToken::"+deviceToken);
			String  []ss = deviceToken.split("_");
			if(ss.length<2) return;
			String user_id = ss[0];
			Long longId = new Long(ss[1]);
			
			
			
			PushUnicastMessageRequest request = new PushUnicastMessageRequest();
			request.setDeviceType(3);	// device_type => 1: web 2: pc 3:android 4:ios 5:wp		
			request.setChannelId(longId);	
			request.setUserId(user_id);	 
			request.setMessageType(1);

			
			
			NotificationBean bean = new NotificationBean();
			bean.setTitle("通知");
			bean.setDescription(message);
			
			Map <String,String>map = new HashMap<String,String>();
			map.put("token", messageType);
			map.put("type", type);
			map.put("from", from);
			
			bean.setCustom_content(map);
			
//			bean.setToken(messageType);
//			bean.setType(type);
//			bean.setFrom(from);
			System.out.println(JsonUtil.ObjectToJsonString(bean));
			String s = JsonUtil.ObjectToJsonString(bean);
			request.setMessage(s);
			request.setMsgKey(s);
			//request.setMessage(message);
			PushUnicastMessageResponse response = channelClient.pushUnicastMessage(request);
			System.out.println("push amount : " + response.getSuccessAmount()); 
		} catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	
	public static void main(String[] args) {
		String deviceToken = "vPfLISBjMAbYE/Z3PnSFXQ3tJEnPiB+xV2OwBLc9g7Q=";//"PDpn56qGTX5tx/RrC9QO1DCzPjMOyzJhXTBOQktj0DA=";//
//		byte []b = DataUtil.decodeBase64(deviceToken);
//		deviceToken = DataUtil.Bytes2HexString(b);
//		logger.debug(deviceToken);
		//d2dedf70 07e60cc0 c12f0cda 52ae2d06 e8c3bff0 d880f6bd d7b045e3 93da32fa
		Notification.sendIosNotification(deviceToken, "中午吃饭了", 2);
		
		//String deviceToken = "840849833945539839_3702534168146537272";//"840849833945539839";
		//String deviceToken = "973539912792297113_3904456420671494812";
		//String deviceToken = "840849833945539839_3904456420671494812";//"840849833945539839";
		//String deviceToken = "973539912792297113";//"587084440905503819";//"SZVBSQIfxjOUfUt8pQRH0KBx";
		//Notification.pushAndroidNotification(deviceToken ,"hellosss3", 0,"1","2","3");
//		Notification.pushAndroid_(deviceToken ,"hello3", 0);
		
	}
}
