<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@include file="signout.jsp" %>

<c:set var="context" value="${pageContext.request.contextPath}" />

<html>
	<body>
		<a href="${context}/allUsers">All Users</a><br>
		<h1>
			Welcome <font color=red>
						<sec:authentication property="principal.username" />
					</font>
			<sec:authentication property="principal.authorities"/>
		</h1>
		
		<h2>Authenticated page.This page comes after authentication</h2>
		
		<sec:authorize access="hasRole('ROLE_SUPER_ADMIN')">
			 <a href="${context}/registration">User SignUp</a>
		</sec:authorize>
		
		<sec:authorize method="GET" url="/registration">
			this content is visible to those user which have access to /registration GET.
		</sec:authorize>
		
	</body>
</html>
