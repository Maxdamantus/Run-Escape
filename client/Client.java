package client;

import java.awt.Color;
import java.awt.Frame;
import java.io.*;
import java.lang.reflect.Field;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import data.Database;

import serialization.Serializers;
import ui.isometric.IsoInterface;

import game.*;

/**
 * Main class for the client.
 * 
 * @author greenwthom
 * 
 */
public class Client implements ClientMessageHandler {
	private Socket skt;
	private String uid;
	private InputStreamReader in;
	private BufferedReader reader;
	private OutputStreamWriter out;
	private BufferedWriter writer;
	private IsoInterface view;
	private GameWorld world = new GameWorld();
	private boolean debugMode;
	private Color chatTextColor = Color.getHSBColor((float) Math.random(), 1, 1);

	public static void main(String[] args) {
		boolean debugMode = false;
		String host = "localhost";
		String uid = "";
		int port = 32765;
		// take command line server and info
		if (args.length == 3) {
			host = args[0];
			port = Integer.parseInt(args[1]);
			uid = args[2];
		} else { // user input dialogs to get server and player information
			String server = JOptionPane
					.showInputDialog("Please enter a server ( [hostname]:[port] or [hostname] )");
			if (server == null)
				System.exit(0); // closes if cancel is pressed
			uid = JOptionPane
					.showInputDialog("Please pick a username (if you have previously connected, please use the same name)");
			if (uid == null)
				System.exit(0);// closes if cancel is pressed
			if (uid.equals(""))
				uid = "Player" + (int) (Math.random() * 1000);
			if (server.length() > 0) {
				String[] split = server.split(":");
				host = split[0];
				if (split.length == 2)
					port = Integer.parseInt(split[1]);

			}
		}
		if (debugMode)
			System.out.println(host + ", " + port);
		Client client = new Client(host, port, uid, debugMode);

	}

	/**
	 * Network client for the game
	 * 
	 * @param host
	 *            server hostname
	 * @param port
	 *            server port
	 */
	public Client(String host, int port, String uid, boolean debugMode) {
		this.uid = uid;
		this.debugMode = debugMode;
		boolean debug = true;
		try {

			// creating socket and readers/writers
			skt = new Socket(host, port);
			if (debug)
				System.out.println("connected to " + host + " on " + port);
			in = new InputStreamReader(skt.getInputStream());
			reader = new BufferedReader(in);
			out = new OutputStreamWriter(skt.getOutputStream());
			writer = new BufferedWriter(out);

			// creating GUI
			view = new IsoInterface("IsoTest", world, this);
			NetworkListenerThread updater = new NetworkListenerThread(reader, view, world);

			// sending name
			writer.write("uid " + uid + "\n");
			updater.start();
			writer.flush();

			// showing GUI

			view.show();

		} catch (UnknownHostException e) {
			Client.exit("Unknown host name");
		} catch (IOException e) {
			Client.exit("Connection to server lost");
		}

	}

	public void sendMessage(ClientMessage message) {
		try {
			String send = "cmg " + Database.escapeNewLines(Database.treeToString(new Serializers.Nullable<ClientMessage>(ClientMessage.serializer(world, 0)).write(message))) + "\n";
			if (debugMode)
				System.out.print("Sent: " + send);
			writer.write(send);
			writer.flush();
		} catch (IOException e) {
			Client.exit("Connection to server lost");
		}

	}


	public void sendChat(String chatText) {
		try {
			if (debugMode)
				System.out.print("Sent chat: " + chatText);
			if (chatText.startsWith("/me"))
				chatText = "*" + uid + " " + chatText.substring(4);
			else if (chatText.startsWith("/color")) {
				Color newColor = null;
				if (chatText.substring(7).startsWith("#")) 
						newColor = Color.decode("0x"+chatText.substring(8));
				else {
					//code from http://www.java-forums.org/advanced-java/27084-rgb-color-name.html.
					Field field;
					try {
						field = Class.forName("java.awt.Color").getField(chatText.substring(7));
						int rgb = ((Color)field.get(null)).getRGB();
						newColor = new Color(rgb);
					} catch (NoSuchFieldException e) {
						view.incomingChat("GAME: Color \"" + chatText.substring(7) + "\" not found", Color.RED);
					} catch (Exception e) {
						if (debugMode) {
							System.err.println("That's what you get for using reflection");
							e.printStackTrace();
						}
					}
					
				}
				if (newColor != null) chatTextColor = newColor;
				return;
			} else if (chatText.startsWith("/resetcolor")) {
				chatTextColor = Color.getHSBColor((float) Math.random(), 1, 1);
				return;
			} else
				chatText = uid + ": " + chatText;
			String send = "cts " + chatTextColor.getRGB() + "::::" + chatText + "\n";
			writer.write(send);
			writer.flush();
		} catch (IOException e) {
			Client.exit("Connection to server lost");
		}
	}


	/**
	 * Exits the application with a Message Dialog explaining what happened
	 * 
	 * @param message
	 *            message to be printed
	 */
	public static void exit(String message) {
		System.out.println(message);
		System.out.flush();
		JOptionPane.showMessageDialog(null, message);
		System.exit(0);
	}
	
	

}
