package serialization;

/**
 *
 * An interface which then get called by Serializers class.
 * This way you are not bounded to implement both Reader and Writer when writing a serializer
 * 
 * @author wafaahma
 * 
 * @param <T>
 */
public interface Writer<T> {
	public Tree write(T in);
}