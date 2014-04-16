package cn.yikuair.business;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.yikuair.base.Log;
import cn.yikuair.dao.BaseDao;

public class TinderManager {
	
	static Logger logger = Log.getLogger(TinderManager.class);
	private static TinderManager tinderManager = null;
	public static TinderManager getTinderManager(){
		if(tinderManager==null){
			tinderManager = new TinderManager();
		}
		return tinderManager;
	}
	
	public boolean upLoadHeader(String id,String headUrl,String nickname,String sex){
		try{
			String sql = "select 1 from t_tinder where id="+id;
			BaseDao baseDao = BaseDao.getBaseDao();
			int count = baseDao.getCount(sql);
			String execSql = "update t_tinder set headurl='"+headUrl+"', nickname='"+nickname+"',sex='"+sex+"' where id='"+id+"'";
			if(count==0) {
				execSql = "insert into t_tinder (id,nickname,headurl,sex) values('"+id+"','"+nickname+"','"+headUrl+"','"+sex+"')";
			}
			baseDao.execSave(execSql);
			return true;
		} catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
	
	public List <Map<String,String>> getTinder (String id) {
		String sql = "select * from t_tinder where id='"+id+"'";
		BaseDao baseDao = BaseDao.getBaseDao();
		return baseDao.getData(sql);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
