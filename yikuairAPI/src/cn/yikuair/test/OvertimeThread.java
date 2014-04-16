package cn.yikuair.test;

public class OvertimeThread implements Runnable{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		OvertimeThread thread = new OvertimeThread();
		Thread t = new Thread(thread);
		t.start();
	}
	
}