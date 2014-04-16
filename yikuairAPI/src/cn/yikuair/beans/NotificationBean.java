package cn.yikuair.beans;

public class NotificationBean {
	private String title;
	private String description;
	private Object custom_content;
//	private String from;
//	private String type;//  如果普通消息   1 个人   2 群组
//	private String token;//1 文本   2 图片  3   声音
//	public String getToken() {
//		return token;
//	}
//	public void setToken(String token) {
//		this.token = token;
//	}
//	public String getFrom() {
//		return from;
//	}
//	public void setFrom(String from) {
//		this.from = from;
//	}
//	public String getType() {
//		return type;
//	}
//	public void setType(String type) {
//		this.type = type;
//	}
//	
	public String getTitle() {
		return title;
	}
	public Object getCustom_content() {
		return custom_content;
	}
	public void setCustom_content(Object custom_content) {
		this.custom_content = custom_content;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
