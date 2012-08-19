package com.thetransactioncompany.cors;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Cross-Origin Resource Sharing (CORS) servlet filter.
 *
 * <p>The filter intercepts incoming HTTP requests and applies the CORS
 * policy as specified by the filter init parameters. The actual CORS
 * request is processed by the {@link CORSRequestHandler} class.
 *
 * <p>Supported filter init parameters:
 *
 * <ul>
 *     <li>cors.allowGenericHttpRequests {true|false} defaults to {@code true}.
 *     <li>cors.allowOrigin {"*"|origin-list} defaults to {@code *}.
 *     <li>cors.supportedMethods {method-list} defaults to {@code "GET, POST, HEAD, OPTIONS"}.
 *     <li>cors.supportedHeaders {header-list} defaults to empty list.
 *     <li>cors.exposedHeaders {header-list} defaults to empty list.
 *     <li>cors.supportsCredentials {true|false} defaults to {@code true}.
 *     <li>cors.maxAge {int} defaults to {@code -1} (unspecified).
 * </ul>
 *
 * @author Vladimir Dzhuvinov
 * @version $version$ (2011-12-02)
 */
public class CORSFilter
	implements Filter {


	/**
	 * The CORS filer configuration.
	 */
	private CORSConfiguration config;
	
	
	/**
	 * Encapsulates the CORS request handling logic.
	 */
	private CORSRequestHandler handler;
	
	
	/**
	 * Converts the initial filter parameters (typically specified in the 
	 * {@code web.xml} file) to a Java properties hashtable. The parameter
	 * names become property keys.
	 *
	 * @param config The filter configuration.
	 *
	 * @return The context parameters as Java properties.
	 */
	private static Properties getFilterInitParameters(final FilterConfig config) {
	
		Properties props = new Properties();
	
		Enumeration en = config.getInitParameterNames();
		
		while (en.hasMoreElements()) {
			
			String key = (String)en.nextElement();
			String value = config.getInitParameter(key);
			
			props.setProperty(key, value);
		}
	
		return props;
	}
	
	
	/**
	 * Serialises the items of an array into a string. Each item must have a 
	 * meaningful {@code toString()} method.
	 * 
	 * @param array The array to serialise, may be {@code null}.
	 * @param sep   The string separator to apply.
	 *
	 * @return The serialised array as string.
	 */
	private static String serialize(final Object[] array, final String sep) {
	
		if (array == null)
			return "";
	
		String s = "";
		
		for (int i=0; i<array.length; i++) {
			s = s + array[i].toString();
			if (i+1 < array.length)
				s = s + sep;
		}
		
		return s;
	}
	
	
	/**
	 * This method is invoked by the web container to initialise the
	 * filter at startup.
	 *
	 * @param filterConfig The filter configuration.
	 *
	 * @throws ServletException On a filter initialisation exception.
	 */
	public void init(final FilterConfig filterConfig)
		throws ServletException {
		
		// Get the init params
		Properties props = getFilterInitParameters(filterConfig);
		
		// Extract and parse all required CORS filter properties
		try {
			config = new CORSConfiguration(props);
			
		} catch (CORSConfigurationException e) {
		
			throw new ServletException(e);
		}
		
		handler = new CORSRequestHandler(config);
	}
	
	
	/**
	 * Produces a simple HTTP text/plain response with the specified status
	 * code and message.
	 *
	 * <p>Note: The CORS filter avoids falling back to the default web
	 * container error page (typically a richly-formatted HTML page) to make
	 * it easier for XHR debugger tools to identify the cause of failed 
	 * requests.
	 *
	 * @param sc      The HTTP status code.
	 * @param message The message.
	 *
	 * @throws IOException      On a I/O exception.
	 * @throws ServletException On a general request processing exception.
	 */
	private void printMessage(final HttpServletResponse response, final int sc, final String msg)
		throws IOException, ServletException {
	
		// Set the status code
		response.setStatus(sc);
		
		
		// Write the error message
		
		response.resetBuffer();
		
		response.setContentType("text/plain");
		
		PrintWriter out = response.getWriter();
		
		out.println("Cross-Origin Resource Sharing (CORS) Filter: " + msg);
	}
	
	
	/**
	 * Filters an HTTP request/reponse pair according to the configured CORS
	 * policy. Also tags the request with CORS information to downstream
	 * handlers.
	 * 
	 * @param request  The servlet request.
	 * @param response The servlet response.
	 * @param chain    The filter chain.
	 *
	 * @throws IOException      On a I/O exception.
	 * @throws ServletException On a general request processing exception.
	 */
	private void doFilter(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
		throws IOException, ServletException {
	
		// Tag
		handler.tagRequest(request);
		
		CORSRequestType type = CORSRequestType.detect(request);
	
		try {
			if (type == CORSRequestType.ACTUAL) {
				// Simple/actual CORS request
				handler.handleActualRequest(request, response);
				chain.doFilter(request, response);
			}
			else if (type == CORSRequestType.PREFLIGHT) {
				// Preflight CORS request, handle but don't pass
				// further down the chain
				handler.handlePreflightRequest(request, response);
			}
			else if (config.allowGenericHttpRequests) {
				// Not a CORS request, but allow it through
				request.setAttribute("cors.isCorsRequest", false); // tag
				chain.doFilter(request, response);
			}
			else {
				// Generic HTTP requests denied
				request.setAttribute("cors.isCorsRequest", false); // tag
				printMessage(response, HttpServletResponse.SC_FORBIDDEN, "Generic HTTP requests not allowed");
			}
				
		} catch (InvalidCORSRequestException e) {
			request.setAttribute("cors.isCorsRequest", false); // tag
			printMessage(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		
		} catch (CORSOriginDeniedException e) {
			
			String msg = e.getMessage() + ": " + serialize(e.getRequestOrigins(), " ");
			printMessage(response, HttpServletResponse.SC_FORBIDDEN, msg);
			
		} catch (UnsupportedHTTPMethodException e) {
		
			String msg = e.getMessage();
			
			HTTPMethod method = e.getRequestedMethod();
			
			if (method != null)
				msg = msg + ": " + method.toString();
		
			printMessage(response, HttpServletResponse.SC_METHOD_NOT_ALLOWED, msg);
			
		} catch (UnsupportedHTTPHeaderException e) {
		
			String msg = e.getMessage();
			
			HeaderFieldName header = e.getRequestHeader();
			
			if (header != null)
				msg = msg + ": " + header.toString();
		
			printMessage(response, HttpServletResponse.SC_FORBIDDEN, msg);
		}
	}
	
	
	
	/**
	 * Called by the servlet container each time a request/response pair is 
	 * passed through the chain due to a client request for a resource at 
	 * the end of the chain.
	 * 
	 * @param request  The servlet request.
	 * @param response The servlet response.
	 * @param chain    The filter chain.
	 *
	 * @throws IOException      On a I/O exception.
	 * @throws ServletException On a general request processing exception.
	 */
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
		throws IOException, ServletException {
		
		if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
		
			// Cast to HTTP
			doFilter((HttpServletRequest)request, (HttpServletResponse)response, chain);
		}
		else {
			throw new ServletException("Cannot filter non-HTTP requests/responses");	
		}
	}
	
	
	/**
	 * Called by the web container to indicate to a filter that it is being 
	 * taken out of service.
	 */
	public void destroy() {
	
		// do nothing
	}
}
