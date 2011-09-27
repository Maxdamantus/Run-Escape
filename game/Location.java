package game;

import serialization.*;

public interface Location {
	public static final Writer<Location> WRITER = new Writer<Location>(){
		public Tree write(Location in){
			if(in instanceof Level.Location){
				Tree out = new Tree();
				out.add(new Tree.Entry("type", new Tree("level")));
				out.add(new Tree.Entry("just", Level.Location.serializer(null).write((Level.Location)in)));
				return out;
			}
			throw new RuntimeException("wtf");
		}
	};
	public void put(GameThing gt);
	public void remove(GameThing gt);
}
