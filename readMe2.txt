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










REFERENCES:
    https://docs.oracle.com/cd/E19798-01/821-1841/bncbq/index.html