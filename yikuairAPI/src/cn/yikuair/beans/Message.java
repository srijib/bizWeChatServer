package cn.yikuair.beans;

public class Message {
	
	private String from;//发送者的id
	private String to;//接收者id
	private String longDate;//消息时间
	private String token;//文件内型
	private String content;//token为 2，3，4的时候内容为空
	private String type = "1";//1 为一对一   2为一对多
	private String msguuid;//消息的64位唯一id
	private String smallImgPath;//缩略图片路径
	private String filePath;//文件路径
	private String offline="0";//是否离线消息
	private String fromName;
	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getOffline() {
		return offline;
	}

	public void setOffline(String offline) {
		this.offline = offline;
	}

	public String getSmallImgPath() {
		return smallImgPath;
	}

	public void setSmallImgPath(String smallImgPath) {
		this.smallImgPath = smallImgPath;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getType() {
		return type;
	}

	public String getMsguuid() {
		return msguuid;
	}

	public void setMsguuid(String msguuid) {
		this.msguuid = msguuid;
	}

	public void setType(String type) {
		this.type = type;
	}


	
	
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getLongDate() {
		return longDate;
	}

	public void setLongDate(String longDate) {
		this.longDate = longDate;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
