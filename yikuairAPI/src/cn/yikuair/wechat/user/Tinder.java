package cn.yikuair.wechat.user;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.FileItem;

import cn.yikuair.arithmetic.LongitudeLatitude;
import cn.yikuair.base.Log;
import cn.yikuair.business.TinderManager;
import cn.yikuair.business.UserManager;
import cn.yikuair.dao.BaseDao;
import cn.yikuair.utils.Conf;
import cn.yikuair.utils.DataUtil;
import cn.yikuair.utils.DateUtil;
import cn.yikuair.utils.FileUtil;
import cn.yikuair.utils.HttpParams;
import cn.yikuair.utils.JsonUtil;
import cn.yikuair.utils.UuidUtil;
import cn.yikuair.wechat.star.Msg;

public class Tinder {
	Properties property = Conf.getProperty();
	String path = property.getProperty("path.imgpath");
	private static Logger logger = Log.getLogger(Tinder.class);
	public String uploadHeader(HttpParams params){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			FileItem fileItem = params.getFileItem("upload");
			String fileName = fileItem.getName().toLowerCase();
			logger.info("fileName:::"+fileName);
			int dottedIndex = fileName.lastIndexOf(".");
			if(dottedIndex<0) {
				map.put("code", "201");
				map.put("message","上传的文件不能识别");
				logger.debug("不能识别的文件："+fileName);
				return JsonUtil.ObjectToJsonString(map);
			} 
			String username = params.getStr("username", "");
			String password = params.getStr("password", "");
			String id = params.getStr("id", "");
			String nickname = params.getStr("nickname", "");
			String sex = params.getStr("sex", "");
			
			if(username.equals("")||password.equals("")){
				map.put("code", "201");
				map.put("message","用户名和密码不能为空");
				return JsonUtil.ObjectToJsonString(map);
			} 
			String fileType = fileName.substring(dottedIndex+1);
			byte bytes[] = fileItem.get();
			String finalPath = "";
			String time = DateUtil.getTimeString("yyyy-MM-dd");
			String name = username+"_"+new Date().getTime()+"."+fileType;
			if(fileType.equals("png")||fileType.equals("jpg")||fileType.equals("jpeg")){
				finalPath = path+"/tinder/"+time+"/"+name;
			}
			FileUtil.byteToFile(bytes, finalPath);
			
			int headerIndex = finalPath.indexOf("/tinder/");
			finalPath = finalPath.substring(headerIndex);
			
			TinderManager tinderManager = TinderManager.getTinderManager();
			boolean bool = tinderManager.upLoadHeader(id, finalPath,nickname,sex);
			if (bool) {
				map.put("code", "200");
				map.put("message", "success");
				Map<String,String> _map = new HashMap<String,String>();
				_map.put("id", id);
				_map.put("headurl", finalPath);
				map.put("data", _map);
				return JsonUtil.ObjectToJsonString(map);
			} else {
				map.put("code", "200");
				map.put("message", "sql错误");
				return JsonUtil.ObjectToJsonString(map);
			}
					
		}catch(Exception e){
			e.printStackTrace();
			map.put("code", "201");
			map.put("message", "failed");
			return JsonUtil.ObjectToJsonString(map);
		}
	}
	
	public String getTinder(HttpParams params) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		String username = params.getStr("username", "");
		String password = params.getStr("password", "");
		String id = params.getStr("id", "");
		
		if(username.equals("")||password.equals("")){
			map.put("code", "201");
			map.put("message","用户名和密码不能为空");
			return JsonUtil.ObjectToJsonString(map);
		} 
		TinderManager tinderManager = TinderManager.getTinderManager();
		List <Map<String,String>> list = tinderManager.getTinder(id);
		
		if(list.size()>0) {
			map.put("code", "200");
			map.put("message","success");
			map.put("data", list);
			
		} else {
			map.put("code", "201");
			map.put("message", "查询无此数据");
		}
		
		return JsonUtil.ObjectToJsonString(map);
	}
	
	//用来修改经纬度的值,3000米
	public String updateLocation(HttpParams params) {
		String lon = params.getStr("lon", "");
		String lan = params.getStr("lan", "");
		String id = params.getStr("id", "");
		Map<String,String> map = new HashMap<String,String>();
		
		String ssql = "select 1 from t_tinder where id="+id;
		BaseDao baseDao = BaseDao.getBaseDao();
		int count = baseDao.getCount(ssql);
		if(count==0) {
			String insql = "insert into t_tinder (id) values('"+id+"')";
			baseDao.execSave(insql);
		}
		
		logger.info(lan+" "+lon);
		
		if(lon.equals("")||lon.equals("")){
			map.put("code", "201");
			map.put("message", "木有经纬度");
			return JsonUtil.ObjectToJsonString(map);
		}
		
		//double []dou = LongitudeLatitude.getAround(new Double(lan), new Double(lon), 3000);
		double []dou = LongitudeLatitude.getRanage(new Double(lan), new Double(lon), 3);
		// minLat, minLng, maxLat, maxLng
		if(dou.length!=4) {
			map.put("code", "201");
			map.put("message", "经纬度转换错误");
			return JsonUtil.ObjectToJsonString(map);
		} 
		
		String location = dou[0]+","+dou[1]+","+dou[2]+","+dou[3];
		
		String sql = "update t_tinder set lon='"+lon+"',lan='"+lan+"',rangeLocaiton='"+location+"', uploadTime='"+DateUtil.getTimeString("yyyy-MM-dd HH:mm:ss")+"' where id = '"+id+"' ";
		
		logger.info("info:"+sql);
		

		baseDao.execSave(sql);
		
		
		map.put("code", "200");
		map.put("message", "success");
		return JsonUtil.ObjectToJsonString(map);
	}
	
	//用来查找附近的人
	public String findNearbyTinder ( HttpParams params ) {
		String id = params.getStr("id", "");
		Map<String,Object> map = new HashMap<String,Object>();
		TinderManager tinderManager = TinderManager.getTinderManager();
		List <Map<String,String>> list = tinderManager.getTinder(id);
		if(list.size()==0){
			map.put("code", "201");
			map.put("message", "查询无此人");
			return JsonUtil.ObjectToJsonString(map);
		}
		
		Map<String,String> _map = list.get(0);
		String rangeLocaiton = _map.get("rangeLocaiton");
//		String lan = _map.get("lan");
//		String lon = _map.get("lon");
		String []s = rangeLocaiton.split(",");
		
		
		
//		String sql = "select * from t_tinder where (lon!='' and lan!='') and lon > '"+s[0]+"' and lon < '"+s[1]+"'" +
//				" and lan > '"+s[2]+"' and lan < '"+s[3]+"' and id!='"+id+"'" +
//						"order by ACOS (SIN(("+lan+" * 3.1415) / 180 ) * SIN(( lan * 3.1415) / 180 ) " +
//						"COS(("+lan+" * 3.1415) / 180 ) * COS((lan * 3.1415) / 180 ) " +
//						"COS(("+lon+" * 3.1415) / 180 - (lon * 3.1415) / 180 ) ) asc limit 1,100 ";
		
		//String sql = "select * from t_tinder where (lon!='' and lan!='') and lon > '"+s[0]+"' and lon < '"+s[1]+"'" +
		//		" and lan > '"+s[2]+"' and lan < '"+s[3]+"' and id!='"+id+"'";
		//return new double[] { minLat, minLng, maxLat, maxLng };	
		
		//String excludeSql = "select user_id from t_doyoulike where img_user_id in (select user_id from t_doyoulike where img_user_id='"+id+"' and yes > 0 ) and yes > 0 ";
		String excludeSql = "select img_user_id from t_doyoulike where (yes>0 or `no`>0) and user_id = '"+id+"'";
		String sql = "SELECT * FROM t_tinder WHERE (lon!='' and lan!='') and id!='"+id+"' and id not in ("+excludeSql+")  and  ((lan BETWEEN '"+s[0]+"' AND '"+s[2]+"') AND (lon BETWEEN '"+s[1]+"' AND '"+s[3]+"')) LIMIT 0,100";		
		logger.info("qsql::"+sql);
		logger.info("lan,lon:"+sql);
		
		BaseDao baseDao  = BaseDao.getBaseDao();
		List <Map<String,String>> newlist = baseDao.getData(sql);
		map.put("code", "200");
		map.put("message", "success");
		
		map.put("data", newlist);
		
		
		return JsonUtil.ObjectToJsonString(map);
	}
	
	//lon varchar(50) default '' comment '经纬度坐标 精度',
	  //lan
	
	public String doyoulike(HttpParams params) {
		//username=&password=&id=&to=&like=
		String username = params.getStr("username", "");
		String password = params.getStr("password", "");
		String id = params.getStr("id", "");
		String to = params.getStr("to", "");
		String like = params.getStr("like", "");//1 喜欢   0 不喜欢
		
		
		Map<String,Object> map = new HashMap<String,Object>();
		

		
		if(username.equals("")||password.equals("")){
			map.put("code", "201");
			map.put("message","用户名和密码不能为空");
			return JsonUtil.ObjectToJsonString(map);
		} 
		
		String sql = "select 1 from t_doyoulike where user_id = "+id+" and img_user_id = "+to+"";
		
		logger.debug("sql::"+sql);
		
		BaseDao baseDao = BaseDao.getBaseDao();
		int count = baseDao.getCount(sql);
		if(count==0) {
			String insertsql = "insert into t_doyoulike(user_id,img_user_id,yes,no) values("+id+","+to+",0,0)";
			logger.info("insql:"+insertsql);
			baseDao.execSave(insertsql);
		}
		String updateSql = "update t_doyoulike set yes=yes+1 where user_id = "+id+" and img_user_id = "+to+"";
		
		if(like.equals("0")){
			updateSql = "update t_doyoulike set no=no+1 where user_id = "+id+" and img_user_id = "+to+"";
		}
		
		baseDao.execSave(updateSql);
		
		map.put("code", "200");
		map.put("message","success");
		
		
		//查找对方是否也喜欢你
		String findsql = "select yes as count from t_doyoulike where user_id = "+to+" and img_user_id = "+id+"";
		int yescount = baseDao.getCount(findsql);
		
		map.put("data", yescount);
		
		
		
		return JsonUtil.ObjectToJsonString(map);
		
	}
	
	public String getMatchFriend(HttpParams params) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		String username = params.getStr("username", "");
		String password = params.getStr("password", "");
		String id = params.getStr("id", "");
		
		if(username.equals("")||password.equals("")){
			map.put("code", "201");
			map.put("message","用户名和密码不能为空");
			return JsonUtil.ObjectToJsonString(map);
		} 
		
		String sql = "select * from t_tinder where id in " +
				"(select img_user_id from t_doyoulike  where  yes>0 and img_user_id in " +
				"(select user_id from t_doyoulike where img_user_id='"+id+"' and yes>0))";
		
		BaseDao baseDao = BaseDao.getBaseDao();
		List <Map<String,String>> list = baseDao.getData(sql);
		
		map.put("code", "200");
		map.put("message", "success");
		map.put("data", list);
		
		return JsonUtil.ObjectToJsonString(map);
	}
	
	public String doyoulike(String username,String password,String id,String to,String like) {
		//username=&password=&id=&to=&like=
		//1 喜欢   0 不喜欢
		
		
		Map<String,Object> map = new HashMap<String,Object>();
		

		
		if(username.equals("")||password.equals("")){
			map.put("code", "201");
			map.put("message","用户名和密码不能为空");
			return JsonUtil.ObjectToJsonString(map);
		} 
		
		String sql = "select 1 from t_doyoulike where user_id = "+id+" and img_user_id = "+to+"";
		
		logger.debug("sql::"+sql);
		
		BaseDao baseDao = BaseDao.getBaseDao();
		int count = baseDao.getCount(sql);
		if(count==0) {
			String insertsql = "insert into t_doyoulike(user_id,img_user_id,yes,no) values("+id+","+to+",0,0)";
			logger.info("insql:"+insertsql);
			baseDao.execSave(insertsql);
		}
		String updateSql = "update t_doyoulike set yes=yes+1 where user_id = "+id+" and img_user_id = "+to+"";
		
		if(like.equals("0")){
			updateSql = "update t_doyoulike set no=no+1 where user_id = "+id+" and img_user_id = "+to+"";
		}
		
		baseDao.execSave(updateSql);
		
		map.put("code", "200");
		map.put("message","success");
		
		
		//查找对方是否也喜欢你
		String findsql = "select yes as count from t_doyoulike where user_id = "+to+" and img_user_id = "+id+"";
		int yescount = baseDao.getCount(findsql);
		
		map.put("data", yescount);
		
		
		
		return JsonUtil.ObjectToJsonString(map);
		
	}
	
	
	public String pushMessage (String string) {
		try {
			JSONObject json = JsonUtil.StringToObject(string);
			
			
			if (json.has("id") ){
				String id = json.getString("id");
				UserManager userManager = UserManager.getUserManager();
				List<Map<String,String>> list = userManager.getUserById(id);
				
				logger.info("list size::"+list.size());
				
				if (list.size() > 0 ) {
					String username = list.get(0).get("username");
					String password = list.get(0).get("password");
					String msguuid = UuidUtil.getUUID();
					String longDate = new Date().getTime()+"";
					if (json.has("content")) {
						String content = json.getString("content");
						content = DataUtil.ecodeBase64(content.getBytes());
						json.accumulate("content", content);
					}
					if (json.has("title")) {
						String title = json.getString("title");
						title = DataUtil.ecodeBase64(title.getBytes());
						json.accumulate("title", title);
					} 
				
					
					json.remove("id");
					
					HttpParams params = new HttpParams();
					params.isMultipart = true;
					params.avs = new HashMap<String,String>();
					logger.info("avs");
					params.avs.put("username", username);
					logger.info("info"+params.avs.get("username"));
					params.avs.put("password", password);
					params.avs.put("msguuid", msguuid);
					params.avs.put("longDate", longDate);
					logger.info("longDate::"+longDate);
					params.avs.put("data", JsonUtil.ObjectToJsonString(json));
					logger.info(JsonUtil.ObjectToJsonString(params.avs));
					Msg msg = new Msg();
					return msg.pushMsg(params);
					
				}
			}
		} catch (Exception ex) {
			
			ex.printStackTrace();
			return "{\"code\":500,\"message\":\"服务器异常\"}";
		}
		return "{\"code\":201,\"message\":\"数据错误\"}";
	}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Tinder tinder = new Tinder();
		tinder.doyoulike("wenwei.wan", "123456", "453", "483", "1");
	}

}
