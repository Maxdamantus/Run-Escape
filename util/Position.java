package util;

import serialization.*;

public class Position {
	private final int px, py;

	public static final Serializer<Position> SERIALIZER = new Serializer<Position>(){
		public Tree write(Position in){
			Tree out = new Tree();
			out.add(new Tree.Entry("x", serialization.util.Serializers.Serializer_Integer.write(in.px)));
			out.add(new Tree.Entry("y", serialization.util.Serializers.Serializer_Integer.write(in.py)));
			return out;
		}

		public Position read(Tree in){
			return new Position(
				serialization.util.Serializers.Serializer_Integer.read(in.find("x")),
				serialization.util.Serializers.Serializer_Integer.read(in.find("y")));
		}
	};

	public Position(int x, int y){
		px = x; py = y;
	}

	public int x(){
		return px;
	}

	public int y(){
		return py;
	}

	public boolean equals(Object o){
		if(o instanceof Position){
			Position p = (Position)o;
			return px == p.px && py == p.py;
		}
		return false;
	}

	public int hashCode(){
		return px ^ (py >> 16) ^ (py << 16);
	}

	public String toString(){
		return "(" + px + ", " + py + ")";
	}
}
