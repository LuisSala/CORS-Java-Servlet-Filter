package com.thetransactioncompany.cors;


import junit.framework.*;


/**
 * Tests the header field name class.
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2010-09-26)
 */
public class HeaderFieldNameTest extends TestCase {
        
        
        public void testFormatCanonical1() {
		
		assertEquals(HeaderFieldName.formatCanonical("content-type"), "Content-Type");
        }
	
	
	public void testFormatCanonical2() {
		
		assertEquals(HeaderFieldName.formatCanonical("CONTENT-TYPE"), "Content-Type");
        }
	
	
	public void testFormatCanonical3() {
		
		assertEquals(HeaderFieldName.formatCanonical("X-type"), "X-Type");
        }
	
	
	public void testFormatCanonical4() {
		
		assertEquals(HeaderFieldName.formatCanonical("Origin"), "Origin");
        }
	
	
	public void testFormatCanonical5() {
		
		assertEquals(HeaderFieldName.formatCanonical("A"), "A");
        }
	
	
	public void testFormatCanonical6() {
		
		try {
			assertEquals(HeaderFieldName.formatCanonical(""), "");
			fail("Failed to raise IllegalArgumentException on empty string");
			
		} catch (IllegalArgumentException e) {
			// ok
		}
        }
	
	
	public void testConstructor1() {
	
		HeaderFieldName n = new HeaderFieldName("content-type");
		
		assertEquals(n.toString(), "Content-Type");
	}
	
	
	public void testConstructor2() {
	
		HeaderFieldName n = new HeaderFieldName("X-ABC");
		
		assertEquals(n.toString(), "X-Abc");
	}
	
	
	public void testEquality1() {
	
		HeaderFieldName n1 = new HeaderFieldName("content-type");
		HeaderFieldName n2 = new HeaderFieldName("CONTENT-TYPE");
		
		assertTrue(n1.equals(n2));
	}
	
	
	public void testEquality2() {
	
		HeaderFieldName n1 = new HeaderFieldName("content-type");
		HeaderFieldName n2 = new HeaderFieldName("CONTENT");
		
		assertFalse(n1.equals(n2));
	}
	
	
	public void testInvalid1() {
	
		try {
			HeaderFieldName h = new HeaderFieldName("X-r%b");
			
			fail("Failed to raise exeption on bad header name");
			
		} catch (IllegalArgumentException e) {
			// ok
		}
		
	}
	
	
	public void testInvalid2() {
	
		try {
			HeaderFieldName h = new HeaderFieldName("1-X-r");
			
			fail("Failed to raise exeption on bad header name");
			
		} catch (IllegalArgumentException e) {
			// ok
		}
		
	}
	
	
	public void testInvalid3() {
	
		try {
			HeaderFieldName h = new HeaderFieldName("Aaa Bbb");
			
			fail("Failed to raise exeption on bad header name");
			
		} catch (IllegalArgumentException e) {
			// ok
		}
		
	}
	
}
