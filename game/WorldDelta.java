package game;

import serialization.*;
import serialization.util.Serializers;

public class WorldDelta {
	public static interface Action {
		public void apply(GameWorld w);
		public Tree toTree(GameWorld w);
		public String type();
	}

	public static class Put implements Action {
		private final int gid;
		private final Location loc;

		public Put(int g, Location l){
			gid = g; loc = l;
		}

		public void apply(GameWorld world){
			loc.put(world.thingWithGID(gid));
		}

		public static Serializer<Put> serializer(final GameWorld w){
			return new Serializer<Put>(){
				public Tree write(/* Vladimir */ Put in){
					Tree out = new Tree();
					out.add(new Tree.Entry("gid", Serializers.Serializer_Integer.write(in.gid)));
					out.add(new Tree.Entry("location", LocationS.s(w).write(in.loc)));
					return out;
				}

				public Put read(Tree in){
					return new Put(
						Serializers.Serializer_Integer.read(in.find("gid")),
						LocationS.s(w).read(in.find("location")));
				}
			};
		}

		public Tree toTree(GameWorld w){
			return serializer(w).write(this);
		}

		public String type(){
			return "put";
		}
	}

	private final Action action;

	public WorldDelta(Action a){
		action = a;
	}

	public final static Serializer<WorldDelta> serializer(final GameWorld w){
		return new Serializer<WorldDelta>(){
			public Tree write(WorldDelta in){
				Tree out = new Tree();
				out.add(new Tree.Entry("type", new Tree(in.action.type())));
				out.add(new Tree.Entry("action", in.action.toTree(w)));
				return out;
			}

			public WorldDelta read(Tree in){
				Reader<? extends Action> as = null;
				for(Tree.Entry te : in.children())
					if(te.name().equals("type")){
						if(te.tree().value().equals("put"))
							as = Put.serializer(w);
					}else if(te.name().equals("action"))
						return new WorldDelta(as.read(te.tree()));
				throw new RuntimeException("wtf");
			}
		};
	}
}
