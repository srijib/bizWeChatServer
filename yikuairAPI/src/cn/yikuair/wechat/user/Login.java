package cn.yikuair.wechat.user;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.yikuair.base.Log;
import cn.yikuair.beans.LogMsg;
import cn.yikuair.business.UserManager;
import cn.yikuair.dao.BaseDao;
import cn.yikuair.message.SocketThread;
import cn.yikuair.utils.DataUtil;
import cn.yikuair.utils.HttpParams;
import cn.yikuair.utils.JsonUtil;
import cn.yikuair.utils.Md5Util;
import cn.yikuair.utils.UuidUtil;

public class Login {
	private static Logger logger = Log.getLogger(Login.class);
	public String getUser(HttpParams params){
		String username = params.getStr("username", "");
		String password = params.getStr("password", "");
		String token = params.getStr("token", "");
		String machinetype = params.getStr("device","0"); 
		logger.debug(username+" : "+password);
		if(username.equals("")||password.equals("")){
			LogMsg err = new LogMsg();
			err.setMessage("用户名和密码不能为空");
			return JsonUtil.ObjectToJsonString(err);
		} else {
			BaseDao dao = BaseDao.getBaseDao();
			//password = Md5Util.MD5(password);//正式环境要去掉
			
			//修改token
			String updateToken = "update t_userinfo set token='"+token+"',machinetype='"+machinetype+"' where username='"+username+"' and password='"+password+"'";
			logger.debug("updateTokenSql:"+updateToken);
			dao.execSave(updateToken);
			
			
			Map <String,String> map = dao.getFistRow("select * from t_userinfo where username='"+username+"' and password='"+password+"'");
			if(map==null||map.size()==0){
				LogMsg err = new LogMsg();
				err.setCode(404);
				err.setMessage("用户名和密码错误");
				return JsonUtil.ObjectToJsonString(err);
			}
			LogMsg logMsg = new LogMsg();
			logMsg.setCode(200);
			logMsg.setMessage("success");
			logMsg.setData(map);
			
			//登陆成功挤掉在线的token
			String old_id = map.get("id");
			SocketThread.logOut(old_id);			
			return JsonUtil.ObjectToJsonString(logMsg);
		}
	}
	
	public String validateLogin (String username,String password) throws Exception {
		UserManager manager = UserManager.getUserManager();
		
		Map<String , String> dataMap = new HashMap<String,String>();
		password = Md5Util.MD5(password);
		Map <String,String> map = manager.getLoginData(username, password);
		logger.debug(username+"---"+password);
		LogMsg msg = new LogMsg();
		if(map==null||map.size()==0){
			dataMap.put("code", "404");
			dataMap.put("message", "用户名和密码错误");
			return JsonUtil.ObjectToJsonString(dataMap);
		}
		String own = map.get("own");
		String star = map.get("star");
		
		if(own.equals("com")||star.equals("1")) { //公司全线
			dataMap.put("code", "200");
			dataMap.put("message", "success");
			dataMap.put("own", own);
			dataMap.put("msguuid",UuidUtil.getUUID());
			dataMap.put("id", map.get("id"));
			String com_id = map.get("com_id");
			dataMap.put("com_id", com_id);
			dataMap.put("star", star);
			dataMap.put("username", username);
			String comname = manager.getCom_name(com_id);
			dataMap.put("comname", comname);
			
		} else {
			msg.setCode(202);
			msg.setMessage("没有权限登陆");
			return JsonUtil.ObjectToJsonString(msg);
		}
		msg.setCode(200);
		String s = JsonUtil.ObjectToJsonString(dataMap);
		s = DataUtil.encodeECBAsBase64String("_yikuair",s);
		//s = URLEncoder.encode(s,"utf-8");
		msg.setMessage(s);
		Map <String,String> _map = new HashMap<String,String>();
		_map.put("star", star);
		_map.put("own", own);
		msg.setData(_map);
		return JsonUtil.ObjectToJsonString(msg);
	}
}
