<%@page session="false"%>
<%@page import="org.apache.sling.api.SlingHttpServletRequest"%>
<%@page import="org.apache.sling.api.resource.Resource"%>
<%@taglib prefix="sling" uri="http://sling.apache.org/taglibs/sling/1.0"%>

<sling:defineObjects/>

<%
	SlingHttpServletRequest req = (SlingHttpServletRequest) request;
	String suffixTest = "" ;
	suffixTest = req.getRequestPathInfo().getSuffix() ;
%>

The suffix in suffixtest.jsp is <%= suffixTest %>
