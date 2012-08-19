package com.thetransactioncompany.cors;


/**
 * Base Cross-Origin Resource Sharing (CORS) exception, typically thrown during
 * processing of CORS requests.
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2010-09-27)
 */
public class CORSException extends Exception {


	/**
	 * Creates a new CORS exception with the specified message.
	 *
	 * @param message The message.
	 */
	public CORSException (final String message) {
	
		super(message);
	}
}
