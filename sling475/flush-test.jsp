<%@page session="false"%>
<%@taglib prefix="sling" uri="http://sling.apache.org/taglibs/sling/1.0"%>

<sling:defineObjects/>

<sling:include flush="1" addSelectors="selector"  replaceSelectors="replace" />

<%
	String flushTest = "";
	boolean b = response.isCommitted();

	if(b==true){
		flushTest = "Flush Successful becasue commit is true";
	}
	else{
		flushTest = "Flush Not Successful becasue commit is false";
	
	}		

%>

<%= flushTest %>