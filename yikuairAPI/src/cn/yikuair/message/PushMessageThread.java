package cn.yikuair.message;



public class PushMessageThread extends Thread{
//	private static Logger logger = Log.getLogger(PushMessageThread.class);
	private String id;
	private String message;
	private String from;
	private String type;
	private String messageType;
	
	public PushMessageThread(String id,String message,String from,String type, String messageType){
		this.id = id;
		this.message = message;
		this.from = from;
		this.type = type;
		this.messageType = messageType;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try{
			SocketThread.pushMessage(id, message, from, type, messageType);
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
