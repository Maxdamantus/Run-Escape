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
			BufferedReader rd = new BufferedReader(in);
			OutputStreamWriter out = new OutputStreamWriter(skt.getOutputStream());
			
			//test code
			out.write("Hello World\n");
			out.flush();
			while (true){System.out.println(rd.readLine());}
		
		
		
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
}