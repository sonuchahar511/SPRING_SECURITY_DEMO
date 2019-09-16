Step 3: Add Spring Security Configuration Class:::
This configuration creates a Servlet Filter known as the springSecurityFilterChain which is responsible for all
the security (protecting the application URLs, validating submitted username and passwords, redirecting
to the log in form, etc) within our application.
    @Configuration
    @EnableWebSecurity
    public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

        @Autowired
        public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication().withUser("bill").password("abc123").roles("USER");
            auth.inMemoryAuthentication().withUser("admin").password("root123").roles("ADMIN");
            auth.inMemoryAuthentication().withUser("dba").password("root123").roles("ADMIN","DBA");
            //dba have two roles.
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {

          http.authorizeRequests()
            .antMatchers("/", "/home").permitAll()
            .antMatchers("/admin/**").access("hasRole('ADMIN')")
            .antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')")
            .and().formLogin()
            .and().exceptionHandling().accessDeniedPage("/Access_Denied");

        }
    }

    xml equivalent:
        <beans:bean xmlns="http://www.springframework.org/schema/security"
        	xmlns:beans="http://www.springframework.org/schema/beans"
        	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        	xsi:schemaLocation="http://www.springframework.org/schema/security
        	http://www.springframework.org/schema/security/spring-security-4.2.xsd
        	http://www.springframework.org/schema/beans
        	http://www.springframework.org/schema/beans/spring-beans-4.3.xsd">
            <http auto-config="true" >
                <intercept-url pattern="/" access="permitAll" />
                <intercept-url pattern="/home" access="permitAll" />
                <intercept-url pattern="/admin**" access="hasRole('ADMIN')" />
                <intercept-url pattern="/dba**" access="hasRole('ADMIN') and hasRole('DBA')" />
                <form-login  authentication-failure-url="/Access_Denied" />
            </http>

            <authentication-manager >
                <authentication-provider>
                    <user-service>
                        <user name="bill"  password="abc123"  authorities="ROLE_USER" />
                        <user name="admin" password="root123" authorities="ROLE_ADMIN" />
                        <user name="dba"   password="root123" authorities="ROLE_ADMIN,ROLE_DBA" />
                    </user-service>
                </authentication-provider>
            </authentication-manager>
        </beans:beans>

    Method configureGlobalSecurity in above class configures AuthenticationManagerBuilder with user credentials
    and allowed roles. This AuthenticationManagerBuilder creates AuthenticationManager which is responsible for
    processing any authentication request. Notice that in above example, we have used in-memory authentication
    while you are free to choose from JDBC, LDAP and other authentications.

    The overridden Method Configure configures HttpSecurity which allows configuring web based security for
    specific http requests. By default it will be applied to all requests, but can be restricted using
    requestMatcher(RequestMatcher)/antMathchers or other similar methods.

    In above configuration, we say that URL’s ‘/’ & ‘/home’ are not secured, anyone can access them.
    URL ‘/admin/**’ can only be accessed by someone who have ADMIN role. URL ‘/db/**’ can only be accessed
    by someone who have both ADMIN and DBA roles.

    Method formLogin provides support for form based authentication and will generate a default form asking for
    user credentials. You are allowed to configure your own login form.We will see examples for the same in
    subsequent posts.

    We have also used exceptionHandling().accessDeniedPage() which in this case will catch all
    403 [http access denied] exceptions and display our user defined page instead of showing
    default HTTP 403 page


Step 4: Register the springSecurityFilter with war
    Below specified initializer class registers the springSecurityFilter
    [created in Step 3] with application war.

    package com.websystique.springsecurity.configuration;
    import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
    public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {
    }

    Above setup in XML configuration format would be:

    <filter>
        <filter-name>springSecurityFilterChain</filter-name>
        <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>springSecurityFilterChain</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>