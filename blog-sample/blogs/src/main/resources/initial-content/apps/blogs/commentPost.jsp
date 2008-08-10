<%@page import="javax.jcr.Session"%>
<%@page import="javax.jcr.Node"%>
<%@page import="javax.jcr.version.Version"%>
<%@page import="java.io.ByteArrayInputStream"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="org.apache.sling.api.resource.ResourceUtil"%>

<%@taglib prefix="sling" uri="http://sling.apache.org/taglibs/sling/1.0"%>
<sling:defineObjects/>

<%@ include file="/apps/blogs/engine.jsp" %>



<%
// get the session
Session mySession = slingRequest.getResourceResolver().adaptTo(Session.class);
Node node = mySession.getRootNode().getNode(resource.getPath().substring(1));

// add properties
if(request.getParameter("author") != null) {
	node.setProperty("author", request.getParameter("author"));
}	

if(request.getParameter("url") != null) {
	node.setProperty("url", request.getParameter("url"));
}	

if(request.getParameter("comment") != null) {
	node.setProperty("comment", request.getParameter("comment"));
}

mySession.save();

//response.sendRedirect(request.getParameter(":redirect"));
%>