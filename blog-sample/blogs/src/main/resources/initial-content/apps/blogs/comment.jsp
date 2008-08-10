
<html>
<head>
<title>Untitled Document</title>
<link rel="stylesheet" type="text/css" href="/apps/blogs/styles.css" />

</head>

<body>


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


<form action="/content/blogs" method="commentPost" id="commentform">


<p><input type="text" name="author" id="author" value="" size="24" tabindex="1" />
<label for="author"><strong>Name</strong></label></p>


<p><input type="text" name="url" id="url" value="" size="24" tabindex="3" />
<label for="url"><strong>Website</strong></label></p>

<p><textarea name="comment" id="comment" cols="80%" rows="8" tabindex="4"></textarea></p>

<p><input name="submit" type="submit" id="submit" tabindex="5" value="Submit Comment" />
<input type="hidden" name="comment_post_ID" value="81" />
</p>

</form>

</div>

</body>
</html>