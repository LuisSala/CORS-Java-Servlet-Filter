package com.thetransactioncompany.cors;


import java.net.URI;
import java.net.URISyntaxException;
import java.net.IDN;


/**
 * Represents a resource request origin, as defined in IETF's 
 * draft-abarth-origin-07. Supported protocols are {@code http}, {@code https}
 * and {@code file}.
 *
 * <p>Examples:
 *
 * <pre>
 *         http://www.example.com
 *         https://sso.example.com:8080
 *         http://192.168.0.1
 *         file:///data/file.html
 *         null
 * </pre>
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2010-09-24)
 */
public class Origin {


	/**
	 * The origin scheme, may be "http", "https" or "file".
	 */
	private String scheme = null;
	
	
	/**
	 * The origin host.
	 */
	private String host = null;
	
	
	/**
	 * The origin port, -1 assumes default.
	 */
	private int port = -1;
	
	
	/**
	 * The origin path, for the file:// protocol.
	 */
	private String path = null;
	
	
	/**
	 * Unknown/unspecified origin constant.
	 */
	public static Origin UNKNOWN = new Origin();
	
	
	/**
	 * Creates a new unknown/unspecified origin.
	 *
	 * @see #UNKNOWN
	 */
	public Origin() {
	
		// Leave all fields at their default null
	}

	
	/**
	 * Creates a new origin from the specified URI string.
	 *
	 * <p>Note: Applies IDNA algorigthm to host portion (RFC 3490).
	 *
	 * @param uriSpec The URI string representing the origin, {@code "null"}
	 *                or {@code null} if unknown/unspecified.
	 *
	 * @throws OriginException On bad URI syntax or unexpected scheme/
	 *                         protocol.
	 */
	public Origin(final String uriSpec)
		throws OriginException {
	
		if (uriSpec == null || uriSpec.equals("null")) {
			// Leave all fields at their default null
			return;
		}
	
		URI uri = null;
	
		try {
			uri = new URI(uriSpec);
			
		} catch (URISyntaxException e) {
		
			throw new OriginException("Bad origin URI: " + e.getMessage());
		}
		
		scheme = uri.getScheme();
		host = uri.getHost();
		port = uri.getPort();
		path = uri.getPath();
		
		if (scheme == null)
			throw new OriginException("Bad origin URI: Missing scheme, must be http, https or file");
			
		scheme = scheme.toLowerCase();
		
		if (! scheme.equals("http") && ! scheme.equals("https") && ! scheme.equals("file"))
			throw new OriginException("Bad origin URI: Scheme must be http, https or file");
		
		if (scheme.equals("http") || scheme.equals("https")) {
		
			if (host == null)
				throw new OriginException("Bad origin URI: Missing host name / IP address");
		
			// Apply the IDNA ToASCII algorithm [RFC3490] to /host/
			host = IDN.toASCII(host, IDN.ALLOW_UNASSIGNED | IDN.USE_STD3_ASCII_RULES);
	
			// Finally, convert to lower case	
			host = host.toLowerCase();
		}
	}
	
	
	/**
	 * Returns a string URI representation of the origin.
	 *
	 * @return The origin as an URI string.
	 */
	public String toString() {
	
		if (scheme == null)
			return "null";
		
		String s = scheme + "://";
		
		if (scheme.equals("http") || scheme.equals("https")) {
			
			s = s + host;
		
			if (port != -1)
				s = s + ":" + port;
		
		}
		else if (scheme.equals("file")) {
		
			if (path != null)
				s = s + path;
		}
		
		return s;
	}
	
	
	public String getSuffix() {
		String s = host;
		
		if (port != -1)
			s = s + ":" + port;
		
		return s;
	}
	
	public String getScheme() {

		return scheme;
	}
	
	/**
	 * Overrides {@code Object.hashCode}.
	 *
	 * @return The object hash code.
	 */
	public int hashCode() {
	
		return this.toString().hashCode();
	}
	
	
	/**
	 * Overrides {@code Object.equals()}.
	 *
	 * @param object The object to compare to.
	 *
	 * @return {@code true} if the objects have the same value, otherwise
	 *         {@code false}.
	 */
	public boolean equals(Object object) {
		
		if (object instanceof Origin)
			return this.toString().equals(object.toString());
			
		else if (object instanceof String)
			return this.toString().equals(object);
		
		else
			return false;
	}
}
