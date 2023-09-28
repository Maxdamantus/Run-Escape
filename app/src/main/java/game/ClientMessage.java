package game;

import serialization.*;

/**
 * Representation of messages from the client to the server; the only type of message using this interface though is a GameThing interaction.
 */
public class ClientMessage {
	/**
	 * An action performed by a client.
	 */
	public static interface Action {
		/**
		 * Apply the action to the given server world.
		 */
		public void apply(GameWorld w, long from);
		/**
		 * Get a tree representation of this object.
		 */
		public Tree toTree(GameWorld w);
		/**
		 * A name that identifies this type of Action.
		 */
		public String type();
	}

	public static class Interaction implements Action {
		private final long gid;
		private final String name;

		public Interaction(long g, String n){
			gid = g; name = n;
		}

		public void apply(GameWorld world, long from){
			world.thingWithGID(gid).interact(name, (game.things.Player)world.thingWithGID(from));
		}

		public static Serializer<Interaction> serializer(final GameWorld w){
			return new Serializer<Interaction>(){
				public Tree write(Interaction in){
					Tree out = new Tree();
					out.add(new Tree.Entry("gid", Serializers.Serializer_Long.write(in.gid)));
					out.add(new Tree.Entry("name", new Tree(in.name)));
					return out;
				}

				public Interaction read(Tree in) throws ParseException {
					return new Interaction(
						Serializers.Serializer_Long.read(in.find("gid")),
						in.find("name").value());
				}
			};
		}

		public Tree toTree(GameWorld w){
			return serializer(w).write(this);
		}

		public String type(){
			return "interaction";
		}
	}

	private final Action action;
	// the Player's gid
	private final long from;

	/**
	 * @param a Composed action
	 * @param f GID of player whom it's from
	 */
	public ClientMessage(Action a, long f){
		action = a;
		from = f;
	}

	/**
	 * Apply this message to the given server-type GameWorld.
	 */
	public void apply(GameWorld w){
		action.apply(w, from);
	}

	public final static Serializer<ClientMessage> serializer(final GameWorld w, final long from){
		return new Serializer<ClientMessage>(){
			public Tree write(ClientMessage in){
				Tree out = new Tree();
				out.add(new Tree.Entry("type", new Tree(in.action.type())));
				out.add(new Tree.Entry("action", in.action.toTree(w)));
				return out;
			}

			public ClientMessage read(Tree in) throws ParseException {
				Reader<? extends Action> as = null;
				String type = in.find("type").value();
				if(type.equals("interaction"))
					as = Interaction.serializer(w);
				return new ClientMessage(as.read(in.find("action")), from);
			}
		};
	}
}
