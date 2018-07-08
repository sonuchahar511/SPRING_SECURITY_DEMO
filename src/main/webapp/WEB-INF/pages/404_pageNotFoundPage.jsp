<%@ page language="java" isErrorPage="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="context" value="${pageContext.request.contextPath}" />

<html>
	<header>
		<title>404 - Oops, Page Not Found </title>
	</header>
	<body>
		<h1>page=404_pageNotFoundPage.jsp</h1> <br>
		
		<a href="${context}/index">HOME</a> <br>
		
		<h1>404 Page Not Found.</h1>
	    <br />
	    <p><b>Error code:</b> ${pageContext.errorData.statusCode}</p>
	    <p><b>Request URI:</b> ${pageContext.request.scheme}://${header.host}${pageContext.errorData.requestURI}</p>
	    <br />
	    
	</body>	
</html>

