package com.thetransactioncompany.cors;


/**
 * CORS filter configuration exception, intended to report invalid init 
 * parameters at startup.
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2010-09-27)
 */
public class CORSConfigurationException extends Exception {


	/**
	 * Creates a new CORS filter configuration exception with the specified
	 * message.
	 * 
	 * @param message The exception message.
	 */
	public CORSConfigurationException(final String message) {
	
		super(message);
	}
}
