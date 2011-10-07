package util;

import java.util.*;

/**
 * @author zerzoumax
 */

public class DegenerateTrie<T> implements Iterable<Map.Entry<Position, T>> {
	private final int CHILDREN = 4;
	// Map<Position, Set<T>[][]>
	private Map<Position, Object[][]> roots = new HashMap<Position, Object[][]>();

	public void put(Position p, T v){
		put(p.x(), p.y(), v);
	}

	private static int moda(int q, int d){
		return q >= 0? q%d : d + q%d;
	}

	public void put(int x, int y, T v){
		System.out.println("put(" + x + ", " + y + ", " + v + ")");
		//System.out.println("-> " + set());
		Position p;
		Object[][] aa = roots.get(p = new Position(x/(CHILDREN*CHILDREN), y/(CHILDREN*CHILDREN)));
		if(aa == null)
			roots.put(p, aa = new Object[CHILDREN*CHILDREN][]);
		System.out.println("roots(" + x/(CHILDREN*CHILDREN) + ", " + y/(CHILDREN*CHILDREN) + ")");
		x = moda(x, CHILDREN*CHILDREN);
		y = moda(y, CHILDREN*CHILDREN);
		Object[] a = aa[CHILDREN*(y/CHILDREN) + x/CHILDREN];
		if(a == null)
			aa[CHILDREN*(y/CHILDREN) + x/CHILDREN] = a = new Object[CHILDREN*CHILDREN];
		System.out.println("(" + x/CHILDREN + ", " + y/CHILDREN + ")");
		x = moda(x, CHILDREN);
		y = moda(y, CHILDREN);
		Set<T> s = (Set<T>)a[y*CHILDREN + x];
		if(s == null)
			a[y*CHILDREN + x] = s = new HashSet<T>();
		System.out.println("(" + x + ", " + y + ")");
		s.add(v);
	}

	public boolean remove(Position p, T v){
		return remove(p.x(), p.y(), v);
	}

	public boolean remove(int x, int y, T v){
		Position p;
		Object[][] aa = roots.get(p = new Position(x/(CHILDREN*CHILDREN), y/(CHILDREN*CHILDREN)));
		if(aa == null)
			return false;
		int x1 = moda(x, CHILDREN*CHILDREN);
		int y1 = moda(y, CHILDREN*CHILDREN);
		Object[] a = aa[CHILDREN*(y1/CHILDREN) + x1/CHILDREN];
		if(a == null)
			return false;
		int x2 = moda(x1, CHILDREN);
		int y2 = moda(y1, CHILDREN);
		Set<T> s = (Set<T>)a[y2*CHILDREN + x2];
		if(s == null)
			return false;
		boolean r = s.remove(v);
		if(s.isEmpty()){
			a[y2*CHILDREN + x2] = null;
			for(int z = 0; z < CHILDREN*CHILDREN; z++)
				if(a[z] != null)
					return r;
			aa[CHILDREN*(y1/CHILDREN) + x1/CHILDREN] = null;
			for(int z = 0; z < CHILDREN*CHILDREN; z++)
				if(aa[z] != null)
					return r;
			roots.remove(p);
		}
		return r;
	}

	private class DTIterator implements Iterator<Map.Entry<Position, T>> {
		private final boolean all;
		private int l0x, l0y, l1x, l1y, l2x, l2y;
		private int a0x, a0y, a1x, a1y, a2x, a2y;
		private final int minx, miny, maxx, maxy;
		private final Iterator<Map.Entry<Position, Object[][]>> iroots;
		private Object[][] l1o;
		private Object[] l2o;
		private Iterator<T> iset;

		public DTIterator(){
			all = true;
			iroots = roots.entrySet().iterator();
			l1x = l2x = l2y = CHILDREN;
			minx = miny = maxx = maxy = 0;
		}

		public DTIterator(int ix, int iy, int ax, int ay){
			all = false;
			iroots = null;
			minx = ix; miny = iy; maxx = ax; maxy = ay;
			l1x = l2x = l2y = CHILDREN;
			// -1 in case of negative: rounds towards zero unfortunately.
			l0x = minx/(CHILDREN*CHILDREN) - 1;
			l0y = miny/(CHILDREN*CHILDREN) - 1;
			System.out.println("DTIterator(" + ix + ", " + iy + ", " + ax + ", " + ay + ")");
			System.out.println("l0x = " + l0x + ", l0y = " + l0y);
		}

		public DTIterator(int ix, int iy, int ax, int ay, boolean _){
			all = true;
			iroots = roots.entrySet().iterator();
			minx = ix; miny = iy; maxx = ax; maxy = ay;
		}

		public void remove(){
			throw new UnsupportedOperationException();
		}

