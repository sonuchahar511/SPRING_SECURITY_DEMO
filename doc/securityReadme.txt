Security tasks such as authentication of user and authorization of a user to view application resources are usually handled by the application server. These tasks can be delegated to Spring security flow relieving application server from handling these tasks. Spring security basically handles these tasks by implementing standard javax.servlet.Filter. For initializing Spring security into your application, you need to declare the following filter in your web.xml:

<filter>
    <filter-name>springSecurityFilterChain</filter-name>
    <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
  </filter>
 
  <filter-mapping>
    <filter-name>springSecurityFilterChain</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>

Now this filter(springSecurityFilterChain) merely delegates the request to Spring security framework where the security tasks defined will be handled by security filters defined in application context. So how does this happen?
Inside the doFilter method of DelegatingFilterProxy(implementation of javax.servlet.Filter), the spring application context will be checked for a bean named ‘springSecurityFilterChain’. This ‘springSecurityFilterChain’ bean is actually an alias defined for spring filter chain.
1
<alias name="filterChainProxy" alias="springSecurityFilterChain"/>
So when the check is done on application context, it returns the filterChainProxy bean. This filter chain is different from that of javax.servlet.FilterChain which is used by Java filters defined in web.xml to invoke the next possible filter if there exists one or pass on the request to a servlet/jsp. The bean filterChainProxy consists of an ordered list of security filters that are defined in the spring application context. So here are the next set of questions:
1. Who initializes/defines this filterChainProxy?
2. What security filters are defined in Spring application context?
3. How are these security filters different from the normal filters defined in web.xml?
Now coming to the first question, filterChainProxy is initialized when the ‹http› element from security namespace is defined in the application context. Here is the basic structure of ‹http› element:

<sec:http auto-config="true">
 <sec:intercept-url pattern="/**" access="ROLE_USER" />
</sec:http>
 
<sec:authentication-manager id="authenticationManager">
  <sec:authentication-provider>
    <sec:user-service>
      <sec:user name="admin" password="password" authorities="ROLE_USER, ROLE_ADMIN" />
      <sec:user name="user" password="password" authorities="ROLE_USER" />
    </sec:user-service>
  </sec:authentication-provider>
</sec:authentication-manager>
Now the HttpSecurityBeanDefinitionParser from Spring framework reads this ‹http› element to register filterChainProxy in application context. The http element with auto-config set to true is actually a short hand notation for the following:

<sec:http>
  <sec:form-login />
  <sec:http-basic />
  <sec:logout />
</sec:http>
We will discuss the sub elements of ‹http› later. So now coming to the second question, what all filters get registered in filter chain by default? Here is the answer from Spring documentation:
The ‹http› namespace block always creates an SecurityContextPersistenceFilter, an ExceptionTranslationFilter and a FilterSecurityInterceptor. These are fixed and cannot be replaced with alternatives.
So by default when we add ‹http› element, the above three filters will be added. And as we have set auto-config to true, BasicAuthenticationFilter, LogoutFilter and UsernamePasswordAuthenticationFilter also gets added to the filter chain. Now if you look at the source code of any of these filters, these are also standard javax.servlet.Filter implementations. But by defining these filters in application context rather than in web.xml, the application server transfers the control to Spring to deal with security related tasks. And the Spring’s filterChainProxy will take care of chaining security filters that are to be applied on the request. This answers the third question.
To gain finer control on the security filters that are to be applied on the request, we can define our own FilterChainProxy implementation.

<bean id="filterChainProxy" class="org.springframework.security.web.FilterChainProxy">
  <sec:filter-chain-map path-type="ant">
    <sec:filter-chain pattern="/images/*" filters="none"/>
    <sec:filter-chain pattern="/**" filters="securityContextFilter, logoutFilter, formLoginFilter, servletApiFilter, anonFilter, exceptionTranslator, 
    filterSecurityInterceptor, customFilter1, customeFilter2" />
  </sec:filter-chain-map>
