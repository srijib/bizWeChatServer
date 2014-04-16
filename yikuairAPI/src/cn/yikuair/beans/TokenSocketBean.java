package cn.yikuair.beans;

import java.net.Socket;

public class TokenSocketBean {
	
	public Socket socket;//用户的socket消息发送器
	public long longDate;//服务器记录的时间
	public String user_id;//用户的id
	public String username;//用户名
	public String password;//密码
	public String device;//设备
	public String devicetoken;//设备token 如果token值不一致，就退出重新登录
	
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public long getLongDate() {
		return longDate;
	}
	public void setLongDate(long longDate) {
		this.longDate = longDate;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public String getDevicetoken() {
		return devicetoken;
	}
	public void setDevicetoken(String devicetoken) {
		this.devicetoken = devicetoken;
	}
}
