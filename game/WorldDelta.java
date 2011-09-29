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

		public void apply(GameWorld world){
			new DumbGameThing(world, gid);
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

		public void apply(GameWorld world){
			new DumbGameThing(world, gid);
		}

		public static Serializer<Forget> serializer(final GameWorld w){
			return new Serializer<Forget>(){
				public Tree write(Forget in /* what you saw */){
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

	public static class Update implements Action {
		private final DumbGameThing dgt;

		public Update(DumbGameThing d){
			dgt = d;
		}

		public void apply(GameWorld world){
			// assumptions ..
			((DumbGameThing)world.thingWithGID(dgt.gid())).update(dgt);
		}

		public static Serializer<Update> serializer(final GameWorld w){
			return new Serializer<Update>(){
				public Tree write(Update in){
					return DumbGameThing.serializer(w).write(in.dgt);
				}

				public Update read(Tree in){
					return new Update(DumbGameThing.serializer(w).read(in));
				}
			};
		}

		public Tree toTree(GameWorld w){
			return serializer(w).write(this);
		}

		public String type(){
			return "update";
		}
	}

	public static class Say implements Action {
		private final String what;
		private final int gid;

		public Say(int g, String w){
			gid = g;
			what = w;
		}

		public void apply(GameWorld world){
			world.emitSay(world.thingWithGID(gid), what);
		}

		public final static Serializer<Say> serializer(final GameWorld w){
			return new Serializer<Say>(){
				public Tree write(Say in){
					Tree out = new Tree();
					out.add(new Tree.Entry("gid", Serializers.Serializer_Integer.write(in.gid)));
					out.add(new Tree.Entry("what", new Tree(in.what)));
					return out;
				}

				public Say read(Tree in){
					return new Say(
						 Serializers.Serializer_Integer.read(in.find("gid")),
						 in.find("what").value());
				}
			};
		}

		public Tree toTree(GameWorld w){
			return serializer(w).write(this);
		}

		public String type(){
			return "say";
		}
	}

	private final Action action;

	public WorldDelta(Action a){
		action = a;
	}

	public void apply(GameWorld w){
		action.apply(w);
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
				else if(type.equals("update"))
					as = Update.serializer(w);
				else if(type.equals("say"))
					as = Say.serializer(w);
				return new WorldDelta(as.read(in.find("action")));
			}
		};
	}
}
