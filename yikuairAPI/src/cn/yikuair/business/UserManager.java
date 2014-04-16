package cn.yikuair.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import cn.yikuair.base.Log;
import cn.yikuair.beans.LogMsg;
import cn.yikuair.beans.SystemMessage;
import cn.yikuair.dao.BaseDao;
import cn.yikuair.message.SocketConfig;
import cn.yikuair.message.SocketThread;
import cn.yikuair.utils.ExcelUtil;
import cn.yikuair.utils.HttpParams;
import cn.yikuair.utils.JsonUtil;
import cn.yikuair.utils.PinyinUtil;
import cn.yikuair.utils.UuidUtil;

public class UserManager {
	static Logger logger = Log.getLogger(UserManager.class);
	private static UserManager userManager = null;
	public static UserManager getUserManager(){
		if(userManager==null){
			userManager = new UserManager();
		}
		return userManager;
	}
	
	public boolean login (String username,String password){
		BaseDao dao = BaseDao.getBaseDao();
		Map <String,String> map = dao.getFistRow("select 1 from t_userinfo where username='"+username+"' and password='"+password+"'");
		if(null!=map&&map.size()>0) return true;
		return false;
	}
	
	public String login(String username,String password,String token,String machinetype){
		LogMsg logMsg = new LogMsg();
		logMsg.setToken(SocketConfig.TOKEN_LOGIN);
		if(username.equals("")||password.equals("")){
			
			logMsg.setMessage("用户名和密码不能为空");
			return JsonUtil.ObjectToJsonString(logMsg);
		} else {
			BaseDao dao = BaseDao.getBaseDao();
			//password = Md5Util.MD5(password);//正式环境要去掉
			
			
			
			//查找机器是否为第一次登陆
//			String findMachineTypeSql =  "select machinetype from t_userinfo where username='"+username+"' and password='"+password+"'";
//			List <Map <String,String>> machineV =  dao.getData(findMachineTypeSql);
//			Map <String,String> dataMap = new HashMap<String,String>();
//			if(machineV!=null&&machineV.size()>0){
//				Map <String,String> mm = machineV.get(0);
//				String machine = mm.get("machinetype");
//				if(machine!=null){
//					
//					if(machine.equals("1")||machine.equals("0")){
//						dataMap.put("updatepwd", "1");
//					} else {
//						dataMap.put("updatepwd", "0");
//					}
//					
//					//logMsg.setData(machine);
//				} else {
//					dataMap.put("updatepwd", "0");
//				}
//			} else {
//				dataMap.put("updatepwd", "0");
//			}
			
			
			//修改token
			String updateToken = "update t_userinfo set token='"+token+"',machinetype='"+machinetype+"' where username='"+username+"' and password='"+password+"'";
			logger.debug("updateTokenSql:"+updateToken);
			dao.execSave(updateToken);
			
			
			Map <String,String> map = dao.getFistRow("select * from t_userinfo where username='"+username+"' and password='"+password+"'");
			if(map==null||map.size()==0){
				logMsg.setCode(404);
				logMsg.setMessage("用户名和密码错误");
				return JsonUtil.ObjectToJsonString(logMsg);
			}
			logMsg.setCode(200);
			logMsg.setMessage("success");//E10ADC3949BA59ABBE56E057F20F883E
			if("E10ADC3949BA59ABBE56E057F20F883E".equals(password)){
				map.put("updatepwd","0");
			} else {
				map.put("updatepwd","1");
			}
			
			
			logMsg.setData(map);
			
			//登陆成功挤掉在线的token
			String old_id = map.get("id");
			SocketThread.logOut(old_id);			
			return JsonUtil.ObjectToJsonString(logMsg);
		}
	}
	
