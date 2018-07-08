<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<c:set var="context" value="${pageContext.request.contextPath}" />
context=${context}

<c:set value="${context}/index" var="homePageUrl" />
<c:set value="${context}/logout" var="logoutFilterUrl" />
<c:set value="${context}/mylogout" var="customLoginFormLogoutUrl" />
<c:set value="${context}/mylogout1" var="authenticationLogoutUrl" />

<html>
	<body>
		<sec:authorize access="isRememberMe()">
			<h2># <sec:authentication property="principal.username" /> is loggedin by "Remember Me Cookies".</h2>
		</sec:authorize>
		<sec:authorize access="isFullyAuthenticated()">
			<h2># <sec:authentication property="principal.username" /> is loggedin by username / password.</h2>
		</sec:authorize>
		
		<sec:authorize access="isAuthenticated()">
			<sec:authentication property="principal.username" /> are now loggedIN. <br>
			<a href="${customLoginFormLogoutUrl}">Anchor Logout via Controller(${customLoginFormLogoutUrl}+GET)</a>
			<!-- <form method="POST" action="${logoutUrl}">
				<button type="submit">Form logout</button>
			</form> -->
			<form method="POST" action="${customLoginFormLogoutUrl}">
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				<button type="submit">Logout via Custom Logout Filter for Custom LoginForm (mylogout+POST)</button>
			</form>
			<br>
			<form method="POST" action="${logoutFilterUrl}">
				<button type="submit">Logout via Logout Filter(logout+POST)</button>
			</form>
			<button type="button" onclick="logoutBasicAuthentication('${authenticationLogoutUrl}','${homePageUrl}')">Logout for Basic/Digest Authentication</button><br>
		</sec:authorize>
		
		<script type="text/javascript">
				function logoutBasicAuthentication(url,homePageUrl) {
					//(url);
					//alert(homePageUrl);
					
				    var xmlhttp = new XMLHttpRequest();
				    
				    xmlhttp.onreadystatechange = function() {
				        if (xmlhttp.readyState == XMLHttpRequest.DONE ) {
				           if (xmlhttp.status == 200) {
				               //document.getElementById("myDiv").innerHTML = xmlhttp.responseText;
				           }
				           else if (xmlhttp.status == 401) {
				              alert('401 Unauthorized response,After logout ,Redirecting to Home Page');
				              
				              //Redirect to home page
				              window.location.href=homePageUrl;
				           }
				           else {
				               alert('something else other than 200 was returned');
				           }
				        }
				    };
				
				    xmlhttp.open("GET",url, true);
				    xmlhttp.send();
				}
		</script>
	
	</body>
</html>
		
		