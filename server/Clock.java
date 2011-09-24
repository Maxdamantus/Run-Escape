package server;

import server.Clock.status;

public class Clock extends Thread {
	
	int counter;
	status curstatus;
	
	public Clock(int i){
		counter = i;
	}
	
	enum status{GO,STOP};
	
	public void run() {
		curstatus = status.GO;
		try {
			Thread.sleep(1000);
			curstatus = status.STOP;
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public int getCounter(){
		return counter;
	}
	
	public status getStatus(){
		return curstatus;
	}
}
