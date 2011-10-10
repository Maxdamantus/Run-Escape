package server;

import java.io.IOException;


public class Main {


	public static void main(String[] args) throws IOException {
		new Server(args).run();
		System.exit(0);
	}
}