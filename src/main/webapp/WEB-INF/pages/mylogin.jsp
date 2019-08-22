<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="context" value="${pageContext.request.contextPath}" />
<c:set value="${context}/login" var="loginSubmitUrl" />
		
<html>
	<head>
		<title>
			Custom Login Form page
		</title>
	</head>
	<body>
		<a href="${context}/index">HOME page</a> <br>
		<font color=red>
				<%
				    if (request.getParameter("login_error") != null) {
				        out.println("Error: "+request.getParameter("login_error"));
				    }
				%>	
				<c:if test="${not empty error}">
					<div><h3>${error}</h3></div>
				</c:if>
				<c:if test="${not empty exceptionType}">
					<div><h3>${exceptionType}</h3></div>
				</c:if>
				<c:if test="${not empty exceptionMessage}">
					<div><h3>${exceptionMessage}</h3></div>
				</c:if>
				<c:if test="${not empty msg}">
					<div><h3>${msg}</h3></div>
				</c:if>
		</font> 
		<br>
		
		=============================================================<br>
		_csrf.parameterName=${_csrf.parameterName} <br>
		_csrf.token=${_csrf.token} <br>
		_csrf.headerName=${_csrf.headerName} <br>
		=============================================================<br>
		
		<br>this is custom login form <br>
		<form name="loginForm" action="${loginSubmitUrl}" method="POST">
			<table>
				<tr>
					<td>User:</td>
					<td><input type='text' name='myusername'></td>
				</tr>
				<tr>
					<td>Password:</td>
					<td><input type='password' name='mypassword' /></td>
				</tr>
				<tr>
					<td colspan="2"><input type='checkbox' name="remember-me"/>Remember Me? </td>
				</tr>
				<tr>
					 <td colspan="2">
					 	<!-- <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" /> -->
					 	 <input type="hidden"  name="${_csrf.parameterName}"   value="${_csrf.token}"/>
					 </td>

				</tr>
				<tr>
					<td colspan='2'><input name="submit" type="submit" value="submit" /></td>
				</tr>
			</table>
		</form>
		
	</body>
</html>