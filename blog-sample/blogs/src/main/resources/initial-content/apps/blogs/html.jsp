<%@page import="javax.jcr.Session"%>
<%@page import="javax.jcr.Node"%>
<%@page import="javax.jcr.NodeIterator"%>

<%@taglib prefix="sling" uri="http://sling.apache.org/taglibs/sling/1.0"%>
<sling:defineObjects/>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Untitled Document</title>
<link rel="stylesheet" type="text/css" href="/apps/blogs/styles.css" />
</head>

<body>
<div class="container">
<div class="header">
	<div class="sitename">Sling Blog</div>
</div>

<div class="main-nav">
  <ul>
    <li><a href="/content/blogs.html" class="link1"></a></li>
    <li><a href="/content/blogs.html" class="link2">View Blog</a></li>
    <li><a href="/content/blogs.edit.html" class="link3">New Post</a></li>
    <li><a href="/content/blogs.edit.html" class="link4"></a></li>
  </ul>
</div>

<div class="navbar"> &nbsp; </div>

 <div class="contentside">
    <div class="contentitemside">
      <div class="sideh1">Archive</div>
      <div class="sidelinks"><a href="http://www.free-css.com/" class="sidelinks">December 2006</a> </div>
    </div>
 </div>


<div class="content">

<%
// get all threads and include them
NodeIterator childrenIterator = currentNode.getNodes();
if(childrenIterator.hasNext()) {
%>


<%
while(childrenIterator.hasNext()) {
	Node n = childrenIterator.nextNode();
%>

    <h1><%=n.getProperty("title").getString()%></h1>
    <div class="contentdate"><%=n.getProperty("time").getString()%></div>
    <div class="contentitem">
      <p><%=n.getProperty("body").getString()%> </p>
    </div>
<%
}}
%>
</div>

</div>

</body>
</html> 

