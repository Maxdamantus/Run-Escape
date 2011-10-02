package tests.client.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;

public class ClientTestServer {
	ServerSocket ss;
	Socket client;
	private BufferedReader reader;
	private BufferedWriter writer;
	
	public ClientTestServer() throws IOException {
		ss = new ServerSocket(32765);
		client = ss.accept();
		reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
		writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
		
	}
	
	public String readInput() throws IOException {
		return reader.readLine();
	}
	
	public void sendString(String send) throws IOException {
		writer.write(send);
		writer.flush();
	}
	
}
