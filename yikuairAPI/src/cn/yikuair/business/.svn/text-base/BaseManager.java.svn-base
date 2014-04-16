package cn.yikuair.business;

import org.apache.log4j.Logger;

import cn.yikuair.base.Log;
import cn.yikuair.dao.BaseDao;

public class BaseManager {
	static Logger logger = Log.getLogger(BaseManager.class);
	private static BaseManager baseManager = null;
	public static BaseManager getBaseManager(){
		if(baseManager==null){
			baseManager = new BaseManager();
		}
		return baseManager;
	}
	public boolean saveEmail(String email){
		String sql = "insert into t_email(email) values('"+email+"')";
		logger.debug("save email:"+email);
		BaseDao baseDao = BaseDao.getBaseDao();
		baseDao.execSave(sql);
		return true;
	}
} 
