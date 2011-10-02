package client;

import game.*;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;

import javax.swing.JOptionPane;

import data.Database;

import serialization.Tree;
import ui.isometric.IsoInterface;

/**
 * Handles all incoming network data
 * 
 * @author greenwthom
 * 
 */
public class NetworkListenerThread extends Thread {
	private BufferedReader reader;
	private IsoInterface view;
	private GameWorld world;
	private boolean debugMode = false;

	/**
	 * 
	 * @param reader
	 *            BufferedReader to receive game model updates on
	 * @param view
	 *            GUI to send messages to
	 * @param world
	 *            World to apply updates to.
	 */
	public NetworkListenerThread(BufferedReader reader, IsoInterface view, GameWorld world) {
		this.reader = reader;
		this.view = view;
		this.world = world;
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
					view.logMessage(message);
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
					view.incomingChat(splitString[1], new Color(Integer.parseInt(splitString[0])));
					

				} else if (incoming.startsWith("svm")) { // if server message
					String chatString = incoming.substring(4);
					view.incomingChat(chatString, Color.YELLOW);

				}
			}
			Client.exit("Connection to server lost");

		} catch (IOException e) {
			Client.exit("Connection to server lost");
		}

	}
}