	//
	public Map <String,String>  getLoginData(String username,String password) {
		BaseDao baseDao = BaseDao.getBaseDao();
		String sql = "select * from t_userinfo where username='"+username+"' and password='"+password+"'";
		Map <String,String> map  = baseDao.getFistRow(sql);
		return map;
	}
	
	
	public List <Map<String,String>> getList(String username,String password,String com_id,String de_name,String user_id){
		
		BaseDao baseDao = BaseDao.getBaseDao();
		String friendsql = "select launch_id,to_id from t_joinFriend where (launch_id="+user_id+" or to_id="+user_id+") and isFriend=1";
		logger.debug("friendsql:"+friendsql);
		List <Map<String,String>> friendlist = baseDao.getData(friendsql);
		List <String>fl = new ArrayList<String>();
		for(int i=0,j=friendlist.size();i<j;i++){
			Map <String,String> _map = friendlist.get(i);
			String launch_id = _map.get("launch_id");
			String to_id = _map.get("to_id");
			if(!user_id.equals(launch_id)){
				fl.add(launch_id);
			} else {
				fl.add(to_id);
			}
		}
		StringBuffer  sb =  new StringBuffer();
		for(int i=0,j=fl.size();i<j;i++){
			if(i==j-1){
				sb.append(fl.get(i));
			} else {
				sb.append(fl.get(i)+",");
			}
		}
		String joinIn = "";
		if(sb.length()!=0){
			joinIn = "or id in ("+sb.toString()+")";
		}
		
		String sql = "select * from t_userinfo where (com_id="+com_id +" and (de_name='"+de_name+"' or interface='1')) "+joinIn+" order by pinyin asc";
		logger.debug(com_id+"公司列表:"+sql);
		
		List <Map<String,String>> list = baseDao.getData(sql);
		logger.debug("list"+list.size());
		return list;
	}
	
	

	
	
	
	public List <String> getListId(String com_id,String de_name,String user_id){
		
		BaseDao baseDao = BaseDao.getBaseDao();
		String friendsql = "select launch_id,to_id from t_joinFriend where (launch_id="+user_id+" or to_id="+user_id+") and isFriend=1";
		logger.debug("friendsql:"+friendsql);
		List <Map<String,String>> friendlist = baseDao.getData(friendsql);
		List <String>fl = new ArrayList<String>();
		for(int i=0,j=friendlist.size();i<j;i++){
			Map <String,String> _map = friendlist.get(i);
			String launch_id = _map.get("launch_id");
			String to_id = _map.get("to_id");
			if(!user_id.equals(launch_id)){
				fl.add(launch_id);
			} else {
				fl.add(to_id);
			}
		}
		StringBuffer  sb =  new StringBuffer();
		for(int i=0,j=fl.size();i<j;i++){
			if(i==j-1){
				sb.append(fl.get(i));
			} else {
				sb.append(fl.get(i)+",");
			}
		}
		String joinIn = "";
		if(sb.length()!=0){
			joinIn = "or id in ("+sb.toString()+")";
		}
		
		String sql = "select id from t_userinfo where (com_id="+com_id +" and (de_name='"+de_name+"' or interface='1')) "+joinIn+" order by pinyin asc";
		logger.debug(com_id+"公司列表:"+sql);
		
		List <Map<String,String>> list = baseDao.getData(sql);
		List <String> ids = new ArrayList<String>();
		for(int i=0,j=list.size();i<j;i++){
			Map<String,String> m = list.get(i);
			String friendId = m.get("id");
			ids.add(friendId);
		}
		return ids;
	}
	
	
	