</bean>
From the xml above, we see that we don’t want any filters to be applied for images where as for rest of the requests there is a list of filters specified that have to be applied. So, in general, we specify the filter-chains in the order of least constrained to most constrained. But this kind of registering our own filter chains is in general not needed. Spring, through ‹http› element, provides several hooks through which we can get finer control on how security is applied. So, we will look in detail in what all can be configured through ‹http› element.
1. Authentication: HttpBasicAuthentication and Form Login Based Authentication
2. Authorization support through ACL(Access control list)
3. Log out support
4. Anonymous Login support
5. Remember-me Authentication
6. Concurrent Session Management
(1) Authentication: Authentication can be handled in two ways – HttpBasicAuthentication and Form Login based authentication. We will discuss these two in brief shortly. Before understanding these, it would be good have a basic understanding of AuthenticationManager which lies at the core of implementing authentication through spring security. Inside the authentication manager element, we define all the authentication providers available for the application. And the authentication provider contains an implementation of UserDetailsService. Spring loads the user information in UserDetailsService and compares the username/password combination with the credentials supplied at login. Here is the UserDetailsService interface:

package org.springframework.security.core.userdetails;
 
import org.springframework.dao.DataAccessException;
 
/**
 * Core interface which loads user-specific data.
 * It is used throughout the framework as a user DAO and is the strategy used by the
 * {@link org.springframework.security.authentication.dao.DaoAuthenticationProvider DaoAuthenticationProvider}.
 * The interface requires only one read-only method, which simplifies support for new data-access strategies.
 * @see org.springframework.security.authentication.dao.DaoAuthenticationProvider
 * @see UserDetails
 * @author Ben Alex
 */
public interface UserDetailsService {
    /**
     * Locates the user based on the username. In the actual implementation, the search may possibly be case
     * insensitive, or case insensitive depending on how the implementation instance is configured. In this case, the
     * <code>UserDetails</code> object that comes back may have a username that is of a different case than what was
     * actually requested..
     *
     * @param username the username identifying the user whose data is required.
     *
     * @return a fully populated user record (never <code>null</code>)
     *
     * @throws UsernameNotFoundException if the user could not be found or the user has no GrantedAuthority
     * @throws DataAccessException if user could not be found for a repository-specific reason
     */
    UserDetails loadUserByUsername(String username)
        throws UsernameNotFoundException, DataAccessException;
}
Spring provides two built in implementations of this service:
(a) Store user login/password details in the application context:
This is well suitable when there are only few users of the application. This can be initialized as follows:

<sec:authentication-manager id="authenticationManager">
  <sec:authentication-provider>
     <sec:user-service>
      <sec:user name="admin" password="password" authorities="ROLE_ADMIN,ROLE_USER"/>
      <sec:user name="user" password="password" authorities="ROLE_USER"/>
    </sec:user-service>
  </sec:authentication-provider>
</sec:authentication-manager>
The ‹authentication-provider› tag corresponds to DaoAuthenticationProvider which actually invokes the implementation of UserDetailsService provided. In this case, we are providing the user names and passwords directly in XML. When the user base of application is huge, we would prefer to store the information in database.
The corresponding bean that gets initialized for ‹user-service› is org.springframework.security.core.userdetails.memory.InMemoryDaoImpl
(b) Storing user details in database: Here is how this has to be initialized.

<sec:authentication-manager id="authenticationManager">
  <sec:authentication-provider>
    <sec:jdbc-user-service data-source-ref="dataSource" />
  </sec:authentication-provider>
</sec:authentication-manager>
The corresponding class in Spring is org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl. If you look at this class, you can find out that the username and password are stored in users table and the roles that can be assigned to users are stored in authorities table. We will discuss about the roles later. Here are the queries that this class perform to fetch users credentials and authorities from database:

-- Fetch user credentials: 
select username,password,enabled from users where username = ?
-- Fetch user authorities: 
select username,authority from authorities where username = ?
Now suppose you have a legacy database where your user details are stored in some other tables, then we can configure the fetch queries that Spring performs to fetch the user credentials and authorities. Say I have a member table which has id, username, password fields and Role table which has username, role fields. Here is how we have to configure:

<sec:authentication-manager id="authenticationManager">
  <sec:authentication-provider>
    <!-- TBD <password-encoder hash="md5"/> -->
    <sec:jdbc-user-service id="userDetailsService" data-source-ref="dataSource"
      users-by-username-query=
        "SELECT username, password, true as enabled
         FROM MEMBER
         WHERE username=?"
      authorities-by-username-query=
        "SELECT member.username, role.role as authorities
         FROM ROLE role, MEMBER member
         WHERE role.member_id=member.id and member.username=?"/>
  </sec:authentication-provider>
