package cn.yikuair.beans;

import java.io.Serializable;
import java.util.Date;


//200 执行成功，404数据库没有记录 403权限转移  500 错误 201 失败
public class SystemMessage implements Serializable{
	private static final long serialVersionUID = 8246504689391700391L;
	private int code = 201;
	private Object data;
	private String token;
	private String longDate;//消息时间
	private String msguuid;//消息的64位唯一id
	public String getMsguuid() {
		return msguuid;
	}
	public void setMsguuid(String msguuid) {
		this.msguuid = msguuid;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getLongDate() {
		return longDate;
	}
	public void setLongDate(String longDate) {
		this.longDate = longDate;
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