	public List <Map<String,String>> getDe_names(String com_id,String de_name){
		//获取分组信息
		
		if(com_id.equals("3")){
			String []s = {"龍華人力資源部","廊坊人力資源部","華北衛生部","薪酬績效管理中心","人資規畫暨專案部","海外人資管理部","廊坊黨委辦","NLV北京營運管理產品處","人力資源處","組裝部","北遷專案部","組裝二部","總務部","LF資訊科技部","工会","一块儿"};
			
			List <Map<String,String>> list = new ArrayList<Map<String,String>>();
			for(int i=0,j=s.length;i<j;i++){
				Map<String,String> map = new HashMap<String,String>();
				map.put("de_name", s[i]);
				map.put("count", "2");
				list.add(map);
			}
			return list;
			
		} else {
			BaseDao dao = BaseDao.getBaseDao();
			List <Map<String,String>> list = dao.getData("select de_name, count(de_name) as count from t_userinfo   where com_id="+com_id+" and (de_name='"+de_name+"' or interface='1')  group by `de_name`");
			return list;
		}
		
		
	}
	
	public List <Map<String,String>> getPinyinGroupBy(String com_id,String de_name){
		BaseDao dao = BaseDao.getBaseDao();
		List <Map<String,String>> list  = dao.getData("select pinyin from t_userinfo  where pinyin != 'null' and pinyin!='' and com_id="+com_id+" and (de_name='"+de_name+"' or interface='1')  group by pinyin");
		return list;
	}
	
	
	public int userSave(JSONObject json){
		StringBuffer sb  = new StringBuffer();
		Map<String,String> saveMap  = new HashMap<String,String>();
		String id = "";
		if(json.has("from")){
			id = json.getString("from");
			saveMap.put("from", id);
		} else {
			logger.debug("该用户id不存在:"+id);
			return 0;
		}
		sb.append("update t_userinfo set ");
		if(json.has("nickname")){
			String nickname = json.getString("nickname");
			sb.append("nickname = '"+nickname+"',");
			saveMap.put("nickname", nickname);
		}
		
		if(json.has("realname")){
			String realname = json.getString("realname");
			sb.append("realname = '"+realname+"',");
			saveMap.put("realname", realname);
			String pinyin = PinyinUtil.getFirstCharToString(realname);
			sb.append("pinyin = '"+pinyin+"',");
		}
		
		if(json.has("password")){
			String password = json.getString("password");
			sb.append("password = '"+password+"',");
			saveMap.put("password", password);
		}
		
		
		
		if(json.has("age")){
			String age = json.getString("age");
			sb.append("age = "+age+",");
			saveMap.put("age", age);
		}
		
		
		if(json.has("birthday")){
			String birthday = json.getString("birthday");
			sb.append("birthday = '"+birthday+"',");
			saveMap.put("birthday", birthday);
		}
		
		
		if(json.has("entrytime")){
			String entrytime = json.getString("entrytime");
			sb.append("entrytime = '"+entrytime+"',");
			saveMap.put("entrytime", entrytime);
		}
		
		
		if(json.has("hometown")){
			String hometown = json.getString("hometown");
			sb.append("hometown = '"+hometown+"',");
			saveMap.put("hometown", hometown);
		}
		
		
		if(json.has("address")){
			String address = json.getString("address");
			sb.append("address = '"+address+"',");
			saveMap.put("address", address);
		}
		
		
		if(json.has("sex")){
			String sex = json.getString("sex");
			sb.append("sex = "+sex+",");
			saveMap.put("sex", sex);
		}
		
		
		if(json.has("mobile")){
			String mobile = json.getString("mobile");
			sb.append("mobile = '"+mobile+"',");
			saveMap.put("mobile", mobile);
		}
		
		if(json.has("dataopen")){
			String dataopen = json.getString("dataopen");
			sb.append("dataopen = "+dataopen+",");
			saveMap.put("dataopen", dataopen);
		}
		
		
		if(json.has("phone")){
			String phone = json.getString("phone");
			sb.append("phone = '"+phone+"',");
			saveMap.put("phone", phone);
		}
		
		
		if(json.has("email")){
			String email = json.getString("email");
			sb.append("email = '"+email+"',");
			saveMap.put("email", email);
		}
		
		
		if(json.has("signature")){
			String signature = json.getString("signature");
			sb.append("signature = '"+signature+"',");
			saveMap.put("signature", signature);
		}
		
		
		if(json.has("longAlt")){
			String longAlt = json.getString("longAlt");
			sb.append("longAlt = '"+longAlt+"',");
			saveMap.put("longAlt", longAlt);
		}
		
		
		if(json.has("machinetype")){
			String machinetype = json.getString("machinetype");
			sb.append("machinetype = '"+machinetype+"',");
			saveMap.put("machinetype", machinetype);
		}
		
		if(json.has("headurl")){ //修改头像
			String headurl = json.getString("headurl");
			sb.append("headurl = '"+headurl+"',");
			saveMap.put("headurl", headurl);
		}
		
		String uuid = "";
		if(json.has("msguuid")){
			uuid = json.getString("msguuid");
		} else {
			uuid = UuidUtil.getUUID();
		}
		
		
		
		sb = new StringBuffer(sb.substring(0, sb.length()-1));
		
		sb.append(" where id="+id);
		
		String sql = sb.toString();
		logger.debug("update user : "+sql);
		
		BaseDao baseDao = BaseDao.getBaseDao();
		int count = baseDao.execSave(sql);
		
		if(count>0){// socket to client friend by update message
			
			SystemMessage systemMessage = new SystemMessage();
			systemMessage.setCode(200);
			systemMessage.setToken(SocketConfig.TOKEN_USERINFO_SAVE);//用户的资料修改
			systemMessage.setMsguuid(uuid);
			saveMap.put("msguuid", uuid);
			systemMessage.setData(saveMap);
			systemMessage.setLongDate(new Date().getTime()+"");
			
			String systemData = JsonUtil.ObjectToJsonString(systemMessage);
			
			//查找该的id的好友（同公司的所有的人）
			//List <String> coworks = this.getCoWorkers(id);
			

			
			List<Map<String,String>> users = this.getUserById(id);
			Map <String,String>mapUser = users.get(0);
			
			List <String> coworks = this.getListId(mapUser.get("com_id"), mapUser.get("de_name"), id);
			
			MessageManager messageManager = MessageManager.getMessageManager();
			messageManager.saveSystemMessage(systemMessage, systemData, coworks);
			
			
			//给在线的用户发送socket通知
			for(int i=0,j=coworks.size();i<j;i++){
				//logger.debug("insert :::"+i+"----"+coworks.get(i)+"----"+systemData);
				SocketThread.getSocketToMessage(coworks.get(i), systemData,SocketConfig.TOKEN_USERINFO_SAVE,id,"");
			}
			//logger.debug("into 2222222");
		}
		
		return count;
	}
	
