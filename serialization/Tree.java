package serialization;

public interface Tree {
	public static class Entry {
		private final String name;
		private final Tree tree;

		public Entry(String n, Tree t){
			name = n; tree = t;
		}

		public String name(){
			return name;
		}

		public Tree tree(){
			return tree;
		}
	}
	
	public boolean isLeaf();

	/**
	 * Throws an exception if this a a leaf
	 * @return
	 */
	public String value();

	/**
	 * Throws an exception if this is a leaf
	 * @return
	 */
	public Iterable<Entry> children();

	public void add(Entry e);

	public Entry get(int i);
}