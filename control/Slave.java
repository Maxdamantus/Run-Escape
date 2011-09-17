// This file is part of the Multi-player Pacman Game.
//
// Pacman is free software; you can redistribute it and/or modify 
// it under the terms of the GNU General Public License as published 
// by the Free Software Foundation; either version 3 of the License, 
// or (at your option) any later version.
//
// Pacman is distributed in the hope that it will be useful, but 
// WITHOUT ANY WARRANTY; without even the implied warranty of 
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See 
// the GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public 
// License along with Pacman. If not, see <http://www.gnu.org/licenses/>
//
// Copyright 2010, David James Pearce. 

package control;

import java.awt.event.*;
import java.io.*;
import java.net.*;


/**
 * A slave connection receives information about the current state of the board
 * and relays that into the local copy of the board. The slave connection also
 * notifies the master connection of key presses by the player.
 */
public final class Slave extends Thread implements KeyListener {
	private final Socket socket;
	private Board game;	
	private DataOutputStream output;
	private DataInputStream input;
	private int uid;
	private int totalSent;

	/**
	 * Construct a slave connection from a socket. A slave connection does no
	 * local computation, other than to display the current state of the board;
	 * instead, board logic is controlled entirely by the server, and the slave
	 * display is only refreshed when data is received from the master
	 * connection.
	 * 
	 * @param socket
	 * @param dumbTerminal
	 */
	public Slave(Socket socket) {				
		this.socket = socket;				
	}
	
	public void run() {
		try {			
			output = new DataOutputStream(socket.getOutputStream());
			input = new DataInputStream(socket.getInputStream());
			
			// First job, is to read the period so we can create the clock				
			uid = input.readInt();					
			int width = input.readInt();
			int height = input.readInt();	
			int bitwidth = width%8 == 0 ? width : width+8;
			int bitsize = (bitwidth/8)*height;
			byte[] wallBytes = new byte[bitsize];
			input.read(wallBytes);			
			System.out.println("PACMAN CLIENT UID: " + uid);
			System.out.println("PACMAN CLIENT BOARD DIMENSIONS: " + width + " x " + height);			
			game = new Board(width,height);
			game.wallsFromByteArray(wallBytes);			
			BoardFrame display = new BoardFrame("Pacman (client@" + socket.getInetAddress() + ")",game,uid,this);			
			boolean exit=false;
			long totalRec = 0;

			while(!exit) {
				// read event
				int amount = input.readInt();
				byte[] data = new byte[amount];
				input.readFully(data);					
				game.fromByteArray(data);
				display.repaint();
				totalRec += amount;
				// print out some useful information about the amount of data
				// sent and received
				System.out.print("\rREC: " + (totalRec / 1024) + "KB ("
						+ (rate(amount) / 1024) + "KB/s) TX: " + totalSent
						+ " Bytes");			
			}
			socket.close(); // release socket ... v.important!
		} catch(IOException e) {
			System.err.println("I/O Error: " + e.getMessage());
			e.printStackTrace(System.err);
		}
	}

	/**
	 * The following method calculates the rate of data received in bytes/s, albeit
	 * in a rather coarse manner.
	 * 
	 * @param amount
	 * @return
	 */
	private int rate(int amount) {
		rateTotal += amount;
		long time = System.currentTimeMillis();
		long period = time - rateStart;		
		if(period > 1000) {
			// more than a second since last calculation
			currentRate = (rateTotal * 1000) / (int) period;
			rateStart = time;
			rateTotal = 0;
		}
		
		return currentRate;		
	}
	private int rateTotal = 0;   // total accumulated this second
	private int currentRate = 0; // rate of reception last second
	private long rateStart = System.currentTimeMillis();  // start of this accumulation perioud 
	
	// The following intercept keyboard events from the user.
	
	public void keyPressed(KeyEvent e) {		
		try {
			int code = e.getKeyCode();
			if(code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_KP_RIGHT) {													
				output.writeInt(3);
				totalSent += 4;
			} else if(code == KeyEvent.VK_LEFT || code == KeyEvent.VK_KP_LEFT) {				
				output.writeInt(4);
				totalSent += 4;
			} else if(code == KeyEvent.VK_UP) {				
				output.writeInt(1);
				totalSent += 4;
			} else if(code == KeyEvent.VK_DOWN) {						
				output.writeInt(2);
				totalSent += 4;
			}
			output.flush();
		} catch(IOException ioe) {
			// something went wrong trying to communicate the key press to the
			// server.  So, we just ignore it.
		}
	}
	
	public void keyReleased(KeyEvent e) {		
	}
	
	public void keyTyped(KeyEvent e) {
		
	}		
}
