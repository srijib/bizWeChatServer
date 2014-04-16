package cn.yikuair.utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonUtil {

	
	public static <T> String ObjectToJsonString(Object object){ 
		JSONObject obj = JSONObject.fromObject(object);
		return (obj.toString());
	}
	
	public static <T> String ArrayToJsonString(Object object) {
		JSONArray array = JSONArray.fromObject(object);
		return array.toString();
	}
	
	
	/** 
	* @Title: StringToObject 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param jsonString    设定文件 
	* @return void    返回类型 json.getString("b");
	* @throws 
	*/
	public static JSONObject StringToObject(String jsonString){
		JSONObject json = JSONObject.fromObject(jsonString);
		return json;
	}
	public static String getParam(JSONObject json,String key,String not_found_return_vlaue){
		if(key==null||!json.has(key)) return not_found_return_vlaue;
		return json.getString(key);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JSONObject json = JsonUtil.StringToObject("{'a':'1','b':'2',c:{'c1':'99','c2':'98'}}");
		System.out.println(json.getString("a"));
		
		JSONObject jsonc = json.getJSONObject("c");
		System.out.println(jsonc.getString("c1"));
	
	}

}
