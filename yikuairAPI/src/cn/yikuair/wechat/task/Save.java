package cn.yikuair.wechat.task;

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
import cn.yikuair.business.MessageManager;
import cn.yikuair.business.UserManager;
import cn.yikuair.dao.BaseDao;
import cn.yikuair.message.SocketConfig;
import cn.yikuair.message.SocketThread;
import cn.yikuair.utils.DateUtil;
import cn.yikuair.utils.JsonUtil;
import cn.yikuair.utils.UuidUtil;

public class Save {
	private static Logger logger = Log.getLogger(Save.class);
	private static Save save = null;
	public static Save getSave(){
		if(null==save){
			save = new Save();
		}
		return save;
	}
	public String mettingComfirm(JSONObject json){
		
		String to_id = JsonUtil.getParam(json,"to", "");
		String task_id = JsonUtil.getParam(json,"task_id", "");
		String from_id = JsonUtil.getParam(json,"from", "");
		String type = JsonUtil.getParam(json,"type", "");
		String msguuid = JsonUtil.getParam(json, "msguuid", "");
		
//		String username = JsonUtil.getParam(json,"username", "");
//		String password = JsonUtil.getParam(json,"password", "");
//		
//		
//		if(username.equals("")||password.equals("")){
//			LogMsg err = new LogMsg();
//			err.setCode(201);
//			err.setMessage("用户名和密码不能为空");
//			return JsonUtil.ObjectToJsonString(err);
//		}
		
		if(to_id.equals("")||task_id.equals("")){
			LogMsg err = new LogMsg();
			err.setCode(201);
			err.setMessage("请传入当前用户id和任务id");
			return JsonUtil.ObjectToJsonString(err);
		}
		
		
		String sql = "update t_taskcomfirm set status=1 where user_id="+to_id+"  and task_id="+task_id;
		logger.debug("message comfirm:"+sql);
		BaseDao baseDao = BaseDao.getBaseDao();
		int count = baseDao.execSave(sql);
		LogMsg logMsg = new LogMsg();
		logMsg.setToken(SocketConfig.TOKEN_COMFIRM_TASK);
		if(count>0){
			//确定后给，发送者推送消息消息
			MessageManager manager = MessageManager.getMessageManager();
			Message message = new Message();
			message.setToken(SocketConfig.TOKEN_COMFIRM_TASK);
			message.setTo(from_id);
			message.setFrom(to_id);
			message.setType(type);
			message.setContent("success");
			message.setMsguuid(msguuid);
			//String comfirmContent = JsonUtil.ObjectToJsonString(message);
			
			
			String jsonString = "{\"token\":"+SocketConfig.TOKEN_COMFIRM_TASK+",\"from\":"+from_id+",\"to\":"+to_id+",\"task_id\":"+task_id +
			  				",\"type\":"+type+",\"msguuid\":\""+msguuid+"\"}";
			manager.setMessage(message,jsonString);//保存消息
			
			logger.debug("jsonString:::"+jsonString);
			
			SocketThread.getSocketToMessage(from_id, jsonString,SocketConfig.TOKEN_COMFIRM_TASK,task_id,type);
			
			logMsg.setCode(200);
			logMsg.setMessage("success");
			Map <String,String> msgmap = new HashMap<String,String>();
			msgmap.put("msguuid", msguuid);
			logMsg.setData(msgmap);
			return JsonUtil.ObjectToJsonString(logMsg);
		}
		
		logMsg.setCode(404);
		logMsg.setMessage("fialed");
		return JsonUtil.ObjectToJsonString(logMsg);
	}
	
