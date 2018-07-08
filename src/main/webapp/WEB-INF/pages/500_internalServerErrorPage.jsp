<%@ page language="java" isErrorPage="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="context" value="${pageContext.request.contextPath}" />

<c:set var="context" value="${pageContext.request.contextPath}" />

<html>
	<header>
		<title>500- IT's NOT YOU , ITS US </title>
	</header>
	<body>
		<h1>page=500_internalServerErrorPage.jsp</h1> <br>
		
		<a href="${context}/index">HOME</a> <br>
		
		<h1>Internal Server error: Something went wrong. IT'S NOT YOU , IT'S US </h1>
	    <br />
	    <p><b>Exception is: <%= exception %> </p>
	    <p><b>Error code:</b> ${pageContext.errorData.statusCode}</p>
	    <p><b>Request URI:</b> ${pageContext.request.scheme}://${header.host}${pageContext.errorData.requestURI}</p>
	    <br />
	    
	</body>	
</html>

