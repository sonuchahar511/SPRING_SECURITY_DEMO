<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="context" value="${pageContext.request.contextPath}" />


<html>
	<header>
		<title>401 - Unauthorized page</title>
	</header>
	<body>
		<a href="${context}/index">HOME</a> <br>
		<b>401_unauthorizedPage </b> <br>
		AuthorizationType=${authorizationType} </br>
		Result=${result} </br>
	</body>
</html>


