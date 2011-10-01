

package server;

import game.ClientMessage;
import game.GameWorld;
import game.WorldDelta;
import game.things.Player;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.io.*;
import java.net.*;

import data.Database;

import util.*;

//new ClientMessage(mygid, new ClientMessage.Interaction(thatgid, foo)).apply(game);

/**
 * A Server thread receives xml formatted-instructions from a client connection via a socket.
 * These intructions are registered with the game model. The Server connection is also
 * responsible for transmitting information to the client about the updates to the game
 * state.
 */
public final class ServerThread {
	private final GameWorld model;
	private int usrNo;
	private int usrGID;
	private String usrName;
	private final Socket socket;
	private LinkedBlockingQueue<String> outqueue = new LinkedBlockingQueue<String>();
	boolean exit=false;
	private Listener listener = new Listener(this);
	private Talker talker = new Talker(this);
	private Server server;
	
	private static class Listener extends Thread {
		private ServerThread parent;
		
		public Listener(ServerThread parent) {
			this.parent = parent;
		}
		
		public void run() {
			try {
				InputStreamReader input = new InputStreamReader(parent.socket.getInputStream());
				BufferedReader rd = new BufferedReader(input);
				while(!parent.exit) {
					String xmlupdate = "";
					String temp;
					
					temp = rd.readLine();
					if(temp == null) { // End of stream
						break;
					}
					
					if((temp.startsWith("uid"))) {
						xmlupdate+= temp;
						parent.usrName = xmlupdate.substring(4);
						Player plyr = new Player(parent.model);
						parent.model.level(0).location(new Position((int)(Math.random()*10 - 5), (int)(Math.random()*10 - 5)), Direction.NORTH).put(plyr);
						parent.usrGID = plyr.gid();
						//
						
					}
					else if(temp.startsWith("cmg")){
						String action = temp.substring(4);
						action = Database.unescapeNewLines(action);
						ClientMessage msg = ClientMessage.serializer(parent.model, parent.usrGID).read(Database.xmlToTree(action));
						msg.apply(parent.model);
					}
					else if(temp.startsWith("cts")) {
						String chat = temp.substring(4);
						final String msg = "ctc "+chat+"\n";
						parent.server.toAllPlayers(new Server.ClientMessenger() {
							@Override
							public void doTo(ServerThread client) {
								client.queueMessage(msg);
							}
						});
					}
				}
			} catch(IOException e) {
				System.err.println("PLAYER " + parent.usrNo +"/" + "usrName" + " DISCONNECTED");
			}
			parent.exit = true;
		}
	}
	
	private static class Talker extends Thread {
		private ServerThread parent;
		
		public Talker(ServerThread parent) {
			this.parent = parent;
		}
		
		public void run() {
			try {
				OutputStreamWriter output = new OutputStreamWriter(parent.socket.getOutputStream());			
				BufferedWriter bw = new BufferedWriter(output);
				while(!parent.exit) {
					String msg = null;
					
					try { msg = parent.outqueue.poll(2, TimeUnit.SECONDS); } catch (InterruptedException e) {}
					
					if(msg != null) {
						bw.write(msg);
						bw.flush();
					}
				}
			} catch(IOException e) {
				System.err.println("PLAYER " + parent.usrNo +"/" + "usrName" + " DISCONNECTED");
			}
			parent.exit = true;
		}
	}

	public ServerThread(Socket socket, int usrNo, GameWorld model, Clock timer, Server server) {
		this.model = model;	
		this.socket = socket;
		this.usrNo = usrNo;
		this.server = server;
	}
	
	public void addDelta(WorldDelta d){
		String deltaupdate = Database.escapeNewLines(Database.treeToXML(WorldDelta.SERIALIZER.write(d)));
		this.queueMessage("upd " + deltaupdate + "\n");
	}
	
	private void queueMessage(String msg) {
		outqueue.add(msg);
	}
	
	public void start() {
		listener.start();
		talker.start();
	}

	public boolean isAlive(){
		return !exit;
	}
	
	public String name() {
		return usrName;
	}
}
