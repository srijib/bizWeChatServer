package cn.yikuair.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import cn.yikuair.base.Log;
import cn.yikuair.beans.LogMsg;
import cn.yikuair.dao.BaseDao;
import cn.yikuair.utils.HttpParams;
import cn.yikuair.utils.JsonUtil;

public class UserGroupManager {
	
	
	private static Logger logger = Log.getLogger(UserGroupManager.class);
	
	private static UserGroupManager userGroupManager = null;
	public static UserGroupManager getUserGroupManager(){
		if(userGroupManager==null){
			userGroupManager = new UserGroupManager();
		}
		return userGroupManager;
	}
	
	public String getGroupsAndMembers(HttpParams params){
		String id = params.getStr("id", "");
		
		String username = params.getStr("username", "");
		String password = params.getStr("password", "");
		
		
		if(username.equals("")||password.equals("")){
			LogMsg err = new LogMsg();
			err.setCode(201);
			err.setMessage("用户名和密码不能为空");
			return JsonUtil.ObjectToJsonString(err);
		}
		
		if(id.equals("")){
			LogMsg err = new LogMsg();
			err.setCode(201);
			err.setMessage("用户名和密码不能为空");
			return JsonUtil.ObjectToJsonString(err);
		}
		List <String>ids = this.getGroups(id);
		List <Map<String,Object>>gm = new ArrayList<Map<String,Object>>();
		for(int i=0,j=ids.size();i<j;i++){
			Map<String,Object> map = new HashMap<String,Object>();
			List <Map<String,String>> list = this.getGroupUsers(ids.get(i));
			map.put("group_id",ids.get(i));
			map.put("users", list);
			gm.add(map);
		}
		
		LogMsg logMsg = new LogMsg();
		logMsg.setCode(200);
		logMsg.setMessage("success");
		logMsg.setData(gm);
		
		return JsonUtil.ObjectToJsonString(logMsg);
	}
	public List<String> getGroups(String id){
		String sql = "select group_id from t_groupMember where user_id="+id;
		logger.debug("getGroups:"+sql);
		BaseDao baseDao = BaseDao.getBaseDao();
		List <String>list_ids = new ArrayList<String>();
		List <Map<String,String>> list  = baseDao.getData(sql);
		for(int i=0,j=list.size();i<j;i++){
			Map<String,String> map = list.get(i);
			String group_id = map.get("group_id");
			list_ids.add(group_id);
		}
		return list_ids;
	}
	
	//查找组内成员
	public List <Map<String,String>> getGroupUsers(String groupId){//, b.nickname as nickname
		String sql = "select a.user_id as user_id from t_groupMember a left join t_userinfo b on a.user_id=b.id where a.group_id = "+groupId;
		logger.debug("getGroupUsers:"+sql);
		BaseDao baseDao = BaseDao.getBaseDao();
		List <Map<String,String>> list = baseDao.getData(sql);
		return list;
	}
	
	//查找组内成员
	public String getGroupUsers(HttpParams params){
		String id = params.getStr("id", "");
		List <Map<String,String>> list = this.getGroupUsers(id);
		LogMsg logMsg = new LogMsg();
		logMsg.setCode(200);
		logMsg.setMessage("success");
		logMsg.setData(list);
		
		return JsonUtil.ObjectToJsonString(logMsg);
		
	}
	
	
	public int saveGroup(JSONObject json){
		String sql = "";
		
		BaseDao baseDao  = BaseDao.getBaseDao();
		
		String status = "0";
		if(json.has("status")){
			status = json.getString("status");
		}
		String from = "";
		if(!json.has("from")){
			return 0;
		}
		from = json.getString("from");
		if(!json.has("group_id")){
			String groupName = "";
			if(json.has("group_name")){
				groupName = json.getString("group_name");
			}
			
			sql = "insert into t_group (groupName,createTime,author_id) values('"+groupName+"',now(),"+from+")";
			int group_id = baseDao.execSaveAndPK(sql);
			logger.debug("group_id::"+group_id);
			if(json.has("ids")){
				String []ids = (json.getString("ids")+","+from).split(",");
				for(int i=0,j=ids.length;i<j;i++){
					String fndMemberSql = "select count(*) as count from t_groupMember where group_id="+group_id+" and user_id="+ids[i];
					int count = baseDao.getCount(fndMemberSql);
					if(count>0) continue;
					String memberSql = "insert into t_groupMember(group_id,user_id,status,joinTime) values("+group_id+","+ids[i]+","+status+",now())";
					logger.debug("add group memeber:"+memberSql);
					baseDao.execSave(memberSql);
				}
				logger.debug("group_id return :"+group_id);
				return group_id;
			}
		} else {
			String group_id = json.getString("group_id");
			sql = "update t_groupMember set status="+status+" where group_id="+group_id+"";
			logger.debug("update group status:"+sql);
			baseDao.execSave(sql);
			return -1;
		}
		return 0;
	}
	
	public boolean addGroupMember(JSONObject json){
		//String userId = json.getString("from");
		String groupId = json.getString("to");
		String ids = json.getString("ids");
		String []users = ids.split(",");
		for(int i = 0,j=users.length;i<j;i++){
			String findSql = "select 1 from t_groupmember where group_id="+groupId+" and user_id="+users[i]+"";
			BaseDao baseDao = BaseDao.getBaseDao();
			Map<String,String> _m= baseDao.getFistRow(findSql);
			logger.debug("_m::"+_m);
			//logger.debug("size:"+_m.size());
			if(_m!=null&&_m.size()>0) continue; 
			String sql = "insert into t_groupmember (group_id,user_id) values("+groupId+","+users[i]+")";
			logger.debug("add group member:" + sql);
			baseDao.execSave(sql);
		}
		return true;
	}
	
	public boolean removeGroupMember(JSONObject json){
		String groupId = json.getString("to");
		//String from = json.getString("from");
		String ids = json.getString("ids");
		String []users = ids.split(",");
		BaseDao baseDao = BaseDao.getBaseDao();
		for(int i = 0,j=users.length;i<j;i++){
			//delete from `t_groupmember` where group_id=1 and user_id=2
			//String sql = "delete from t_groupmember (group_id,user_id) values("+groupId+","+users[i]+")";
			//if(from.equals(users[i])) continue;
			String sql = "delete from `t_groupmember` where group_id="+groupId+" and user_id="+users[i]+"";
			baseDao.execSave(sql);
		}
		
		List <Map<String,String>> list = this.getGroupUsers(groupId);
		if(list==null||list.size()==0){//组内没有成员将删除该组
			String t_group = "delete from t_group where id="+groupId;
			baseDao.execSave(t_group);
		}
		
		return true;
	}
	
}
