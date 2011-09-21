package serialization;

public interface Writer<T> {
	public Tree write(T in);
}