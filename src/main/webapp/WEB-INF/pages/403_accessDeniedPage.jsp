<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<c:set var="context" value="${pageContext.request.contextPath}" />
<c:set value="${context}/mylogout" var="customLoginFormLogoutUrl" />

<html>
	<header>
		<title>403- Access denied page</title>
	</header>
	
	<body>
		<a href="${context}/index">HOME</a> <br>
		
		<sec:authorize access="isAuthenticated()">
			You are now loggedIN.<br/>
		</sec:authorize>
		<h4>
			Welcome <font color=red>
						<sec:authentication property="principal.username" />
					</font>
			<sec:authentication property="principal.authorities"/>
		</h4>
		<h1> 
			<b>Access Denied Page</b>  <a href="${customLoginFormLogoutUrl}"> Logout</a>
		</h1>
	</body>
</html>