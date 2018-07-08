<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page session="false"%>

<%@include file="signout.jsp" %>

<c:set var="context" value="${pageContext.request.contextPath}" />
--------------------------------------------------- <br>

Result=${message}

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
		<title>Create Account</title>
	</head>
<body>
	
    <H1>User Registration Form</H1>
    <form:form modelAttribute="user" method="POST"  action="${registrationUrl}" enctype="utf8" >
	    <tr>
	        <td><label>Username</label></td>
	        <td><form:input path="username" value="" /></td>
	        <form:errors path="username" element="div"/>
	    </tr>
	    <tr>
	        <td><label>Password</label></td>
	        <td><form:input path="password" value="" type="password" /></td>
	        <form:errors path="password" element="div" />
	    </tr>
	    <tr>
	        <td><label>Email</label></td>
	        <td><form:input path="email" value="" /></td>
	        <form:errors path="email" element="div" />
	    </tr>
	    <tr>
	        <td><label>Role</label></td>
	        <td><form:select path="role">
	        		<c:forEach items="${roles}" var="role">
	        			<form:option value="${role}">${role}</form:option> 
		        	</c:forEach>
	       		 </form:select>
	       </td>
	    </tr>
        <tr><td colspan="2"><button type="submit">Submit</button></td></tr>
    </form:form>
    <br>
    
    <a href="${context}/index">HomePage</a>
</body>
</html>