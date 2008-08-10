<%@page import="javax.jcr.Session"%>
<%@page import="javax.jcr.Node"%>
<%@page import="javax.jcr.NodeIterator"%>
<%@page import="java.util.*"%>

<%@taglib prefix="sling" uri="http://sling.apache.org/taglibs/sling/1.0"%>
<sling:defineObjects/>

<%@ include file="/apps/blogs/engine.jsp" %>

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


<%
// get all threads and include them
NodeIterator childrenIterator = currentNode.getNodes();

PostTreeControler pc = new PostTreeControler();
TreeNode posts = pc.createPostTree(childrenIterator);
ArrayList monthList = posts.getChildList();
String monthToRetrive ;
String yearToRetrive ;
ArrayList l;
%>


			

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

<%			for(int i=0 ; i<monthList.size() ; i++){
				TreeNode y = (TreeNode)monthList.get(i);
				ArrayList yearList = y.getChildList();
					for(int j=0 ; j<yearList.size() ; j++){
						TreeNode pr = (TreeNode)yearList.get(j);
						String years = pr.getName();
						String months = y.getName();
%>
						<a href="/content/blogs.html" onclick="" class="sidelinks"><%=years%>  <%=months%></a> 

<%
					}
			}
%>
	</div>
</div>



<div class="content">

<%			for(int i=0 ; i<monthList.size() ; i++){
				TreeNode y = (TreeNode)monthList.get(i);
				ArrayList yearList = y.getChildList();
					for(int j=0 ; j<yearList.size() ; j++){
						TreeNode pr = (TreeNode)yearList.get(j);
						ArrayList pp = pr.getPostList();
						for(int k=0 ; k<pp.size() ; k++){	
							Post p = (Post)pp.get(k);					
%>
							
							<h1><%=p.title%></h1>
    							<div class="contentdate"><%=p.time%></div>
    							<div class="contentitem">
      								<p><%=p.body%> </p>
							</div>
    							<li><a href="/content/blogs.comment.html" class="link1">Add comment</a></li> 
<%
						}
					}
			}
		
%>	


</div>


</div>

</body>
</html> 

