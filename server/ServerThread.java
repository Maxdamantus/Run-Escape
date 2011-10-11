

package server;

import game.ClientMessage;
import game.GameWorld;
import game.WorldDelta;
import game.WorldDelta.Say;
import game.things.Player;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.io.*;
import java.net.*;

import data.Database;

import serialization.Tree;
import util.*;

//Author: wheelemaxw

/**
 * A ServerThread contains a listener and a talker thread that
 * are responsible for communication with the client, but store all user info in the
 * ServerThread parent
 */
public final class ServerThread {
	private final GameWorld model;
	//No. of the joined user, in order of joining
	private int usrNo;
	//GID of the GameThing Character
	private long usrGID;
	//Provided username from Player
	private String usrName;
	private final Socket socket;
	//For queueing messages to be sent out the respective client
	private LinkedBlockingQueue<String> outqueue = new LinkedBlockingQueue<String>();
	boolean exit=false;
	private Listener listener = new Listener(this);
	private Talker talker = new Talker(this);
	private Server server;
	
	/**
	 * The listener thread is responsible for incoming messages from the client, which are all
	 * one per line, it processes these by the decided schema(3 letter code at the beginning of each
	 * method) and then enacts the appropriate action
	 */
	private static class Listener extends Thread {
		private ServerThread parent;
		
		public Listener(ServerThread parent) {
			this.parent = parent;
		}
		
		
		/**
		 * Gets the input stream (via a buffered reader) and takes input. UID is the initial connection code
		 * which then gets the player-avatar from the GameWorld with the provided username. This creates
		 * a new one if the provided name has not connected before
		 * 
		 * CID is used for avatar selection, if username did not exist prior
		 * 
		 * CMG is used for commands from client for the gameworld (eg display inventory)
		 * 
		 * CTS is chat to server, used for chatting between players, emits a Say delta to all players.
		 * 
		 * Throws exception if any issue with message processing, and throws a IOException if there
		 * is an issue within the rest of the try block
		 * 
		 * Finally, logs the player Gamething out of the gameworld, and informs other players.
		 * 
		 */
		public void run() {
			Player plyr = null;
			try {
				InputStreamReader input = new InputStreamReader(parent.socket.getInputStream());
				BufferedReader rd = new BufferedReader(input);
				while(!parent.exit) {
					String temp;
					
					temp = rd.readLine();
					//System.out.println(temp);
					if(temp == null) { // End of stream
						break;
					}
					
					try {
						if(temp.startsWith("uid")) {
							String name = temp.substring(3);
							parent.usrName = name;
							if(parent.model.checkPlayer(parent.usrName)){
								plyr = parent.model.getPlayer(parent.usrName, null);
								plyr.login();
								System.err.println("plyr logged in");
						//		parent.model.level(0).location(new Position((int)(Math.random()*10 - 5), (int)(Math.random()*10 - 5)), Direction.NORTH).put(plyr);
								parent.usrGID = plyr.gid();
								parent.queueMessage("uid " + parent.usrGID + "::::" + parent.usrName + "\n");
							}
							else{
								parent.queueMessage("noid\n");
							}
						}
						else if(temp.startsWith("cid")){
							String character = temp.substring(4);
							plyr = parent.model.getPlayer(parent.usrName, character);
							plyr.login();
							System.err.println("plyr logged in");
							parent.usrGID = plyr.gid();
							parent.queueMessage("uid " + parent.usrGID + "::::" + character + "\n");
						}
						else if(temp.startsWith("cmg")){
							String action = temp.substring(4);
							action = Database.unescapeNewLines(action);
							ClientMessage msg = (ClientMessage.serializer(parent.model, parent.usrGID)).read(Tree.fromString(action));
							msg.apply(parent.model);
						}
						//hopefully shouldnt be reached
						else if(temp.startsWith("cts")) {
							String chat = temp.substring(4);
							parent.model.emitSay(null, null, chat);;
						}
					}
					catch (Exception e) { // Catch everything while processing message
						System.err.println("Exception handling message from client...");
						e.printStackTrace();
					}
				}
			} catch(IOException e) {
				
				System.err.println("PLAYER " + parent.usrNo +"/" + "usrName" + " DISCONNECTED");
				
			}
			finally{
				parent.exit = true;
				if(plyr != null){
				plyr.logout();
				parent.model.emitSay(null,null,parent.usrName + " logged out");	
				}
			}
		}
	}
	
	/**
	 * The talker thread is responsible for outgoing messages to the client, which are all
	 * one per line, it takes these from the message queue and sends out.
	 */
	private static class Talker extends Thread {
		private ServerThread parent;
		
		public Talker(ServerThread parent) {
			this.parent = parent;
		}
		
		/**
		 * Writes messages if there are messages in the queue, if there is an IOException, assumes client
		 * has disconnected
		 */
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
	
	/**
	 * Creates the ServerThread
	 * @param Client-connection socket
	 * @param No. (in order of connection)
	 * @param The created GameWorld
	 */
	public ServerThread(Socket socket, int usrNo, GameWorld model) {
		this.model = model;	
		this.socket = socket;
		this.usrNo = usrNo;

	}
	
	/**
	 * adds the WorldDeltas (which have been serialized to string), prefaces with ctc(chat to client) if a 
	 * say message, and cmg otherwise.
	 * @param d
	 */
	public void addDelta(WorldDelta d){
		if(d.to() == usrGID || d.to() == -1){
			String deltaupdate = Database.escapeNewLines(Database.treeToString(WorldDelta.SERIALIZER.write(d)));
			//System.out.print(deltaupdate);
			if(d.action() instanceof Say)
				this.queueMessage("ctc " + deltaupdate + "\n");
			else
				this.queueMessage("upd " + deltaupdate + "\n");
		}
	}
	
	/**
	 * Adds message to the queue
	 * @param Message to be queued
	 */
	private void queueMessage(String msg) {
		outqueue.add(msg);
	}
	
	/**
	 * 'Starting' a ServerThread starts the respective listeners and talkers
	 */
	public void start() {
		listener.start();
		talker.start();
	}

	/**
	 * Considered dead if it is ready to be exited
	 * @return true if dead.
	 */
	public boolean isAlive(){
		return !exit;
	}
	
	public String name() {
		return usrName;
	}
}
