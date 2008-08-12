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
String monthToRetrive = "" ;
String yearToRetrive = "";

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


<script language="JavaScript">

	function printContent(month , year){
		document.archive.month.value = month;
		document.archive.year.value = year;
		document.archive.submit();

	}

	function printComment(path){
		alert(path);
		document.commentForm.path.value = path;
		document.commentForm.submit();

	}

</script>


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
						<li><a href="#" onclick="printContent('<%=months%>' , '<%=years%>')" class="sidelinks"><%=years%>  <%=months%></a> </li>

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
							<p> Comments for this post </p>
							<hr>
<%

							ArrayList l = p.getCommentList();
							for(int q=0 ; q< l.size() ; q++){
								Comment com = (Comment)l.get(q);
%>
							<p>Name    : <%=com.name%></p>
							<p>URL     : <%=com.weblink%></p>
							<p>Comment : </p>
   
							<p><%=com.comment%></p>
							<hr>
							<%}%>

							

						<% 
							String fullPath = p.resourcePath;
							String[] splitPath = fullPath.split("/content/");
						%>	

    							<li><a href="#" onclick="printComment('<%=splitPath[1]%>')" class="link1">Add comment</a></li> 
<%
						}
					}
			}
		
%>	


</div>


 

<form name="archive" method="post" action="blogs.html" type="hidden" >
	<input name="month" type="hidden" value="" />
	<input name="year" type="hidden" value="" />
  	<input name=":redirect" value="/content/blogs.archive.html" type="hidden"/>
</form>


<form name="commentForm" method="post" action="blogs" type="hidden" >
	<input name="path" type="hidden" value="" />
  	<input name=":redirect" value="/content/blogs.comment.html" type="hidden"/>
</form>

</div>

</body>
</html> 