	//查找组内成员
	public List<String> getGroupUsers(String groupId){
		String sql = "select user_id from t_groupMember where group_id = "+groupId;
		BaseDao baseDao = BaseDao.getBaseDao();
		logger.debug("find group id,sql:"+sql);
		List <Map<String,String>> list = baseDao.getData(sql);
		List <String>ids = new ArrayList<String>();
		for(int i=0,j=list.size();i<j;i++){
			Map <String,String>map  = list.get(i);
			String id = map.get("user_id");
			ids.add(id); 
		}
		return ids;
	}
	//查找all的公司同事
	public List <String> getCoWorkers(String id){
		String findComIdSql = "select a.id as id from t_userInfo a left join t_com b on a.com_id = b.id where a.id!="+id;
		BaseDao baseDao = BaseDao.getBaseDao();
		List <String>ids = new ArrayList<String>();
		List <Map<String,String>> list = baseDao.getData(findComIdSql);	
		for(int i=0,j=list.size();i<j;i++){
			Map<String,String> m = list.get(i);
			String friendId = m.get("id");
			ids.add(friendId);
		}
		return ids;
	}
	
	public String inportUsers(String com_id,String path){
		List<Map<String,String>> list  = ExcelUtil.getXlsUserInfoData(path);
		
		if(list!=null){
			for(int i=0,j=list.size();i<j;i++){
				Map <String,String> map = list.get(i);
				//String com_name = map.get("公司名");
				String realname = map.get("姓名");
				logger.info("realname::"+realname);
				String email = map.get("邮箱");
				String username = map.get("员工ID");
				String sex = map.get("性别").equals("男")?"1":"0";
				String de_name = map.get("部门");
				String duty = map.get("职位");
				String mobile = map.get("手机号");
				String phone = map.get("座机号");
				String _interface = map.get("接口人").equals("1")?"1":"0";
				String own = map.get("通讯录权限");
				
				String pinyin = PinyinUtil.getFirstCharToString(realname).toUpperCase();
				
				String sql = "insert into t_userinfo(com_id,realname,email,username,sex,de_name,duty,mobile,phone,interface,own,pinyin) " +
						"values ("+com_id+",'"+realname+"','"+email+"','"+username+"',"+sex+",'"+de_name+"','"+duty+"','"+mobile+"','"+phone+"','"+_interface+"','"+own+"','"+pinyin+"')";
				BaseDao baseDao = BaseDao.getBaseDao();
				
				logger.debug("inport:"+sql);
				
				baseDao.execSave(sql);
				
			}
		}
		return null;
	}
	
	
	public List<Map<String,String>> getUserById(String id){
		String sql = "select * from t_userinfo where id='"+id+"'";
		logger.debug("get search sql:"+sql);
		BaseDao baseDao = BaseDao.getBaseDao();
		List<Map<String,String>> list = baseDao.getData(sql);
		return list;
	}
	
	
	
