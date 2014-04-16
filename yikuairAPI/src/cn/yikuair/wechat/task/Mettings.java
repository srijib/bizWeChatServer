package cn.yikuair.wechat.task;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.yikuair.base.Log;
import cn.yikuair.beans.LogMsg;
import cn.yikuair.dao.BaseDao;
import cn.yikuair.utils.DateUtil;
import cn.yikuair.utils.HttpParams;
import cn.yikuair.utils.JsonUtil;

public class Mettings {
	private static Logger logger = Log.getLogger(Mettings.class);
	public String getMettings(HttpParams params){
		String user_id = params.getStr("id", "");
		
		String username = params.getStr("username", "");
		String password = params.getStr("password", "");
		
		if(username.equals("")||password.equals("")){
			LogMsg err = new LogMsg();
			err.setCode(201);
			err.setMessage("用户名和密码不能为空");
			return JsonUtil.ObjectToJsonString(err);
		}
		
		if(user_id.equals("")){
			LogMsg err = new LogMsg();
			err.setCode(201);
			err.setMessage("请传入当前id");
			return JsonUtil.ObjectToJsonString(err);
		}
		String sql = "select b.title as title,b.address as address, b.user_id as from_id," +
				"b.filepath as filepath,b.createTime as createTime, b.btime as btime ,b.etime as etime " +
				"from t_taskcomfirm a left join t_tasklist b on a.task_id = b.id where a.user_id="+user_id+"" +
						" and a.status=1 and b.type=1 and (b.btime >= '"+DateUtil.getTimeString("yyyy-MM-dd HH:mm:ss")+"') order by b.createTime";
		logger.debug("mettings:"+sql);
		BaseDao dao = BaseDao.getBaseDao();
		List <Map<String,String>> list= dao.getData(sql);
		LogMsg logMsg = new LogMsg();
		logMsg.setCode(200);
		logMsg.setMessage("success");
		logMsg.setData(list);
		return JsonUtil.ObjectToJsonString(logMsg);
	}
	
	public String comfrimMessage(HttpParams params){
		String user_id = params.getStr("id", "");
		String task_id = params.getStr("task_id", "");
		
		String username = params.getStr("username", "");
		String password = params.getStr("password", "");
		
		if(username.equals("")||password.equals("")){
			LogMsg err = new LogMsg();
			err.setCode(201);
			err.setMessage("用户名和密码不能为空");
			return JsonUtil.ObjectToJsonString(err);
		}
		
		if(user_id.equals("")||task_id.equals("")){
			LogMsg err = new LogMsg();
			err.setCode(201);
			err.setMessage("请传入当前用户id和任务id");
			return JsonUtil.ObjectToJsonString(err);
		}
		
		String sql = "update t_taskcomfirm set status=1 where user_id="+user_id+" and task_id="+task_id;
		logger.debug("确认任务消息："+sql);
		BaseDao dao = BaseDao.getBaseDao();
		int count = dao.execSave(sql);
		if(count>0){
			LogMsg logMsg = new LogMsg();
			logMsg.setCode(200);
			logMsg.setMessage("success");
			return JsonUtil.ObjectToJsonString(logMsg);
		} else {
			LogMsg logMsg = new LogMsg();
			logMsg.setCode(201);
			logMsg.setMessage("数据更新失败...");
			return JsonUtil.ObjectToJsonString(logMsg);
		}
	}
}
