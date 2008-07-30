<%@page import="javax.jcr.Session"%>
<%@page import="javax.jcr.Node"%>
<%@page import="javax.jcr.NodeIterator"%>

<%@taglib prefix="sling" uri="http://sling.apache.org/taglibs/sling/1.0"%>
<sling:defineObjects/>

<html>
<head>
<title>Untitled Document</title>
</head>

<body>
<p>Sling Blog Sample</p>

<p>Create new post <a href="<%=currentNode.getPath()%>.edit.html" /> </p>

<%
// get all threads and include them
NodeIterator childrenIterator = currentNode.getNodes();
if(childrenIterator.hasNext()) {
%>
<p>Existing posts</p>

<%
while(childrenIterator.hasNext()) {
	Node n = childrenIterator.nextNode();
%><p><%=n.getProperty("title").getString()%> </p>
  <p><%=n.getProperty("body").getString()%> </p><%
}}
%>

</body>
</html> 
