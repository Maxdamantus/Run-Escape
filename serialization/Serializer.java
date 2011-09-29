package serialization;

/**
 * 
 * The implementation of this interface will deal with serializing objects to the Tree 
 * 
 * @author wafaahma
 *
 * @param <T>
 */
public interface Serializer<T> extends Reader<T>, Writer<T> {}