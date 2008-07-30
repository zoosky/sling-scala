<%@page import="javax.jcr.Session"%>
<%@page import="javax.jcr.Node"%>
<%@page import="java.text.SimpleDateFormat"%>

<%!

	public class BlogEngine{

	public static Node addNodeRecursively(String path, String type, Session session) throws Exception {
		Node currentRoot = session.getRootNode();
		String[] pathSegments = path.split("/");
		for(int i = 1; i < pathSegments.length; i++) {			
			if(!currentRoot.hasNode(pathSegments[i])) {
				currentRoot.addNode(pathSegments[i], "nt:unstructured");
			}
			currentRoot = currentRoot.getNode(pathSegments[i]);
		}
		return currentRoot;
	}
	
%>