</sec:authentication-manager>
Now coming to ways of performing authentication:
HttpBasicAuthentication: This can be configured as follows:

<sec:http auto-config="true">
  <sec:http-basic />
</sec:http>
By default, when this is enabled, browser will typically display a login dialog for users to login. Instead of login dialog we can configure it to show a specific login page. This kind of authentication is formally defined in the Hypertext Transfer Protocol standard. The login credentials(base 64 encoded) are sent to the server under Authentication http header. But it has it’s own downsides. The biggest problem with it has to do with the logging off the server. Most browsers tend to cache sessions, different user couldn’t log back in by refreshing the browser. Defining ‹http-basic› actually defines a BasicAuthenticationFilter filter behind the scenes. On successful authentication, Authentication object will be put into Spring securityContext. The security context can be accessed through the class SecurityContextHolder. Here is how the BasicAuthenticationFilter bean declaration looks like:

<sec:custom-filter position="BASIC_AUTH_FILTER" ref="basicAuthenticationFilter" />
 
<bean id="basicAuthenticationFilter" class="org.springframework.security.web.authentication.www.BasicAuthenticationFilter">
  <property name="authenticationManager" ref="authenticationManager"/>
  <property name="authenticationEntryPoint" ref="authenticationEntryPoint"/>
</bean>
 
<bean id="authenticationEntryPoint" class="org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint">
    <property name="loginFormUrl" value="/login.jsp"/>
</bean>
For more details on filter positions, refer to the enum org.springframework.security.config.http.SecurityFilters
Form Login Based Authentication: Here is how we enable it:

<sec:form-login login-page="/login.jsp"/>
But there are multiple hooks provided by Spring. The attribute default-target-url specifies where the login page should go once the user is authenticated and authentication-failure-url defines the page the user should be taken if the authentication is a failure.

<sec:form-login login-page="/login.jsp" default-target-url="/app/messagePost"
  authentication-failure-url="/login.jsp?error=true"/>
The next set of attributes are: always-use-default-target, authentication-success-handler-ref and authentication-failure-handler-ref. authentication-success-handler-ref gets called on successful authentication and authentication-failure-handler-ref gets called on authentication failure. Here are the interfaces for AuthenticationSuccessHandler and AuthenticationFailureHandler.

/**
 * Strategy used to handle a successful user authentication.
 * <p>
 * Implementations can do whatever they want but typical behaviour would be to control the navigation to the
 * subsequent destination (using a redirect or a forward). For example, after a user has logged in by submitting a
 * login form, the application needs to decide where they should be redirected to afterwards
 * (see {@link AbstractAuthenticationProcessingFilter} and subclasses). Other logic may also be included if required.
 *
 * @author Luke Taylor
 * @since 3.0
 */
public interface AuthenticationSuccessHandler {
 
    /**
     * Called when a user has been successfully authenticated.
     *
     * @param request the request which caused the successful authentication
     * @param response the response
     * @param authentication the <tt>Authentication</tt> object which was created during the authentication process.
     */
    void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException;
}
 
/**
 * Strategy used to handle a failed authentication attempt.
 * <p>
 * Typical behaviour might be to redirect the user to the authentication page (in the case of a form login) to
 * allow them to try again. More sophisticated logic might be implemented depending on the type of the exception.
 * For example, a {@link CredentialsExpiredException} might cause a redirect to a web controller which allowed the
 * user to change their password.
 *
 * @author Luke Taylor
 * @since 3.0
 */
public interface AuthenticationFailureHandler {
 
    /**
     * Called when an authentication attempt fails.
     * @param request the request during which the authentication attempt occurred.
     * @param response the response.
     * @param exception the exception which was thrown to reject the authentication request.
     */
    void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException;
}
Spring has 2 built-in implementations for success handlers. SimpleUrlAuthenticationSuccessHandler and SavedRequestAwareAuthenticationSuccessHandler. The latter extends the former.
The purpose of SavedRequestAwareAuthenticationSuccessHandler is to take the user to the page from where he has been redirected to the Login page for authentication.This is the default success handler when ‹form-login› element is defined. We can override this with our custom implementation as well. Suppose we always want to show a particular page once the user logs in rather than taking him to page he is on earlier, we can set always-use-default-target to true.
Also there are 2 built in implementations for failure handlers: SimpleUrlAuthenticationFailureHandler and ExceptionMappingAuthenticationFailureHandler. The latter extends the former.
We only specify a single URL in case of SimpleUrlAuthenticationFailureHandler where the user will be taken to on failure of authentication where as in case of ExceptionMappingAuthenticationFailureHandler we specify multiple URLs where user will be taken to based on the kind of authentication exception(subclasses of org.springframework.security.core.AuthenticationException) being thrown during the authentication process(UserDetailsService implementation would throw the exception).
Also when we define our custom login page, we mark the user name and password fields as j_username and j_passwordrespectively and the submit action would be defaulted to j_spring_security_check. We can also configure these field names and submit action by specifying the attributes: username-parameter, password-parameter and login-processing-urlrespectively.
Here is how the filter definition looks like:

