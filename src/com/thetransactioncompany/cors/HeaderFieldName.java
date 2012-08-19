package com.thetransactioncompany.cors;


/**
 * Represents an HTTP header field name. Provides an {@link #equals} method
 * to compare two header names using case-insensitive matching (RFC 2616, 
 * section 4.2).
 *
 * <p>Header field name examples:
 * 
 * <ul>
 *     <li>Content-Type
 *     <li>User-Agent
 *     <li>X-Requested-With
 * </ul>
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2010-09-26)
 */
public class HeaderFieldName {


	/**
	 * The header field name, in canonical form, for example
	 * {@code Aaa-Bbb-Ccc}.
	 */
	private String name;
	
	
	/**
	 * Applies a canonical format, for example {@code Aaa-Bbb-Ccc}.
	 *
	 * @param name The name to format, must not be an empty string.
	 *
	 * @return The formatted name.
	 *
	 * @throws IllegalArgumentException On a empty or invalid header field
	 *                                  name.
	 */
	protected static String formatCanonical(final String name) {
	
		String nameTrimmed = name.trim();
	
		if (nameTrimmed.isEmpty())
			throw new IllegalArgumentException("The header field name must not be an empty string");
		
		// Check for valid syntax: must begin with letter, then only word and dash chars allowed
		if (! nameTrimmed.matches("^[a-zA-Z][\\w-]*$"))
			throw new IllegalArgumentException("The header field name has invalid syntax");
		
	
		String[] tokens = nameTrimmed.toLowerCase().split("-");
		
		String out = "";
		
		for (int i=0; i < tokens.length; i++) {
		
			char[] c = tokens[i].toCharArray();
			
			// Capitalise first char
			c[0] = Character.toUpperCase(c[0]);
			
			if (i >= 1)
				out = out + "-";
			
			out = out + new String(c);
		}
		
		return out;
	}
	
	
	/**
	 * Creates a new header field name from the specified string. Does not
	 * perform validation if the input string is a valid name.
	 *
	 * @param name The header field name, empty strings are not allowed.
	 *
	 * @throws IllegalArgumentException On a empty or invalid header field
	 *                                  name.
	 */
	public HeaderFieldName (final String name) {
		
		this.name = formatCanonical(name);
	}
	
	
	/**
	 * Returns a string representation of a header field name in canonical
	 * format, e.g. {@code Aaa-Bbb-Ccc}.
	 *
	 * @return The header field name as string.
	 */
	public String toString() {
	
		return name;
	}
	
	
	/**
	 * Overrides {@code Object.hashCode}.
	 *
	 * @return The object hash code.
	 */
	public int hashCode() {
	
		return name.hashCode();
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
		
		return object instanceof HeaderFieldName && name.equals(object.toString());
	}

}
