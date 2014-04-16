package cn.yikuair.utils;

import java.io.File;
import java.util.Map;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


@SuppressWarnings({ "deprecation" })
public class ClientUtil {
	
	private static HttpClient httpclient;

	public static String postFile(String url,String filePath,Map<String,String> map){
		httpclient = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		File file = new File(filePath);
		try{
		     MultipartEntity mpEntity = new MultipartEntity();
		     FileBody filebody = new FileBody(file,ContentType.APPLICATION_OCTET_STREAM,"temp.aac");
		     mpEntity.addPart("upload", filebody);
		     mpEntity.addPart("username", new StringBody("ady@yikuair.com"));
		     mpEntity.addPart("token", new StringBody("2"));
		     mpEntity.addPart("from",new StringBody("2"));
		     mpEntity.addPart("to",new StringBody("1"));
		     mpEntity.addPart("type", new StringBody("1"));
		     mpEntity.addPart("msguuid", new StringBody("1cbmb"));
		     mpEntity.addPart("password", new StringBody("61F0B1C8257282E95C3786C8851AA137"));
		     post.setEntity(mpEntity);
		     
		     HttpResponse response = httpclient.execute(post);
		     HttpEntity resEntity = response.getEntity();
		     System.out.println(response.getStatusLine());//通信Ok
		     System.out.println(EntityUtils.toString(resEntity,"utf-8"));
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
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
		
		Properties property = Conf.getProperty();
		String path = property.getProperty("path.imgpath2");
		postFile("http://localhost:8080/yikuairAPI/a/upload/file",path+"/talk.aac",null);
		
//		Map <String,String>map = new HashMap <String,String>();
//		map.put("username", "ady");
//		map.put("token", "2");
//		map.put("from", "2");
//		map.put("to", "1");
//		map.put("type", "1");
//		map.put("msguuid", "19876dddd524sdsddddas323235324323");
//		map.put("password", "61F0B1C8257282E95C3786C8851AA137");

	}

}
