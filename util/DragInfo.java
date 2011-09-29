package util;

/**
 * An interface to specify how JComponents can contain drag information
 * 
 * @author melby
 *
 */
public interface DragInfo {
	/**
	 * A object that can be used for drag operations
	 * @return
	 */
	public Object dragObject();
	
	/**
	 * Set the object to be used for drag operations
	 * @param o
	 */
	public void setDragObject(Object o);
}
