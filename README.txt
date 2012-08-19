Java Cross-Origin Resource Sharing (CORS) Filter

Copyright (c) Vladimir Dzhuvinov, 2010 - 2012


README

This package provides a Java servlet filter that implements the Cross-Origin 
Resource Sharing (CORS) mechanism for allowing cross-domain HTTP requests from 
web browsers. The CORS W3C working draft settled in 2009 and as of 2010 CORS is 
supported by all major browsers such as Firefox, Safari, Chrome and IE.

To enable CORS for a particular HTTP resource, such as a servlet, a JSP or a
static file, attach a CORSFilter to it via a <filter-mapping> element in the 
web.xml descriptor file. The default CORS filter policy is to allow any origin 
(including credentials). To impose a stricter access policy configure the filter 
using the supported <init-param> tags in the web.xml file. See the CORSFilter 
online documentation for configuration details.

This CORS filter version implements the W3C working draft from 2010-07-27:

	http://www.w3.org/TR/2010/WD-cors-20100727/


For installation instructions, usage and other information visit the CORS Filter
website:

	http://software.dzhuvinov.com/cors-filter.html



Content of this package:

	README.txt                This file.
	
	LICENSE.txt               The software license.
	
	cors-filter-<version>.jar JAR file containing the CORS filter and any 
	                          other required classes.

	cors-demo.war             CORS demo web application.
	
	build.xml                 Apache Ant build file.
	
	demo/                     CORS filter demo with requesting page.
	
	javadoc/                  JavaDoc files.
	
	lib/                      Build, test and run-time dependencies.
	
	src/                      The source code.
	
	test/                     JUnit tests.



Change log:

version 1.0 (2010-09-29)
	* First official release.

version 1.1 (2010-10-10)
	* Tags CORS requests for downstream notification using 
	  HttpServletRequest.addAttribute().

version 1.2 (2010-12-13)
	* Released under the Apache Open Source License 2.0.

version 1.2.1 (2011-07-29)
	* Updates Property Util JAR to 1.4.
	* Updates documentation to reflect the latest W3C CORS terminology.

version 1.3 (2011-12-02)
	* Fixes improper detection of actual HTTP OPTIONS CORS requests.
	* Updates Property Util JAR to 1.5.

version 1.3.1 (2011-12-02)
	* Removes improper filter chain for preflight HTTP OPTIONS CORS 
	  requests.

version 1.3.2 (2012-07-31)
	* Updates Property Util JAR to 1.6.
	* Adds Maven POM.
	* Updates Ant build.xml script.

[EOF]
