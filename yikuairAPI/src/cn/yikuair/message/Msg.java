package cn.yikuair.message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;

import sun.misc.BASE64Encoder;
import cn.yikuair.base.Log;



@SuppressWarnings("deprecation")
public class Msg {
	private static Logger logger = Log.getLogger(Notification.class);
	private final static String APIKEY="ac4d8c07c985bd29065e73ed3c50738c";//"dfcef3114a7bb056f2c52c70ec3082ec";
	@SuppressWarnings({"resource" })
	public static String api(String mobile,String messageContent){
		DefaultHttpClient client = new DefaultHttpClient();
		client.addRequestInterceptor(new HttpRequestInterceptor() {
            @Override
            public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
                request.addHeader("Accept-Encoding", "gzip");
                request.addHeader("Authorization", "Basic " + new BASE64Encoder().encode(("api:"+APIKEY).getBytes("utf-8")));                
            }
        });
		client.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
        client.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
        HttpPost request = new HttpPost("https://sms-api.luosimao.com/v1/send.json");
        ByteArrayOutputStream bos = null;
        InputStream bis = null;
        byte[] buf = new byte[10240];
        String content = null;
        
        try{
        	List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("mobile", mobile));
            params.add(new BasicNameValuePair("message", messageContent));
            request.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                bis = response.getEntity().getContent();
                Header[] gzip = response.getHeaders("Content-Encoding");
                bos = new ByteArrayOutputStream();
                int count;
                while ((count = bis.read(buf)) != -1) {
                    bos.write(buf, 0, count);
                }
                bis.close();
                if (gzip.length > 0 && gzip[0].getValue().equalsIgnoreCase("gzip")) {
                    GZIPInputStream gzin = new GZIPInputStream(new ByteArrayInputStream(bos.toByteArray()));
                    StringBuffer sb = new StringBuffer();
                    int size;
                    while ((size = gzin.read(buf)) != -1) {
                        sb.append(new String(buf, 0, size, "utf-8"));
                    }
                    gzin.close();
                    bos.close();
                    content = sb.toString();
                } else {
                    content = bos.toString();
                }
                System.out.println(content);
            } else {
                logger.debug("error code is " + response.getStatusLine().getStatusCode());
            }
            return content;
        } catch(Exception ex){
        	ex.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();// 最后要关闭BufferedReader
                } catch (Exception e) {
                	e.printStackTrace();
                }
            }
        }
        return content;
	}
	
	@SuppressWarnings({ "resource" })
	public static String getBalance()  {
		DefaultHttpClient client = new DefaultHttpClient();
        client.addRequestInterceptor(new HttpRequestInterceptor() {
            @Override
            public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
                request.addHeader("Accept-Encoding", "gzip");
                request.addHeader("Authorization", "Basic " + new BASE64Encoder().encode(("api:"+APIKEY).getBytes("utf-8")));
            }
        });
        client.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
        client.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
        HttpGet request = new HttpGet("https://sms-api.luosimao.com/v1/status.json");
        
        ByteArrayOutputStream bos = null;
        InputStream bis = null;
        byte[] buf = new byte[10240];
        String content = null;
        try {
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                bis = response.getEntity().getContent();
                Header[] gzip = response.getHeaders("Content-Encoding");
                bos = new ByteArrayOutputStream();
                int count;
                while ((count = bis.read(buf)) != -1) {
                    bos.write(buf, 0, count);
                }
                bis.close();
                if (gzip.length > 0 && gzip[0].getValue().equalsIgnoreCase("gzip")) {
                    GZIPInputStream gzin = new GZIPInputStream(new ByteArrayInputStream(bos.toByteArray()));
                    StringBuffer sb = new StringBuffer();
                    int size;
                    while ((size = gzin.read(buf)) != -1) {
                        sb.append(new String(buf, 0, size, "utf-8"));
                    }
                    gzin.close();
                    bos.close();
                    content = sb.toString();
                } else {
                    content = bos.toString();
                }
            } else {
            	logger.debug("error code is " + response.getStatusLine().getStatusCode());
            }
            return content;
        } catch(Exception ex){
        	ex.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();// 最后要关闭BufferedReader
                } catch (Exception e) {
                	e.printStackTrace();
                }
            }
        }
        return content;
    }
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//System.out.println(api("15810783193","验证码：44556【一块儿】"));
		System.out.println(api("18010180804","2214【一块儿】"));
		//System.out.println(getBalance());
	}

}
