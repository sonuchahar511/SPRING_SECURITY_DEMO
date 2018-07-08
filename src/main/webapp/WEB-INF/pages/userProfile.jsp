<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%@include file="signout.jsp" %>

<c:set var="context" value="${pageContext.request.contextPath}" />

<h1>User Profile Section</h1>
<a href="${context}/index">HOME</a> <br>

<table border="1">
		<tr>
			<td>NAME</td>
			<td>EMAIL</td>
			<td>ENABLED</td>
			<td>ROLES</td>
		</tr>
		<tr>
			<td>${user.username}</td>
			<td>${user.email}</td>
			<td>${user.enabled}</td>
			<td>
				<table>
					<c:forEach items="${user.roles}" var="role">
						<tr>
							<td> <li>${role.name}</li> </td>
						</tr>
					</c:forEach>
				</table>
			</td>
		</tr>

</table>