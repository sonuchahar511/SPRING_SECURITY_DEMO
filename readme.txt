Software version:
	tomcat 7.0.77

1)Copy  mysql connector jar into TOMCAT_HOME/lib directory for tomcat session store persistence in DB.
	This jar is downloaded during mvn clean install during project build in maven repository.
	
	session specific db properties is present in catalina.properties file.
	
2)Copy catalina.properties,context.xml from META-INF to TOMCAT_HOME/conf directory.
3)

Steps completed.
=========================================================================================================
Create & install Certificate in tomcat.
https://docs.oracle.com/cd/E19798-01/821-1841/gjrgy/index.html

1-generate Certificate(private+pulbic keys) done by Server.
keytool -genkey -alias server-alias -keyalg RSA -keypass changeit -storepass changeit -keystore mykeystore.jks

2-Export the generated server certificate(distribute by server to Ecommerce Merchant) from mykeystore.jks into the file myserver.cer
keytool -export -alias server-alias -storepass changeit -file myserver.cer -keystore mykeystore.jks

3-To add the server certificate to the truststore file mykeystore.jks,
	run keytool from the directory where you created the keystore and server certificate.
keytool -import -v -trustcacerts -alias server-alias -file myserver.cer -keystore mykeystore.jks -keypass changeit -storepass changeit	

Verify & configure SSL support	
https://docs.oracle.com/cd/E19798-01/821-1841/bnbxx/index.html

https://tomcat.apache.org/tomcat-7.0-doc/ssl-howto.html

<!-- Define a HTTP/1.1 Connector on port 8443, JSSE BIO implementation -->
<Connector protocol="org.apache.coyote.http11.Http11Protocol" port="8443" .../>
<Listener className="org.apache.catalina.core.AprLifecycleListener" SSLEngine="on" SSLRandomSeed="builtin" />
=========================================================================================================================
mvn tomcat7:deploy
mvn tomcat7:undeploy
mvn tomcat7:redeploy
==========================================================================================================================
LDAP authentication in spring security
{java.naming.provider.url=ldap://localhost:8389/dc=springframework,dc=org, java.naming.factory.initial=com.sun.jndi.ldap.LdapCtxFactory, com.sun.jndi.ldap.connect.pool=true, org.springframework.ldap.base.path=dc=springframework,dc=org, java.naming.factory.object=org.springframework.ldap.core.support.DefaultDirObjectFactory}

org.springframework.security.authentication.encoding.PlaintextPasswordEncoder@4f5f6e45

LdapAuthenticationProviderConfigurer<B>.PasswordCompareConfigurer 

LdapAuthenticationProviderConfigurer.this.passwordAttribute = "userPassword"


Using default security password: bb4c9e50-5405-465f-8b76-014bae8c86f1
org.springframework.security.ldap.authentication.PasswordComparisonAuthenticator

[uid=bob,ou=people]
===========================================================================================================================================
Q1- Disable Spring Security
    i) <http security="none">
    ii) Delete/Comment all child of http tag
	
Q2- 
