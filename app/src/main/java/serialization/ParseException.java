package serialization;

import javax.xml.transform.TransformerFactoryConfigurationError;

/**
 * Exception for parsing errors
 * 
 * @author melby
 *
 */
public class ParseException extends Exception {
	private static final long serialVersionUID = 1L;
	public ParseException(Exception e){
		super(e);
	}
	public ParseException(TransformerFactoryConfigurationError e) {
		super(e);
	}
	public ParseException(String string, Object o) {
		super(string + " " + o);
	}

	public ParseException(){
		super();
	}
}
