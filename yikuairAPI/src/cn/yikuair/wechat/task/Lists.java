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

public class Lists {
	private static Logger logger = Log.getLogger(Lists.class);
	public String getTasks(HttpParams params){
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
		
		String sql = "select b.title as title,b.address as address, b.user_id as from_id,b.filepath as filepath,b.createTime as createTime," +
				" b.btime as btime ,b.etime as etime " +
				"from t_taskcomfirm a left join t_tasklist b on a.task_id = b.id where a.user_id="+user_id+" and (b.btime >= '"+DateUtil.getTimeString("yyyy-MM-dd HH:mm:ss")+"') order by b.createTime";
		logger.debug("tasks:"+sql);
		BaseDao dao = BaseDao.getBaseDao();
		List <Map<String,String>> list= dao.getData(sql);
		LogMsg logMsg = new LogMsg();
		logMsg.setCode(200);
		logMsg.setMessage("success");
		logMsg.setData(list);
		return JsonUtil.ObjectToJsonString(logMsg);
	}
}
