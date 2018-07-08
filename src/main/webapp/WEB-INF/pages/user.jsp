<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@include file="signout.jsp" %>

<c:set var="context" value="${pageContext.request.contextPath}" />

<html>
	<body>
		<h1>
			Welcome <font color=red>
						<sec:authentication property="principal.username" />
					</font>
					<sec:authentication property="principal.authorities"/>
		</h1> 
		this is user page. <br>
		<sec:authorize access="hasRole('ROLE_ADMIN')">
			<a href="#">EditFood</a> This content will be seen for ROLE_ADMIN role only <br>
		</sec:authorize>
		
		<sec:authorize access="hasRole('ROLE_USER')">
			<a href="#">Foods</a> This content will be seen for ROLE_USER only <br>
		</sec:authorize>
		
		<sec:authorize access="hasAnyRole('ROLE_USER', 'ROLE_ADMIN')">
			This Line will visible to user which have ADMIN OR USER OR BOTH.  <br>
		</sec:authorize>
		<sec:authorize  access="hasRole('ROLE_USER') && hasRole('ROLE_ADMIN')">
			This Line will visible to user which have ADMIN AND USER BOTH Role.  
		</sec:authorize>
		
	</body>
</html>