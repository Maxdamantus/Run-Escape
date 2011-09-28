package game;

import serialization.*;
import serialization.util.Serializers;

public class ClientMessage {
	public static interface Action {
		public void apply(GameWorld w, int from);
		public Tree toTree(GameWorld w);
		public String type();
	}

	public static class Interaction implements Action {
		private final int gid;
		private final String name;

		public Interaction(int g, String n){
			gid = g; name = n;
		}

		public void apply(GameWorld world, int from){
			world.thingWithGID(gid).interact(name, (game.things.Player)world.thingWithGID(from));
		}

		public static Serializer<Interaction> serializer(final GameWorld w){
			return new Serializer<Interaction>(){
				public Tree write(Interaction in){
					Tree out = new Tree();
					out.add(new Tree.Entry("gid", Serializers.Serializer_Integer.write(in.gid)));
					out.add(new Tree.Entry("name", new Tree(in.name)));
					return out;
				}

				public Interaction read(Tree in){
					return new Interaction(
						Serializers.Serializer_Integer.read(in.find("gid")),
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
	private final int from;

	public ClientMessage(Action a, int f){
		action = a;
		from = f;
	}

	public void apply(GameWorld w){
		action.apply(w, from);
	}

	public final static Serializer<ClientMessage> serializer(final GameWorld w, final int from){
		return new Serializer<ClientMessage>(){
			public Tree write(ClientMessage in){
				Tree out = new Tree();
				out.add(new Tree.Entry("type", new Tree(in.action.type())));
				out.add(new Tree.Entry("action", in.action.toTree(w)));
				return out;
			}

			public ClientMessage read(Tree in){
				Reader<? extends Action> as = null;
				String type = in.find("type").value();
				if(type.equals("interaction"))
					as = Interaction.serializer(w);
				return new ClientMessage(as.read(in.find("action")), from);
			}
		};
	}
}
