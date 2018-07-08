<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%@include file="signout.jsp" %>

<c:set var="context" value="${pageContext.request.contextPath}" />

<h1>List of All Users</h1>
<a href="${context}/index">HOME</a> <br>

<table border="1">
		<tr>
			<td>NAME</td>
			<td>EMAIL</td>
			<td>ENABLED</td>
			<td>ROLES</td>
		</tr>
	<c:forEach items="${users}" var="user">
		<tr>
			<td> <a href="${context}/allUsers1/${user.username}">${user.username}</a> </td>
			<td>${user.email}</td>
			<td>${user.enabled}</td>
			<td>
				<table>
					<c:forEach items="${user.roles}" var="role">
						<tr>
							<td>${role.name}</td>
						</tr>
					</c:forEach>
				</table>
			</td>
			<td><a href="${context}/allUsers1/delete/${user.username}">Delete</a></td>
		</tr>
	</c:forEach>

</table>