	public List<Map<String,String>> getUserByMobile(String mobile){
		String sql = "select * from t_userinfo where mobile='"+mobile+"'";
		logger.debug("get search sql by mobile:"+sql);
		BaseDao baseDao = BaseDao.getBaseDao();
		List<Map<String,String>> list = baseDao.getData(sql);
		return list;
	}
	
	
	public String getNameById(String id){
		String sql = "select realname from t_userinfo where id='"+id+"'";
		BaseDao baseDao = BaseDao.getBaseDao();
		List<Map<String,String>> list = baseDao.getData(sql);
		if(list!=null&&list.size()>0){
			return list.get(0).get("realname");
		}
		
		return "";
	}
	
	public String getIdByName(String id){
		String sql = "select id from t_userinfo where username='"+id+"'";
		BaseDao baseDao = BaseDao.getBaseDao();
		List<Map<String,String>> list = baseDao.getData(sql);
		if(list!=null&&list.size()>0){
			return list.get(0).get("id");
		}
		
		return "";
	}
	
	
	
	public String getuserNameById(String id){
		String sql = "select username from t_userinfo where id='"+id+"'";
		BaseDao baseDao = BaseDao.getBaseDao();
		List<Map<String,String>> list = baseDao.getData(sql);
		if(list!=null&&list.size()>0){
			return list.get(0).get("username");
		}
		
		return "";
	}
	
	
	
	public int joinFriend(String from,String to,String type){
		String sql = "select 1 from t_joinFriend where launch_id="+from+" and to_id="+to+"";
		BaseDao baseDao = BaseDao.getBaseDao();
		List<Map<String,String>> list = baseDao.getData(sql);
		if(list.size()==0){
			String insql = "";
			if(type.equals("4")){
				insql = "insert into t_joinFriend(launch_id,to_id,isFriend) values("+from+","+to+",1)";
			}else 
				insql = "insert into t_joinFriend(launch_id,to_id) values("+from+","+to+")";
			logger.debug("加好友信息："+insql);
			return baseDao.execSave(insql);
		}
		return 0;
	}
	
