package common;

import serialization.*;

public interface Location<T extends GameThing> {
	public static final Writer<Location> WRITER = new Writer<Location>(){
		public Tree write(Location in){
			if(in instanceof LevelLocation){
				Tree out = new Tree();
				out.add(new Tree.Entry("type", new Tree("level")));
				out.add(new Tree.Entry("just", LevelLocation.WRITER.write((LevelLocation<T>)in)));
				return out;
			}
			throw new RuntimeException("wtf");
		}
	};
	public void put(T gt);
	public void remove(T gt);
}
