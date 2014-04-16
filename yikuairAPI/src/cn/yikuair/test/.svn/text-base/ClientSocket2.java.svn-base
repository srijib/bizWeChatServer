package cn.yikuair.test;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

import cn.yikuair.message.SocketConfig;
import cn.yikuair.utils.DataUtil;

public class ClientSocket2 {
	public Socket socket = null;
	public InputStream in = null;
	public OutputStream out = null;
	public Socket instanceSocket() {
		if(socket==null){
			try{
				socket = new Socket("192.168.101.154",SocketConfig.PORT);
			} catch(Exception ex){
				ex.printStackTrace();
			}
			
		}
		return socket;
	}
	
	public void closeSocket(){
		try {
			out.close();
			in.close();
			socket.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	/*
	 * json 字符串中的token用来验证服务器接收类型 0，用户发起第一次请求进行验证，1.文字，2.voice,3.img 4.video
	 */
	public void createClient(String validateString){
		try{
			socket = this.instanceSocket();
			in  = socket.getInputStream();//server to client
			out = socket.getOutputStream();// client to server
			String string = validateString;
			out.write(string.getBytes());
			out.write(SocketConfig.WRIETEFLAGBYTES);
			out.flush();
			
			
			//Timer timer = new Timer();
			//timer.schedule(new SocketTimer(socket), 3000,4000);
			
			byte[] buf = new byte[24];
			
			int len = 0;
			
			byte[] arrayByte = null;
			while((len=in.read(buf))!=-1){
				arrayByte = DataUtil.byteArray(arrayByte,DataUtil.subBytes(buf, 0, len));
				int ablen = arrayByte.length;
				int flaglen = SocketConfig.WRIETEFLAGBYTES.length;
				byte[] byteCode = DataUtil.subBytes(arrayByte, ablen-flaglen,flaglen);
				boolean isEnd = DataUtil.isBytesEquals(byteCode, SocketConfig.WRIETEFLAGBYTES);	
				if(isEnd){
					String socketString = new String(DataUtil.subBytes(arrayByte, 0, ablen-flaglen));
					arrayByte = null;
					//socket处理
					System.out.println("socket:"+socketString);
					
					System.out.println("wanwenwei -- in");
					
					
//					String message = "{'token':'1','userName':'wanwenwei','password':'123456','to':'liudehua',time:'"+(new Date()).getTime()+"','content':'你能邀请我看你的演唱会吗'}";
//					this.sendMessage(message.getBytes());
					
					
//					System.out.println("liudehua -- in");
//					String message2 = "{'token':'1','userName':'liudehua','password':'123456','to':'wanwenwei',time:'"+(new Date()).getTime()+"','content':'我邀请你看我的演唱会'}";
//					this.sendMessage(message2.getBytes());
					
				}
			}	
			
			
		} catch(Exception ex){
			this.closeSocket();
			ex.printStackTrace();
		}
	}
	
	public void sendMessage(byte[] buf){
		try{
			System.out.println(2);
			out.write(buf);
			out.write(SocketConfig.WRIETEFLAGBYTES);
			out.flush();
		} catch(Exception ex){
			this.closeSocket();
			ex.printStackTrace();
		}
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
		
		
		ClientSocket2 clientSocket = new ClientSocket2();
		String user1 = "{'token':'0','userName':'wanwenwei','password':'123456你好','time':"+(new Date()).getTime()+"}";
		clientSocket.createClient(user1);
		
		
		
//		String user2 = "{'token':'0','userName':'liudehua','password':'123456','time':"+(new Date()).getTime()+"}";
//		clientSocket.createClient(user2);
	}

}
