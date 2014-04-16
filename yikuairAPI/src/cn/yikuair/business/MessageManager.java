package cn.yikuair.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.yikuair.base.Log;
import cn.yikuair.beans.Message;
import cn.yikuair.beans.SystemMessage;
import cn.yikuair.dao.BaseDao;
import cn.yikuair.utils.DateUtil;

public class MessageManager {
	private static Logger logger = Log.getLogger(MessageManager.class);
	private static MessageManager messageManager = null;
	public static MessageManager getMessageManager(){
		if(messageManager==null){
			messageManager = new MessageManager();
		}
		return messageManager;
	}
	
	public List<String> getUnreadMessage(String user_id){
		String sql = "select b.content as content from " +
				"t_msgcomfirm a right Join t_messagelog b " +
				"on a.msguuid= b.msguuid where a.to_id="+user_id+" and a.status=0";
		BaseDao baseDao = BaseDao.getBaseDao();
		List <String>contentList = new ArrayList<String>();
		List <Map<String,String>> list = baseDao.getData(sql);
		for(int i=0,j=list.size();i<j;i++){
			Map<String,String> map = list.get(i);
			contentList.add(map.get("content"));
		}
		return contentList;
	}
	
	/**
	 * 
	* @Title: setMessage 
	* @Description: 保存1to1的消息文本，和确认信息 
	* @param @param message
	* @param @param reciveMessage
	* @param @return    设定文件 
	* @return int    返回类型 
	* @throws
	 */
	public int setMessage(Message message,String reciveMessage){
		String token = message.getToken();
		String msgSql = "",comfirmSql="",ismsguuidSql="";
		logger.debug("token:"+token);
		//reciveMessage = reciveMessage.replace("'", "\\'");
		comfirmSql = "insert into t_msgcomfirm (msguuid,from_id,to_id,status) values('"+message.getMsguuid()+"',"+message.getFrom()+","+message.getTo()+",0)";
		ismsguuidSql = "select count(*) as count from t_messagelog where msguuid='"+message.getMsguuid()+"'";	
		if(token.equals("1")||token.equals("13")){//文字
			msgSql = "insert into t_messagelog(dialogtype,sender_id,receiver_id,messagetype,content,createTime,msguuid) " +
					"values ("+message.getType()+","+message.getFrom()+","+message.getTo()+","+message.getToken()+"," +
							"'"+reciveMessage+"',now(),'"+message.getMsguuid()+"')";
			
		} else if (token.equals("2")){//图片
			//检测msguuid是否存在,如果存在就返回
			msgSql = "insert into t_messagelog(dialogtype,sender_id,receiver_id,messagetype,createTime,msguuid,content) " +
					"values ("+message.getType()+","+message.getFrom()+","+message.getTo()+","+message.getToken()+"" +
							",now(),'"+message.getMsguuid()+"','"+reciveMessage+"')";
		} else if(token.equals("3")){//声音
			msgSql = "insert into t_messagelog(dialogtype,sender_id,receiver_id,messagetype,createTime,msguuid,content) " +
					"values ("+message.getType()+","+message.getFrom()+","+message.getTo()+","+message.getToken()+"" +
							",now(),'"+message.getMsguuid()+"','"+reciveMessage+"')";
		} else {
			//String []arr = {SocketConfig.TOKEN_ADD_BLACK,SocketConfig.TOKEN_REMOVE_BLACK};
			msgSql = "insert into t_messagelog(dialogtype,sender_id,receiver_id,messagetype,createTime,msguuid,content) " +
					"values ("+message.getType()+","+message.getFrom()+","+message.getTo()+","+message.getToken()+"" +
					",now(),'"+message.getMsguuid()+"','"+reciveMessage+"')";
		}
		//logger.debug("..................");
		BaseDao baseDao = BaseDao.getBaseDao();
		logger.debug("uuid:"+ismsguuidSql);
		int count = baseDao.getCount(ismsguuidSql);
		if(count>0) return 0;
		baseDao.execSave(msgSql);
		logger.debug("msgSql::"+msgSql);
		int status = baseDao.execSave(comfirmSql);
		return status;
	} 
	
