package cn.yikuair.wechat.user;

import org.apache.log4j.Logger;

import cn.yikuair.base.Log;
import cn.yikuair.beans.LogMsg;
import cn.yikuair.dao.BaseDao;
import cn.yikuair.utils.HttpParams;
import cn.yikuair.utils.JsonUtil;
import cn.yikuair.utils.Md5Util;

public class Password {
	static Logger logger = Log.getLogger(Password.class);
	public String userAndOldpwdToNewpwd(HttpParams params){
		String username = params.getStr("username", "");
		String oldpwd = params.getStr("password", "");
		String newpwd = params.getStr("newpassword", "");
		logger.debug(username+" "+oldpwd+" : "+newpwd);
		LogMsg logMsg = new LogMsg();
		if(oldpwd.equals("")||newpwd.equals("")){
			
			logMsg.setMessage("请填写密码");
			return JsonUtil.ObjectToJsonString(logMsg);
		} else {
			BaseDao dao = new BaseDao();
			//oldpwd = Md5Util.MD5(oldpwd);
			//newpwd = Md5Util.MD5(newpwd);
			int count = dao.execSave("update t_userinfo set password='"+newpwd+"' where username='"+username+"' and password ='"+oldpwd+"'");
			logger.debug("count::"+count);
			if(count==0){
				logMsg.setCode(404);
				logMsg.setMessage("密码修改失败");
				return JsonUtil.ObjectToJsonString(logMsg);
			}
			logMsg.setCode(200);
			logMsg.setMessage("密码修改成功");
			return JsonUtil.ObjectToJsonString(logMsg);
		}
	}
}
