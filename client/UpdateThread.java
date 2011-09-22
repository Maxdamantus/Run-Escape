package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import ui.isometric.IsoInterface;

/**
 * Handles all game state updates 
 * @author greenwthom
 *
 */
public class UpdateThread extends Thread{
	private BufferedReader reader;
	private BufferedWriter writer;
	private IsoInterface view;
	
	
	public UpdateThread(BufferedReader reader, IsoInterface view) {
		this.reader = reader;
		this.view = view;
	}
	
	public void run() {
		System.out.println("now using thread");
		try {
			while (true) {
				String incoming = reader.readLine();
				if (incoming.startsWith("msg")) {
					String message = incoming.substring(3);
					view.logMessage(message);
					System.out.println("Message: "+message);
				}
			}
			//throw new IOException();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
