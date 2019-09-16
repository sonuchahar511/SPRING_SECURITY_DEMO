log4j log level hierarchy:
    ALL
        TRACE
            DEBUG
                INFO
                    WARN
                        ERROR
                            FATAL
                                OFF
    ===================================

Filter Ordering:
    MultipartFilter
        HiddenHttpMethodFilter
            SpringSecurityFilterChain

7.3 Filter Ordering
The order that filters are defined in the chain is very important. Irrespective of which filters you are actually using,
the order should be as follows:

ChannelProcessingFilter, because it might need to redirect to a different protocol

SecurityContextPersistenceFilter, so a SecurityContext can be set up in the SecurityContextHolder at the beginning of
a web request, and any changes to the SecurityContext can be copied to the HttpSession when the web request ends
(ready for use with the next web request)

ConcurrentSessionFilter, because it uses the SecurityContextHolder functionality but needs to update the SessionRegistry
to reflect ongoing requests from the principal

Authentication processing mechanisms - UsernamePasswordAuthenticationFilter, CasAuthenticationFilter, BasicAuthenticationFilter
 etc - so that the SecurityContextHolder can be modified to contain a valid Authentication request token

The SecurityContextHolderAwareRequestFilter, if you are using it to install a Spring Security aware HttpServletRequestWrapper
 into your servlet container

RememberMeAuthenticationFilter, so that if no earlier authentication processing mechanism updated the SecurityContextHolder,
 and the request presents a cookie that enables remember-me services to take place, a suitable remembered Authentication object
  will be put there

AnonymousAuthenticationFilter, so that if no earlier authentication processing mechanism updated the SecurityContextHolder,
an anonymous Authentication object will be put there

ExceptionTranslationFilter, to catch any Spring Security exceptions so that either an HTTP error response can be returned or
 an appropriate AuthenticationEntryPoint can be launched

FilterSecurityInterceptor, to protect web URIs and raise exceptions when access is denied


Q) target-proxy-class=true???

JDK Dynamic proxy can only proxy by interface
    (so your target class needs to implement an interface, which is then also implemented by the proxy class).

CGLIB (and javassist) can create a proxy by subclassing.
    In this scenario the proxy becomes a subclass of the target class. No need for interfaces.

So Java Dynamic proxies can proxy: public class Foo implements iFoo where CGLIB can proxy: public class Foo

EDIT:

    I should mention that because javassist and CGLIB use proxy by subclassing,
    that this is the reason you cannot declare final methods or make the class final when using frameworks that rely on this.
    That would stop these libraries from allowing to subclass your class and override your methods.