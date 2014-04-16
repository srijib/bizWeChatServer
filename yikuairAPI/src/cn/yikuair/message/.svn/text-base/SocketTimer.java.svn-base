package cn.yikuair.message;

import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.TimerTask;


public class SocketTimer extends TimerTask{
	private Socket socket = null;
	public SocketTimer(Socket socket){
		this.socket = socket;
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

	}

	@Override
	public void run(){
		// TODO Auto-generated method stub
		try{
			OutputStream out = socket.getOutputStream();
			String message = "{'token':'1','userName':'wanwenwei','password':'123456','to':'liudehua',time:'"+(new Date()).getTime()+"','content':'你能邀请我看你的演唱会吗'}";
			out.write(message.getBytes());
			out.write(SocketConfig.WRIETEFLAGBYTES);
		} catch(Exception ex){
			this.closeSocket();
			ex.printStackTrace();
		} 
	}
	public void closeSocket(){
		try{
			this.cancel();
			socket.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

}