	public String addMetting(JSONObject json){
		  String user_id = JsonUtil.getParam(json,"from", "");
		  String filepath = JsonUtil.getParam(json,"filepath", "");
		  String title = JsonUtil.getParam(json,"title", "");
		  String type = JsonUtil.getParam(json,"type", "");//1 个人，2群组
		  String tasktype = JsonUtil.getParam(json, "tasktype", "");//'1会议，2，任务' 3. 其他
		  String address = JsonUtil.getParam(json,"address", "");
		  String btime = JsonUtil.getParam(json,"btime", "");
		  String etime = JsonUtil.getParam(json,"etime", "");
		  String to = JsonUtil.getParam(json,"to", ""); //join 进来的人
		  String msguuid = JsonUtil.getParam(json, "msguuid", "");
		  //String msguuid = UuidUtil.getUUID();
		  List <String>ids  = new ArrayList<String>();
		  if(type.equals("1")){
			  ids.add(to);
		  } else if(type.equals("2")){
			  UserManager userManager = UserManager.getUserManager();
			  ids= userManager.getGroupUsers(to);
		  }else {
			  logger.debug("to id is null");
			  return null;
		  }
		  
		  
		  if(!btime.equals("")){
			  btime = DateUtil.longToString(Long.parseLong(btime), "yyyy-MM-dd HH:mm:ss");
		  } else {
			  btime = DateUtil.longToString(new Date().getTime(), "yyyy-MM-dd HH:mm:ss");
		  }
		  if(!etime.equals("")){
			  etime = DateUtil.longToString(Long.parseLong(etime), "yyyy-MM-dd HH:mm:ss");
			  
		  } else {
			  etime = "2099-12-12 12:12:12";
		  }
		  
		  BaseDao baseDao  = new BaseDao();
		  MessageManager manager = MessageManager.getMessageManager();
		  
		  
		  String task_id = JsonUtil.getParam(json,"task_id", "");
		  
		  logger.debug("task_id::"+task_id);
		  
		  if(!task_id.equals("")){
			  //任务修改
			  String updateSql = "update t_tasklist set filepath='"+filepath+"' ,title = '"+title+"' ,type="+tasktype+",address='"+address+"',btime='"+btime+"',etime='"+etime+"' where id="+task_id+"";
			  baseDao.execSave(updateSql);
			  
			  LogMsg logMsg = new LogMsg();
			  List<String> listIds = new ArrayList<String>();
			  logMsg.setMessage("success");
			  logMsg.setCode(200);

			  for(int i=0,j=ids.size();i<j;i++){
				  listIds.add(ids.get(i));
			  }
			  
			  Message message = new Message();
			  message.setMsguuid(msguuid);
			  message.setToken("13");
			  message.setFrom(user_id);
			  String jsonString = "{\"token\":13,\"from\":"+user_id+",\"to\":"+to+",\"title\":\""+title+"\"," +
			  		"\"task_id\":"+task_id+",\"type\":"+type+",\"tasktype\":"+tasktype+",\"address\":\""+address+"\"," +
			  				"\"btime\":\""+DateUtil.stringToLong("yyyy-MM-dd HH:mm:ss", btime)+"\",\"etime\":\""+DateUtil.stringToLong("yyyy-MM-dd HH:mm:ss", etime)+"\",\"msguuid\":\""+msguuid+"\",\"longDate\":\""+new Date().getTime()+"\"}";
			  manager.setMessages(message, jsonString, listIds);
			  
			  
			  
			  logMsg.setToken("6");
			  String _message = "{\"to\":"+to+",\"task_id\":"+task_id+",\"token\":6,\"longDate\":\""+new Date().getTime()+"\",\"msguuid\":\""+msguuid+"\",\"from\":"+user_id+",\"type\":"+type+"}";
			  
			//检查token
			  
			  logger.info("ids___"+ids.size());
			  
			  for(int i=0,j=ids.size();i<j;i++){
				  //给在线的人发送task消息
				  SocketThread.getSocketToMessage(ids.get(i), jsonString,SocketConfig.TOKEN_ADD_TASK,msguuid,tasktype);
			  }
			  
			  return _message;
		  }
		  
		  
		  LogMsg logMsg = new LogMsg();
		  String sql = "";
		  if(type.equals("")) {
			  logMsg.setMessage("任务类型不能为空");
		  } else if(tasktype.equals("1")||tasktype.equals("2")||tasktype.equals("3")){
			  sql = "insert into t_tasklist (user_id,filepath,title,type,address,btime,etime) " +
			  		"values("+user_id+",'"+filepath+"','"+title+"',"+tasktype+",'"+address+"','"+btime+"','"+etime+"');";
		  } 
		  
		  int count = baseDao.execSaveAndPK(sql);
		  logger.debug("获取插入的id:"+count);
		  //通过@@identity得到刚插入的主键值
		  
		  logger.debug("ids::"+ids);
		  if(count>0){
			  List<String> listIds = new ArrayList<String>();
			  logMsg.setMessage("success");
			  logMsg.setCode(200);
			  
			  StringBuffer sb = new StringBuffer();
			  for(int i=0,j=ids.size();i<j;i++){
				  sb.append("("+ids.get(i)+","+count+",0),");
				  listIds.add(ids.get(i));
			  }
			  
			  
			   
			  Message message = new Message();
			  message.setMsguuid(msguuid);
			  message.setToken("13");
			  message.setFrom(user_id);
			  String jsonString = "{\"token\":13,\"from\":"+user_id+",\"to\":"+to+",\"title\":\""+title+"\"," +
			  		"\"task_id\":"+count+",\"type\":"+type+",\"tasktype\":"+tasktype+",\"address\":\""+address+"\"," +
			  				"\"btime\":\""+DateUtil.stringToLong("yyyy-MM-dd HH:mm:ss", btime)+"\",\"etime\":\""+DateUtil.stringToLong("yyyy-MM-dd HH:mm:ss", etime)+"\",\"msguuid\":\""+msguuid+"\",\"longDate\":\""+new Date().getTime()+"\"}";
			  manager.setMessages(message, jsonString, listIds);
			  
			  
			  if(ids.size()>0){
				  
				  //找出任务id
				  //插入保存的数据
				  
				  String allData = sb.substring(0, sb.length()-1);
				  
				  String insql = "insert into t_taskcomfirm(user_id,task_id,status) values"+allData+";";
				  
				  logger.debug("任务确认消息："+insql);
				  
				  baseDao.execSave(insql);
			  }
			  
			  
			  
			  //检查token
			  for(int i=0,j=ids.size();i<j;i++){
				  //给在线的人发送task消息
				  SocketThread.getSocketToMessage(ids.get(i), jsonString,SocketConfig.TOKEN_ADD_TASK,user_id,type);
			  }
		  }
		  logMsg.setToken("6");
		  String message = "{\"to\":"+to+",\"token\":6,\"task_id\":"+count+",\"longDate\":\""+new Date().getTime()+"\",\"msguuid\":\""+msguuid+"\",\"from\":"+user_id+",\"type\":"+type+"}";
		  return message;//JsonUtil.ObjectToJsonString(logMsg);
	}
	
	
	public boolean removeTask(JSONObject json){
		if(!json.has("from")||!json.has("task_id")){
			return false;
		}
		String from = json.getString("from");
		String task_id = json.getString("task_id");
		String sql = "delete from t_tasklist where id='"+task_id+"' and user_id='"+from+"'";
		logger.debug("delete task :"+sql);
		BaseDao dao = BaseDao.getBaseDao();
		dao.execSave(sql);
		return true;
	}
	
	
	public static void main(String[] args) {
		String  s = "a,b,d,d,g,y,u,i,y,t";
		String []ss = s.split(",");
		for(int i=0,j=ss.length;i<j;i++){
			System.out.println(ss[i]);
		}
	}
}
