package cn.yikuair.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;

import cn.yikuair.base.Log;

public class HttpParams {
	private static Logger logger = Log.getLogger(HttpParams.class);
	public final static String password = "_yikuair";
	public Map<String, String> avs = null;
	private HttpServletRequest request = null;
    Map<String, FileItem> fileitemMap = null;
    private Map<String, String> params = null;
    public boolean isMultipart = false,encoded = false;
    /**
     * 
    * <p>Title: </p> 带参数的够着函数
    * <p>Description: </p> 获取请求中的参数对象
    * @param request
     * @throws UnsupportedEncodingException 
     */
    
    public HttpParams(){
    	
    }
    
	public HttpParams(HttpServletRequest request) throws UnsupportedEncodingException{
		//检测是否是一个文件上传请求
		this.request = request;
		isMultipart = ServletFileUpload.isMultipartContent(request);
		if(isMultipart){
			FileItemFactory factory = new DiskFileItemFactory();
	        ServletFileUpload upload = new ServletFileUpload(factory);
	        upload.setHeaderEncoding("UTF-8");
	        RequestContext requestContext  =   new  ServletRequestContext(request);
	        try{
	        	List <FileItem> items = upload.parseRequest(requestContext);
	        	Iterator <FileItem> itr = items.iterator();
	        	avs = new HashMap<String, String>();
	        	fileitemMap = new HashMap<String, FileItem>();
	        	while (itr.hasNext()) {
		            FileItem item = (FileItem) itr.next();
		            String fieldName = item.getFieldName();
		            if (item.isFormField()) {
		                avs.put(fieldName, item.getString("UTF-8"));
		            } else {
		            	fileitemMap.put(fieldName, item);
		            }
		        } 
	        } catch(Exception ex){
	        	ex.printStackTrace();
	        }
		} else {//非文件上传请求  （参数加密）
			request.setCharacterEncoding("utf-8");
			String s = request.getParameter("__");
			//s = URLDecoder.decode(s,"UTF-8");
			//logger.info("http reqeust params: "+s);
			if(s!=null&&!s.equals("")){
				try{
					encoded = true;
//					String str = DataUtil.decodeECBString(password, DataUtil.HexString2Bytes(s));
					String str = DataUtil.decodeECBString(password, DataUtil.decodeBase64(s));	
					String[] pairs = str.split("&");
					params = new HashMap<String, String>();
		            
		            for (int i=0; i<pairs.length; i++) {
		                //String[] tokens = pairs[i].split("=");
		                int index = pairs[i].indexOf("=");
		                String key = pairs[i].substring(0,index);
		                String value = pairs[i].substring(index+1,pairs[i].length());
		                params.put(key, URLDecoder.decode(value, "UTF-8"));
		            }
				} catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 
	* @Title: encryptParams 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param params
	* @param @return
	* @param @throws Exception    设定文件 
	* @return String    把url参数加密 
	* @throws
	 */
    public static String encryptParams(Map<String, String> params) throws Exception {
        Iterator<String> iter = params.keySet().iterator();
        StringBuffer sb = new StringBuffer();
        while (iter.hasNext()) {
            String k = iter.next();
            String v = params.get(k);
            if (sb.length()>0)
                sb.append("&");
            sb.append(k);
            sb.append('=');
            sb.append(URLEncoder.encode(v, "UTF-8"));
        }
        String str = sb.toString();
        
        //return "__=" + DataUtil.encodeECBAsHexString(password, str);
        return "__=" + DataUtil.encodeECBAsBase64String(password, str);
    }
    public String getStr(String attr, String not_found_value){
    	String v = isMultipart?avs.get(attr):(!encoded)?HttpUtil.getStr(this.request, attr, not_found_value):params.get(attr);
		if (v==null)
			return not_found_value;
		return v;
	}
	
	/**
	 * 
	* @Title: getFileItem 
	* @Description: TODO(获取文件蚊香) 
	* @param @param attr
	* @param @return
	* @param @throws Exception    设定文件 
	* @return FileItem    返回类型 
	* @throws
	 */
	public FileItem getFileItem(String attr) throws Exception {
		return fileitemMap.get(attr);
	}
	
	/** 
	 * @Title: main 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param args    设定文件 
	 * @return void    返回类型 
	 * @throws 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s = "ddd=wwasd=";
		int index = s.indexOf("=");
		String key = s.substring(0,index);
        String value = s.substring(index+1,s.length());
        System.out.println(key+"------"+value);
	}

}
