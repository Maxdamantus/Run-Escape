package server;

import server.Clock.status;

public class Clock extends Thread {
	
	private int counter;
	private status curstatus;
	private int recieved;
	private int clients;
	
	public Clock(int i){
		counter = i;
	}
	
	enum status{GO,STOP};
	
	public void run() {
		while(1 == 1){
			try {
				curstatus = status.GO;
				Thread.sleep(100);
				curstatus = status.STOP;
				Thread.sleep(1000);
				counter++;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
	
	synchronized public int getCounter(){
		return counter;
	}
	
	synchronized public status getStatus(){
		return curstatus;
	}
}