	public void removeFriend(String from,String to){
		String sql = "delete from t_joinFriend where (launch_id="+from+" and to_id="+to+") or (launch_id="+to+" and to_id="+from+")";
		BaseDao baseDao = BaseDao.getBaseDao();
		logger.debug("delete friend:"+sql);
		baseDao.execSave(sql);
	}
	
	
	public boolean comfirmFriend(String from,String to){
		String sql = "select 1 from t_joinFriend where launch_id="+from+" and to_id="+to+"";
		logger.info("comfirm::"+sql);
		BaseDao baseDao = BaseDao.getBaseDao();
		List<Map<String,String>> list = baseDao.getData(sql);
		if(list.size()>0){
			String insql = "update t_joinFriend set isFriend=1 where launch_id="+from+" and to_id="+to+"";
			baseDao.execSave(insql);
			logger.debug("加好友信息："+insql);
			baseDao.execSave(insql);
			return true;
		}
		return false;
	}
	
	//我把人添加到黑名单的列表
	public List<Map<String,String>> getBlackList(String user_id){
		String sql = "select black_id  from t_blacklist where user_id="+user_id;
		BaseDao baseDao = BaseDao.getBaseDao();
		List<Map<String,String>> list = baseDao.getData(sql);
		return list;
	}
	
	//别人把我添加到黑名单的列表
	public List<Map<String,String>> getBlackToMeList(String user_id){
		String sql = "select user_id from t_blacklist where black_id = "+user_id;
		BaseDao baseDao = BaseDao.getBaseDao();
		List<Map<String,String>> list = baseDao.getData(sql);
		return list;
	}
	
	public int addBlack(String from,String to){
		String sql = "select count(1) as count from t_blacklist where user_id="+from +" and black_id = "+to;
		BaseDao baseDao = BaseDao.getBaseDao();
		int count = baseDao.getCount(sql);
		String saveSql = "";
		if(count==0){
			saveSql = "insert into t_blacklist(user_id,black_id) values("+from+","+to+")";
			logger.debug("insert blacklist sql:"+saveSql);
			baseDao.execSave(saveSql);
			return 1;
		}
		return 0;
	}
	
	/**
	 * 
	* @Title: validateToken 
	* @Description: 1 找到该明星账户 2 普通账户  -1用户名密码验证失败
	* @param @param username
	* @param @param password
	* @param @return    设定文件 
	* @return int    返回类型 
	* @throws
	 */
	public List<Map <String,String>> validateToken(String username,String password){
		String sql = "select id,star,interface,com_id from t_userinfo where username='"+username+"' and password='"+password+"'";
		BaseDao baseDao = BaseDao.getBaseDao();
		List <Map <String,String>>list = baseDao.getData(sql);
		return list;
//		if(list==null||list.size()==0){
//			return "-1";
//		} else {
//			Map <String,String> map = list.get(0);
//			String s = map.get("star");
//			//logger.debug("ss:"+s);
//			if(s!=null&&s.equals("1")) return map.get("id");
//			else return "-2";
//		}
	}
	
	
	public void removeBlack(String from,String to){
		String sql = "delete from t_blacklist where user_id="+from +" and black_id = "+to;
		BaseDao baseDao = BaseDao.getBaseDao();
		logger.debug("delete blacklist sql:"+sql);
		baseDao.execSave(sql);
	}
	
	public List<Map <String,String>> getUserIds(String id){
		String sql = "select launch_id as id from t_joinfriend where to_id='"+id+"' and isFriend=1";
		BaseDao baseDao = BaseDao.getBaseDao();
		List <Map <String,String>>list = baseDao.getData(sql);
		return list;
	}
	
	public List<Map <String,String>> getIdByCom(String com_id){
		String sql = "select id from t_userinfo where com_id='"+com_id+"'";
		BaseDao baseDao = BaseDao.getBaseDao();
		List <Map <String,String>>list = baseDao.getData(sql);
		return list;
	}
	
