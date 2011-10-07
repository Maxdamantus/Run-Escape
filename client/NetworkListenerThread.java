package client;

import game.*;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;

import data.Database;

import serialization.ParseException;
import serialization.Tree;

/**
 * Handles all incoming network data
 * 
 * @author greenwthom
 * 
 */
public class NetworkListenerThread extends Thread {
	private BufferedReader reader;
	private Client client;
	private GameWorld world;
	private boolean debugMode = false;

	/**
	 * 
	 * @param reader
	 *            BufferedReader to receive game model updates on
	 * @param client
	 *            client to send messages to
	 * @param world
	 *            World to apply updates to.
	 */
	public NetworkListenerThread(BufferedReader reader, Client client, GameWorld world) {
		this.reader = reader;
		this.world = world;
		this.client = client;
	}

	/**
	 * Runs the update thread
	 */
	public void run() {
		try {
			// read from the reader continuously
			while (true) {
				String incoming = reader.readLine();
				
				if (incoming == null) break;// indicates connection lost
				
				if (incoming.startsWith("log")) { // if message
					String message = incoming.substring(4);
					client.logMessage(message);
					if (debugMode) System.out.println("Message: " + message);
					
				} else if (incoming.startsWith("upd")) { // if update
					String update = Database.unescapeNewLines(incoming.substring(4));
					if (debugMode) System.out.println("updated: " + update);
					try {
						WorldDelta.SERIALIZER.read(Tree.fromString(update)).apply(world);
					} catch (ParseException e) {
						System.out.println("Tree fromString broke");
					}

				} else if (incoming.startsWith("ctc")) { // if chat
					String chatString = incoming.substring(4);
					String[] splitString = chatString.split("::::");
					client.incomingChat(splitString[1], new Color(Integer.parseInt(splitString[0])));
					

				} else if (incoming.startsWith("svm")) { // if server message
					String chatString = incoming.substring(4);
					client.incomingChat(chatString, Color.YELLOW);

				} else if (incoming.startsWith("uid")) { // if uid notif
					client.receivedUID(Long.parseLong(incoming.substring(4)));

				}
			}
			Client.exit("Connection to server lost");

		} catch (IOException e) {
			Client.exit("Connection to server lost");
		}

	}
}
