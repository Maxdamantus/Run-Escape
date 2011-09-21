package serialization;

public interface Reader<T> {
	public T read(Tree in);
}