package cn.yikuair.beans;

import java.io.Serializable;
import java.util.Date;


//200 执行成功，405 不支持此类型 404数据库没有记录 403权限转移  500 错误 201 失败   202消息重复  203戳失效  204 json错误
public class LogMsg implements Serializable{
	private static final long serialVersionUID = 9012902173528223505L;
	private int code = 201;
	private Object data;
	private String token;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	private String message = "";
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
