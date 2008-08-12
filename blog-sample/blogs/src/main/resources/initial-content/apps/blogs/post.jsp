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
Node node;


// is this a new node?
if(ResourceUtil.isStarResource(resource)) {
	// path: remove star and add a name

	String pathForNewNode = resource.getPath() + d.getTime();
	BlogEngine engine = new BlogEngine()
	node = engine.addNodeRecursively(pathForNewNode, "nt:unstructured", mySession);
	
	// take care of attachment
	if(request.getParameter("title") != null) {
		Node titleNode = node.addNode("title", "nt:data");
		Node contentNode = titleNode.addNode("jcr:content", "nt:resource");
		// set the mandatory properties
		contentNode.setProperty("jcr:data", request.getParameter("title").toString());
		//string calander = Calendar.getInstance().toString();
		//contentNode.setProperty("jcr:lastModified",Calendar.getInstance() );	
		// zip file is hardcoded for now
		contentNode.setProperty("jcr:mimeType", "text");

	}	
} else {
	node = mySession.getRootNode().getNode(resource.getPath().substring(1));
}

// add properties
if(request.getParameter("body") != null) {
	node.setProperty("body", request.getParameter("body"));
}	

java.util.Date d = new java.util.Date();
node.setProperty("time" , request.getParameter("time"));


}

	Node n = mySession.getRootNode();

if(request.getParameter("month") != null){


	nn.setProperty("month" , request.getParameter("month").toString());
}

if(request.getParameter("year") != null){

	nn.setProperty("year" , request.getParameter("year").toString());
}

mySession.save();

response.sendRedirect(request.getParameter(":redirect"));

%>

