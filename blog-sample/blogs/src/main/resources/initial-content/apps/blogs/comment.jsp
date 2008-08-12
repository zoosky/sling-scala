<%@page import="javax.jcr.Session"%>
<%@page import="javax.jcr.Node"%>
<%@page import="javax.jcr.NodeIterator"%>
<%@page import="java.util.*"%>

<%@taglib prefix="sling" uri="http://sling.apache.org/taglibs/sling/1.0"%>
<sling:defineObjects/>

<%@ include file="/apps/blogs/engine.jsp" %>

<html>
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
    <li><a href="/content/blogs.edit.html" class="link1"></a></li>
    <li><a href="/content/blogs.html" class="link2">View Blog</a></li>
    <li><a href="/content/blogs.edit.html" class="link3"></a></li>
    <li><a href="/content/blogs.edit.html" class="link4"></a></li>
  </ul>
</div>


<p>Please comment on this post</p>

<% 
Session mySession = slingRequest.getResourceResolver().adaptTo(Session.class);
String s = currentNode.getProperty("path").getString();
String [] ss = s.split("/");
Node node = mySession.getRootNode().getNode("content/blogs/" + ss[1]);

String th = node.getProperty("numberOfPosts").getString();


int j = Integer.parseInt(th);
j = j + 1;

String author = "author_" + j;
String url = "url_" + j;
String comment = "comment_" + j;
String numb = "" + j;
%>



<form  method="post" action="<%=currentNode.getProperty("path").getString()%>" >


<p><label for="author"><strong>Name</strong></label>
<input type="text" name="<%=author%>" id="author" value="" size="24" tabindex="1" /></p>


<p><label for="url"><strong>URL </strong></label>
<input type="text" name="<%=url%>" id="url" value="" size="24" tabindex="3" /></p>


<p><textarea name="<%=comment%>" id="comment" cols="80%" rows="8" tabindex="4"></textarea></p>

<p><input name="submit" type="submit" id="submit" tabindex="5" value="Submit Comment" /></p>
<input name="numberOfPosts" type="hidden" value="<%= numb %>" />
<input name=":redirect" value="/content/blogs.html" type="hidden"/>


</form>

</div>

</body>
</html>