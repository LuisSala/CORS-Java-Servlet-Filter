package com.thetransactioncompany.cors;


/**
 * Origin exception. Typically thrown by the {@link Origin} class.
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2010-09-27)
 */
public class OriginException extends Exception {


	/**
	 * Creates a new origin exception with the specified message.
	 *
	 * @param message The message.
	 */
	public OriginException(final String message) {
	
		super(message);
	}
}
