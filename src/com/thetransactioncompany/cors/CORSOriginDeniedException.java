package com.thetransactioncompany.cors;


/**
 * CORS origin denied (not allowed) exception.
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2010-09-27)
 */
public class CORSOriginDeniedException extends CORSException {

	
	/**
	 * The request origins (zero or more).
	 */
	private String[] requestOrigins = null;
	
	
	/**
	 * Creates a new CORS origin denied exception with the specified 
	 * message.
	 *
	 * @param message The message.
	 */
	public CORSOriginDeniedException(final String message) {
	
		this(message, null);
	}
	
	
	/**
	 * Creates a new CORS origin denied exception with the specified 
	 * message and request origins.
	 *
	 * @param message        The message.
	 * @param requestOrigins The request origins (zero or more), 
	 *                       {@code null} if unknown.
	 */
	public CORSOriginDeniedException(final String message, final String[] requestOrigins) {
	
		super(message);
		
		this.requestOrigins = requestOrigins;
	}
	
	
	/**
	 * Gets the request origins (zero or more).
	 *
	 * @return The request origins, {@code null} if unknown or not set.
	 */
	public String[] getRequestOrigins() {
	
		return requestOrigins;
	}
}
