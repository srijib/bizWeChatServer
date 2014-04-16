package cn.yikuair.wechat.star;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.apache.log4j.Logger;

import cn.yikuair.base.Log;
import cn.yikuair.beans.Message;
import cn.yikuair.business.MessageManager;
import cn.yikuair.business.StarManager;
import cn.yikuair.business.UserManager;
import cn.yikuair.dao.BaseDao;
import cn.yikuair.message.SocketThread;
import cn.yikuair.utils.DataUtil;
import cn.yikuair.utils.DateUtil;
import cn.yikuair.utils.HttpParams;
import cn.yikuair.utils.HttpUtil;
import cn.yikuair.utils.JsonUtil;
import cn.yikuair.utils.RandomUtil;
import cn.yikuair.utils.UuidUtil;

public class Msg {

	private static Logger logger = Log.getLogger(Msg.class);
	/**
	 * 
	* @Title: pushMsg 
	* @Description: TODO 像服务器push消息 ， 群发给关注的当前明星账号的人 
	* @param @param params  from=2&to=3&id=4&token=fadfadf==fadfad&longDate=32141343141313
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	@SuppressWarnings("unused")
	public String pushMsg(HttpParams params){
		String longDate = params.getStr("longDate", "");//当前时间戳 ，仅当前时间为5分钟有效
		logger.info("ld:"+longDate);
		String username = params.getStr("username", "");//用户名
		String password = params.getStr("password", "");//明星密码
		String data = params.getStr("data", "");//消息内容
		String msguuid = params.getStr("msguuid", "");//唯一编码
		String to = params.getStr("to", "");//带这个参数为明显账户单一发送消息  username
		Map <String,String>map = new HashMap<String,String>();
		UserManager manager = UserManager.getUserManager();
		MessageManager messageManager  = MessageManager.getMessageManager();
		
		logger.debug(msguuid+"[]to::"+to);
		
		//验证时间戳
		if(false&&longDate.equals("")){
			map.put("code", "203");
			map.put("message", "没有时间戳");
			return JsonUtil.ObjectToJsonString(map);
		} else {
			long l = DateUtil.getlong(longDate);
			l = Math.abs(l);
			if(false&&l>1000*60*5){//该请求五分钟内就失效
				map.put("code", "203");
				map.put("message", "longtime验证已经失效"+new Date());
				return JsonUtil.ObjectToJsonString(map);
			} else {
				
				//
				int msgtatus = messageManager.messageStatus(msguuid);
				logger.debug("msguuid::"+msgtatus+"---"+msguuid);
				if(msgtatus>0){
					map.put("code", "202");
					map.put("message", "该消息已经发送过了！"+msguuid);
					return JsonUtil.ObjectToJsonString(map);
				}
				//校验用户的token 和from 是否正确
				List<Map <String,String>> validateList = manager.validateToken(username, password);
				
				String flag = "",userinterface="",com_id="";
				
				if(validateList==null||validateList.size()==0){
					flag = "-1";
				} else {
					Map <String,String> validateMap = validateList.get(0);
					String s = validateMap.get("star");
					userinterface = validateMap.get("interface");
					com_id = validateMap.get("com_id");
					if(s!=null&&s.equals("1")) flag = validateMap.get("id");
					else flag = "-2";
				}
				
				
				
				if(flag.equals("-1")){
					map.put("code", "201");
					map.put("message", "用户验证失败");
					return JsonUtil.ObjectToJsonString(map);
				} else if(flag.equals("-2")){
					//getContent
					map.put("code", "403");
					map.put("message", "用户没有该权限");
					return JsonUtil.ObjectToJsonString(map);
				} else {
					
					if(!to.equals("")){
						to = manager.getIdByName(to);//???
						//检测该to是否关注你
						boolean b = manager.isFriend(flag, to);
						if(b){
							
							try{
								JSONObject json = JsonUtil.StringToObject(data);
								Message msgobj = new Message();
								
								msgobj.setMsguuid(msguuid);
								if(!json.has("token")){
									map.put("code", "204");
									map.put("message", "token必须存在");
									return JsonUtil.ObjectToJsonString(map);
								}
								msgobj.setToken(json.getString("token"));
								msgobj.setType("4");//明星账户
								msgobj.setLongDate(new Date().getTime()+"");
								msgobj.setMsguuid(msguuid);
								msgobj.setFrom(flag);//明星账户id
								msgobj.setTo(to);//给所有的
								
								json.accumulate("to", to);
								json.accumulate("from", flag);
								json.accumulate("type", 4);
								json.accumulate("msguuid", msguuid);
								
								//data = JsonUtil.ObjectToJsonString(json);
								
								String reciveMessage = JsonUtil.ObjectToJsonString(json);
								messageManager.setMessage(msgobj, reciveMessage);
								
								SocketThread.getSocketToMessage(to, reciveMessage,msgobj.getToken(),flag,"4");
								map.put("code", "200");
								map.put("message", "消息发送成功");
								
								
							} catch(Exception ex){
								logger.debug(ex.getMessage());
								map.put("code", "204");
								map.put("message", "数据格式错误"+ex.getMessage());
								return JsonUtil.ObjectToJsonString(map);
							}
							
							
						} else {
							map.put("code", "403");
							map.put("message", "不是好友，不能推送");
						}
						
						
						return JsonUtil.ObjectToJsonString(map);
					}
					
					int countMessage = messageManager.getTodayCountMessage(flag);
					
					if(countMessage>10){
						map.put("code", "403");
						map.put("message", "今天发送次数已经超过10次，每日发送次数不能超过10次");
						map.put("count", countMessage+"");
						return JsonUtil.ObjectToJsonString(map);
					} else {
						map.put("count", (countMessage+1)+"");
					}
					
					List<Map <String,String>> list = manager.getUserIds(flag);
					List <String> ls = new ArrayList<String>();
					
					Set <String>set=new HashSet<String>();
					
					
					if(list!=null){
						for(int i=0,j=list.size();i<j;i++){
							String _id = list.get(i).get("id");
							logger.debug("id::"+_id);
							set.add(_id);
						}
						logger.debug("..."+set.size());
					} else {
						logger.debug("list:"+list);
					}
					
					
					
					
					
					if(userinterface.equals("1")){
						List<Map <String,String>> list2 = manager.getIdByCom(com_id);
						if(list2!=null){
							for(int i=0,j=list2.size();i<j;i++){
								set.add(list2.get(i).get("id"));
							}
						}
					}
					
					logger.debug("set:::"+set.size());
					
					Iterator<String> it = set.iterator();  
					while (it.hasNext()) {  
						 String str = it.next();  
						 ls.add(str);
					} 
					
					//判断明星账户是否为接口人 ， 如果为接口人， 可以给该公司底下所有的员工发消息
					
					
					
					
					
					
					JSONObject json = JsonUtil.StringToObject(data);
					
					Message msgobj = new Message();
					
					msgobj.setMsguuid(msguuid);
					if(!json.has("token")){
						map.put("code", "204");
						map.put("message", "token必须存在");
						return JsonUtil.ObjectToJsonString(map);
					}
					msgobj.setToken(json.getString("token"));
					msgobj.setType("4");//明星账户
					msgobj.setLongDate(new Date().getTime()+"");
					msgobj.setMsguuid(msguuid);
					msgobj.setFrom(flag);//明星账户id
					msgobj.setTo("-1");//给所有的
					
					json.accumulate("to", "-1");
					json.accumulate("from", flag);
					json.accumulate("type", 4);
					json.accumulate("msguuid", msguuid);
					
					String reciveMessage = JsonUtil.ObjectToJsonString(json);
					logger.debug("message::"+reciveMessage);
					
					//messageManager.setMessage(msgobj, reciveMessage);
					
					logger.debug("ls::"+ls.size());
					
					try {
						String msgflag = messageManager.setMessages(msgobj, reciveMessage , ls);
						if(msgflag.equals("1")){
							map.put("code", "202");
							map.put("message", "该消息已经发送过");
						}
						for(int i=0,j=ls.size();i<j;i++){
							if(ls.get(i).equals(msgobj.getFrom())) continue;
							SocketThread.getSocketToMessage(ls.get(i), reciveMessage,"1",flag,"4");
						}
					} catch (Exception ex){
						ex.printStackTrace();
						map.put("code", "200");
						map.put("message", "消息发送成功");
						return JsonUtil.ObjectToJsonString(map);
					}
				}
			}
			
		}
		//根据button_id 获取消息
		map.put("code", "200");
		map.put("message", "消息发送成功");
		
		return JsonUtil.ObjectToJsonString(map);
	}
	
	/**
	 * 
	* @Title: setBtn 
	* @Description:  数据总大小不能超过2kb
	* data:{type:"1","content":""}  type 1文本，type 2 图片，type 3 声音  type 4 列表
	* data:"{type:"2","smallImgPath":"",filePath:""}" 
	* data:{type:"3","filePath":""} 
	* data:{type:"4",data:[
	* 	{title:'',imgpath:'',content:'',url:'//跳转到webview的url'},
	* 	{title:'',imgpath:'',content:'',url:'//跳转到webview的url'}
	* ]} 
	* @param @param params    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@SuppressWarnings("unused")
	public String setBtn(HttpParams params){
		String longDate = params.getStr("longDate", "");//当前时间戳 ，仅当前时间为5分钟有效
		String username = params.getStr("username", "");
		String password = params.getStr("password", ""); //md5
		String buttonName = params.getStr("buttonName", "");
		String msguuid = params.getStr("msguuid", "");
		String status = params.getStr("status", "");//0添加  1 修改 （根据msguuid修改） 2 删除
		String callbackurl = params.getStr("callbackurl", "");//文本数据
		
		Map <String,String>map = new HashMap<String,String>();
		UserManager manager = UserManager.getUserManager();
		StarManager starManager = StarManager.getStarManager();
		longDate = new Date().getTime()+"";
		
		if(false&&longDate.equals("")){
			map.put("code", "203");
			map.put("message", "没有时间戳");
			return JsonUtil.ObjectToJsonString(map);
		} else {
			long l = DateUtil.getlong(longDate);
			l = Math.abs(l);
			if(false&&l>1000*60*5){//该请求五分钟内就失效
				map.put("code", "203");
				map.put("message", "longtime验证已经失效"+new Date());
				return JsonUtil.ObjectToJsonString(map);
			} else {
				//校验用户的token 和from 是否正确
				List<Map <String,String>> validateList = manager.validateToken(username, password);
				
				String flag = "",userinterface="",com_id="";
				
				if(validateList==null||validateList.size()==0){
					flag = "-1";
				} else {
					Map <String,String> validateMap = validateList.get(0);
					String s = validateMap.get("star");
					userinterface = validateMap.get("interface");
					com_id = validateMap.get("com_id");
					if(s!=null&&s.equals("1")) flag = validateMap.get("id");
					else flag = "-2";
				}
				if(flag.equals("-1")){
					map.put("code", "201");
					map.put("message", "用户验证失败");
					return JsonUtil.ObjectToJsonString(map);
				} else if(flag.equals("-2")){
					//getContent
					map.put("code", "403");
					map.put("message", "用户没有该权限");
					return JsonUtil.ObjectToJsonString(map);
				} else {
					
					boolean b = starManager.saveBtn(flag, status, buttonName, callbackurl, msguuid);
					if(!b){
						map.put("code", "403");
						map.put("message", "执行按钮操作失败，status值不对或者是按钮添加上限，最多8个");
						return JsonUtil.ObjectToJsonString(map);
					}
				}
			}
		}
		map.put("code", "200");
		map.put("message", "success");
		
		return JsonUtil.ObjectToJsonString(map);
	}
	
	
	
	
	
	
	@SuppressWarnings("unused")
	public String getListLeaveMesage(HttpParams params){
		String msguuid = params.getStr("msguuid", "");
		String longDate = params.getStr("longDate", "");//当前时间戳 ，仅当前时间为5分钟有效
		String username = params.getStr("username", "");
		String password = params.getStr("password", ""); //md5
		String pageIndex = params.getStr("pageIndex", "");
		String pageSize = params.getStr("pageSize", "");
		Map <String,Object>map = new HashMap<String,Object>();
		longDate = new Date().getTime()+"";
		StarManager starManager = StarManager.getStarManager();
		if(!msguuid.equals("")){
			map.put("code", "200");
			List<Map<String,String>> list = starManager.getMsguuidByMessage(msguuid);
			map.put("data", list);
			
		}
		
		UserManager manager = UserManager.getUserManager();
		
		
		if(false&&longDate.equals("")){
			map.put("code", "203");
			map.put("message", "没有时间戳");
			return JsonUtil.ObjectToJsonString(map);
		} else {
			long l = DateUtil.getlong(longDate);
			l = Math.abs(l);
			if(false&&l>1000*60*5){//该请求五分钟内就失效
				map.put("code", "203");
				map.put("message", "longtime验证已经失效"+new Date());
				return JsonUtil.ObjectToJsonString(map);
			} else {
				List<Map <String,String>> validateList = manager.validateToken(username, password);
				
				String flag = "",userinterface="",com_id="";
				
				if(validateList==null||validateList.size()==0){
					flag = "-1";
				} else {
					Map <String,String> validateMap = validateList.get(0);
					String s = validateMap.get("star");
					userinterface = validateMap.get("interface");
					com_id = validateMap.get("com_id");
					if(s!=null&&s.equals("1")) flag = validateMap.get("id");
					else flag = "-2";
				}
				if(flag.equals("-1")){
					map.put("code", "201");
					map.put("message", "用户验证失败");
					return JsonUtil.ObjectToJsonString(map);
				} else if(flag.equals("-2")){
					//getContent
					map.put("code", "403");
					map.put("message", "用户没有该权限");
					return JsonUtil.ObjectToJsonString(map);
				} else {
					int index = 1,size=10;
					if(!pageIndex.equals("")){
						index = Integer.parseInt(pageIndex);
						size = Integer.parseInt(pageSize);
					}
					List<Map<String,String>> list = starManager.getPageStarMessage(flag, index, size);
					map.put("data", list);
					int total = starManager.getCountMessage();
					map.put("total", total);
				}
			}
		}
		return JsonUtil.ObjectToJsonString(map);
	}
	
	public String getPublicListBtn(HttpParams params){
		String username = params.getStr("username", "");
		String password = params.getStr("password", ""); //md5
		UserManager manager = UserManager.getUserManager();
		StarManager starManager = StarManager.getStarManager();
		Map <String,Object>map = new HashMap<String,Object>();
		if(manager.login(username, password)){
			String id = manager.getIdByName(username);;
			List <Map<String,String>>list = starManager.getBtnList(id);
			map.put("code", "200");
			map.put("data", list);
			return JsonUtil.ObjectToJsonString(map);
		} else {
			map.put("code", "500");
			map.put("message", "用户名和密码验证错误");
		}
		return JsonUtil.ObjectToJsonString(map);
	}
	
	
	//一下是公司app调用
	
	public String getListBtn(HttpParams params){
		String username = params.getStr("username", "");
		String password = params.getStr("password", ""); //md5
		String to = params.getStr("to", "");
		UserManager manager = UserManager.getUserManager();
		StarManager starManager = StarManager.getStarManager();
		Map <String,Object>map = new HashMap<String,Object>();
		if(manager.login(username, password)){
			List <Map<String,String>>list = starManager.getBtnList(to);
			map.put("code", "200");
			map.put("data", list);
			return JsonUtil.ObjectToJsonString(map);
		} else {
			map.put("code", "500");
			map.put("message", "用户名和密码验证错误");
		}
		return JsonUtil.ObjectToJsonString(map);
	}
	
	public String getDevContent ( HttpParams params ) {
		String username = params.getStr("username", "");
		String button_id = params.getStr("id", "");
		String callbackurl = params.getStr("callbackurl", "");
		String contentCode = params.getStr("content", "");
		String star_id = params.getStr("to", "");
		//String callbackurl = "";
		
		if(button_id.equals("-1")){ // 用户发消息走的是回调url
			StarManager star = StarManager.getStarManager();
			callbackurl = star.getCallbackUrlByStarID(star_id);
		}
		
		
		
		
		Map <String,String> map = new HashMap<String,String>();
		if(callbackurl==null||callbackurl.equals("")){
			map.put("code", "204");
			map.put("message", "服务器没有设置回调");
			return JsonUtil.ObjectToJsonString(map);
		}
		
		
		
		logger.info("callbackurl::"+callbackurl);
		
		String code = "{\"msguuid\":\""+UuidUtil.getUUID()+"\"}";
		
		//callbackurl = "http://localhost:8080/yikuairAPI/a/test/callback?code=2";
		
		logger.info("callback1::"+callbackurl);
		
		String key = RandomUtil.getRandom64(8);
		
		int ispar = callbackurl.indexOf("?");
		String concat = "?";
		if(ispar>0){
			concat = "&";
		}
		try{
			String s = DataUtil.encodeECBAsBase64String(key,code);
			s = URLEncoder.encode(s,"utf-8");
			s = key+s;
			
			String buttonString = "";
			if(button_id.equals("-1")){
				buttonString = "&content="+contentCode;
			} else {
				buttonString = "&button_id="+button_id;
			}
			
			String string ="id="+username+buttonString+"&secr="+s;
			logger.debug("string:"+string);
			
			//String deString = URLDecoder.decode(string, "utf-8");
			
			//logger.debug(enString);
			
			callbackurl = callbackurl+concat+string;
			
			logger.info("callbackurl2::"+callbackurl);
			String content = HttpUtil.getUrlContent(callbackurl, "utf-8");
			return content;
		} catch(Exception ex){
			ex.printStackTrace();
		}
		
		return "";
	}
	
	public String getCallbackMessage(String username,String msguuid,String callbackurl){
		String code = "{\"msguuid\":\""+msguuid+"\"}";
		String key = RandomUtil.getRandom64(8);
		try{
			String s = DataUtil.encodeECBAsBase64String(key, code);
			s = key+s;
			s = URLEncoder.encode(s, "utf-8");
			String string ="id="+username+"&secr="+s;
			
			
			int ispar = callbackurl.indexOf("?");
			String concat = "?";
			if(ispar>0){
				concat = "&";
			}
			
			callbackurl = callbackurl+concat+string;
			logger.info("callbackurl:"+callbackurl);
			String content = HttpUtil.getUrlContent(callbackurl, "utf-8");
			return content;
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return "";
	}
	
	@SuppressWarnings("unused")
	public String postServerData(HttpParams params){
		String msguuid = params.getStr("msguuid", "");//唯一编码
		String longDate = params.getStr("longDate", "");//当前时间戳 ，仅当前时间为5分钟有效
		String username = params.getStr("username", "");
		String password = params.getStr("password", ""); //md5
		String _id = params.getStr("id", "");//发送给单个用户的数据 username
		
		
		
		String data = params.getStr("data", "");//json
		logger.debug("data::"+data+_id);
		Map <String,String>map = new HashMap<String,String>();
		UserManager manager = UserManager.getUserManager();
		MessageManager messageManager  = MessageManager.getMessageManager();
		String id = manager.getIdByName(_id);
		logger.debug("id::"+id);
		if(false&&longDate.equals("")){
			map.put("code", "203");
			map.put("message", "没有时间戳");
			return JsonUtil.ObjectToJsonString(map);
		} else {
			long l = DateUtil.getlong(longDate);
			l = Math.abs(l);
			if(false&&l>1000*60*5){//该请求五分钟内就失效
				map.put("code", "203");
				map.put("message", "longtime验证已经失效"+new Date());
				return JsonUtil.ObjectToJsonString(map);
			} else {
				if(msguuid.equals("")){
					map.put("code", "204");
					map.put("message", "msguuid不能为空"+msguuid);
					return JsonUtil.ObjectToJsonString(map);
				}
				int msgtatus = messageManager.messageStatus(msguuid);
				if(msgtatus>0){
					map.put("code", "202");
					map.put("message", "该消息已经发送过了！"+msguuid);
					return JsonUtil.ObjectToJsonString(map);
				}
				
				//校验用户的token 和from 是否正确
				List<Map <String,String>> validateList = manager.validateToken(username, password);
				
				String flag = "",userinterface="",com_id="";
				
				if(validateList==null||validateList.size()==0){
					flag = "-1";
				} else {
					Map <String,String> validateMap = validateList.get(0);
					String s = validateMap.get("star");
					userinterface = validateMap.get("interface");
					com_id = validateMap.get("com_id");
					if(s!=null&&s.equals("1")) flag = validateMap.get("id");
					else flag = "-2";
				}
				
				logger.debug("flag:"+flag);
				if(flag.equals("-1")){
					map.put("code", "201");
					map.put("message", "用户验证失败");
					return JsonUtil.ObjectToJsonString(map);
				} else if(flag.equals("-2")){
					//getContent
					map.put("code", "403");
					map.put("message", "用户没有该权限");
					return JsonUtil.ObjectToJsonString(map);
				} else {
					
					try{
						JSONObject json = JsonUtil.StringToObject(data);
						json.accumulate("from", flag);
						json.accumulate("to", id);
						json.accumulate("type", "4");
						json.accumulate("msguuid", msguuid);
						String reciveMessage = JsonUtil.ObjectToJsonString(json);
						
						Message msgobj = new Message();
						
						msgobj.setMsguuid(msguuid);
						if(!json.has("token")){
							map.put("code", "204");
							map.put("message", "token必须存在");
							return JsonUtil.ObjectToJsonString(map);
						}
						msgobj.setToken(json.getString("token"));
						msgobj.setType("4");
						msgobj.setTo(id);
						msgobj.setFrom(flag);
						messageManager.setMessage(msgobj, reciveMessage);
						logger.debug("dddd");
						SocketThread.getSocketToMessage(id, data,msgobj.getToken(),flag,"4");
						
					} catch(Exception ex){
						logger.debug(ex.getMessage());	
						map.put("code", "204");
						map.put("message", "数据格式错误"+ex.getMessage());
						return JsonUtil.ObjectToJsonString(map);
					}
					
				}
			}
		}
		
		return "";
	}
	
	
	
	
	public String getStarAccount ( HttpParams params ) {
		String username = params.getStr("username", "");
		String password = params.getStr("password", "");
		String com_id = params.getStr("com_id", "");
		

		Map <String,Object>map = new HashMap<String,Object>();
		UserManager manager = UserManager.getUserManager();
		BaseDao dao = BaseDao.getBaseDao();
		List<Map <String,String>> validateList = manager.validateToken(username, password);
		
		String flag = "";
		
		if(validateList==null||validateList.size()==0){
			flag = "-1";
		} else {
			Map <String,String> validateMap = validateList.get(0);
			String s = validateMap.get("star");
			if(s!=null&&s.equals("1")) flag = validateMap.get("id");
			else flag = "-2";
		}
		if(flag.equals("-1")){
			map.put("code", "201");
			map.put("message", "用户验证失败");
			return JsonUtil.ObjectToJsonString(map);
		} else{
			String sql = "select * from t_userinfo where com_id='"+com_id+"' and star=1";
			List <Map <String,String>>list = dao.getData(sql);
			
			map.put("data", list);
		}
		map.put("code", "200");
		return JsonUtil.ObjectToJsonString(map);
	}
	
	/**
	 * 
	* @Title: getAttentionStarAccount 
	* @Description: 查询已经关注的明星账户
	* @param @param params
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public String getAttentionStarAccount(HttpParams params){
		
		String username = params.getStr("username", "");
		String password = params.getStr("password", "");
		String com_id = params.getStr("com_id", "");
		String from = params.getStr("id", "");
		

		Map <String,Object>map = new HashMap<String,Object>();
		UserManager manager = UserManager.getUserManager();
		BaseDao dao = BaseDao.getBaseDao();
		List<Map <String,String>> validateList = manager.validateToken(username, password);
		
		String flag = "";
		
		if(validateList==null||validateList.size()==0){
			flag = "-1";
		} else {
			Map <String,String> validateMap = validateList.get(0);
			String s = validateMap.get("star");
			if(s!=null&&s.equals("1")) flag = validateMap.get("id");
			else flag = "-2";
		}
		if(flag.equals("-1")){
			map.put("code", "201");
			map.put("message", "用户验证失败");
			return JsonUtil.ObjectToJsonString(map);
		} else{
			String sql = "select a.id as id from t_userinfo a left join t_joinfriend b on (a.id=b.to_id) where a.com_id='"+com_id+"' and a.star=1 and b.launch_id  = '"+from+"'";
			logger.info("attention::"+sql);
			List <Map <String,String>>list = dao.getData(sql);
			
			map.put("data", list);
		}
		map.put("code", "200");
		return JsonUtil.ObjectToJsonString(map);
	
	}
	
	@SuppressWarnings("unused")
	public String getCallbackMesageStar(HttpParams params){
		String longDate = params.getStr("longDate", "");//当前时间戳 ，仅当前时间为5分钟有效
		String username = params.getStr("username", "");
		String password = params.getStr("password", "");
		String callbackurl = params.getStr("callbackurl", "");
		Map <String,Object>map = new HashMap<String,Object>();
		UserManager manager = UserManager.getUserManager();
		BaseDao dao = BaseDao.getBaseDao();
		
		
		longDate = new Date().getTime()+"";//系统
		
		
		
		if(longDate.equals("")){
			map.put("code", "203");
			map.put("message", "没有时间戳");
			return JsonUtil.ObjectToJsonString(map);
		} else {
			long l = DateUtil.getlong(longDate);
			l = Math.abs(l);
			if(false){//该请求五分钟内就失效
				map.put("code", "203");
				map.put("message", "longtime验证已经失效"+new Date());
				return JsonUtil.ObjectToJsonString(map);
			} else {
				List<Map <String,String>> validateList = manager.validateToken(username, password);
				String flag = "";
				
				if(validateList==null||validateList.size()==0){
					flag = "-1";
				} else {
					Map <String,String> validateMap = validateList.get(0);
					String s = validateMap.get("star");
					if(s!=null&&s.equals("1")) flag = validateMap.get("id");
					else flag = "-2";
				}
				if(flag.equals("-1")){
					map.put("code", "201");
					map.put("message", "用户验证失败");
					return JsonUtil.ObjectToJsonString(map);
				} else{
					String sql = "select count(*) as count from t_callbackMessageStar where user_id='"+flag+"'";
					int count = dao.getCount(sql);
					if(count>0){
						sql = "update t_callbackMessageStar set callbackurl='"+callbackurl+"' where user_id='"+flag+"'";
					} else {
						sql = "insert into t_callbackMessageStar(user_id,callbackurl) values("+flag+",'"+callbackurl+"')";
					}
					dao.execSave(sql);
					map.put("code", "200");
					map.put("message", "success");
					return JsonUtil.ObjectToJsonString(map);
				}
			}
		}
	}
	
	public void testCallback(String data,String id){
		//request.getParameter("data");
		HttpURLConnection conn = null; 
		StringBuffer params = new StringBuffer();
		params.append("longDate="+new Date().getTime()+"");
		params.append("&username=YY001");
		params.append("&password=C4CA4238A0B923820DCC509A6F75849B");
		params.append("&data="+data);
		params.append("&id="+id);
		params.append("&msguuid="+UuidUtil.getUUID());
		
		logger.debug("params:"+params.toString());
		
		try{
			URL url = new URL("https://api.yikuair.com/yikuairAPI/a/public/post/server");   
			//URL url = new URL("http://localhost:8080/yikuairAPI/a/public/post/server");   
            conn = (HttpURLConnection)url.openConnection();   

            conn.setDoOutput(true);   
            conn.setRequestMethod("POST");   
            conn.setUseCaches(false);   
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");   
            conn.setRequestProperty("Content-Length", String.valueOf(params.length()));   
            conn.setDoInput(true);   
            conn.connect();
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");   
            out.write(params.toString());   
            out.flush();
            out.close(); 
            int code = conn.getResponseCode();   
            if (code != 200) {   
                 logger.debug("ERROR===" + code);   
             } else {      
            	 logger.debug("Success!");   
             }   
		} catch(Exception ex){
			logger.debug(ex.getMessage());
		} finally{
			conn.disconnect(); 
		}
	}
	
	public static void main(String[] args) throws Exception{
//		String code = "{\"uuid\":\""+UuidUtil.getUUID()+"\"}";
//		String key = RandomUtil.getRandom64(8);
//		String s = DataUtil.encodeECBAsBase64String(key, code);
//		System.out.println(key+"   "+s);
//		byte []bb = DataUtil.decodeBase64(s);
//		String dd  = DataUtil.decodeECBString(key, bb);
//		
//	
//		
//		//byte[] b = DataUtil.encodeECBBytes(key, s);
//		//String des = new String(b, "utf-8");
//		System.out.println("ddd:"+dd);
//		
//		String content = HttpUtil.getUrlContent("http://www.baidu.com", "utf-8");
//		System.out.println(content);
		
//		String jsonString = "{\"a\":5}";
//		JSONObject json =  JsonUtil.StringToObject(jsonString);
//		json.accumulate("aa", "ttt");
//		System.out.println(JsonUtil.ObjectToJsonString(json));
//		logger.info("ttttt");
		
		Msg msg = new Msg();
		
		String content = msg.getCallbackMessage("ady","123456789987654321","http://192.168.1.102:8080/yikuairAPI/a/json");
		
		
		logger.debug("content:::"+content);
		
		
	}
}
