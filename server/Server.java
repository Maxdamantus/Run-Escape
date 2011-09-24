

package server;

import game.GameModel;

import java.util.*;
import java.io.*;
import java.net.*;

import data.Database;



/**
 * A Server thread receives xml formatted-instructions from a client connection via a socket.
 * These intructions are registered with the game model. The Server connection is also
 * responsible for transmitting information to the client about the updates to the game
 * state.
 */
public final class Server extends Thread {
	private final GameModel model;
	private final int uid;
	private final Socket socket;
	boolean exit=false;

	public Server(Socket socket, int uid, GameModel model) {
		this.model = model;	
		this.socket = socket;
		this.uid = uid;
	}
	
	public void run() {		
		try {
			InputStreamReader input = new InputStreamReader(socket.getInputStream());
			OutputStreamWriter output = new OutputStreamWriter(socket.getOutputStream());			
			BufferedReader rd = new BufferedReader(input);
			BufferedWriter bw = new BufferedWriter(output);
			int i = 0;
			while(!exit) {
				String xmlupdate = "";
				String temp;
				if(rd.ready()){
					if(( temp = rd.readLine()) != null) {
						xmlupdate+= temp;
					}			
				} 
				System.out.println(xmlupdate);
				/**
				 * Insert game altering here
				 */
				
//		xmlDecrypt(xmlupdate);
//		model.update();
				// Now, broadcast the state of the board to client
				//Update to game array 
				
//			To Client	
//			msg [message]\n	messages to send to the client
//			upd [update]\n	updates to the game model
					

//			To Server	
//			uid [username]\n	HANDSHAKE STAGE. sets the user id, for returning after quitting
//			[action]\n[object gid]\n	sends an interaction to the game model
				
				String update = Database.treeToString(model.serialize()) + " " + i;
				System.out.println(Database.treeToString(model.serialize()) + " " + i);
				bw.write(update);
				bw.flush();
				try {
					this.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				i++;
			}
			socket.close(); // release socket
		} catch(IOException e) {
			System.err.println("PLAYER " + uid + " DISCONNECTED");
			this.exit = true;
		}		
	}
	
	//decrypts
	private String xmlDecrypt(String xmlupdate) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean getExit(){
		return exit;
	}
}
