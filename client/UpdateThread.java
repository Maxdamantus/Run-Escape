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
	private OutputStreamWriter writer;
	
	
	public UpdateThread(BufferedReader reader, OutputStreamWriter writer) {
		this.reader = reader;
		this.writer = writer;
	}
	
	public void run() {
		System.out.println("now using thread");
		try {
			//writer.write("now on thread\n");
			//writer.flush();
			//while (true) System.out.println(reader.readLine());
			throw new IOException();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