	public String setMessages(Message message,String reciveMessage,List<String> ids){
		String token = message.getToken();
		String msgSql = "",comfirmSql="",ismsguuidSql="";
		//reciveMessage = reciveMessage.replace("'", "\\'");
		logger.debug("token:"+token);
		ismsguuidSql = "select count(*) as count from t_messagelog where msguuid='"+message.getMsguuid()+"'";
		BaseDao baseDao = BaseDao.getBaseDao();
		int count = baseDao.getCount(ismsguuidSql);
		
		logger.debug("message token count::"+count + "------"+message.getMsguuid());
		
		if(count>0) return "1";
		
		if(token.equals("1")||token.equals("13")||token.equals("16")||token.equals("17")){//文字   / task message
			msgSql = "insert into t_messagelog(dialogtype,sender_id,receiver_id,messagetype,content,createTime,msguuid) " +
					"values ("+message.getType()+","+message.getFrom()+","+message.getTo()+","+message.getToken()+"," +
							"'"+reciveMessage+"',now(),'"+message.getMsguuid()+"')";
			
		} else if (token.equals("2")){//图片
			//检测msguuid是否存在,如果存在就返回
			msgSql = "insert into t_messagelog(dialogtype,sender_id,receiver_id,messagetype,createTime,msguuid,content) " +
					"values ("+message.getType()+","+message.getFrom()+","+message.getTo()+","+message.getToken()+"" +
							",now(),'"+message.getMsguuid()+"','"+reciveMessage+"')";
		} else if(token.equals("3")){//声音
			msgSql = "insert into t_messagelog(dialogtype,sender_id,receiver_id,messagetype,createTime,msguuid,content) " +
					"values ("+message.getType()+","+message.getFrom()+","+message.getTo()+","+message.getToken()+"" +
							",now(),'"+message.getMsguuid()+"','"+reciveMessage+"')";
		} else {
			msgSql = "insert into t_messagelog(dialogtype,sender_id,receiver_id,messagetype,content,createTime,msguuid) " +
					"values ("+message.getType()+","+message.getFrom()+","+message.getTo()+","+message.getToken()+"," +
							"'"+reciveMessage+"',now(),'"+message.getMsguuid()+"')";
		}
		
		logger.debug("msgsql:"+msgSql);
		baseDao.execSave(msgSql);
		
		//生成确认消息
		for(int i=0,j=ids.size();i<j;i++){
			if(ids.get(i).equals(message.getFrom())) continue;
			comfirmSql = "insert into t_msgcomfirm (msguuid,from_id,to_id,status) " +
					"values('"+message.getMsguuid()+"',"+message.getFrom()+","+ids.get(i)+",0)";
			
			logger.debug("comfirmSql:"+comfirmSql);
			
			baseDao.execSave(comfirmSql);
		}
		return "0";
	}
	
	
	
	
	
	public void saveSystemMessage(SystemMessage message,String reciveMessage,List <String>ids){
		String msgSql="",comfirmSql="";
		
		String token = message.getToken();
		if(token.equals("12")){//资料修改
			msgSql = "insert into t_messagelog(dialogtype,messagetype,content,createTime,msguuid) " +
					"values (3,1," +
							"'"+reciveMessage+"',now(),'"+message.getMsguuid()+"')";
			                                              
		}
		logger.debug("comfirm");
		BaseDao baseDao = BaseDao.getBaseDao();
		baseDao.execSave(msgSql);
		logger.debug("comfirm start message");
		for(int i=0,j=ids.size();i<j;i++){
			
			comfirmSql = "insert into t_msgcomfirm (msguuid,to_id,status) " +
					"values('"+message.getMsguuid() +"',"+ids.get(i)+",0)";
			
			logger.debug("comfirmSql:"+comfirmSql);
			
			baseDao.execSave(comfirmSql);
		}
		logger.debug("update comfirm message success");
	}
	
	
	public int saveComfirmMsg(int status,Message message){
		String sql = "update t_msgcomfirm set status="+status+" where msguuid='"+message.getMsguuid()+"' and to_id="+message.getTo()+"";
		logger.debug("token "+status+"----"+sql);
		BaseDao baseDao = BaseDao.getBaseDao();
		return baseDao.execSave(sql);
	}
	