	public boolean isFriend(String from,String to){
		String sql = "select count(*) as count t_joinfriend  where ((launch_id="+from+" and to_id="+to+") or (launch_id="+to+" and to_id="+from+")) and isFriend=1";
		BaseDao dao = BaseDao.getBaseDao();
		int count = dao.getCount(sql);
		if(count==0){
			sql = "select com_id,de_name,interface from t_userinfo where id='"+from+"'";
			Map <String,String> map = dao.getFistRow(sql);
			if(null==map) return false;
			String sql2 = "select com_id,de_name,interface from t_userinfo where id='"+to+"'";
			Map <String,String> map2 = dao.getFistRow(sql2);
			if(null==map2) return false;
			String com_id = map.get("com_id");
			String com_id2 = map2.get("com_id");
			if(!com_id.equals(com_id2)) return false;//不是一个公司
			String interface1 = map.get("interface");
			String interface2 = map2.get("interface");
			if(interface1.equals("1")||interface2.equals("1")){
				return true;
			} else {
				String de_name1 = map.get("de_name");
				String de_name2 = map2.get("de_name");
				if(de_name1.equals(de_name2)) return true;
				return false;
			}
		} 
		return true;
	}
	
	public String getCom_name(String com_id) {
		String sql = "select comname from t_com where id='"+com_id+"' ";
		BaseDao baseDao = BaseDao.getBaseDao();
		Map<String,String> map = baseDao.getFistRow(sql);
		if (null!=map && map.size()>0) {
			return map.get("comname");
		}
		return null;
	}
	
	public List<Map <String,String>> getPageComMember (String com_id,String star,int index,int pageSize,HttpParams params) {
		
		StringBuffer joinCondition = new StringBuffer();
		String realname = params.getStr("realname", "");
		if(!realname.equals("")) {
			joinCondition.append(" and realname='"+realname+"' ");
		}
		String sex = params.getStr("sex", "");
		if (!sex.equals("")){
			joinCondition.append(" and sex='"+sex+"' ");
		}
		
		String username = params.getStr("username", "");
		if (!username.equals("")){
			joinCondition.append(" and username='"+username+"' ");
		}
		String mobile = params.getStr("mobile", "");
		if (!mobile.equals("")){
			joinCondition.append(" and mobile='"+mobile+"' ");
		}
		
		String de_name = params.getStr("de_name", "");
		if (!de_name.equals("")){
			joinCondition.append(" and de_name='"+de_name+"' ");
		}
		String duty = params.getStr("duty", "");
		if (!duty.equals("")){
			joinCondition.append(" and duty='"+duty+"' ");
		}
		String _interface = params.getStr("interface", "");
		if (!_interface.equals("")){
			joinCondition.append(" and interface='"+_interface+"' ");
		}
		
		String sql = "select id,username,de_name,realname,duty,mobile,sex,interface from" +
				" t_userinfo where com_id='"+com_id+"' and star='"+star+"' "+joinCondition.toString()+" order by id desc " +
						"limit "+(index-1)*pageSize+","+pageSize+"";
		logger.debug("page sql:"+sql);
		BaseDao dao = BaseDao.getBaseDao();
		List<Map <String,String>> list = dao.getData(sql);
		return list;
	}
	
	public int getTotalComMember (String com_id,String star,HttpParams params){
		

		StringBuffer joinCondition = new StringBuffer();
		String realname = params.getStr("realname", "");
		if(!realname.equals("")) {
			joinCondition.append(" and realname='"+realname+"' ");
		}
		String sex = params.getStr("sex", "");
		if (!sex.equals("")){
			joinCondition.append(" and sex='"+sex+"' ");
		}
		
		String username = params.getStr("username", "");
		if (!username.equals("")){
			joinCondition.append(" and username='"+username+"' ");
		}
		String mobile = params.getStr("mobile", "");
		if (!mobile.equals("")){
			joinCondition.append(" and mobile='"+mobile+"' ");
		}
		
		String de_name = params.getStr("de_name", "");
		if (!de_name.equals("")){
			joinCondition.append(" and de_name='"+de_name+"' ");
		}
		String duty = params.getStr("duty", "");
		if (!duty.equals("")){
			joinCondition.append(" and duty='"+duty+"' ");
		}
		String _interface = params.getStr("interface", "");
		if (!_interface.equals("")){
			joinCondition.append(" and interface='"+_interface+"' ");
		}
		
		String sql = "select count(*) as count from t_userinfo where com_id='"+com_id+"' and star='"+star+"' "+joinCondition.toString()+"";
		logger.info("sql::"+sql);
		BaseDao dao = BaseDao.getBaseDao();
		int count = dao.getCount(sql);
		return count;
	}
	
