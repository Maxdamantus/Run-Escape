package util;

import java.util.*;

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
	}

	public static <T> Node<T> dijkstra(T from, T to, Nextator<T> next){
		PriorityQueue<Node<T>> pq = new PriorityQueue<Node<T>>();
		Map<T, Integer> scores = new HashMap<T, Integer>();
		pq.add(new Node<T>(from));

		while(pq.size() > 0){
			Node<T> n = pq.poll();
			if(n.value().equals(to))
				return n;
			for(Node<T> v : next.next(n)){
				Integer i = scores.get(v.value());
				if(i == null || v.cost() < i){
					pq.add(v);
					scores.put(v.value(), v.cost());
				}
			}
		}
		return null;
	}
}
