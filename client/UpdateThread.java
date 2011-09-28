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
	
	
	public UpdateThread(BufferedReader reader, IsoInterface view , GameWorld world) {
		this.reader = reader;
		this.view = view;
	}
	
	public void run() {
		try {
			while (true) {
				String incoming = reader.readLine();
				if (incoming.startsWith("msg")) {
					String message = incoming.substring(3);
					view.logMessage(message);
					System.out.println("Message: "+message);
				}
				else if (incoming.startsWith("upd")) {
					String update= incoming.substring(3);
					
					WorldDelta.serializer(world).read(Database.xmlToTree(update)).apply(world);
					System.out.println("updated: "+update);				
				}
				else{
					String other = incoming;
					System.out.println(other);
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
