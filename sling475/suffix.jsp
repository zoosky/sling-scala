<%@page session="false"%>
<%@page import="org.apache.sling.api.SlingHttpServletRequest"%>
<%@page import="org.apache.sling.api.resource.Resource"%>
<%@taglib prefix="sling" uri="http://sling.apache.org/taglibs/sling/1.0"%>

<sling:defineObjects/>

<sling:include replaceSuffix="a.b" />

<%
	SlingHttpServletRequest req = (SlingHttpServletRequest) request;
	String suffixTest = "" ;
	suffixTest = req.getRequestPathInfo().getSuffix() ;

	if(suffixTest==null){
		suffixTest = "Suffix Not Successful";
	}
	else{
		suffixTest = "Suffix Successful";
	
	}		

%>



<%= suffixTest %>