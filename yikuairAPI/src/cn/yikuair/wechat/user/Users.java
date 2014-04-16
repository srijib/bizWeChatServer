package cn.yikuair.wechat.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import cn.yikuair.base.Log;
import cn.yikuair.beans.LogMsg;
import cn.yikuair.business.UserManager;
import cn.yikuair.dao.BaseDao;
import cn.yikuair.utils.DataUtil;
import cn.yikuair.utils.HttpParams;
import cn.yikuair.utils.JsonUtil;

public class Users {
	static Logger logger = Log.getLogger(Users.class);
	public String getUsers(HttpParams params){
		String com_id = params.getStr("com_id", "");
		String username = params.getStr("username", "");
		String password = params.getStr("password", "");
		String de_name = params.getStr("de_name", "");
		String user_id = params.getStr("id", "");
		LogMsg logMsg = new LogMsg();
		if(com_id.equals("")){
			logMsg.setCode(201);   
			logMsg.setMessage("公司id不能为空");
			return JsonUtil.ObjectToJsonString(logMsg);
		} else {
			UserManager manager = UserManager.getUserManager();
			List <Map<String,String>> list = manager.getList(username,password,com_id,de_name,user_id);
			
			
			logger.debug("list size:"+list.size());
			
			
			if(list==null||list.size()==0){
				logMsg.setCode(404);
				logMsg.setMessage("该公司id不存在");
				return JsonUtil.ObjectToJsonString(logMsg);
			}
			
			logMsg.setCode(200);
			logMsg.setMessage("success");
			List <Map<String,String>> de_names = manager.getDe_names(com_id,de_name);
			
			//获取所有的拼音存在的数组
			List <Map<String,String>> pinyins = manager.getPinyinGroupBy(com_id,de_name);
			Map <String,Object> dataMap = new HashMap <String,Object>();
			
			
			
			
			dataMap.put("contacts", list);
			dataMap.put("de_names", de_names);
			dataMap.put("pinyins", pinyins);
			
			
			//添加黑名单 blacks
			
			List <Map<String,String>> blacks  = manager.getBlackList(user_id);
			dataMap.put("blacks", blacks);
			
			//添加黑名用http方式
			
			List <Map<String,String>> tomes  = manager.getBlackToMeList(user_id);
			dataMap.put("tomes", tomes);
			
			logMsg.setData(dataMap);
			
			return JsonUtil.ObjectToJsonString(logMsg);
		}
	}

	
	public String getUser (HttpParams params){
		String username = params.getStr("username", "");
		String password = params.getStr("password", "");
		String id = params.getStr("id", "");
		if(username.equals("")||password.equals("")){
			LogMsg err = new LogMsg();
			err.setMessage("用户名和密码不能为空");
			return JsonUtil.ObjectToJsonString(err);
		} else {
			LogMsg logMsg  = new LogMsg();
			UserManager userManager = UserManager.getUserManager();
			List<Map<String,String>> list = userManager.getUserById(id);
			if(list.size()==0){
				logMsg.setCode(201);
				logMsg.setMessage("查找无该用户");
			} else{
				logMsg.setCode(200);
				logMsg.setMessage("success");
				logMsg.setData(list);
			}
			return JsonUtil.ObjectToJsonString(logMsg);
		}
	}
	
	public String getPageUser (HttpParams params) {
		String com_id = params.getStr("com_id", "");
		String star = params.getStr("star", "");
		String pageIndex = params.getStr("pageIndex", "");
		String pageSize = params.getStr("pageSize", "");
		
		logger.info("com_id"+com_id);

		
		UserManager manager = UserManager.getUserManager();
		int total = manager.getTotalComMember(com_id, star,params);
		
		logger.info("total::"+total);
		
		int index = 1,size=10;
		if(!pageIndex.equals("")){
			index = Integer.parseInt(pageIndex);
			size = Integer.parseInt(pageSize);
		}
		
		List<Map <String,String>> list = manager.getPageComMember(com_id, star, index, size,params);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("total", total);
		map.put("pageIndex", pageIndex);
		map.put("data", list);
		return JsonUtil.ObjectToJsonString(map);
	}
	
	
	public String addUser(HttpParams params) {
		String com_id = params.getStr("com_id", "");
		String realname = params.getStr("realname", "");
		String username = params.getStr("username", "");
		String de_name = params.getStr("de_name", "");
		Map <String,String>map = new HashMap<String,String>();
		logger.info("realname::"+realname);
		
		String message = "";
		if (com_id.equals("")) {
			message = "公司id不能为空";
		} else if (realname.equals("")) {
			message = "名字不能为空";
		} else if (de_name.equals("")){
			message = "部门不能为空";
		} else if (username.equals("")) {
			message = "用户名不能为空";
		}
		if (!message.equals("")) {
			map.put("code", "201");
			map.put("message", message);
			return JsonUtil.ObjectToJsonString(map);
		}  else {
			UserManager manager = UserManager.getUserManager();
			boolean bool = manager.addUser(params);
			if(!bool) {
				map.put("code", "201");
				map.put("message", "添加用户失败，请检查是否已经添加");
				
				return JsonUtil.ObjectToJsonString(map);
			}
			
			map.put("code", "200");
			map.put("message", "success");
			return JsonUtil.ObjectToJsonString(map);
		}
		
	}
	
	public String deleteUsers (HttpParams params) {
		String ids = params.getStr("ids", "");
		Map <String,String>map = new HashMap<String,String>();
		if(ids.equals("")){
			map.put("code", "201");
			map.put("message", "没有删除者id");
			return JsonUtil.ObjectToJsonString(map);
		}
		UserManager manager = UserManager.getUserManager();
		manager.deleteUser(ids);
		map.put("code", "200");
		map.put("message", "success");
		return JsonUtil.ObjectToJsonString(map);
	}
	
	
	public String editUsers(HttpParams params){
		String ids = params.getStr("ids", "");
		Map <String,String>map = new HashMap<String,String>();
		if(ids.equals("")){
			map.put("code", "201");
			map.put("message", "请选择修改者");
			return JsonUtil.ObjectToJsonString(map);
		}
		String sql = "update t_userinfo set password = 'E10ADC3949BA59ABBE56E057F20F883E' where id in ("+ids+")";
		BaseDao baseDao = BaseDao.getBaseDao();
		baseDao.execSave(sql);
		map.put("code", "200");
		map.put("message", "success");
		return JsonUtil.ObjectToJsonString(map);
	}
	
	public String editUsersJson (String s) {
		logger.info("ss:"+s);
		JSONObject json = JsonUtil.StringToObject(s);
		if(json.has("signature")) {
			String sinature = json.getString("signature");
			sinature = DataUtil.ecodeBase64(sinature.getBytes());
			json.accumulate("sinature", sinature);
		}
		
		UserManager manager = UserManager.getUserManager();
		manager.userSave(json);
		return "";
	}
	
	
	
	public String queryRealName (HttpParams params){
		String com_id = params.getStr("com_id", "");
		String realname = params.getStr("realname","");
		
		logger.info(com_id+"----"+realname);
		UserManager manager = UserManager.getUserManager();
		List<Map <String,String>> list = manager.queryRealName(com_id, realname);
		
		Map <String,Object>map = new HashMap<String,Object>();
		map.put("code", "200");
		map.put("data", list);
		return JsonUtil.ObjectToJsonString(map);
	}
}
