package com.thetransactioncompany.cors;


import java.util.*;

import com.thetransactioncompany.cors.CORSConfiguration;

import junit.framework.*;


/**
 * Tests the CORS configuration class.
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2010-09-27)
 */
public class CORSConfigurationTest extends TestCase {
	
	
	public void testParseWordsSpace() {
	
		String[] p1 = CORSConfiguration.parseWords("GET POST HEAD");
		
		assertEquals(3, p1.length);
	}
	
	
	public void testParseWordsComma() {
	
		String[] p1 = CORSConfiguration.parseWords("GET,POST,HEAD");
		
		assertEquals(3, p1.length);
	}
	
	
	public void testParseWordsMixed1() {
	
		String[] p1 = CORSConfiguration.parseWords("GET, POST, HEAD");
		
		assertEquals(3, p1.length);
	}
	
	
	public void testParseWordsMixed2() {
	
		String[] p1 = CORSConfiguration.parseWords("GET , POST , HEAD");
		
		assertEquals(3, p1.length);
	}
	
	
	public void testParseWordsEmpty() {
	
		String[] p1 = CORSConfiguration.parseWords("");
		
		assertEquals(0, p1.length);
	}
	
	
	public void testDefaultConfig() {
        
		Properties p = new Properties();
		
		CORSConfiguration c = null;
		
		try {
			c = new CORSConfiguration(p);
		
		} catch (CORSConfigurationException e) {
			fail(e.getMessage());
		}
		
		assertTrue(c.allowGenericHttpRequests);
		
		assertTrue(c.allowAnyOrigin);
		assertTrue(c.isAllowedOrigin("http://example.com"));
		
		assertTrue(c.isSupportedMethod(HTTPMethod.GET));
		assertTrue(c.isSupportedMethod(HTTPMethod.POST));
		assertTrue(c.isSupportedMethod(HTTPMethod.HEAD));
		assertTrue(c.isSupportedMethod(HTTPMethod.OPTIONS));
		assertFalse(c.isSupportedMethod(HTTPMethod.DELETE));
		assertFalse(c.isSupportedMethod(HTTPMethod.PUT));
		assertFalse(c.isSupportedMethod(HTTPMethod.TRACE));
		
		assertFalse(c.isSupportedHeader(new HeaderFieldName("X-Requested-By")));
		
		assertEquals(-1, c.maxAge);
        }
        
        
        public void testPublicConfig() {
        
		Properties p = new Properties();
		p.setProperty("cors.allowGenericHttpRequests", "true");
		p.setProperty("cors.allowOrigin", "*");
		p.setProperty("cors.supportedMethods", "GET, POST, OPTIONS");
		p.setProperty("cors.supportedHeaders", "");
		p.setProperty("cors.supportsCredentials", "false");
		
		CORSConfiguration c = null;
		
		try {
			c = new CORSConfiguration(p);
		
		} catch (CORSConfigurationException e) {
			fail(e.getMessage());
		}
		
		assertTrue(c.allowGenericHttpRequests);
		
		assertTrue(c.allowAnyOrigin);		
		assertTrue(c.isAllowedOrigin("http://example.com"));
		
		assertTrue(c.isSupportedMethod(HTTPMethod.GET));
		assertTrue(c.isSupportedMethod(HTTPMethod.POST));
		assertTrue(c.isSupportedMethod(HTTPMethod.OPTIONS));
		assertFalse(c.isSupportedMethod(HTTPMethod.DELETE));
		assertFalse(c.isSupportedMethod(HTTPMethod.PUT));
		assertFalse(c.isSupportedMethod(HTTPMethod.TRACE));
        }
	
	
	public void testRestrictedConfig() {
        
		Properties p = new Properties();
		p.setProperty("cors.allowGenericHttpRequests", "false");
		p.setProperty("cors.allowOrigin", "http://example.com:8080");
		p.setProperty("cors.supportedMethods", "GET, POST, OPTIONS");
		p.setProperty("cors.supportedHeaders", "");
		p.setProperty("cors.supportsCredentials", "false");
		
		CORSConfiguration c = null;
		
		try {
			c = new CORSConfiguration(p);
		
		} catch (CORSConfigurationException e) {
			fail(e.getMessage());
		}
		
		assertFalse(c.allowGenericHttpRequests);
		
		assertFalse(c.allowAnyOrigin);
		assertTrue(c.isAllowedOrigin("http://example.com:8080"));
		assertFalse(c.isAllowedOrigin("http://example.com:8008"));
		assertFalse(c.isAllowedOrigin("http://example.com"));
		assertFalse(c.isAllowedOrigin("http://deny-origin.com"));
        }

