package cn.yikuair.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.yikuair.base.Log;
import cn.yikuair.db.sql.ConnObject;
import cn.yikuair.db.sql.ConnPool;



public class BaseDao {
	static Logger logger = Log.getLogger(BaseDao.class);
	private static BaseDao baseDao = null;
	public static BaseDao getBaseDao(){
		if(baseDao==null){
			baseDao = new BaseDao();
		}
		return baseDao;
	}
	
	public List <Map<String,String>> getData(String sql){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ConnObject connObject = ConnPool.getConnection();
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		try{
			conn = connObject.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			ResultSetMetaData data = rs.getMetaData();
			int columnCount = data.getColumnCount();
			
			while(rs.next()){
				Map<String,String> map  = new HashMap<String, String>();
				for(int i=0;i<columnCount;i++){
					map.put(data.getColumnName(i+1), rs.getString(i+1));
				}
				list.add(map);
			}
		} catch(Exception ex){
			connObject.setBusy(false);
			logger.error("exec sql:"+sql+" getData:"+ex.getMessage());
		} finally{
			connObject.setBusy(false);
			closeCP(rs,pstmt);
		}
		return list;
	}
	
	
	
	
	public int execSave(String sql){
		Connection conn = null;
		PreparedStatement pstmt = null;
		int flag = 0;
		ConnObject connObject = ConnPool.getConnection();
		try{
			conn = connObject.getConnection();
			pstmt = conn.prepareStatement(sql);
			return pstmt.executeUpdate();
		} catch(Exception ex){
			connObject.setBusy(false);
			logger.error("exec sql:"+sql+" execErr:"+ex.getMessage());
		} finally{
			connObject.setBusy(false);
			closeCP(null,pstmt);
		}
		return flag;
	}
	
	public int execSaveAndPK(String sql){
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int flag = 0;
		ConnObject connObject = ConnPool.getConnection();
		try{
			conn = connObject.getConnection();
			pstmt = conn.prepareStatement(sql);
			int _f = pstmt.executeUpdate();
			if(_f>0){
				pstmt = conn.prepareStatement("SELECT @@IDENTITY as count;");
				rs = pstmt.executeQuery();
				if(rs.next()){
					return rs.getInt(1);
				}
			}
		} catch(Exception ex){
			connObject.setBusy(false);
			logger.error("exec sql:"+sql+" SELECT @@IDENTITY as count execErr:"+ex.getMessage());
		} finally{
			connObject.setBusy(false);
			closeCP(rs,pstmt);
		}
		return flag;
	}
	
	
	public void closeCP(ResultSet rs,PreparedStatement pstmt){
		try{
			if(rs!=null) rs.close();
			if(pstmt!=null) pstmt.close();
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public Map<String,String> getFistRow(String sql){
		List <Map<String,String>> list = this.getData(sql);
		logger.debug("getFistRow:"+sql);
		if(list.size()==0) return null;
		return list.get(0);
	}
	
	public int getCount(String sql){
		List <Map<String,String>> list = this.getData(sql);
		if(null==list||list.size()==0){
			logger.debug("执行的不是count的sql："+sql);
			return 0;
		}
		Map<String,String> map = list.get(0);
		String count = "0";
		if(map!=null&&(count=map.get("count"))!=null){
			return Integer.parseInt(count);
		} 
		return list.size();
	}
}