<sec:custom-filter position="FORM_LOGIN_FILTER" ref="formLoginFilter" />
<bean id="formLoginFilter" class="org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter">
  <property name="authenticationManager" ref="authenticationManager"/>
  <property name="filterProcessesUrl" value="/j_spring_security_check"/>
  <property name="usernameParameter" value="username "/>
  <property name="passwordParameter" value="password"/>
  <property name="authenticationSuccessHandler">
    <bean class="org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler ">
      <property name="alwaysUseDefaultTargetUrl" value="true"/>
      <property name="defaultTargetUrl" value="/success.jsp"/>
    </bean>
  </property>
  <property name="authenticationFailureHandler">
    <!--bean class=" org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler "/-->
    <bean id="authenticationFailureHandler" class="org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler">
      <property name="exceptionMappings">
        <props>
          <prop key="org.springframework.security.authentication.BadCredentialsException">/login/badCredentials</prop>
          <prop key="org.springframework.security.authentication.CredentialsExpiredException">/login/credentialsExpired</prop>
          <prop key="org.springframework.security.authentication.LockedException">/login/accountLocked</prop>
          <prop key="org.springframework.security.authentication.DisabledException">/login/accountDisabled</prop>
        </props>
      </property>
    </bean>
  </property>
 </bean>
In case of form login, there would not be any problem with logging out as discussed in basic authentication. But the downside is that the user name and password are sent as clear text in headers. This can be handled by encoding passwords using encryption techniques. Spring provides a built in support for this using ‹password-encoder› element in authentication provider. Here is how we have to configure it:

<sec:authentication-manager id="authenticationManager">
  <sec:authentication-provider>
    <sec:password-encoder hash="md5"/>
    <sec:jdbc-user-service data-source-ref="dataSource" />
  </sec:authentication-provider>
</sec:authentication-manager>
2. Authorization support through ACL: Spring supports authorization through ‹intercept-url› in ‹http›

<sec:http access-decision-manager-ref="accessDecisionManager">
  <sec:intercept-url pattern="/app/messageList*" access="ROLE_USER,ROLE_ANONYMOUS"/>
  <sec:intercept-url pattern="/app/messagePost*" access="ROLE_USER"/>
  <sec:intercept-url pattern="/app/messageDelete*" access="ROLE_ADMIN"/>
  <sec:intercept-url pattern="/app/*" access="ROLE_USER"/>
 
  <form-login login-page="/login.jsp" default-target-url="/app/messagePost"
    authentication-failure-url="/login.jsp?error=true"/>
  <!-- Other settings -->
</sec:http>
Each intercept-url specifies a url pattern and the roles that user must have to access those urls which match the specified pattern. Note that the url-patterns are always ending with a ‘*’. If ‘*’ is not specified then the problem is that hacker can bypass the security mechanism by just passing some parameters in the url.
So what happens behind the scenes is when Spring passes all these urls to be intercepted as metadata to FilterSecurityInterceptor. So here is how the same can be configured without using ‹intercept-url›:

<sec:custom-filter position="FILTER_SECURITY_INTERCEPTOR" ref="filterSecurityInterceptor" />
<bean id="filterSecurityInterceptor" class="org.springframework.security.web.access.intercept.FilterSecurityInterceptor">
  <property name="authenticationManager" ref="authenticationManager"/>
  <property name="accessDecisionManager" ref="accessDecisionManager"/>
  <property name="securityMetadataSource">
  <sec:filter-security-metadata-source lowercase-comparisons="true" request-matcher="ant" use-expressions="true">
    <sec:intercept-url pattern="/app/messageList*" access="ROLE_USER,ROLE_ANONYMOUS"/>
    <sec:intercept-url pattern="/app/messagePost*" access="ROLE_USER"/>
    <sec:intercept-url pattern="/app/messageDelete*" access="ROLE_ADMIN"/>
    <sec:intercept-url pattern="/app/*" access="ROLE_USER"/>
  </sec:filter-security-metadata-source>
  </property>
