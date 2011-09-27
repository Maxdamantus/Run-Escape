package game;

import serialization.*;

// hrm ..
public class LocationS {
	public static Serializer<Location> s(final GameWorld w){
		return new Serializer<Location>(){
			public Tree write(Location in){
				if(in instanceof Level.Location){
					Tree out = new Tree();
					out.add(new Tree.Entry("type", new Tree("level")));
					out.add(new Tree.Entry("just", Level.Location.serializer(null).write((Level.Location)in)));
					return out;
				}
				throw new RuntimeException("wtf");
			}
			
			// will think of making a general union type serializer helper thing later
			public Location read(Tree in){
				String type = in.find("type").value();
				Tree cont = in.find("just");
				if(type.equals("level"))
					return Level.Location.serializer(w).read(cont);
				throw new RuntimeException("wtf");
			}
		};
	}
}
