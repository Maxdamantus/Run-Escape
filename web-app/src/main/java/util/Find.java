package util;

import java.util.PriorityQueue;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

import game.Level;

public class Find {
	public static class Node<T> implements Comparable<Node<T>> {
		private final T value;
		private final int cost, len;
		private final Node<T> from;

		private Node(T v, Node<T> f, int c, int l){
			value = v;
			from = f;
			cost = c;
			len = l;
		}

		public Node(T v){
			this(v, null, 0, 0);
		}

		public Node<T> next(T v, int c){
			return new Node<T>(v, this, cost + c, len + 1);
		}

		public Node<T> from(){
			return from;
		}

		public int cost(){
			return cost;
		}

		public int length(){
			return len;
		}

		public T value(){
			return value;
		}

		public int hashCode(){
			return cost;
		}

		public boolean equals(Object o){
			return this == o || o instanceof Node && cost == ((Node<?>)o).cost;
		}

		public int compareTo(Node<T> o){
			return cost - o.cost;
		}
	}

	public static interface Nextator<T> {
		public Iterable<Node<T>> next(Node<T> v);
		public boolean end(Node<T> v);
	}

	private static JSString jsKey(Level.Location l) {
		var p = l.position();
		return jsKey(p.x(), p.y(), l.level().z());
	}

	private static interface JSMap extends JSObject {}
	private static interface JSMaybeInt extends JSObject {}

	@JSBody(params = { "x", "y", "z" }, script = "return x + \",\" + y + \",\" + z;")
	private static native JSString jsKey(int x, int y, int z);

	@JSBody(script = "return new Map();")
	private static native JSMap jsMapNew();

	@JSBody(params = { "map", "key", "val" }, script = "map.set(key, val);")
	private static native void jsMapSet(JSMap map, JSString key, int val);

	@JSBody(params = { "map", "key" }, script = "return map.get(key);")
	private static native JSMaybeInt jsMapGet(JSMap map, JSString key);

	@JSBody(params = { "maybe" }, script = "return maybe !== void 0;")
	private static native boolean jsIsPresent(JSMaybeInt maybe);

	@JSBody(params = { "maybe" }, script = "return maybe;")
	private static native int jsGet(JSMaybeInt maybe);

	public static <T extends Level.Location> Node<T> dijkstra(T from, Nextator<T> next){
		PriorityQueue<Node<T>> pq = new PriorityQueue<Node<T>>();
		var scores = jsMapNew();
		pq.add(new Node<T>(from));

		while(pq.size() > 0){
			Node<T> n = pq.poll();
			if(next.end(n))
				return n;
			for(Node<T> v : next.next(n)){
				JSString key = jsKey(v.value());
			    JSMaybeInt i = jsMapGet(scores, key);
				if(!jsIsPresent(i) || v.cost() < jsGet(i)){
					pq.add(v);
					jsMapSet(scores, key, v.cost());
				}
			}
		}
		return null;
	}
}