</bean>
So from the code above you can see that anonymous users can only access messageList page and for any of the other pages to be viewed, he should be logged in as a user into application. Also if you closely observe the bean declaration, there is a property ‘accessDecisionManager’. What is the purpose of this?
It is the bean which actually makes the access control decisions. It has to implement the AccessDecisionManager interface. Spring provides three built-in access decision managers. Before understanding how access decision manager works, we need to know what exactly a AccessDecisionVoter is. AccessDecisionManager is actually composed with one or multiple access decision voters. This voter encapsulates the logic to allow/deny/abstain the user from viewing the resource. Voting the decision as abstain is more or less similar to not voting at all.So the voting results are represented by the ACCESS_GRANTED, ACCESS_DENIED, and ACCESS_ABSTAIN constant fields defined in the AccessDecisionVoter interface. We can define custom access decision voters and inject them into our access decision manager definition. So now coming back to the built-in decision managers, here are they:
    1. AffirmativeBased: At least one voter must vote to grant access
    2. ConsensusBased: Majority of voters must vote to grant access
    3. UnanimousBased: All voters must vote to abstain or grant access (no voter votes to deny access)
By default, an AffirmativeBased access decision manager will be intialized with 2 voters: RoleVoter and AuthenticatedVoter. RoleVoter grants access if the user has some role as the resouce required. But note that the role must start with “ROLE_” prefix if the voter has to grant access. But this can be customized for some other prefix as well. We will see soon how to do it. AuthenticatedVoter grants access only if user is authenticated. The authentication levels accepted are IS_AUTHENTICATED_FULLY, IS_AUTHENTICATED_REMEMBERED, and IS_AUTHENTICATED_ANONYMOUSLY. Suppose we want to define a custom voter and add it to the access decision manager, here is we do it:

<sec:http access-decision-manager-ref="accessDecisionManager" auto-config="true">
  <!-- filters declaration go here-->
</sec:http>
 
<bean id="accessDecisionManager" class="org.springframework.security.access.vote.AffirmativeBased">
  <property name="decisionVoters">
    <list>
      <bean class="org.springframework.security.access.vote.RoleVoter">
    <!-- Customize the prefix-->
    <property name="rolePrefix" value="ROLE_"/>
      </bean>
      <bean class="org.springframework.security.access.vote.AuthenticatedVoter"/>
      <bean class="com.pramati.security.voters.CustomVoter"/>
    </list>
  </property>
</bean>
3. Log out support: Spring provides a handler to handle log out requests. This can be configured as follows:

<sec:http>
 
  <!-- Other filter declarations here -->
 
  <sec:logout />
 
</sec:http>
By default the log-out url is mapped to /j_spring_security_logout. We can customize this url by specifying logout-url attribute. Also when an user is logged out, he will be taken to context path root. If the user has to be redirected to some other url then this has to be configured via logout-success-url. Here is how you do it:
1
<sec:logout logout-url="/j_logMeOut" logout-success-url="/app/messageList"/>
If you want the landing page to be different in different scenarios instead of defaulting to one specific url, then we have to implement the LogoutSuccessHandler and provide a reference of it to ‹logout› element
1
<sec:logout logout-url="/j_logMeOut" success-handler-ref="customLogoutSuccessHandler"/>
Here is the how the underlying filter can be defined if you don’t want to use ‹logout› element:

<sec:custom-filter position="LOGOUT_FILTER" ref="logoutFilter" />
<bean id="logoutFilter" class="org.springframework.security.web.authentication.logout.LogoutFilter">
  <constructor-arg value="/pages/Security/logout.html" />
    <constructor-arg>
      <list>
        <bean class="org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler"/>
      </list>
    </constructor-arg>
  <property name="filterProcessesUrl" value="/j_logMeOut"/>
