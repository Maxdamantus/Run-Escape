package client;

import game.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import data.Database;

import serialization.Tree;
import ui.isometric.IsoInterface;

/**
 * Handles all game state updates 
 * @author greenwthom
 *
 */
public class UpdateThread extends Thread{
	private BufferedReader reader;
	private IsoInterface view;
	private GameWorld world;
	
	/**
	 * 
	 * @param reader BufferedReader to receive game model updates on
	 * @param view GUI to send messages to 
	 * @param world World to apply updates to.
	 */
	public UpdateThread(BufferedReader reader, IsoInterface view , GameWorld world) {
		this.reader = reader;
		this.view = view;
	}
	
	/**
	 * Runs the update thread
	 */
	public void run() {
		try {
			//read from the reader continuously
			while (true) {
				String incoming = reader.readLine();
				if (incoming.startsWith("msg")) { //if message
					String message = incoming.substring(4);
					view.logMessage(message);
					System.out.println("Message: "+message);
				}
				else if (incoming.startsWith("upd")) { //if update
					String update= incoming.substring(4);
					WorldDelta.serializer(world).read(Database.xmlToTree(Database.unescapeNewLines(update))).apply(world);
					System.out.println("updated: "+update);				
				}
				else{ //not needed, but can be used for printing network debugging info
					String other = incoming;
					System.out.println(other);
				}
			}
			
		} catch (IOException e) {
			System.out.println("Oops, unable to read from ");
		}
		
	}
}
