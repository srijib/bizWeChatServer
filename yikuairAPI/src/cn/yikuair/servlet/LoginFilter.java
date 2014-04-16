package cn.yikuair.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import cn.yikuair.base.Log;
import cn.yikuair.utils.DataUtil;
import cn.yikuair.utils.HttpParams;
import cn.yikuair.utils.JsonUtil;

public class LoginFilter implements Filter{

	/** 
	 * @Title: main 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param args    设定文件 
	 * @return void    返回类型 
	 * @throws 
	 */
	static Logger logger = Log.getLogger(LoginFilter.class);
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filter) throws IOException, ServletException {
		// TODO Auto-generated method stub
		System.out.println("loginFilter..");
		
		HttpServletRequest req = (HttpServletRequest) request; 
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session=req.getSession();
        HttpParams params = new HttpParams(req);
        String message = params.getStr("message", "");
        
        if (message==null || message.equals("")){
        	if(null!=session.getAttribute("message"))
        		message = session.getAttribute("message").toString();
        } else {
        	byte []bb = DataUtil.decodeBase64(message);
            try{
            	message  = DataUtil.decodeECBString("_yikuair", bb);
            } catch (Exception ex){
            	ex.printStackTrace();
            }
        }
        
        session.setAttribute("message", message);
        String role = "";
		logger.info("message::"+message);
		//验证失效
		if(message==null || message.equals("")) {
			res.sendRedirect("http://www.yikuair.com");
		} else  {
			JSONObject jsonObject = JsonUtil.StringToObject(message);
			
			
			if(jsonObject.has("com_id")) {
				String com_id = jsonObject.getString("com_id");
				logger.info("com_id::"+com_id);
				session.setAttribute("com_id", com_id);
			}
			
			if(jsonObject.has("star")) {
				String own = jsonObject.getString("star");
				if(own.equals("1")) {
					role = "star";
				}
			}
			if(jsonObject.has("own")) { 
				String own = jsonObject.getString("own");
				if(own.equals("com")) {
					role = "admin";
				}
			} 
			if(jsonObject.has("comname")){
				String comname = jsonObject.getString("comname");
				session.setAttribute("comname", comname);
			}
			session.setAttribute("role", role);
			
			//Map <String,String> map = new HashMap<String,String>();
			String treeString = "";
			if(role.equals("")) { //无权限
				res.sendRedirect("http://www.yikuair.com?d="+new Date().getTime()+"code="+204);
			} else if (role.equals("star")){
				//map.put("id", jsonObject.getString("id"));
				
				Map <String,String> accountMessageMap = new HashMap<String,String>();
				accountMessageMap.put("name", "群发消息");
				accountMessageMap.put("url", "/yikuairAPI/admin/star/account/message");
				
				Map <String,String> accountSettingMap = new HashMap<String,String>();
				accountSettingMap.put("name", "账户设置");
				accountSettingMap.put("url", "/yikuairAPI/admin/star/account/setting");
				
				List <Object>list = new ArrayList<Object>();
				list.add(accountMessageMap);
				list.add(accountSettingMap);
				
				treeString = JsonUtil.ArrayToJsonString(list);
				
				
				
				//treeString = "[{\"name\":\"官方账号管理\",\"url\":\"/yikuairAPI/admin/star/manager\"}]";
				//res.sendRedirect("http://www.yikuair.com?d="+new Date().getTime()+"code="+205);
			} else if (role.equals("admin")){
				
				Map <String,String> userManagerMap = new HashMap<String,String>();
				userManagerMap.put("name", "人员管理");
				userManagerMap.put("url", "/yikuairAPI/admin/user/manager");
				
				Map <String,String> starManagerMap = new HashMap<String,String>();
				starManagerMap.put("name", "官方账户管理");
				starManagerMap.put("url", "/yikuairAPI/admin/star/manager");
				
				List <Object>list = new ArrayList<Object>();
				list.add(userManagerMap);
				list.add(starManagerMap);
				
				treeString = JsonUtil.ArrayToJsonString(list);
				
				logger.debug("treeString::"+treeString);
				
				//treeString = "[{\"name\":\"人员管理\",\"url\":\"/yikuairAPI/admin/user/manager\"}]";
			}
			session.setAttribute("treeObj", treeString);
		}
		
		filter.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
