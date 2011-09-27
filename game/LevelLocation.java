package game;

import util.*;
import serialization.*;

public interface LevelLocation extends Location {
	public static final Writer<LevelLocation> WRITER = new Writer<LevelLocation>(){
		public Tree write(LevelLocation in){
			Tree out = new Tree();
			out.add(new Tree.Entry("position", Position.SERIALIZER.write(in.position())));
			out.add(new Tree.Entry("direction", Direction.SERIALIZER.write(in.direction())));
			return out;
		}
	};

	public Position position();
	public Direction direction();
	public int levelInt();
}
