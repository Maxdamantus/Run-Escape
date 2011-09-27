package common;

import serialization.*;
import serialization.util.Serializers;

public class WorldDelta {
	public static interface Action {
		public void apply(client.model.GameWorld world);
		public Tree toTree();
		public String type();
	}

	public static class Put implements Action {
		private final int gid;
		private final client.model.Location loc;

		public Put(int g, client.model.Location l){
			gid = g; loc = l;
		}

		public void apply(client.model.GameWorld world){
			loc.put(world.thingWithGID(gid));
		}

		public static Serializer<Put> serializer(){
			return new Serializer<Put>(){
				public Tree write(/* Vladimir */ Put in){
					Tree out = new Tree();
					out.add(new Tree.Entry("gid", Serializers.Serializer_Integer.write(in.gid)));
					out.add(new Tree.Entry("location", client.model.Location.WRITER.write(in.loc)));
					return out;
				}

				public Put read(Tree in){
					int gid = -1;
					client.model.Location loc = null;
					for(Tree.Entry te : in.children())
						if(te.name().equals("gid"))
							gid = Serializers.Serializer_Integer.read(te.tree());
						else if(te.name().equals("location"))
							loc = client.model.Location.READER.read(te.tree());
					return new Put(gid, loc);
				}
			};
		}

		public Tree toTree(){
			return serializer().write(this);
		}

		public String type(){
			return "put";
		}
	}

	private final Action action;

	public WorldDelta(Action a){
		action = a;
	}

	public final static Serializer<WorldDelta> serializer = new Serializer<WorldDelta>(){
		public Tree write(WorldDelta in){
			Tree out = new Tree();
			out.add(new Tree.Entry("type", new Tree(in.action.type())));
			out.add(new Tree.Entry("action", in.action.serialiser().write(action)));

			return out;
		}

		public WorldDelda read(Tree in){
			Serializer<Action> as = null;
			for(Tree.Entry te : in.children())
				if(te.name().equals("type")){
					if(te.tree().value().equals("put"))
						as = Put.serializer();
				}else if(te.name().equals("action"))
					return as.read(te.tree());
			throw new RuntimeException("wtf");
		}
	};
}
