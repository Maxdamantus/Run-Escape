package client;

import game.*;
import game.WorldDelta.Action;

import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;

import javax.swing.JDialog;

import data.Database;

import serialization.ParseException;
import serialization.Tree;
import util.GUI;

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
					String update = Database.unescapeNewLines(incoming.substring(4));
					try {
						WorldDelta.Action act = WorldDelta.SERIALIZER.read(Tree.fromString(update)).action();
						String[] chat = ((WorldDelta.Say)act).what().split("::::");
						if (chat.length == 2)client.incomingChat(chat[1], new Color(Integer.parseInt(chat[0])));
						else client.incomingChat(chat[0], Color.YELLOW);
					} catch (ParseException e) {}
					
					

				} else if (incoming.startsWith("svm")) { // if server message
					String chatString = incoming.substring(4);
					client.incomingChat(chatString, Color.YELLOW);

				} else if (incoming.startsWith("uid")) { // if uid notification
					
					String[] incomingStrings = incoming.substring(4).split("::::");
					client.setCharacterName(incomingStrings[1]);
					client.receivedUID(Long.parseLong(incomingStrings[0]));

				} else if (incoming.startsWith("noid")) {
					CharacterSelector dialog = new CharacterSelector();
					dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
					dialog.setSize(new Dimension(640,400));
					GUI.centerWindow(dialog);
					dialog.setVisible(true);
					
					// sending name
					client.setAndSendCharacterName(dialog.getCharacterName());
				}
			}
			Client.exit("Connection to server lost, you can reconnect using the same using name to return where you were at");

		} catch (IOException e) {
			Client.exit("Connection to server lost, you can reconnect using the same using name to return where you were at");
		}

	}
}
