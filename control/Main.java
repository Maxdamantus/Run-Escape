
package control;

import java.util.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

import control.*;

import Game.game.*;


public class Main {
	private static final int CLOCK_TIME = 20;
	private static final int BROADCAST_TIME = 5;
	
	public static void main(String[] args) {
		// Choice of Server or Client		
		String filename = null;
		boolean server = false;
		boolean fromSave = false;
		int nclients = 0;		
		String url = null;		
		int gameClock = CLOCK_TIME;
		int broadcastClock = BROADCAST_TIME;
		int port = 32768; // default
		int nHomerGhosts = 2;
		int nRandomGhosts = 2;
		String choice;
		while(!choice.toLowerCase().equals("client") || !choice.toLowerCase().equals("server")){
			Object[] possibilities = {"client", "server"};
			String s = (String)JOptionPane.showInputDialog(
			                    null,
			                    "Running as a client, or a server?",
			                    "Customized Dialog",
			                    JOptionPane.PLAIN_MESSAGE,
			                    null,
			                    possibilities,
			                    "client");
		}
		if(choice.toLowerCase().equals("client")){
			server = false;
			url = JOptionPane.showInputDialog("Address of server?");
		}
		
		else if(choice.toLowerCase().equals("server")){
			server = false;
			nclients = Integer.parseInt(JOptionPane.showInputDialog("Number of max players"));
		}
			
	
				

/**				} else if(arg.equals("-clock")) {
					gameClock = Integer.parseInt(args[++i]);
				} else if(arg.equals("-port")) {
					port = Integer.parseInt(args[++i]);
				} else if(arg.equals("-nhoming")) {
					nHomerGhosts = Integer.parseInt(args[++i]);
				} else if(arg.equals("-nrandom")) {
					nRandomGhosts = Integer.parseInt(args[++i]);
				}
			}   else {
				filename = args[i];
			}
		}
		**/
		
		// Sanity checks
		if(url != null && server) {
			System.out.println("Cannot be a server and connect to another server!");
			System.exit(1);
			} 
		else if(url != null && gameClock != CLOCK_TIME) {
			System.out.println("Cannot overide clock period when connecting to server.");
			System.exit(1);
			} 
		else if(url == null && filename == null) {
			System.out.println("Board file must be provided for single user, or server mode.");
			System.exit(1);
			}
		
		try {
			if(server) {
				// Run in Server mode
				if(fromSave){
					Game board = createBoardFromFile(filename,nHomerGhosts,nRandomGhosts);
					runServer(port,nclients,gameClock,broadcastClock, board);
				}
				else{
					Game board = createBoard();
					runServer(port,nclients,gameClock,broadcastClock, board);
				}
			} 
			else if(url != null) {
				// Run in client mode
				runClient(url,port);
				}
			else{
				System.exit(1);
			}
		} catch(IOException ioe) {
			System.out.println("I/O error: " + ioe.getMessage());
			System.exit(1);
		}
		
		System.exit(0);
	}
	
	private static void runClient(String addr, int port) {
		try {
			Socket s = new Socket(addr,port);
			System.out.println("GAME CLIENT CONNECTED TO " + addr + ":" + port);			
			new Slave(s).run();
		} catch(IOException e) {
			System.err.println("I/O error: " + e.getMessage());	
		}
	}
	
	private static void runServer(int port, int nclients, int gameClock, int broadcastClock, Board game) {		
		ClockThread clk = new ClockThread(gameClock,game,null);	
		
		// Listen for connections
		System.out.println("GAME SERVER LISTENING ON PORT " + port);
		System.out.println("GAME SERVER AWAITING " + nclients + " CLIENTS");
		try {
			Master[] connections = new Master[nclients];
			// Now, we await connections.
			ServerSocket ss = new ServerSocket(port);
			while (1 == 1) {
				// 	Wait for a socket
				Socket s = ss.accept();
				System.out.println("ACCEPTED CONNECTION FROM: " + s.getInetAddress());				
				int uid = game.registerPacman();
				connections[--nclients] = new Master(s,uid,broadcastClock,game);
				connections[nclients].start();				
				if(nclients == 0) {
					System.out.println("ALL CLIENTS ACCEPTED --- GAME BEGINS");
					multiUserGame(clk,game,connections);
					System.out.println("ALL CLIENTS DISCONNECTED --- GAME OVER");
					return; // done
				}
			}
		} catch(IOException e) {
			System.err.println("I/O error: " + e.getMessage());
		} 
	}

	/**
	 * The following method controls a multi-user game. When a given game is
	 * over, it will simply restart the game with whatever players are
	 * remaining. However, if all players have disconnected then it will stop.
	 * 
	 * @param clk
	 * @param game
	 * @param connections
	 * @throws IOException
	 */
	private static void multiUserGame(ClockThread clk, Board game,
			Master... connections) throws IOException {
		// save initial state of board, so we can reset it.
		byte[] state = game.toByteArray();		
	
		clk.start(); // start the clock ticking!!!				
		
		// loop forever
		while(atleastOneConnection(connections)) {
			game.setState(Board.READY);
			pause(3000);
			game.setState(Board.PLAYING);
			// now, wait for the game to finish
			while(game.state() == Board.PLAYING) {
				Thread.yield();
			}
			// If we get here, then we're in game over mode
			pause(3000);
			// Reset board state
			game.setState(Board.WAITING);
			game.fromByteArray(state);			
		}
	}

	/**
	 * Check whether or not there is at least one connection alive.
	 * 
	 * @param connections
	 * @return
	 */
	private static boolean atleastOneConnection(Master... connections) {
		for (Master m : connections) {
			if (m.isAlive()) {
				return true;
			}			
		}
		return false;
	}
	
	private static void pause(int delay) {
		try {
			Thread.sleep(delay);
		} 
		catch(InterruptedException e){			
		}
	}		
		
}
