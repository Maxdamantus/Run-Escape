package shim;

public class locks {
	// all locks are mostly used to synchronise low-level collection operations, which shouldn't perform IO. hopefully TeaVM's threads only yield during IO.
	public static class FakeLock {
		public FakeLock readLock() {
			return this;
		}

		public FakeLock writeLock() {
			return this;
		}

		public void lock() {}

		public void unlock() {}
	}

	public static class ReadWriteLock extends FakeLock {}

	public static class ReentrantReadWriteLock extends ReadWriteLock {}
}
