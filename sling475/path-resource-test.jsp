<%@page session="false"%>
<%@page import="org.apache.sling.api.SlingHttpServletRequest"%>
<%@page import="org.apache.sling.api.resource.Resource"%>

<%@taglib prefix="sling" uri="http://sling.apache.org/taglibs/sling/1.0"%>

<sling:defineObjects/>


<%
	SlingHttpServletRequest req = (SlingHttpServletRequest) request;
	Resource res = req.getResourceResolver().getResource("./z");
%>

<sling:include resource= "<%= res %>" resourceType="/x/z"  />

<sling:include path="./y.html" resourceType="/x/y"  />