</bean>
4. Anonymous Login support: By default an anonymous role is created by Spring.
So when you specify role as ‘ROLE_ANONYMOUS’ or ‘IS_AUTHENTICATED_ANONYMOUSLY’, any anonymous user can view that page. In AffirmativedBased accession decision manager, RoleVoter grants access when it sees the access attribute set to ‘ROLE_ANONYMOUS’. Similarly AuthenticatedVoter grants access if access attribute is set to ‘IS_AUTHENTICATED_ANONYMOUSLY’.
Suppose you want a different role name to be assigned for anonymous users, you can override the default configuration as follows:

<sec:http>
  <sec:intercept-url pattern="/login.jsp*" filters="none"/>
  <sec:intercept-url pattern="/*" access="ROLE_USER"/>
 
  <!-- Defines a custom role in place of ROLE_ANONYMOUS. 
  ROLE_ANONYMOUS will no more work, use ROLE_GUEST instead of it-->
  <sec:anonymous username="guest" granted-authority="ROLE_GUEST" />
</sec:http>
 
<p style="text-align: justify;">Here is the how the underlying filter can be defined if you don't want to use ‹anonymous› element:</p>
 
1
<sec:custom-filter position="ANONYMOUS_FILTER" ref="anonymousFilter" />
<bean id="anonymousFilter" class="org.springframework.security.web.authentication.AnonymousAuthenticationFilter" >
    <property name="userAttribute" value="ROLE_GUEST" />
</bean>
5. Remember-me Authentication: This refers to websites being able to remember the identity of principal between sessions. Spring achieves this by sending a cookie to the browser upon successful interactive authentication, with the cookie being composed as follows:
base64(username + “:” + expirationTime + “:” + md5Hex(username + “:” + expirationTime + “:” password + “:” + key))
Now when the browser makes a next request to the server, it also sends this cookie along with it. Now behind the scenes, Spring does the following:
(a) Retrieves the password from the back end for the given username
(b) Fetches the pasword from database for the uername and computes the md5Hex() of username, password, expirationTime and key and compares it to the value in the cookie
(c) If they match – you are logged in! If not a match, then you’ve supplied a forged cookie or one of the username/password/key has changed.
We can enable remember-me authentication just by adding the element inside ‹http›. Here is how we do it:

<sec:http>
 
  <!-- Other filter declarations here -->
 
  <sec:remember-me key="myAppKey"/>
 
</sec:http>
The UserDetailsService will normally be selected automatically. If you have more than one in your application context, you need to specify which one should be used with the user-service-ref attribute, where the value is the name of your UserDetailsService bean. A point to note is that there is a potential security issue here as the remember-me token can be captured and may be misused as it is valid until it is expired. This can be avoided by using rolling tokens. Here is how you can implement token based remember me service:

<sec:http access-decision-manager-ref="accessDecisionManager">
 
    <!-- Other filter declarations here -->
 
    <remember-me services-alias="rememberMeService" data-source-ref="dataSource"/>
    <!-- <remember-me data-source-ref="dataSource" key="pramati"/> -->
 
</sec:http>
 
<bean id="tokenRepository" class="org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl">
    <property name="dataSource" ref="dataSource"/>
    <property name="createTableOnStartup" value="true"/>
</bean>
 
<bean id="rememberMeService" class="org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices">
    <property name="userDetailsService" ref="userDetailsService"/>
    <property name="tokenRepository" ref="tokenRepository"/>
</bean>
Points to note:
(a) A new table persistent_logins will be created in database as we have specified ‘createTableOnStartup’ to be true while defining bean ‘tokenRepository’. Here is the sql for table creation:

create table persistent_logins (
 username varchar(64) not null,
 series varchar(64) primary key,
 token varchar(64) not null,
 last_used timestamp not null);
(b) We are no more giving our own safety token. Spring will automatically generate the token and put/update it in persistent_tokens table. When the user access the application from a browser and logs in to application by selecting remember me option, an entry will be created in this table. Next time the user logs in from the same browser, user will be automatically logged in and the token value in the DB will be changed to a new value but the series value remains unchanged. Suppose the user now logs in from a different browser opting remember me, a new entry will be created for that browser. And subsequent updates happens on that particular row itself when he accesses the application from that browser.
So the benefit of using this approach is an attacker will only be able to use a stolen cookie until the victim user next accesses the application instead of for the full lifetime of the remembered cookie as seen in the earlier single token approach. When the victim next accesses the web site, he will be using the same cookie. Now Spring will throw a CookieTheftException, which can be used to inform the user that the theft occurred.
Instead of using the element that we have been using so far, we can define it as custom filter in the security chain as follows:

