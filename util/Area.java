	package util;
	
	import java.util.*;
	
	public class Area implements Iterable<Position> {
		private final Position pos;
		private final int wd, ht;
	
		public Area(int x, int y, int w, int h){
			pos = new Position(x, y); wd = w; ht = h;
		}
		
		public Area(Position p, int w, int h){
			pos = p; wd = w; ht = h;
		}
	
		public Position position(){
			return pos;
		}
	
		public int width(){
			return wd;
		}
	
		public int height(){
			return ht;
		}
		
		public int x() {
			return pos.x();
		}
		
		public int y() {
			return pos.y();
		}
	
		public boolean equals(Object o){
			if(!(o instanceof Area))
				return false;
			Area p = (Area)o;
			return p.wd == wd && p.ht == ht && pos.equals(p.pos);
		}
	
		public int hashCode(){
			return pos.hashCode() ^ wd ^ (ht << 16) ^ 296650768;
		}
	
		public Area translated(Position p){
			return new Area(new Position(pos.x() + p.x(), pos.y() + p.y()), wd, ht);
		}
	
		public Iterator<Position> iterator(){
			Set<Position> ret = new HashSet<Position>();
			int x = pos.x(), y = pos.y();
	
			for(int dx = 0; dx < wd; dx++)
				for(int dy = 0; dy < ht; dy++)
					ret.add(new Position(x + dx, y + dy));
			return ret.iterator();
		}
	
		public String toString(){
			return "[" + pos + ", (" + wd + ", " + ht + ")]";
		}
	}
