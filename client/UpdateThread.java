package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Handles all game state updates 
 * @author greenwthom
 *
 */
public class UpdateThread extends Thread{
	private BufferedReader reader;
	private BufferedWriter writer;
	
	
	public UpdateThread(BufferedReader reader, BufferedWriter writer) {
		this.reader = reader;
		this.writer = writer;
	}
	
	public void run() {
		System.out.println("now using thread");
		try {
			writer.write("now on thread\n");
			writer.write("again\n");
			Thread.sleep(3000);
			writer.write("yet again\n");
			writer.flush();
			while (true) System.out.println(reader.readLine());
			//throw new IOException();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
