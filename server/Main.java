package server;

import java.io.IOException;
//Author: wheelemaxw

public class Main {
	
	/**
	 * For initalising the Server
	 * @param CLI if starting on command line, and if so followed by filename to load
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		new Server(args).run();
		System.exit(0);
	}
}