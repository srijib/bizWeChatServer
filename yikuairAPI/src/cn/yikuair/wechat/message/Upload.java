package cn.yikuair.wechat.message;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.FileItem;

import cn.yikuair.base.Log;
import cn.yikuair.beans.LogMsg;
import cn.yikuair.beans.Message;
import cn.yikuair.business.BaseManager;
import cn.yikuair.business.MessageManager;
import cn.yikuair.business.UserManager;
import cn.yikuair.dao.BaseDao;
import cn.yikuair.message.SocketConfig;
import cn.yikuair.utils.Conf;
import cn.yikuair.utils.DateUtil;
import cn.yikuair.utils.FileUtil;
import cn.yikuair.utils.HttpParams;
import cn.yikuair.utils.ImageUtil;
import cn.yikuair.utils.JsonUtil;

public class Upload {
	
	Properties property = Conf.getProperty();
	String path = property.getProperty("path.imgpath");
	private static Logger logger = Log.getLogger(Upload.class);
	
	private static Upload upload = null;
	public static Upload getUpload(){
		if(upload==null){
			upload = new Upload();
		}
		return upload;
	}
	
	public String uploadFile(HttpParams params){
		LogMsg logMsg = new LogMsg();
		Map<String,String> longmap = new HashMap<String,String>();
		longmap.put("longDate", new Date().getTime()+"");
		logMsg.setData(longmap);
		try {
			FileItem fileItem = params.getFileItem("upload");
			logger.info(":::"+fileItem.getName());
			String fileName = fileItem.getName().toLowerCase();
			logger.debug("fileName::"+fileName);
			int dottedIndex = fileName.lastIndexOf(".");
			if(dottedIndex<0) {
				logMsg.setMessage("上传的文件不能识别");
				logger.debug("不能识别的文件："+fileName);
				return JsonUtil.ObjectToJsonString(logMsg);
			}
			String username = params.getStr("username", "");
			String password = params.getStr("password", "");
			String token = params.getStr("token", "");
			String type = params.getStr("type", "");
			String from = params.getStr("from", "");
			String to = params.getStr("to", "");
			String msguuid = params.getStr("msguuid", "");
			if(username.equals("")||password.equals("")){
				//logMsg.setMessage("username和password不能为空");
			} else if(token.equals("")||!(token.equals("2")||token.equals("3"))){
				logMsg.setMessage("token不能为空,token:"+token);
			} else if(type.equals("")){
				logMsg.setMessage("type不能为空");
			} else if(from.equals("")||to.equals("")){
				logMsg.setMessage("from和to不能为空");
			} else if(msguuid.equals("")){
				logMsg.setMessage("msguuid不能为空");
			}
			
			
			String countSql = "select count(*) as count from t_messagelog where msguuid='"+msguuid+"'";
			BaseDao baseDao = BaseDao.getBaseDao();
			int count = baseDao.getCount(countSql);
			if(count>0){
				logMsg.setMessage("该消息已经被保存："+msguuid);
			}
			
			String err = logMsg.getMessage();
			if(null!=err&&err.length()>0){
				return JsonUtil.ObjectToJsonString(logMsg);
			}
			
			String fileType = fileName.substring(dottedIndex+1);
			byte bytes[] = fileItem.get();
			boolean flag = false,isPic = false;
			String finalPath = "",finalSmallImgPath="";
			String time = DateUtil.getTimeString("yyyy-MM-dd");
			String name = username+"_"+new Date().getTime()+"."+fileType;
			if(fileType.equals("png")||fileType.equals("jpg")||fileType.equals("jpeg")){
				flag = true;isPic=true;
				finalPath = path+"/img/"+time+"/";
				finalSmallImgPath = path+"/smallimg/"+time+"/"; 
			} else if(fileType.equals("aac")){
				flag = true;
				finalPath = path+"/voice/"+time+"/"; 
			} else if (fileType.equals("xls")){
				finalPath = path+"/excel/"+time+"/"; 
				finalPath = finalPath+name;
				FileUtil.byteToFile(bytes, finalPath);
				//解析数据插入库
				UserManager manager = UserManager.getUserManager();
				String com_id = params.getStr("com_id", "");
				if(com_id.equals("")) return null;
				manager.inportUsers(com_id,finalPath);
				
				logger.debug("upload userinfo success");
				
				return null;
			}else {
				logMsg.setMessage("文件类型错误");
				return JsonUtil.ObjectToJsonString(logMsg);
			}
			finalPath = finalPath+name;
			
			if(flag){
				FileUtil.byteToFile(bytes, finalPath);
			}
			if(isPic){
				finalSmallImgPath = finalSmallImgPath+name;
				ImageUtil.compressImg(bytes, finalSmallImgPath);
			}
			
			if(flag){
				logMsg.setCode(200);
				logMsg.setMessage("success");
				
				Message message = new Message();
				message.setToken(token);
				message.setFrom(from);
				message.setTo(to);
				message.setType(type);
				message.setMsguuid(msguuid);
				message.setLongDate(new Date().getTime()+"");
				
				String subPath  = "";
				if(isPic) {//photo
					subPath = finalPath.substring(finalPath.indexOf("/img/"));
					String subSmallImgPath = finalSmallImgPath.substring(finalSmallImgPath.indexOf("/smallimg/"));
					message.setSmallImgPath(subSmallImgPath);
				} else if(fileType.equals("aac")){//voice
					int voiceIndex = finalPath.indexOf("/voice/");
					subPath = finalPath.substring(voiceIndex);
				}
				message.setFilePath(subPath);
				MessageManager manager = MessageManager.getMessageManager();
				
				
				String reciveMessage = JsonUtil.ObjectToJsonString(message);
				
				
				//socket 发送消息
				logger.debug("send file type:"+type);
				if(type.equals("1")){
					logger.debug("save file message :"+reciveMessage);
					manager.setMessage(message,reciveMessage);//保存消息
//					logger.debug("在线给发送者发送消息："+to);
//					SocketThread.getSocketToMessage(to, reciveMessage);
					//http方式取不到socket中填充的值
				} else if(type.equals("2")){
					//发送消息
					//查找msguuid对应的用户id
					UserManager userManager = UserManager.getUserManager();
					List<String> ids = userManager.getGroupUsers(to);
					//保存消息
					manager.setMessages(message, reciveMessage, ids);
					//发送消息
//					for(int i=0,j=ids.size();i<j;i++){
//						if(ids.get(i).equals(message.getFrom()))continue;
//						SocketThread.getSocketToMessage(ids.get(i), reciveMessage);
//					}
				}
				logMsg.setToken(SocketConfig.TOKEN_C1_COMFIRM_S_RECEIVE);
				logMsg.setData(message);
				return JsonUtil.ObjectToJsonString(logMsg);
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logMsg.setCode(201);
		logMsg.setMessage("failed");
		
		return JsonUtil.ObjectToJsonString(logMsg);
	}
	
	public String uplaodXls(HttpParams params) {
		Map <String,String>map = new HashMap<String,String>();
		try{
			FileItem fileItem = params.getFileItem("upload");
			logger.info("fileItem:"+fileItem);
			String fileName = fileItem.getName().toLowerCase();
			logger.debug("fileName::"+fileName);
			int dottedIndex = fileName.lastIndexOf(".");
			if(dottedIndex<0) {
				map.put("code", "201");
				map.put("message","上传的文件不能识别");
				logger.debug("不能识别的文件："+fileName);
				return JsonUtil.ObjectToJsonString(map);
			}
			byte bytes[] = fileItem.get();
			String username = params.getStr("username", "");
			String finalPath = "";
			String time = DateUtil.getTimeString("yyyy-MM-dd");
			String name = username+"_"+new Date().getTime()+"."+"xls";
			finalPath = path+"/excel/"+time+"/"; 
			finalPath = finalPath+name;
			FileUtil.byteToFile(bytes, finalPath);
			//解析数据插入库
			UserManager manager = UserManager.getUserManager();
			String com_id = params.getStr("com_id", "");
			if(com_id.equals("")) {
				map.put("code", "201");
				map.put("message","请填写公司id");
				logger.debug("不能识别的文件："+fileName);
				return JsonUtil.ObjectToJsonString(map);
			}
			manager.inportUsers(com_id,finalPath);
			map.put("code", "200");
			map.put("message","success");
			return JsonUtil.ObjectToJsonString(map);
		} catch (Exception ex) {
			ex.printStackTrace();
			map.put("code", "500");
			map.put("message","exception:"+ex.getMessage());
			return JsonUtil.ObjectToJsonString(map);
		}
	}
	
	
	public String uploadHeader(HttpParams params){
		LogMsg logMsg = new LogMsg();
		Map<String,String> longmap = new HashMap<String,String>();
		longmap.put("longDate", new Date().getTime()+"");
		try {
			FileItem fileItem = params.getFileItem("upload");
			String fileName = fileItem.getName().toLowerCase();
			logger.debug("fileName::"+fileName);
			int dottedIndex = fileName.lastIndexOf(".");
			if(dottedIndex<0) {
				logMsg.setMessage("上传的文件不能识别");
				logger.debug("不能识别的文件："+fileName);
				return JsonUtil.ObjectToJsonString(logMsg);
			} 
			String username = params.getStr("username", "");
			//String password = params.getStr("password", "");
			String from = params.getStr("from", "");
			String msguuid = params.getStr("msguuid", "");
			
			if(username.equals("")){
				logMsg.setMessage("username");
			} else if(from.equals("")){
				logMsg.setMessage("from不能为空");
			} else if(msguuid.equals("")){
				logMsg.setMessage("msguuid不能为空");
			}
			logMsg.setToken(SocketConfig.TOKEN_USERINFO_SAVE);
			String err = logMsg.getMessage();
			if(null!=err&&err.length()>0){
				return JsonUtil.ObjectToJsonString(logMsg);
			}
			String fileType = fileName.substring(dottedIndex+1);
			byte bytes[] = fileItem.get();
			String finalPath = "";
			String time = DateUtil.getTimeString("yyyy-MM-dd");
			String name = username+"_"+new Date().getTime()+"."+fileType;
			if(fileType.equals("png")||fileType.equals("jpg")||fileType.equals("jpeg")){
				finalPath = path+"/header/"+time+"/"+name;
			}
			FileUtil.byteToFile(bytes, finalPath);
			
			int headerIndex = finalPath.indexOf("/header/");
			finalPath = finalPath.substring(headerIndex);
			
			
			String updateHeader = "{\"id\":"+from+",\"headurl\":\""+finalPath+"\",\"msguuid\":\""+msguuid+"\"}";
			logger.debug("updateheader::"+updateHeader);
//			UserManager userManager = UserManager.getUserManager();
//			int count = userManager.userSave(JsonUtil.StringToObject(updateHeader));
			//if(count>0){
				logMsg.setCode(200);
				logMsg.setMessage("success");
				longmap.put("headurl", finalPath);
				logMsg.setData(longmap);
				String json = JsonUtil.ObjectToJsonString(logMsg);
				logger.debug("update header url:"+json);
				return json;
			//}			
		}catch(Exception e){
			e.printStackTrace();
		}
		logMsg.setMessage("failed");
		return JsonUtil.ObjectToJsonString(logMsg);
	}
	
}
