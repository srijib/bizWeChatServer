package cn.yikuair.message;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import cn.yikuair.base.Log;
import cn.yikuair.beans.LogMsg;
import cn.yikuair.beans.Message;
import cn.yikuair.beans.TokenSocketBean;
import cn.yikuair.business.MessageManager;
import cn.yikuair.business.StarManager;
import cn.yikuair.business.UserGroupManager;
import cn.yikuair.business.UserManager;
import cn.yikuair.dao.BaseDao;
import cn.yikuair.utils.BoolUtil;
import cn.yikuair.utils.DataUtil;
import cn.yikuair.utils.JsonUtil;
import cn.yikuair.wechat.task.Save;

public class SocketThread extends Thread{
	
	private static Logger logger = Log.getLogger(SocketThread.class);
	private Socket socket = null;
	private byte[] buf = new byte[32];
	
	public SocketThread(Socket socket){
		this.socket = socket;
	}
	

	
	public static void closeSocket(Socket socket){
		try{
			if(!socket.isClosed()){
				logger.debug("ready close...");
				socket.close();
			}
			logger.debug("当前用户socket已经退出");
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static void serviceToClient(Socket socket,String content){
		try{
			OutputStream out = socket.getOutputStream();
			logger.debug("content::"+content);
			out.write(content.getBytes());
			out.write(SocketConfig.WRIETEFLAGBYTES);
			out.flush();
		} catch(Exception ex){
			logger.debug("不在线:"+content);
			closeSocket(socket);
			ex.printStackTrace();
		}
	}
	
	public static void logOut(String old_id){
		TokenSocketBean bean = SocketConfig.socketBeanMap.get(old_id);
		if(bean!=null){
			Socket _oldSocket = bean.getSocket();
			if(!_oldSocket.isClosed()){
				logger.debug("exit");
				serviceToClient(_oldSocket,"{\"code\":403,\"message\":\"该用户已经通过其他设备登录过,请检测是否安全息\",\"token\":0}");
			}
			closeSocket(_oldSocket);
			SocketConfig.socketBeanMap.remove(old_id);
		}
	}
	
	public static void pushMessage(String id,String message,String from,String type,String messageType){
		logger.debug("push message");
		MessageManager manager = MessageManager.getMessageManager();
		Map <String,String> map = manager.addNotification(id);
		String countString = map.get("count");
		if(null==countString||countString.equals("")) countString = "1";
		int count = Integer.parseInt(countString);
		logger.debug("count::"+count);
		String token = map.get("token");
		String machinetype = map.get("machinetype");
		if(token!=null&&!token.equals("")){
			if(machinetype==null){
				logger.debug("非法登陆，找不到匹配的机型");
			} else if(machinetype.equals("1")){
				Notification.sendIosNotification(token, message, count);
			} else if(machinetype.equals("0")){
				//android  没有消息机制
				Notification.pushAndroidNotification(token, message, count,from,type,messageType);
				logger.debug("android 机器:"+id+"--"+machinetype +"---"+ token + message+"---"+count+"---"+from+"---"+type+"---"+messageType);
			} else { 
				logger.debug("该账户从未登陆过，或者是未知机型");
			}
			
		}
	}
	
	public static void getSocketToMessage(String to,String reciveMessage,String messageToken,String from,String type){
		
		logger.info("...to..."+to);
		logger.info("...reciveMessage..."+reciveMessage);
		logger.info("...messageToken..."+messageToken);
		logger.info("...from..."+from);
		logger.info("...type..."+type);
		
		
		if(null!=type&&type.equals("4")){
			PushMessageThread pmt = new PushMessageThread(to,"系统消息",from,type,messageToken);
			pmt.start();
			return;
		} 
		String []tokenArr = {"1","2","3","13","14","18","22"};
		boolean tokenBoolean = BoolUtil.checkStringArray(messageToken, tokenArr);
		//Map <String,TokenSocketBean>map = new HashMap<String,TokenSocketBean>();
		//map = SocketConfig.socketBeanMap;
		TokenSocketBean bean = SocketConfig.socketBeanMap.get(to);
		//TokenSocketBean bean = map.get(to);
		
//		logger.debug("bean ::"+bean+"socket size::"+SocketConfig.socketBeanMap.size()+"--map address:"+SocketConfig.socketBeanMap);
//		Set<String> set = SocketConfig.socketBeanMap.keySet();
//		for (String key : set) {
//			//循环取出了你map里面的值然后再调用你的sql方法想怎么存就怎么存
//			logger.debug(key+" = "+SocketConfig.socketBeanMap.get(key));
//		}
		
		
		JSONObject json = JsonUtil.StringToObject(reciveMessage);
		
		
		String unreadMessage = "你有未读消息";
		String messageFromName = "消息";
		if(json.has("token")){
			String token = json.getString("token");
			//{"content":"fffc","filePath":"","from":"5","longDate":"1381151745508",
			//"msguuid":"1C2B1763-DD02-4026-A976-429C5E905592-5","smallImgPath":"","to":"4","token":"1","type":"1"}
			if(json.has("fromName")&&json.getString("fromName")!=null && !json.getString("fromName").equals("")){
				messageFromName = json.getString("fromName");
				messageFromName = new String(DataUtil.decodeBase64(messageFromName));
			} else {
				if(json.has("from")){
					UserManager userManager = UserManager.getUserManager();
					String pushName = json.getString("from");
					if(token.equals("14")){
						if(json.has("to")){
							pushName = json.getString("to");
						}
					}
					messageFromName = userManager.getNameById(pushName);
				}
				
			}
			if(token.equals("1")){
				String s = json.getString("content");
		
				s = new String(DataUtil.decodeBase64(s));
				//s = DataUtil.ecodeBase64(s.getBytes());
				if(s.length()>30){
					s = s.substring(0,30)+"...";
				}
				
				unreadMessage = messageFromName+"：“"+s+"”";
			} else if(token.equals("2")){
				unreadMessage = messageFromName+"发来一张图片";
			} else if(token.equals("3")){
				unreadMessage = messageFromName+"发来一段语音";
			} else if(token.equals("13")){
				if(type.equals("1")){//1会议，2，任务
					unreadMessage = messageFromName+"发来一个会议";
				} else if(type.equals("2")){
					unreadMessage = messageFromName+"发来一个任务";
				} else {
					unreadMessage = messageFromName+"发来一个待办日程";
				}
				//unreadMessage = "会议";
				
			} else if(token.equals("14")){
				if(type.equals("1")){//1会议，2，任务
					unreadMessage = messageFromName+"发来一个会议确认";
				} else if(type.equals("2")) {
					unreadMessage = messageFromName+"发来一个任务确认";
				} else {
					unreadMessage = messageFromName+"发来一个待办确认";
				}
				
			} else if(token.equals("18")){
				unreadMessage = messageFromName+"加好友";
			} else if(token.equals("22")){
				unreadMessage = messageFromName+"发来地理位置";
			}
		}
		
		logger.debug("---"+bean+unreadMessage);
		
		if(bean!=null){
			logger.debug("接收消息的人在线："+to);
			Socket reciveSocket = bean.getSocket();
			//检测socket是否断开
			boolean isClose = reciveSocket.isClosed();
			logger.debug("接收消息的人状态："+isClose);
			if(!isClose){
				logger.debug("发送消息"+reciveMessage);
				SocketThread.serviceToClient(reciveSocket,reciveMessage);
			} else {
				logger.debug("socket 已经断开，socket id="+to);
				SocketConfig.socketBeanMap.remove(to);
				//add
				
				if(tokenBoolean){
					PushMessageThread pmt = new PushMessageThread(to,unreadMessage,from,type,messageToken);
					pmt.start();
				}
				//pushMessage(to,unreadMessage,from,type,messageToken);
			}
		} else {
			logger.debug("push"+to+"--"+unreadMessage+"--"+from+"--"+type+"--"+messageToken);
			//add
			if(tokenBoolean){
				PushMessageThread pmt = new PushMessageThread(to,unreadMessage,from,type,messageToken);
				pmt.start();
			}
			//pushMessage(to,unreadMessage,from,type,messageToken);
		}
	}
	
	
	

	
	
	public  void socketMessage(Socket socket,String content){
		
		//logger.debug("20131007::"+content);
		
		if(content==null||content.equals("")){
			logger.debug("接收的socket消息为空："+content);
			return;
		} 
		
		
		
		//对content判断是否为多条消息，可以用WRITEFILG 检索下
		JSONObject json = JsonUtil.StringToObject(content.toString());
		MessageManager manager = MessageManager.getMessageManager();
		UserManager userManager = UserManager.getUserManager();
		BaseDao dao = BaseDao.getBaseDao();
		if(json.has("token")){
			String token = json.getString("token");
			if(token.equals(SocketConfig.TOKEN_LOGIN)||token.equals(SocketConfig.TOKEN_SHAKE)){//用户第一次登录和握手
				JSONObject uidObj = json.getJSONObject("uid");
				String username = uidObj.getString("username");
				String password = uidObj.getString("password");
				String devicetoken = uidObj.getString("devicetoken");
				String device = uidObj.getString("device");
				if(token.equals(SocketConfig.TOKEN_LOGIN)){
					String login_message = userManager.login(username, password, devicetoken, device);
					serviceToClient(socket,login_message);
				} else {
					Map <String,String> map = dao.getFistRow("select * from t_userinfo where username='"+username+"' and password ='"+password+"'");
					if(map==null||map.size()==0){
						serviceToClient(socket,"{\"code\":404,\"message\":\"连接验证失败,请重新再试\",\"token\":0}");
					} 
					String user_id = map.get("id");
					//检测
					String sqlToken = map.get("token");
					logger.debug("sqlToken:"+sqlToken+" devicetoken:"+devicetoken);
					if(!(sqlToken!=null&&sqlToken.equals(devicetoken))){//机器的标志唯一，宁外一台手机登陆
						logger.debug("该用户已经通过其他设备登录过,请检测是否安全息");
						serviceToClient(socket,"{\"code\":403,\"message\":\"该用户已经通过其他设备登录过,请检测是否安全息\",\"token\":0}");
						closeSocket(socket);
						return ;
					} 
					TokenSocketBean tokenSocketBean = new TokenSocketBean();
					tokenSocketBean.setUser_id(user_id);
					tokenSocketBean.setSocket(socket);
					tokenSocketBean.setLongDate(new Date().getTime());
					tokenSocketBean.setDevice(device);
					tokenSocketBean.setUsername(username);
					tokenSocketBean.setPassword(password);
					
					
					SocketConfig.socketBeanMap.put(user_id, tokenSocketBean);
					
					LogMsg logMsg = new LogMsg();
					logMsg.setCode(200);
					logMsg.setMessage("验证通过,更新消息");
					logMsg.setToken(token);
					
					Map <String,String> _map = new HashMap<String,String>();
					if("E10ADC3949BA59ABBE56E057F20F883E".equals(password)){
						_map.put("updatepwd","0");
					} else {
						_map.put("updatepwd","1");
					}
					logMsg.setData(_map);
					
					logger.debug("reply success"+socket.isClosed()+"----"+user_id);
//					serviceToClient(socket,"{\"code\":200,\"message\":\"验证通过,更新消息\",\"token\":0}");
					serviceToClient(socket,JsonUtil.ObjectToJsonString(logMsg));
					
					
					
					//清除推送消息
					manager.clearNotification(user_id);
					
					//读取离线消息
					
					logger.debug("update message");
					
					List <String>contentList = manager.getUnreadMessage(user_id);
					for(int i=0,j=(contentList!=null?contentList.size():0);i<j;i++){
						serviceToClient(socket,contentList.get(i));
					}
					
					//当离线时候，发送的消息收信者已经接收 service告诉发送者已经发送成功 status=3 
					List <Map<String,String>> uuidList = manager.setMsgStatus(3,user_id);	
					for(int i=0,j=uuidList.size();i<j;i++){
						Map<String,String> uuidMap = uuidList.get(i);
						Message message = new Message();
						message.setTo(uuidMap.get("to"));
						message.setFrom(uuidMap.get("from"));
						message.setType(uuidMap.get("type"));
						message.setMsguuid(uuidMap.get("msguuid"));
						message.setOffline("1");
						message.setLongDate(new Date().getTime()+"");
						String backString = this.sendBackMsg("7",message);
						serviceToClient(socket,backString);
						//修改数据库为 status 5
						
						manager.saveComfirmMsg(5,message);
					}
					
					
					
					//当离线时候，发送的消息收信者已经读到 service告诉发送者已经发送成功 status=4
					List <Map<String,String>> uuidList2 = manager.setMsgStatus(4,user_id);
					for(int i=0,j=uuidList2.size();i<j;i++){
						Map<String,String> uuidMap = uuidList2.get(i);
						Message message = new Message();
						message.setTo(uuidMap.get("to"));
						message.setFrom(uuidMap.get("from"));
						message.setType(uuidMap.get("type"));
						message.setOffline("1");
						message.setLongDate(new Date().getTime()+"");
						message.setMsguuid(uuidMap.get("msguuid"));
						String backString = this.sendBackMsg("8",message);
						serviceToClient(socket,backString);
						//修改数据库为 status 6
						manager.saveComfirmMsg(6,message);
					}

					
						
				}
				
			} else if(token.equals("1")||token.equals(SocketConfig.TOKEN_MAPADDRESS)){//文本消息
				
				logger.debug("1 socket size::"+SocketConfig.socketBeanMap.size());
				
				Message message = this.setMessage(json);
				String to  = message.getTo();
				//是否已经接受过该消息，如果已经接受过，就给c1发送确认消息，就不执行以下操作。
				
				String sql = "select count(*) as count from t_messagelog where msguuid='"+message.getMsguuid()+"'";
				int count = dao.getCount(sql);
				String backString = this.sendBackMsg("6",message);//告诉发送者消息服务器已经接收
				if(count>0){
					logger.debug("该消息已经发送成功。"+message.getContent()+"online count:"+SocketConfig.socketBeanMap.size()+" object="+SocketConfig.socketBeanMap.hashCode());
					serviceToClient(socket,backString);
					return;
				}
				String type = message.getType();
				String from =  message.getFrom();
				String reciveMessage = JsonUtil.ObjectToJsonString(message);
				logger.debug("debug::"+reciveMessage+6);
				
				serviceToClient(socket,backString);
				
				//logger.debug("test 1.1 ::"+SocketConfig.socketBeanMap.size());
				
				//socket 发送消息
				if(type.equals("1")){
					//将聊天记录保存到数据库
					manager.setMessage(message,reciveMessage);
					//,from,type,messageType
					SocketThread.getSocketToMessage(to, reciveMessage,token,from,type);
				} else if(type.equals("2")){
					//发送消息
					//查找msguuid对应的用户id
					logger.debug("群组2");
					
					//需要判断该用户是否在这个群组里面，如果没有在这个里面 ，就应该返回
					
					List <String>ids = userManager.getGroupUsers(to);
					
					//
					
					//保存消息
					manager.setMessages(message, reciveMessage, ids);
					//发送消息
					for(int i=0,j=ids.size();i<j;i++){
						if(ids.get(i).equals(message.getFrom())) continue;
						SocketThread.getSocketToMessage(ids.get(i), reciveMessage,token,to,type);
					}
				} else if(type.equals("3")){
					logger.debug("系统消息");
					List <String>ids = userManager.getCoWorkers(to);
					//保存消息
					manager.setMessages(message, reciveMessage, ids);
					//发送消息
					for(int i=0,j=ids.size();i<j;i++){
						if(ids.get(i).equals(message.getFrom()))continue;
						else SocketThread.getSocketToMessage(ids.get(i), reciveMessage,token,to,type);
					}
				} else if(type.equals("4")){//明星账户
					
					StarManager starManager = StarManager.getStarManager();
					logger.info("satr 4:"+reciveMessage);
					starManager.setMessage(message, reciveMessage);
					String callbackurl= starManager.getCallback(to);
					if(!callbackurl.equals("")){
						String username = userManager.getuserNameById(from);
						cn.yikuair.wechat.star.Msg m = new cn.yikuair.wechat.star.Msg();
						m.getCallbackMessage(username,message.getMsguuid(),callbackurl);
					}
				}
				
//				
//				Set<String> set = SocketConfig.socketBeanMap.keySet();
//				for (String key : set) {
//					//循环取出了你map里面的值然后再调用你的sql方法想怎么存就怎么存
//					logger.debug("消息：：："+key+" = "+SocketConfig.socketBeanMap.get(key));
//				}
				
			} else if(token.equals(SocketConfig.TOKEN_MESSAGE_IMAGE)||token.equals(SocketConfig.TOKEN_MESSAGE_VOICE)){//图片,声音
				Message message = this.setMessage(json);
				String type = message.getType();
				String reciveMessage = JsonUtil.ObjectToJsonString(message);
				String to = message.getTo();
				String from = message.getFrom();
				if(type.equals("1")){
					SocketThread.getSocketToMessage(to, reciveMessage,token,from,type);
				} else if(type.equals("2")){
					List<String> ids = userManager.getGroupUsers(to);
					for(int i=0,j=ids.size();i<j;i++){
						if(ids.get(i).equals(message.getFrom())) continue;
						logger.debug("reciveMessage::"+reciveMessage);
						SocketThread.getSocketToMessage(ids.get(i), reciveMessage,token,to,type);
					}
				} else if(type.equals("3")){
					logger.debug("系统消息");
					List <String>ids = userManager.getCoWorkers(to);
					//保存消息
					manager.setMessages(message, reciveMessage, ids);
					//发送消息
					for(int i=0,j=ids.size();i<j;i++){
						if(ids.get(i).equals(message.getFrom()))continue;
						SocketThread.getSocketToMessage(ids.get(i), reciveMessage,token,to,type);
					}
				} else if(type.equals("4")){
					StarManager starManager = StarManager.getStarManager();
					starManager.setMessage(message, reciveMessage);
					String callbackurl= starManager.getCallback(to);
					if(!callbackurl.equals("")){
						String username = userManager.getuserNameById(from);
						cn.yikuair.wechat.star.Msg m = new cn.yikuair.wechat.star.Msg();
						m.getCallbackMessage(username,message.getMsguuid(),callbackurl);
					}
				}
			} else if(token.equals("4")){//视频
				
			} else if(token.equals("5")){//心跳包
				String user_id = json.getString("user_id");
				TokenSocketBean bean = SocketConfig.socketBeanMap.get(user_id);
				if(bean!=null){//更新心跳包
					bean.setLongDate(new Date().getTime());
				}
			} else if(token.equals("7")){// c2 告诉 s 已经收到
				Message message = this.setMessage(json);
				manager.saveComfirmMsg(3,message);
				String from = message.getFrom();
				String type = message.getType();
//				if(type.equals("1")){//单人
//					logger.debug("token 7");
//					String backString = this.sendBackMsg("7",message);
//					SocketThread.getSocketToMessage(from, backString,token,from,type);
//				} else {
//					
//				}
				String backString = this.sendBackMsg("7",message);
				SocketThread.getSocketToMessage(from, backString,token,from,type);
			} else if(token.equals("8")){//c2 告诉 s 已读 {'token':8,type:'1',from:'',to:'',msguuid:''} 
				Message message = this.setMessage(json);
				manager.saveComfirmMsg(4,message);
				String from = message.getFrom();
				String type = message.getType();
				if(type.equals("1")||type.equals("2")){
					//单人聊天 
					logger.debug("token 8");
					String backString = this.sendBackMsg("8",message);
					SocketThread.getSocketToMessage(from, backString,token,from,type);
				} else if(type.equals("3")){//系统消息
					
				}
				
				//if type=2 就是群消息返回的 就不给发消息的用户反馈
				
			} else if(token.equals("9")){ // c1 知道 c2 已经接收  （c2 给 服务器发 token9）
				Message message = this.setMessage(json);
				manager.saveComfirmMsg(5,message);//s 确认  c1已经确认c2已收到
			} else if (token.equals("10")){// c1 知道 c2 已读     （c2给服务器发  token10）
				//System.out.println("token::"+10);
				Message message = this.setMessage(json);
				manager.saveComfirmMsg(6,message);//s 确认  c1已经确认c2已读
			} else if(token.equals(SocketConfig.TOKEN_USERINFO_SAVE)){// 用户资料修改  12
				
				logger.debug("12 into ");
				
				//String from = json.getString("from");
				userManager.userSave(json);
				//int count = 
//				if(count>0){
//					Message message = new Message();
//					message.setFrom(from);
//					message.setToken("12");
//					message.set
//					message.setLongDate(new Date().getTime()+"");
					
					LogMsg msg = new LogMsg();
					msg.setCode(200);
					msg.setToken(SocketConfig.TOKEN_USERINFO_SAVE);
					msg.setMessage("success");
					Map <String,String> map = new HashMap<String,String>();
					map.put("msguuid", json.getString("msguuid"));
					msg.setData(map);
					String s = JsonUtil.ObjectToJsonString(msg);
					logger.debug("msg::"+s);
					serviceToClient(socket,s);
//				}
			} else if(token.equals(SocketConfig.TOKEN_ADD_TASK)){//13
				Save save  = Save.getSave();
				String callString = save.addMetting(json);
				
				logger.debug("callString::"+callString);
				
				serviceToClient(socket,callString);
			} else if(token.equals(SocketConfig.TOKEN_COMFIRM_TASK)){
				Save save  = Save.getSave();
				String callString = save.mettingComfirm(json);
				logger.debug("task_comfirm::"+callString);
				serviceToClient(socket,callString);
			}else if(token.equals(SocketConfig.TOKEN_REMVOE_TASK)){
				//{"token":23,from:'',to:'',task_id:'',type:'',msguuid:''} //from 用户id（创建者），to为群组id ， task_id ,任务id
				Save save = Save.getSave();
				boolean b = save.removeTask(json);
				LogMsg msg = new LogMsg();
				Message message = this.setMessage(json);
				String msguuid = message.getMsguuid();
				String type = message.getType();
				String to = message.getTo();
				if(b){

					String jsonString = JsonUtil.ObjectToJsonString(json);
					List <String>ids = new ArrayList<String>();
					if(type.equals("2")){
						ids  = userManager.getGroupUsers(to);
					} else {
						ids.add(to);
					}
					
					manager.setMessages(message, jsonString, ids);
					
					//检查token
					  for(int i=0,j=ids.size();i<j;i++){
						  //给在线的人发送task消息
						  SocketThread.getSocketToMessage(ids.get(i), jsonString,SocketConfig.TOKEN_ADD_TASK,msguuid,type);
					  }
					
					
				}
				

				String s = this.sendBackMsg(SocketConfig.TOKEN_C1_COMFIRM_S_RECEIVE, message);
				
				
				//Map <String,String>map = new HashMap<String,String>();
				//map.put("msguuid", message.getMsguuid());
				//msg.setData(map);
				//msg.setToken(SocketConfig.TOKEN_C1_COMFIRM_S_RECEIVE);
				//String s = JsonUtil.ObjectToJsonString(msg);
				
				
				serviceToClient(socket,s);
				
			}else if(token.equals(SocketConfig.TOKEN_LOGOUT)||token.equals(SocketConfig.TOKEN_IN_BG)){
				String user_id = json.getString("user_id");
				TokenSocketBean bean = SocketConfig.socketBeanMap.get(user_id);
				Socket _socket = bean.getSocket();
				closeSocket(_socket);
				SocketConfig.socketBeanMap.remove(user_id);
				if(token.equals(SocketConfig.TOKEN_LOGOUT)){
					manager.closeNotification(user_id);
				}
			}else if(token.equals(SocketConfig.TOKEN_SAVE_GROUPS)){//15 创建群组
				UserGroupManager userGroupManager = UserGroupManager.getUserGroupManager();
				int status = userGroupManager.saveGroup(json);
				Message message = this.setMessage(json);
				LogMsg logMsg = new LogMsg();
				logMsg.setToken(SocketConfig.TOKEN_SAVE_GROUPS);
				Map<String,Object> map = new HashMap<String,Object>();
				String msguuid = json.getString("msguuid");
				map.put("msguuid", msguuid);
				logMsg.setCode(200);
				if(status==0){//error
					logMsg.setCode(500);
					logMsg.setMessage("数据错误");
				} else if(status==-1){//修改群组状态
					logMsg.setMessage("修改群组状态成功");
				} else{// create group 
					
					map.put("group_id", status+"");
					logMsg.setMessage("创建群组成功");
				} 
				logMsg.setData(map);
				
				
				String from = message.getFrom();
				
				//logger.debug("from::"+status+"--"+from);
				
				if(status==-1){// 修改
					String groupId = json.getString("group_id");
					List <Map<String,String>>list = userGroupManager.getGroupUsers(groupId);
					List <String>l  = new ArrayList<String>();
					String r = "{\"token\":"+token+",\"from\":"+groupId+",\"msguuid\":\""+msguuid+"\",\"author\":"+from+",\"flag\":1}";//修改
					
					for(int i=0,j=list.size();i<j;i++){
						Map <String,String> umap = list.get(i);
						String u_id = umap.get("user_id");
						l.add(u_id);
					}
					
					manager.setMessages(message, r, l);
					
					for(int i=0,j=l.size();i<j;i++){
						//Map <String,String> umap = list.get(i);
						String u_id = l.get(i);//umap.get("user_id");
						
						SocketThread.getSocketToMessage(u_id, r,token,from,null);
					}
				} else if(status!=0) {
					String idString = json.getString("ids");
					logger.debug("ids::"+idString);
					String []ids = idString.split(",");
					logger.debug("add group ids:"+ids);
					List <String>l  = new ArrayList<String>();
					for(int i=0,j=ids.length;i<j;i++){
						l.add(ids[i]);
					}
					String r  ="{\"token\":"+token+",\"msguuid\":\""+msguuid+"\",\"from\":"+status+",\"author\":"+from+",\"flag\":2}";//创建
					logger.debug("setMessages::"+r);
					manager.setMessages(message, r, l);
					//logger.debug("11111111111--"+ids.length);
					for(int i =0,j=ids.length;i<j;i++){
						//logger.debug("rrrr:"+i);
						SocketThread.getSocketToMessage(ids[i],r ,token,from,null);
					}
					//logger.debug("ttttttt");
				}
				//logger.debug("comfirm");
				String s  = JsonUtil.ObjectToJsonString(logMsg);
				logger.debug("s::"+s);
				serviceToClient(socket,s);
			} else if(token.equals(SocketConfig.TOKEN_ADD_GROUPMEMBER)) {//add group member
				//{token:16,from:'',to:'',ids:'',msguuid:''}//from 谁发起的，to 群组id  , ids是加进来的人 
				String to = json.getString("to");
				String from = json.getString("from");
				UserGroupManager userGroupManager = UserGroupManager.getUserGroupManager();
				userGroupManager.addGroupMember(json);
				List <Map<String,String>>list = userGroupManager.getGroupUsers(to);
				
				List <String>l = new ArrayList<String>();
				if(list!=null&&list.size()>0){
					for(int i=0,j=list.size();i<j;i++){
						l.add(list.get(i).get("user_id"));
					}
				}
				
				
				
				logger.debug("add_group member:"+list.size());
				Message message = this.setMessage(json);
				message.setToken(SocketConfig.TOKEN_ADD_GROUPMEMBER);
				message.setType("2");
				//List <String>users = new ArrayList<String>();
				//String group_add_string = "{\"from\":"++"}";
				String reciveMessage = JsonUtil.ObjectToJsonString(message);
				
				manager.setMessages(message, reciveMessage, l);
				
				for(int i=0,j=l.size();i<j;i++){
					//Map <String,String> map = list.get(i);
					String u_id = l.get(i);//map.get("user_id");
					
					logger.debug(u_id+"add group member:"+reciveMessage);
					SocketThread.getSocketToMessage(u_id, reciveMessage,token,from,"2");
					logger.debug("is success");
					//users.add(u_id);
					
					
				}
				LogMsg logMsg = new LogMsg();
				logMsg.setCode(200);
				logMsg.setToken(token);
				logMsg.setMessage("success");
				Map <String,String>logMap = new HashMap<String,String>();
				logMap.put("msguuid", message.getMsguuid());
				logMsg.setData(logMap);
				String s = JsonUtil.ObjectToJsonString(logMsg);
				logger.debug("add group return from:"+s);
				serviceToClient(socket,s);
				
			} else if(token.equals(SocketConfig.TOKEN_REMOVE_GROUPMEMBER)){//remove group member
				//{token:17,from:'',to:'',ids:'',msguuid:''}//from 谁发起的，to 群组id  , ids是需要移除的人 
				String to = json.getString("to");
				String from = json.getString("from");
				UserGroupManager userGroupManager = UserGroupManager.getUserGroupManager();
				
				
				List <Map<String,String>>list = userGroupManager.getGroupUsers(to);
				userGroupManager.removeGroupMember(json);
				
				List <String>l = new ArrayList<String>();
				if(list!=null&&list.size()>0){
					for(int i=0,j=list.size();i<j;i++){
						l.add(list.get(i).get("user_id"));
					}
				}
				
				
				Message message = this.setMessage(json);
				message.setToken(token);
				message.setType("2");
				String reciveMessage = JsonUtil.ObjectToJsonString(message);
				manager.setMessages(message, reciveMessage, l);
				
				//List <String>users = new ArrayList<String>();
				for(int i=0,j=l.size();i<j;i++){
					//Map <String,String> map = list.get(i);
					String u_id = l.get(i);// map.get("user_id");
					
					//users.add(u_id);
					//manager.setMessage(message, reciveMessage);
					
					SocketThread.getSocketToMessage(u_id,reciveMessage,token,from,"2");
					
				}
				
				LogMsg logMsg = new LogMsg();
				logMsg.setCode(200);
				logMsg.setToken(token);
				logMsg.setMessage("success");
				Map <String,String>logMap = new HashMap<String,String>();
				logMap.put("msguuid", message.getMsguuid());
				logMsg.setData(logMap);
				serviceToClient(socket,JsonUtil.ObjectToJsonString(logMsg));
				
			}else if(token.equals(SocketConfig.TOKEN_ADD_FRIEND)){//添加好友
				Message message = this.setMessage(json);
				String reciveMessage = JsonUtil.ObjectToJsonString(message);
				//manager.setMessage(message,reciveMessage);
				String from = message.getFrom();
				String to  = message.getTo();
				String type = message.getType();
				
				
				//join friend   int joinFlag = 
				userManager.joinFriend(from, to,type);//joinFlag!=0&&
				if(!type.equals("4")){
					manager.setMessage(message,reciveMessage);
					SocketThread.getSocketToMessage(to, reciveMessage,token,from,type);
				}
				String backString = this.sendBackMsg("6",message);//告诉发送者消息服务器已经接收
				SocketThread.serviceToClient(socket, backString);
				
				
			} else if(token.equals(SocketConfig.TOKEN_REMOVE_FRIEND)){
				Message message = this.setMessage(json);
				String from = message.getFrom();
				String to  = message.getTo();
				String type = message.getType();
				//String msguuid = message.getMsguuid();
				userManager.removeFriend(from,to);
				String reciveMessage = JsonUtil.ObjectToJsonString(message);
				if(!type.equals("4")){
					manager.setMessage(message,reciveMessage);
					SocketThread.getSocketToMessage(to,reciveMessage,token,from,type);
				}
				String backString = this.sendBackMsg("6",message);//告诉发送者消息服务器已经接收
				SocketThread.serviceToClient(socket, backString);
				
			} else if(token.equals(SocketConfig.TOKEN_COMFIRM_ADD_FRIEND)){
				Message message = this.setMessage(json);
				String from = message.getFrom();
				String to = message.getTo();
				String type = message.getType();
				String reciveMessage = JsonUtil.ObjectToJsonString(message);
				manager.setMessage(message,reciveMessage);
				String backString = this.sendBackMsg("6",message);
				boolean b = userManager.comfirmFriend(to, from);
				logger.debug("bb:"+b);
				if(b){
					SocketThread.getSocketToMessage(to, reciveMessage,token,from,type);
					
					
					SocketThread.getSocketToMessage(from, backString,token,to,type);
				} 
//				else {
//					LogMsg msg = new LogMsg();
//					msg.setCode(201);
//					msg.setMessage("加好友失败");
//					serviceToClient(socket,JsonUtil.ObjectToJsonString(msg));
//				}
			} else if (token.equals(SocketConfig.TOKEN_ADD_BLACK)){ //添加黑名单
				Message message = this.setMessage(json);
				String from =message.getFrom();
				String to = message.getTo();
				int i = userManager.addBlack(from, to);
				if(i>0){
					message.setContent("add black "+to+" success");
					message.setToken("6");
					String reciveMessage = JsonUtil.ObjectToJsonString(message);
					//保证接受者受到的token20
					serviceToClient(socket,reciveMessage);
					
					
					message.setToken(token);
					reciveMessage = JsonUtil.ObjectToJsonString(message);
					manager.setMessage(message,reciveMessage);
					SocketThread.getSocketToMessage(to, reciveMessage,token,from,"1");
				}
			} else if(token.equals(SocketConfig.TOKEN_REMOVE_BLACK)){
				Message message = this.setMessage(json);
				String from =message.getFrom();
				String to = message.getTo();
				userManager.removeBlack(from, to);
				message.setToken("6");
				message.setContent("remove black "+to+" success");
				String reciveMessage = JsonUtil.ObjectToJsonString(message);
				serviceToClient(socket,reciveMessage);
				
				
				message.setToken(token);
				reciveMessage = JsonUtil.ObjectToJsonString(message);
				manager.setMessage(message,reciveMessage);
				
				SocketThread.getSocketToMessage(to, reciveMessage,token,from,"1");
			} 
		}
	}
	
	public Message setMessage(JSONObject json){
		Message message = new Message();
		String msguuid = json.getString("msguuid");
		
		if(json.has("from")){
			String from = json.getString("from");
			message.setFrom(from);
		}
		
		
		
		if(json.has("to")){
			String to  = json.getString("to");
			message.setTo(to);
		}
		
		
		if(json.has("type")){
			String type = json.getString("type");
			message.setType(type);
		}
		
		if(json.has("content")){
			message.setContent(json.getString("content"));
		}
		if(json.has("smallImgPath")){
			message.setSmallImgPath(json.getString("smallImgPath"));
		}
		
		if(json.has("filePath")){
			message.setFilePath(json.getString("filePath"));
		}
		
		if(json.has("longDate")){
			message.setLongDate(json.getString("longDate"));
		}
		
		if(json.has("fromName")) {
			message.setFromName(json.getString("fromName"));
		}
		
		message.setToken(json.getString("token"));
		
		
		
		message.setLongDate(new Date().getTime()+"");
		message.setMsguuid(msguuid);
		return message;
	}
	
	public String sendBackMsg(String token,Message message){
		Map <String,String>map = new HashMap<String,String>();
		map.put("token", token);
		map.put("msguuid", message.getMsguuid());
		map.put("type", message.getType());
		map.put("to", message.getTo());
		map.put("longDate", message.getLongDate());
		map.put("from", message.getFrom());
		return JsonUtil.ObjectToJsonString(map);
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try{
			InputStream in = this.socket.getInputStream();

			int len = 0;
			byte[] arrayByte = null;
			
			while((len=in.read(buf))!=-1){
				arrayByte = DataUtil.byteArray(arrayByte,DataUtil.subBytes(buf, 0, len));
				int ablen = arrayByte.length;
				int flaglen = SocketConfig.WRIETEFLAGBYTES.length;
				byte[] byteCode = DataUtil.subBytes(arrayByte,ablen-flaglen,flaglen);
				boolean isEnd = DataUtil.isBytesEquals(byteCode, SocketConfig.WRIETEFLAGBYTES);	
				if(isEnd){
					String socketString = new String(DataUtil.subBytes(arrayByte, 0, ablen-flaglen),"utf-8");
					arrayByte = null;
					//socket处理
					//System.out.println("socket::"+socketString);
					String []ss = socketString.split("[\\_][\\!][\\@][\\#][\\$][\\%][\\^][\\&][\\*][\\_]");
					for(int i=0,j=ss.length;i<j;i++){
						//System.out.println(i+"cccc:"+ss[i]);
						this.socketMessage(this.socket, ss[i]);
					}
					
				}
			}
			
			
		} catch(Exception e){
			//用户断网掉线，异常情况。
			e.printStackTrace();
		}
	}
	


	/** 
	 * @Title: main 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param args    设定文件 
	 * @return void    返回类型 
	 * @throws 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		Map map =  new HashMap();
//		map.put("code", 200);
//		map.put("message","success");
//		Map map2 = new HashMap();
//		List list = new ArrayList();
//		Message message1 = new Message();
//		Message message2 = new Message();
//		list.add(message1);
//		list.add(message2);
//		map2.put("msguuid", "fadfadsfdafadfa");
//		map.put("data", map2);
//		map.put("list", list);
//		System.out.println(JsonUtil.ObjectToJsonString(map));
	}
}
