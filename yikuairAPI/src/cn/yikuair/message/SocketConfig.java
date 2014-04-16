package cn.yikuair.message;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import cn.yikuair.beans.TokenSocketBean;

public  class  SocketConfig implements Serializable{
	private static final long serialVersionUID = -7183232715208345560L;
	public static int  PORT = 4701;//端口
	public static String IPADDRESS = "127.0.0.1";
	public static String WRIETEFLAG="_!@#$%^&*_";//10个结束符
	public static byte[] WRIETEFLAGBYTES = WRIETEFLAG.getBytes();
	public static Map<String,TokenSocketBean> socketBeanMap  = new HashMap<String,TokenSocketBean>();//在线用户
	
	public static final String TOKEN_SHAKE = "0";//首次握手
	public static final String TOKEN_MESSAGE_TEXT = "1";//文本消息
	public static final String TOKEN_MESSAGE_IMAGE = "2";//图片
	public static final String TOKEN_MESSAGE_VOICE = "3";//声音
	public static final String TOKEN_MESSAGE_VIDEO = "4";//视频
	public static final String TOKEN_ONLINE_HEART = "5";//心跳包
	public static final String TOKEN_C1_COMFIRM_S_RECEIVE= "6";//发送消息确认，证明服务器收到
	public static final String TOKEN_C2_RECEIVE = "7";//C2收到消息
	public static final String TOKEN_C2_READ = "8";//c2已读
	public static final String TOKEN_C1_COMFIRM_C2_RECEIVE = "9";//C1确认C2接收
	public static final String TOKEN_C1_COMFIRM_C2_READ = "10";//c1确认c2已读
	public static final String TOKEN_USERINFO_SAVE = "12";//用户资料修改
	public static final String TOKEN_ADD_TASK = "13";//任务添加
	public static final String TOKEN_COMFIRM_TASK = "14";//任务确认
	public static final String TOKEN_SAVE_GROUPS = "15";//创建群组和修改群组
	public static final String TOKEN_ADD_GROUPMEMBER = "16";//添加组成员
	public static final String TOKEN_REMOVE_GROUPMEMBER = "17";//移除组成员
	
	public static final String TOKEN_ADD_FRIEND = "18";//添加好友
	public static final String TOKEN_COMFIRM_ADD_FRIEND = "19";//确认添加好友
	
	
	public static final String TOKEN_ADD_BLACK = "20";//添加黑名单
	public static final String TOKEN_REMOVE_BLACK = "21";//移出黑名单
	
	public static final String TOKEN_MAPADDRESS="22";//地图地址
	
	public static final String TOKEN_REMVOE_TASK="23";//移除任务
	public static final String TOKEN_REMOVE_FRIEND="24";//删除好友
	
	
	public static final String TOKEN_LOGIN = "100";//用户登录 跟http逻辑一样
	public static final String TOKEN_LOGOUT = "101";//用户切换退出
	public static final String TOKEN_IN_BG = "102";//进入后台
	
	
	
	//volatile
	
	//public static Map<String,TokenSocketBean> socketBeanMap2  = Collections.synchronizedMap （new HashMap<String,TokenSocketBean>()）;//在线用户
	//public static Map <String,TokenSocketBean> socketBeanMap =Collections.synchronizedMap(new HashMap<String,TokenSocketBean>());
	
	//public static Map  <String,TokenSocketBean> 
	
	//private static Map <String,TokenSocketBean> socketBeanMap = null;
	
//	private static final Hashtable <String,TokenSocketBean> socketBeanMap =  new Hashtable<String,TokenSocketBean>();// new Hashtable <String,TokenSocketBean>();
//	public synchronized static Hashtable <String,TokenSocketBean> getSocketBeanMap(){
//		return socketBeanMap;
//	}
	
	
	
	//synchronized
//	public static Map <String,TokenSocketBean> getSocketBeanMap(){
//		if(socketBeanMap == null){
//			socketBeanMap  = new HashMap<String,TokenSocketBean>();
//		}
//		return socketBeanMap;
//	}
	
	 
	
	
	public static void main(String[] args) {
		byte [] b = WRIETEFLAG.getBytes();
		for(int i=0,j=b.length;i<j;i++){
			System.out.println(b[i]);
		}
		
	}
}

