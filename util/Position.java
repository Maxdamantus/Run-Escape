package util;

public class Position {
	private final int px, py;

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