	public List <Map<String,String>> setMsgStatus(int status,String from){//3 已经接收    4已读
		String sql = "select a.msguuid as msguuid,a.to_id as `to`,a.from_id as `from`,b.messagetype as `type` from " +
				"t_msgcomfirm a left join t_messagelog b on a.msguuid= b.msguuid where a.status="+status+" and a.from_id="+from;
		BaseDao baseDao = BaseDao.getBaseDao();
		List <Map<String,String>> list = baseDao.getData(sql);
		return list;
	}
	
	//更新推送消息机制  一次加1
	public Map <String,String> addNotification(String id){
		//
		String sql = "update `t_userInfo` set offmessage = offmessage+1   where id="+id;
		BaseDao baseDao = BaseDao.getBaseDao();
		baseDao.execSave(sql);
		String countsql = "select offmessage as count,machinetype,token from t_userinfo where id="+id;
		List <Map <String,String>> list = baseDao.getData(countsql);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
	public void clearNotification(String id){
		String sql = "update `t_userInfo` set offmessage = 0   where id="+id;
		logger.info("set offmessage o:"+sql);
		BaseDao baseDao = BaseDao.getBaseDao();
		baseDao.execSave(sql);
	}
	
	public void closeNotification (String id){
		String sql = "update `t_userInfo` set offmessage = 0 , token=''   where id="+id;
		BaseDao baseDao = BaseDao.getBaseDao();
		baseDao.execSave(sql);
	}
	
	public int messageStatus(String msguuid){
		String sql = "select count(*) as count from t_messagelog where msguuid='"+msguuid+"'";
		BaseDao baseDao = BaseDao.getBaseDao();
		int count = baseDao.getCount(sql);
		return count;
	}
	
	public int getTodayCountMessage(String id){
		String date = DateUtil.getTimeString("yyyy-MM-dd");
		String sql = "select count(*) as count from t_messagelog where createtime>='"+date+"' and sender_id='"+id+"'";
		
		logger.info("sender id count:"+sql);
		
		BaseDao baseDao = BaseDao.getBaseDao();
		int count = baseDao.getCount(sql);
		return count;
	}
	
	public String bakMessageLog(){
		String dateString = DateUtil.getDateBefore("yyyy-MM-dd", 1);
		String sql = "insert into t_bak_msgcomfirm (select * from t_msgcomfirm where createTime <='"+dateString+"')";
		String dsql = "delete from `t_msgcomfirm` where createTime<='"+dateString+"'";
		
		logger.debug("sql:"+sql);
		logger.debug("dsql:"+dsql);
		
		BaseDao baseDao = BaseDao.getBaseDao();
		baseDao.execSave(sql);
		baseDao.execSave(dsql);
		
		sql = "insert into t_bak_messagelog (select * from t_messagelog where createTime <='"+dateString+"')";
		dsql = "delete from `t_messagelog` where createTime<='"+dateString+"'";
		baseDao.execSave(sql);
		baseDao.execSave(dsql);
		
		return "success";
	}
	
	
	//点对点的未读消息
	public List <Map<String,String>> getUnreadMessage(String from,String to){//3 已经接收    4已读
		
		String fromString = ""; 
		if(!from.equals("")  && from!=null && !from.equals("null") ){
			fromString = "and a.from_id="+from +"";
		}
		
		if(to==null || to.equals("") ||to.equals("null") ) {
			return null;
		} 
		
		
		String sql = "select a.msguuid as msguuid,a.to_id as `to`,a.from_id as `from`,b.messagetype as `type`,b.content as content from " +
				"t_msgcomfirm a right join t_messagelog b on (a.msguuid= b.msguuid) where a.status="+0+" "+fromString+"  and to_id='"+to+"'";
		BaseDao baseDao = BaseDao.getBaseDao();
		List <Map<String,String>> list = baseDao.getData(sql);
		
		logger.info("message::"+sql);
		return list;
	}
	
}