	public void testOriginSuffixMatching() {
        
		Properties p = new Properties();
		p.setProperty("cors.allowOriginSuffixMatching", "true");
		p.setProperty("cors.allowOrigin", "http://example.com:8080 https://foobar.com");
		
		CORSConfiguration c = null;
		
		try {
			c = new CORSConfiguration(p);
		
		} catch (CORSConfigurationException e) {
			fail(e.getMessage());
		}
		
		assertFalse(c.allowAnyOrigin);
		assertTrue(c.isAllowedOrigin("http://example.com:8080"));
		assertTrue(c.isAllowedOrigin("http://www.example.com:8080"));
		assertTrue(c.isAllowedOrigin("http://foo.www.example.com:8080"));
		assertTrue(c.isAllowedOrigin("https://www.foobar.com"));
		assertFalse(c.isAllowedOrigin("https://www.example.com:8080"));
		assertFalse(c.isAllowedOrigin("http://example.com:8008"));
		assertFalse(c.isAllowedOrigin("http://www.example.com:8008"));
		assertFalse(c.isAllowedOrigin("http://example.com"));
		assertFalse(c.isAllowedOrigin("http://deny-origin.com"));
		assertTrue(c.isAllowedOrigin("https://foobar.com"));
    }
	
	public void testCustomHeaders() {
	
		String h1 = "X-Requested-By";
		String h2 = "X-Web-Client";
		String h3 = "X-Not-Included";
	
		Properties p = new Properties();
		p.setProperty("cors.supportedHeaders", h1 + " " + h2);
		
		
		CORSConfiguration c = null;
		
		try {
			c = new CORSConfiguration(p);
		
		} catch (CORSConfigurationException e) {
			fail(e.getMessage());
		}
		
		assertTrue(c.isSupportedHeader(new HeaderFieldName(h1)));
		assertTrue(c.isSupportedHeader(new HeaderFieldName(h2)));
		assertFalse(c.isSupportedHeader(new HeaderFieldName(h3)));
	}
	
	
	public void testExposedHeaders() {
	
		String h1 = "X-Powered-By";
		String h2 = "X-Web-Service";
		String h3 = "X-Hidden";
	
		Properties p = new Properties();
		p.setProperty("cors.exposedHeaders", h1 + " " + h2);
		
		
		CORSConfiguration c = null;
		
		try {
			c = new CORSConfiguration(p);
		
		} catch (CORSConfigurationException e) {
			fail(e.getMessage());
		}
		
		assertTrue(c.exposedHeaders.contains(new HeaderFieldName(h1)));
		assertTrue(c.exposedHeaders.contains(new HeaderFieldName(h2)));
		assertFalse(c.exposedHeaders.contains(new HeaderFieldName(h3)));
	}
	
	
	public void testSupportsCredentialsTrue() {
	
		Properties p = new Properties();
		p.setProperty("cors.supportsCredentials", "true");
		
		
		CORSConfiguration c = null;
		
		try {
			c = new CORSConfiguration(p);
		
		} catch (CORSConfigurationException e) {
			fail(e.getMessage());
		}
		
		assertTrue(c.supportsCredentials);
	
	}
	
	
	public void testSupportsCredentialsFalse() {
	
		Properties p = new Properties();
		p.setProperty("cors.supportsCredentials", "false");
		
		
		CORSConfiguration c = null;
		
		try {
			c = new CORSConfiguration(p);
		
		} catch (CORSConfigurationException e) {
			fail(e.getMessage());
		}
		
		assertFalse(c.supportsCredentials);
	
	}
	
	
	public void testMaxAge() {
	
		Properties p = new Properties();
		p.setProperty("cors.maxAge", "100");
		
		
		CORSConfiguration c = null;
		
		try {
			c = new CORSConfiguration(p);
		
		} catch (CORSConfigurationException e) {
			fail(e.getMessage());
		}
		
		assertEquals(100, c.maxAge);
	
	}

}
