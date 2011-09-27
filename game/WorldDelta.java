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

	// I don't like this duplication -_-
	// Meh .. Java's repetitive enough anyway
	// $ grep -r 'public' $(find . -name \*.java) | wc -l
	// 369
	public static class Introduce implements Action {
		private final int gid;

		public Introduce(int g){
			gid = g;
		}

		public int gid(){
			return gid;
		}

		public void apply(GameWorld world){
			new DumbGameThing(world, gid());
		}

		public static Serializer<Introduce> serializer(final GameWorld w){
			return new Serializer<Introduce>(){
				public Tree write(Introduce in){
					Tree out = new Tree();
					out.add(new Tree.Entry("gid", Serializers.Serializer_Integer.write(in.gid)));
					return out;
				}

				public Introduce read(Tree in){
					return new Introduce(Serializers.Serializer_Integer.read(in.find("gid")));
				}
			};
		}

		public Tree toTree(GameWorld w){
			return serializer(w).write(this);
		}

		public String type(){
			return "introduce";
		}
	}

	public static class Forget implements Action {
		private final int gid;

		public Forget(int g){
			gid = g;
		}

		public int gid(){
			return gid;
		}

		public void apply(GameWorld world){
			new DumbGameThing(world, gid());
		}

		public static Serializer<Forget> serializer(final GameWorld w){
			return new Serializer<Forget>(){
				public Tree write(Forget in){
					Tree out = new Tree();
					out.add(new Tree.Entry("gid", Serializers.Serializer_Integer.write(in.gid)));
					return out;
				}

				public Forget read(Tree in){
					return new Forget(Serializers.Serializer_Integer.read(in.find("gid")));
				}
			};
		}

		public Tree toTree(GameWorld w){
			return serializer(w).write(this);
		}

		public String type(){
			return "forget";
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
				String type = in.find("type").value();
				if(type.equals("put"))
					as = Put.serializer(w);
				else if(type.equals("introduce"))
					as = Introduce.serializer(w);
				else if(type.equals("forget"))
					as = Forget.serializer(w);
				return new WorldDelta(as.read(in.find("action")));
			}
		};
	}
}
