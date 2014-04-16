package cn.yikuair.message;

import java.util.Date;
import java.util.Iterator;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import cn.yikuair.base.Log;
import cn.yikuair.beans.TokenSocketBean;


public class ListenTimer extends TimerTask{
	private static Logger logger = Log.getLogger(TimerTask.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run(){
		// TODO Auto-generated method stub
		try{
			//五分钟检测一次socket 情况，清理不常在线的socket
			Iterator <String> iterator = SocketConfig.socketBeanMap.keySet().iterator();
			while (iterator.hasNext()) {   
				String key = iterator.next();
		        TokenSocketBean bean = SocketConfig.socketBeanMap.get(key);
		        long oldTime = bean.getLongDate();
				long curTime = new Date().getTime();
				long ltime = curTime-oldTime;
				if(ltime>=1000*60*60*5){
					SocketConfig.socketBeanMap.remove(key);
					logger.info("用户"+key+"被在线链接移除"+new Date());
				}
		    }
		} catch(Exception ex){
			ex.printStackTrace();
		} 
	}
}
