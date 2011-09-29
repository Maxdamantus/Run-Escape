

package server;

import game.GameWorld;

import java.util.*;
import java.io.*;
import java.net.*;

import data.Database;

//new ClientMessage(mygid, new ClientMessage.Interaction(thatgid, foo)).apply(game);

/**
 * A Server thread receives xml formatted-instructions from a client connection via a socket.
 * These intructions are registered with the game model. The Server connection is also
 * responsible for transmitting information to the client about the updates to the game
 * state.
 */
public final class Server extends Thread {
	private final GameWorld model;
	private final int usrNo;
	private final Socket socket;
	private final Clock timer;
	private int timerint;
	boolean exit=false;

	public Server(Socket socket, int usrNo, GameWorld model, Clock timer) {
		this.model = model;	
		this.socket = socket;
		this.usrNo = usrNo;
		this.timer = timer;
		this.timerint = timer.getCounter();
	}
	
	public void run() {		
		try {
			
			InputStreamReader input = new InputStreamReader(socket.getInputStream());
			OutputStreamWriter output = new OutputStreamWriter(socket.getOutputStream());			
			BufferedReader rd = new BufferedReader(input);
			BufferedWriter bw = new BufferedWriter(output);
			while(!exit) {
				if(timerint != timer.getCounter()){
					String xmlupdate = "";
					String temp;
					if(rd.ready()){
						temp = rd.readLine();
						if((temp.startsWith("uid"))) {
							xmlupdate+= temp;
						}
						else if(temp.startsWith("act")){
							String action = temp.substring(4);
							int objectGid = Integer.getInteger(rd.readLine());
						}
					} 
					System.out.println(timer.getCounter());
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
//			act [action]\n[object gid]\n	sends an interaction to the game model
					String update = "upd "+Database.escapeNewLines(Database.treeToXML(model.serialize()))+"\n";
					System.out.print("upd "+Database.escapeNewLines(Database.treeToXML(model.serialize()))+"\n");
					bw.write(update);
					bw.flush();
					timerint++;
				}
			}
			socket.close(); // release socket
		} catch(IOException e) {
			System.err.println("PLAYER " + usrNo + " DISCONNECTED");
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
