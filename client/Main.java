package client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Main class for the client.
 * @author greenwthom
 *
 */
public class Main {
	public static void main(String[] args) {
		boolean debug = true;
		try {
			//creating socket and readers/writers
			Socket skt = new Socket("caprera", 32768);
			if (debug) System.out.println("connected to caprera on 32768");
			InputStreamReader in = new InputStreamReader(skt.getInputStream());
			BufferedReader reader = new BufferedReader(in);
			OutputStreamWriter out = new OutputStreamWriter(skt.getOutputStream());
			BufferedWriter writer = new BufferedWriter(out);
			
			//test code
			//UpdateThread network = new UpdateThread(reader, out);
			//network.start();
			writer.write("Handshake1\n");
			writer.write("Handshake3\n");
			writer.write("Handshake2\n");

			

			writer.flush();
			while (true) {}
			//out.write("still works here\n");
			//out.flush();
			//System.out.print(reader.readLine());
			//while(true)System.out.println(reader.readLine());
			
		
		
		
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
}