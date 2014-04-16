package cn.yikuair.utils;

import java.io.Serializable;
import java.util.UUID;

public class UuidUtil implements Serializable{
	private static final long serialVersionUID = 7960679849924385396L;
	public static String getUUID(){
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
