package com.thetransactioncompany.cors;


import java.util.*;

import junit.framework.*;


/**
 * Tests the origin class.
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2010-09-24)
 */
public class OriginTest extends TestCase {
        
        
        public void testConstructorUnknown() {
        
                Origin o = new Origin();
		
		assertTrue(o.equals(Origin.UNKNOWN));
        }
	
	
	public void testConstructor1() {
	
		String uri = "http://example.com";
		
		Origin o = null;
		
		try {
			o = new Origin(uri);
		
		} catch (OriginException e) {
			fail(e.getMessage());
		}
		
		assertEquals(uri, o.toString());
	}
	
	
	public void testConstructor2() {
	
		String uri = "HTTP://example.com";
		
		Origin o = null;
		
		try {
			o = new Origin(uri);
		
		} catch (OriginException e) {
			fail(e.getMessage());
		}
		
		assertEquals("http://example.com", o.toString());
	}
	
	
	public void testConstructor3() {
	
		String uri = "https://example.com";
		
		Origin o = null;
		
		try {
			o = new Origin(uri);
		
		} catch (OriginException e) {
			fail(e.getMessage());
		}
		
		assertEquals(uri, o.toString());
	}
	
	public void testConstructor4() {
	
		String uri = "file:///data/file.xml";
		
		Origin o = null;
		
		try {
			o = new Origin(uri);
		
		} catch (OriginException e) {
			fail(e.getMessage());
		}
		
		assertEquals(uri, o.toString());
	}
	
	
	public void testConstructor5() {
	
		String uri = "http://192.168.0.1:8080";
		
		Origin o = null;
		
		try {
			o = new Origin(uri);
		
		} catch (OriginException e) {
			fail(e.getMessage());
		}
		
		assertEquals(uri, o.toString());
	}
	
	
	public void testConstructor6() {
	
		String uri = "https://LOCALHOST:8080/my-app/upload.php";
		
		Origin o = null;
		
		try {
			o = new Origin(uri);
		
		} catch (OriginException e) {
			fail(e.getMessage());
		}
		
		assertEquals("https://localhost:8080", o.toString());
	}
	
	
	public void testConstructor7() {
	
		String uri = "ftp://ftp.example.com";
		
		Origin o = null;
		
		try {
			o = new Origin(uri);
			fail("Failed to raise bad protocol exception on FTP://");
			
		} catch (OriginException e) {
			// ok
		}
	}
	
	
	public void testEquality1() {
	
		String uri1 = "http://MY.service.com";
		String uri2 = "HTTP://my.service.com/my-app";
		
		Origin o1 = null;
		Origin o2 = null;
		
		try {
			o1 = new Origin(uri1);
			o2 = new Origin(uri2);
		
		} catch (OriginException e) {
		
			fail(e.getMessage());
		}
	
		assertTrue(o1.equals(o2));
	}
	
	
	public void testEquality2() {
	
		String uri1 = "http://MY.service.com";
		String uri2 = "HTTPS://my.service.com/my-app";
		
		Origin o1 = null;
		Origin o2 = null;
		
		try {
			o1 = new Origin(uri1);
			o2 = new Origin(uri2);
		
		} catch (OriginException e) {
		
			fail(e.getMessage());
		}
	
		assertFalse(o1.equals(o2));
	}
	
	
	public void testEqualityWithString() {
	
		String uri = "http://my.service.com";
		
		Origin o = null;
		
		try {
			o = new Origin(uri);
		
		} catch (OriginException e) {
		
			fail(e.getMessage());
		}
	
		assertTrue(o.equals(uri));
	}
}