		public boolean hasNext(){
			int x = 0;
			int r = (int)(Math.random()*1000);
			hasNext: for(;;){
		//		System.out.println(r + " tick l0x = " + l0x + ", l0y = " + l0y + ", l1x = " + l1x + ", l1y = " + l1y + ", l2x = " + l2x + ", l2y = " + l2y + ", l1o = " + l1o + ", l2o = " + l2o);
				if(iset != null && iset.hasNext())
					return true;
				iset = null;
				if(l2o != null){
					if(l2x >= CHILDREN){
						l2x = 0;
						l2y++;
					}
					if(l2y >= CHILDREN){
						l2o = null;
						continue hasNext;
					}
					if(l2o[l2y*CHILDREN + l2x] != null && acceptable(a0x*(CHILDREN*CHILDREN) + a1x*CHILDREN + l2x, a0y*(CHILDREN*CHILDREN) + a1y*CHILDREN + l2y)){
						iset = ((Set<T>)l2o[l2y*CHILDREN + l2x]).iterator();
						a2x = l2x;
						a2y = l2y;
					}
					l2x++;
					continue hasNext;
				}
				if(l1o != null){
					if(l1x >= CHILDREN){
						l1x = 0;
						l1y++;
					}
					if(l1y >= CHILDREN){
						l1o = null;
						continue hasNext;
					}
					if(l1o[l1y*CHILDREN + l1x] != null){
						l2o = l1o[l1y*CHILDREN + l1x];
						l2x = l2y = 0;
						a1x = l1x;
						a1y = l1y;
					}
					l1x++;
					continue hasNext;
				}
				/*
					for(; l2y < CHILDREN; l2x = 0, l2y++)
						for(; l2x < CHILDREN; l2x++){
							System.out.println("(l2o = " + l2o + ", l2y = " + l2y + ", l2x = " + l2x + ")");
							if(l2o[l2y*CHILDREN + l2x] != null){
								if(acceptable(l0x*(CHILDREN*CHILDREN) + l1x*CHILDREN + l2x, l0y*(CHILDREN*CHILDREN) + l1y*CHILDREN + l2y)){
									iset = ((Set<T>)l2o[l2y*CHILDREN + l2x]).iterator();
									if(++l2x >= CHILDREN)
										l2x = 0;
									continue hasNext;
								}
							}
						}
					for(; l1y < CHILDREN; l1x = 0, l1y++)
						for(; l1x < CHILDREN; l1x++){
							System.out.println("(l1o = " + l1o + ", l1y = " + l1y + ", l1x = " + l1x + ")");
							if(l1o[l1y*CHILDREN + l1x] != null){
								System.out.println("tack");
								l2o = l1o[l1y*CHILDREN + l1x];
								l2x = l2y = 0;
								l1x++;
								continue hasNext;
							}
						}
						*/
				if(all){
					System.out.println("if(all)");
					if(iroots.hasNext()){
						Map.Entry<Position, Object[][]> kv = iroots.next();
						System.out.println(kv);
						l0x = kv.getKey().x();
						l0y = kv.getKey().y();
						a0x = l0x;
						a0y = l0y;
						l1x = l1y = 0;
						l1o = kv.getValue();
						continue hasNext;
					}
				}else{
					do{
						if(l0x*(CHILDREN*CHILDREN) > maxx){
							l0y++;
							l0x = 0;
						}
						if(l0y*(CHILDREN*CHILDREN) > maxy)
							return false;
						l1o = roots.get(new Position(a0x = l0x, a0y = l0y));
						l0x++;
					}while(l1o == null);
					System.out.println("l1o = (" + a0x + ", " + a0y + ")");
					continue hasNext;
				}
				return false;
			}
		}

		public boolean acceptable(int x, int y){
//			if(all)
//				return true;
			return x >= minx && x <= maxx && y >= miny && y <= maxy;
		}

		public Map.Entry<Position, T> next(){
			if(!hasNext())
				throw new NoSuchElementException();
			return new AbstractMap.SimpleEntry<Position, T>(new Position(a0x*(CHILDREN*CHILDREN) + a1x*CHILDREN + a2x, a0y*(CHILDREN*CHILDREN) + a1y*CHILDREN + a2y), iset.next());
		}
	}

	public Iterator<Map.Entry<Position, T>> iterator(){
		return new DTIterator();
	}

	public Iterable<Map.Entry<Position, T>> portion(final Position min, final Position max){
//		if(5 == 5)
//			return this;
		return new Iterable<Map.Entry<Position, T>>(){
			public Iterator<Map.Entry<Position, T>> iterator(){
				return new DTIterator(min.x(), min.y(), max.x(), max.y(), false);
			}
		};
	}

	public Set<Map.Entry<Position, T>> set(){
		Set<Map.Entry<Position, T>> out = new HashSet<Map.Entry<Position, T>>();
		for(Map.Entry<Position, T> kv : this)
			out.add(kv);
		return out;
	}
}
