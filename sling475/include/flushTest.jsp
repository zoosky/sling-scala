<%@page session="false"%>
<%@taglib prefix="sling" uri="http://sling.apache.org/taglibs/sling/1.0"%>

<sling:defineObjects/>

<sling:include flush="1" addSelectors="selector"  replaceSelectors="replace" />

<%
	String flushTest = "";
	boolean b = response.isCommitted();

	if(b==true){
		flushTest = "flush successful becasue commit is true";
	}
	else{
		flushTest = "flush not successful becasue commit is false";
	
	}		

%>

<%= flushTest %>