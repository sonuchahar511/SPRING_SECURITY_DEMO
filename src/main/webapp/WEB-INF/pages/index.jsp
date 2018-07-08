<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@include file="signout.jsp" %>

<c:set var="context" value="${pageContext.request.contextPath}" />

<html>
	<body>
		
		<h2>Hello World</h2>
		
		<a href="${context}/admin">admin - ROLE_ADMIN</a> <br>
		<a href="${context}/user">user - ROLE_USER/ROLE_DEVELOPERS</a><br>
		<a href="${context}/registration">User Registration - ROLE_SUPER_ADMIN</a><br>
		<a href="${context}/allUsers">All User List - ROLE_ADMIN</a>
		
	</body>
</html>