	public boolean deleteUser(String ids){
		String sql = "delete from t_userinfo where id in ("+ids+")";
		BaseDao dao = BaseDao.getBaseDao();
		dao.execSave(sql);
		return true;
	} 
	
	public boolean addUser (HttpParams params) {
		String realname = params.getStr("realname", "");
		String com_id = params.getStr("com_id", "");
		String email = params.getStr("email", "");
		String sex = params.getStr("sex", "").equals("1")?"1":"0";;
		String username = params.getStr("username", "");
		String de_name = params.getStr("de_name", "");
		String duty = params.getStr("duty", "");
		String mobile = params.getStr("mobile", "");
		String phone = params.getStr("phone", "");
		String _interface = params.getStr("interface", "").equals("1")?"1":"0";
		String  star = params.getStr("star", "0");
		
		BaseDao baseDao = BaseDao.getBaseDao();
		String issql = "select count(*) as count from t_userinfo where username='"+username+"'";
		int count = baseDao.getCount(issql);
		logger.info("info ::"+count);
		if(count>0) return false; 
		
		String own = params.getStr("own", "");
		String pinyin = PinyinUtil.getFirstCharToString(realname).toUpperCase();
		
		String sql = "insert into t_userinfo(com_id,realname,email,username,sex,de_name,duty,mobile,phone,interface,own,pinyin,star) " +
				"values ("+com_id+",'"+realname+"','"+email+"','"+username+"',"+sex+",'"+de_name+"'," +
						"'"+duty+"','"+mobile+"','"+phone+"','"+_interface+"','"+own+"','"+pinyin+"','"+star+"')";
		
		
		logger.debug("inport:"+sql);
		
		baseDao.execSave(sql);
		return true;
	}
	
	
	public List<Map <String,String>> queryRealName (String com_id,String queryString) {
		String sql = "select * from t_userinfo where realname like '%"+queryString+"%' and com_id = '"+com_id+"' limit 0,20";
		logger.info("sql::"+sql);
		BaseDao dao = BaseDao.getBaseDao();
		return dao.getData(sql);
	}
	
	
	
	public static void main(String[] args) {
		//UserManager manager = UserManager.getUserManager();
		//manager.inportUsers("1", "/Users/yikuair/Documents/workspace/yikuairAPI/WebContent/static/excel/employee_list.xls");
		
		String []s = {"龍華人力資源部","廊坊人力資源部","華北衛生部","薪酬績效管理中心","人資規畫暨專案部","海外人資管理部","廊坊黨委辦","NLV北京營運管理產品處","人力資源處","組裝部","北遷專案部","組裝二部","總務部","LF資訊科技部","工会","一块儿"};
		
		List <Map<String,String>> list = new ArrayList<Map<String,String>>();
		for(int i=0,j=s.length;i<j;i++){
			Map<String,String> map = new HashMap<String,String>();
			map.put("de_name", s[i]);
			System.out.println(s[i]);
			map.put("count", "2");
			list.add(map);
		}
		
		
		for(int i=0,j=list.size();i<j;i++){
			Map <String,String>map2 = list.get(i);
			String d = map2.get("de_name");
			
		}
		
		
	}

}
