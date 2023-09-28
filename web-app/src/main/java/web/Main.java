package web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import client.ClientMessageHandler;
import data.Database;
import game.ClientMessage;
import game.GameWorld;
import game.WorldDelta;
import game.WorldDelta.Say;
import game.things.Player;
import serialization.ParseException;
import serialization.Tree;
import shim.Shim;
import shim.awt.Color;
import ui.isometric.client.ClientInterface;
import util.Resources;

public class Main {
	private static final String PROTO_UID = "uid ";
	private static final String PROTO_NOID = "noid"; // no arguments
	private static final String PROTO_CID = "cid ";
	private static final String PROTO_CMG = "cmg ";
	private static final String PROTO_CTS = "cts ";
	private static final String PROTO_CTC = "ctc ";
	private static final String PROTO_UPD = "upd ";
	private static final String PROTO_DELIM = "::::";

	public static void main(String[] argv) {
		Resources.shimInitResources()
			.map(v -> {
				run();
				return null;
			})
			.run();
	}

	private static GameWorld newWorld0() {
		return server.Server.defaultworld();
	}

	private static GameWorld newWorld1() {
		var world = new GameWorld();
		try {
			world.fromTree(Database.xmlToTree(Resources.loadTextResource("/resources/world.wlbrd")));
		} catch (ParseException | IOException e) {
			throw new RuntimeException(e);
		}
		return world;
	}

	private static void run() {
		var s = new Server(newWorld1());
		var client = new Client(s);
		var conn = client.connection;
		conn.writeInput(PROTO_UID + "Player0");
		conn.writeInput(PROTO_CID + "duncan");
		conn.writeInput(PROTO_CTS + "Hello world");
	}

	public interface ConnectionReader {
		void accept(String message);
	}

	public static class Server {
		private final GameWorld world;
		private final ArrayList<ServerConnection> connections = new ArrayList<>();

		private Server(GameWorld world) {
			this.world = world;
			world.addDeltaWatcher(d -> {
				writeDelta(connections, d);
			});
		}

		private void writeDelta(List<ServerConnection> connections, WorldDelta d) {
			String deltaUpdate = Database.treeToString(WorldDelta.SERIALIZER.write(d));
			String message = d.action() instanceof Say?
				PROTO_CTC + deltaUpdate :
				PROTO_UPD + deltaUpdate;
			for (var c : connections)
				c.output.accept(message);
		}

		public ServerConnection addConnection(ConnectionReader output) {
			var sc = new ServerConnection(this, output);
			world.allDeltas(d -> {
				writeDelta(List.of(sc), d);
			});
			connections.add(sc);
			return sc;
		}
	}

	public static class ServerConnection {
		private Server server;
		private final ConnectionReader output;
		private Player player;
		private String tmpUsrName;

		private ServerConnection(Server server, ConnectionReader output) {
			this.server = server;
			this.output = output;
		}

		public boolean isClosed() {
			return server == null;
		}

		public void close() {
			if (server == null)
				return;
			server.connections.remove(this);
			server = null;
		}

		public void writeInput(String message) {
			try {
				if (message.startsWith(PROTO_UID)) {
					// log in to existing player, or store name for new player
					String usrName = message.substring(PROTO_UID.length());
					if (player != null) {
						player.logout();
						player = null;
					}
					if (server.world.checkPlayer(usrName)) {
						var plyr = server.world.getPlayer(usrName, null);
						if (plyr.login()) {
							player = plyr;
							output.accept(PROTO_UID + plyr.gid() + PROTO_DELIM + plyr.type());
							return;
						}
					}
					// usrName already logged in, or non-existent
					tmpUsrName = usrName;
					output.accept(PROTO_NOID);
					return;
				}
				if (message.startsWith(PROTO_CID)) {
					// create new player using previously chosen username
					String character = message.substring(PROTO_CID.length());
					if (player != null || tmpUsrName == null) {
						return;
					}
					for (int x = -1;; x++) {
						String usrName = x == -1?  tmpUsrName : tmpUsrName + x;
						if (server.world.checkPlayer(usrName))
							continue;
						var plyr = server.world.getPlayer(usrName, character);
						plyr.login();
						player = plyr;
						output.accept(PROTO_UID + plyr.gid() + PROTO_DELIM + plyr.type());
						return;
					}
				}
				if (message.startsWith(PROTO_CMG)) {
					String action = message.substring(PROTO_CMG.length());
					if (player == null)
						return;
					ClientMessage msg = ClientMessage.serializer(server.world, player.gid()).read(Tree.fromString(action));
					msg.apply(server.world);
					return;
				}
				if (message.startsWith(PROTO_CTS)) {
					String chat = message.substring(PROTO_CTS.length());
					if (player == null)
						return;
					server.world.emitSay(null, null, player.name() + ": " + chat);
					return;
				}
			} catch (Exception e) {
				new Throwable("Failed to handle message from client", e).printStackTrace();
			}
		}
	}

	public static class Client implements ConnectionReader, ClientMessageHandler {
		private final GameWorld world = new GameWorld();
		private final ServerConnection connection;
		private ClientInterface view;
		private String characterName;
		private List<Runnable> deferred = new ArrayList<>();

		public Client(Server server) {
			connection = server.addConnection(this);
		}

		@Override
		public void accept(String message) {
			try {
				if (message.startsWith(PROTO_UPD)) {
					String update = message.substring(PROTO_UPD.length());
					WorldDelta.SERIALIZER.read(Tree.fromString(update)).apply(world);
				} else if (message.startsWith(PROTO_CTC)) {
					String update = message.substring(PROTO_CTC.length());
					var delta = (WorldDelta.Say) WorldDelta.SERIALIZER.read(Tree.fromString(update)).action();
					if (view == null)
						deferred.add(() -> view.incomingChat(delta.what(), Color.YELLOW));
					else
						view.incomingChat(delta.what(), Color.YELLOW);
				} else if (message.startsWith(PROTO_UID)) {
					int i = message.indexOf(PROTO_DELIM);
					long uid = Long.parseLong(message.substring(PROTO_UID.length(), i));
					characterName = message.substring(i + PROTO_DELIM.length());
					view = new ClientInterface("Run, Escape!", world, this, uid);
					view.show();
					for (var d : deferred)
						d.run();
					deferred.clear();
					return;
				}
			} catch (Exception e) {
				new Throwable("Failed to handle message from server", e).printStackTrace();
				e.printStackTrace();
			}
		}

		@Override
		public void sendMessage(ClientMessage message) {
			String send = PROTO_CMG + Database.treeToString(ClientMessage.serializer(world, 0).write(message));
			System.out.println("<< " + send);
			connection.writeInput(send);
		}

		@Override
		public void sendChat(String chatText) {
			connection.writeInput(PROTO_CTS + chatText);
		}

		@Override
		public String getCharacterName() {
			return characterName;
		}
	}
}