<sec:custom-filter position="REMEMBER_ME_FILTER" ref="rememberMeFilter" />
 
<bean id="rememberMeFilter" class="org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter">
  <property name="rememberMeServices" ref="rememberMeServices"/>
  <property name="authenticationManager" ref="theAuthenticationManager" />
</bean>
 
<bean id="tokenRepository" class="org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl">
    <property name="dataSource" ref="dataSource"/>
    <property name="createTableOnStartup" value="false"/>
</bean>
 
<bean id="rememberMeServices" class="org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices">
    <property name="userDetailsService" ref="userDetailsService"/>
    <property name="tokenRepository" ref="tokenRepository"/>
</bean>
 
<bean id="rememberMeAuthenticationProvider" class="org.springframework.security.authentication.rememberme.RememberMeAuthenticationProvider"/>
6. Concurrent Session Management: Suppose we don’t want a user to log in to application from more than one place at the same time, we have to enable this feature in Spring. Here is how we do it:

<sec:http>
 
  <!-- Other filter declarations here -->
 
  <sec:session-management session-authentication-error-url="/login.jsp?error=alreadyLoggedin" >
    <sec:concurrency-control max-sessions="1" error-if-maximum-exceeded="true"
        expired-url="/login.jsp?error=alreadyLoggedin"/>
  </sec:session-management>
</sec:http>
Also we have to define a listener in web.xml which is needed for raising a event(org.springframework.security.core.session.SessionDestroyedEvent) when user logs out from the application.

<listener>
  <listener-class>
    org.springframework.security.web.session.HttpSessionEventPublisher
  </listener-class>
</listener>
So now what happens behind the scenes? How does Spring achieve this support?
Spring uses a security filter similar to what we have been discussing all the time. Apart from that, it also uses ApplicationEvents. When spring sees that concurrency control is needed, it maintains the list of sessions associated with a principal. The map structure looks like(actually defined in org.springframework.security.core.session.SessionRegistryImpl):

ConcurrentMap<Object,Set<String>> principals =
  new ConcurrentHashMap<Object,Set<String>>();
The key of the map here is the User object and the value is set of session ids associated with him. So an exception is raised when the size of set is more than the value of max-sessions defined in ‹concurrency-control› element. When Spring sees the concurrency-control element defined, SessionRegistryImpl(where the map is defined) is composed inside ConcurrentSessionControlStrategy and is injected into UsernamePasswordAuthenticationFilter. Now as user authentication succeeds, Spring puts an entry into the map discussed above.
Now when user logs out, SessionDestroyedEvent will be raised as seen above when defining the listener in web.xml. SessionRegistryImpl listens for this event and removes the session id entry from the map being maintained. Without this, a user will never be able to log back in again once they have exceeded their session allowance, even if they log out of another session or it times out. So here is the equivalent configuration for ‹concurrency-control›:

<sec:http>
  <sec:custom-filter position="CONCURRENT_SESSION_FILTER" ref="concurrencyFilter" />
  <sec:custom-filter position="FORM_LOGIN_FILTER" ref="myAuthFilter" />
 
  <!-- Other filter declarations here -->
 
  <sec:session-management session-authentication-strategy-ref="sessionAuthenticationStrategy"/>
</sec:http>
 
<bean id="concurrencyFilter" class="org.springframework.security.web.session.ConcurrentSessionFilter">
 <property name="sessionRegistry" ref="sessionRegistry" />
 <property name="expiredUrl" value="/session-expired.htm" />
</bean>
 
<bean id="myAuthFilter" class="org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter">
  <property name="sessionAuthenticationStrategy" ref="sessionAuthenticationStrategy" />
  <property name="authenticationManager" ref="authenticationManager" />
</bean>
 
<bean id="sessionAuthenticationStrategy" class="org.springframework.security.web.authentication.session.ConcurrentSessionControlStrategy">
  <constructor-arg name="sessionRegistry" ref="sessionRegistry" />
  <property name="maximumSessions" value="1" />
</bean>
 
<bean id="sessionRegistry" class="org.springframework.security.core.session.SessionRegistryImpl" />
To conclude the article, it touches the basic configurations and the underlying classes of the framework which are crucial to understand for customizing the security according to our specific requirements.

