package cn.yikuair.business;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.yikuair.base.Log;
import cn.yikuair.beans.Message;
import cn.yikuair.dao.BaseDao;

public class StarManager {
	static Logger logger = Log.getLogger(StarManager.class);
	private static StarManager starManager = null;
	public static StarManager getStarManager(){
		if(starManager==null){
			starManager = new StarManager();
		}
		return starManager;
	}
	public String getContent(String id){
		String sql = "select content from `t_definedStar` where id='"+id+"'";
		BaseDao dao = BaseDao.getBaseDao();
		List <Map <String,String>>list = dao.getData(sql);
		if(list!=null&&list.size()>0){
			return list.get(0).get("content");
		}
		return "";
	}
	
	/**
	 * 
	* @Title: saveBtn 
	* @Description: 
	* create table t_definedStar(
	  id int(11) not null AUTO_INCREMENT comment '主键ID',
	  user_id int (11) not null comment '用户的id',
	  buttonText varchar(100) not null comment '按钮文字',
	  content text comment '文本内容',
	  msguuid varchar(64) comment '消息的唯一标志',
	  createTime timestamp comment '创建时间',
	  PRIMARY KEY (`id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='明星账户';
	* @param @param id
	* @param @param status
	* * @param @param buttonName
	* @param @param content
	* @param @param msguuid
	* @param @return    设定文件 
	* @return boolean    返回类型 
	* @throws
	 */
	public boolean saveBtn(String id,String status,String buttonName,String callbackurl,String msguuid){
		String sql = "";
		BaseDao dao = BaseDao.getBaseDao();
		if(status.equals("0")){//add
			String btnsql = "select count(*) as count from t_definedStar where user_id='"+id+"'";
			int count = dao.getCount(btnsql);
			logger.debug("最多设置8个按钮");
			if(count>=8) return false;
			sql = "insert into t_definedStar(user_id,buttonText,callbackurl,msguuid) values("+id+",'"+buttonName+"','"+callbackurl+"','"+msguuid+"')";
		} else if(status.equals("1")){
			sql = "update t_definedStar set buttonText='"+buttonName+"',callbackurl='"+callbackurl+"' where user_id="+id+" and msguuid='"+msguuid+"'";
		} else if(status.equals("2")){
			sql = "delete from t_definedStar where user_id='"+id+"' and msguuid='"+msguuid+"'";
		} else {
			logger.debug("没有匹配的状态");
			return false;
		}
		logger.debug(sql);
		
		dao.execSave(sql);
		return true;
	}
	
	public List<Map<String,String>> getBtnList(String id){
		String sql = "select * from t_definedStar where user_id='"+id+"'";
		BaseDao dao = BaseDao.getBaseDao();
		List <Map <String,String>>list = dao.getData(sql);
		return list;
	}
	
	public void setMessage(Message message,String reciveMessage){
		String msgSql = "",ismsguuidSql="";
		ismsguuidSql = "select count(*) as count from t_starMessagelog where msguuid='"+message.getMsguuid()+"'";
		BaseDao baseDao = BaseDao.getBaseDao();
		int count = baseDao.getCount(ismsguuidSql);
		if(count>0) return;
		
		msgSql = "insert into t_starMessagelog(dialogtype,sender_id,receiver_id,messagetype,createTime,msguuid,content) " +
				"values ("+message.getType()+","+message.getFrom()+","+message.getTo()+","+message.getToken()+"" +
				",now(),'"+message.getMsguuid()+"','"+reciveMessage+"')";
		
		logger.debug("msgsql:"+msgSql);
		baseDao.execSave(msgSql);
	}
	
	public List<Map<String,String>> getPageStarMessage(String id,int pageIndex,int pageSize){
		int index = (pageIndex-1)*pageSize;
		String sql = "select a.content as content,a.createTime as createTime,b.username as id from t_starMessagelog a left join t_userinfo b on (a.sender_id=b.id)  where a.receiver_id='"+id+"' limit "+(index-1)*pageSize+","+pageSize+"";
		BaseDao baseDao = BaseDao.getBaseDao();
		List <Map <String,String>>list = baseDao.getData(sql);
		return list;
	}
	
	public List<Map<String,String>> getMsguuidByMessage(String msguuid){
		String sql = "select content,createTime from t_starMessagelog where msguuid='"+msguuid+"'";
		BaseDao baseDao = BaseDao.getBaseDao();
		List <Map <String,String>>list = baseDao.getData(sql);
		return list;
	} 
	
	
	public int getCountMessage(){
		String sql = "select count(*) as count from t_starMessagelog";
		BaseDao baseDao = BaseDao.getBaseDao();
		return baseDao.getCount(sql);
	}
	
	public String getCallback(String id){
		String sql = "select callbackurl from t_callbackMessageStar where user_id='"+id+"'";
		BaseDao baseDao = BaseDao.getBaseDao();
		Map <String,String> map = baseDao.getFistRow(sql);
		if(map==null||map.size()==0){
			return "";
		}
		return map.get("callbackurl");
	}
	
	public String getCallbackUrlByStarID(String id){
		String sql = "select callbackurl from t_callbackmessagestar where user_id='"+id+"'";
		BaseDao baseDao = BaseDao.getBaseDao();
		Map <String,String> map = baseDao.getFistRow(sql);
		if(map==null) return null;
		return map.get("callbackurl");
	}
	
	
}
