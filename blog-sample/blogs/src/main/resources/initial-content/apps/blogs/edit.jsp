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


<% java.util.Date d = new java.util.Date(); 

   String date = d.toString();
%>

  <p>Title : </p>
  <form name="form1" method="post" action="/content/blogs/*" enctype="multipart/form-data" >
  <input name="title" type="text" size="100">
  <p>Post:</p>
  <textarea name="body" cols="85" rows="20"></textarea>
	<input name="author" type="hidden" value="" />
	<input name="url" type="hidden" value="" />
	<input name="comment" type="hidden" value="" />
	<input name="numberOfPosts" type="hidden" value="0" />
  <input name="time" value="<%= d %>" type="hidden"/>
  <p>&nbsp; </p>
  <input name="Submit" type="submit" value="Publish">
  <input name=":redirect" value="/content/blogs.html" type="hidden"/>
  </form>

</div>

</body>
</html>
