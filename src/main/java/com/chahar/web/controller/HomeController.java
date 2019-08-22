package com.chahar.web.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.chahar.web.CmsPages;

@Controller
@RequestMapping(value="/")
public class HomeController extends AbstractPageController{
	private static final Log LOGGER = LogFactory.getLog(HomeController.class);
	
	private static final String[] TO_BE_DELETE_COOKIES = {"JSESSIONID","remember-me","rememberMe_Cookie1"};
	private static final List<String> AUTHORIZATIONS = new ArrayList<String>();
	static{
		AUTHORIZATIONS.add("BASIC");
		AUTHORIZATIONS.add("DIGEST");
	}
	
	@RequestMapping(value=CmsPages.HOME_PAGE)
	 public final String welcomePage() {
		 LOGGER.info(getPrincipal()+": index page called");
		 
		 return CmsPages.HOME_PAGE;
	 }
	
	 @RequestMapping(value=CmsPages.MYLOGIN_PAGE, method = {RequestMethod.GET,RequestMethod.POST})
	 public ModelAndView getUserLoginPage(final HttpServletRequest request,final HttpServletResponse response , 
			 @RequestParam(value = "error", required = false) String error,
			 @RequestParam(value = "logout", required = false) String logout) {
		LOGGER.info(getPrincipal()+": mylogin page called with GET ");
		
	    ModelAndView result =  new ModelAndView(CmsPages.MYLOGIN_PAGE);
	    if (error != null) {
	    	result.addObject("error", "Invalid username and password!");
	    	
	    	Object obj = request.getSession().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	    	if(obj != null){
	    		if(obj instanceof SessionAuthenticationException){
	    			SessionAuthenticationException sessionAuthenticationException = (SessionAuthenticationException) obj;
	    			result.addObject("exceptionType", "SessionAuthenticationException");
		    		result.addObject("exceptionMessage",sessionAuthenticationException.getMessage());
	    		}else if(obj instanceof BadCredentialsException){
	    			BadCredentialsException badCredentialsException = (BadCredentialsException) obj;
	    			result.addObject("exceptionType", "BadCredentialsException");
	    			result.addObject("exceptionMessage",badCredentialsException.getMessage());
	    		}	
	    	}
		}
		if (logout != null) {
			result.addObject("msg", "You've been logged out successfully.");
		}
	    return result;
	 }
	
	//make user logout , by hitting this mapping
	//NO need to add <logout logout-url="logout"> in security context
	@RequestMapping(value="/mylogout", method = RequestMethod.GET)
	public String logoutPageGet(HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info(getPrincipal()+": mylogout with GET called");

		Authentication currentAuthentication = SecurityContextHolder.getContext().getAuthentication();
		new CookieClearingLogoutHandler(TO_BE_DELETE_COOKIES).logout(request, response, currentAuthentication);

		doLogoutAction(request,response);
		
	    return REDIRECT_PREFIX+CmsPages.HOME_PAGE;
	}
	
	@RequestMapping(value="/mylogout", method = RequestMethod.POST)
	public String logoutPagePost(HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info(getPrincipal()+": mylogout with Post called");

		Authentication currentAuthentication = SecurityContextHolder.getContext().getAuthentication();
		new CookieClearingLogoutHandler(TO_BE_DELETE_COOKIES).logout(request, response, currentAuthentication);

		doLogoutAction(request,response);
		
	    return REDIRECT_PREFIX+CmsPages.HOME_PAGE;
	}
	
	//logout for basic/digest authentication method
	@RequestMapping(value="/mylogout1", method = RequestMethod.GET)
	public void logoutPage1Get(final HttpServletRequest request,final HttpServletResponse response,final Model model) {
		LOGGER.info(getPrincipal()+": mylogout1 with GET called");
		
		if(request.getHeader("Authorization") != null ) {
			String authorizationType = request.getHeader("Authorization").split(" ")[0];
			LOGGER.info("doing Logout with Authorization="+authorizationType);
			
			if(authorizationType != null &&(authorizationType.equalsIgnoreCase("BASIC")||authorizationType.equalsIgnoreCase("DIGEST")) ){
				try {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"You have been logout successfully");
					
					doLogoutAction(request, response);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	 @RequestMapping(value="/sessionExpired",method=RequestMethod.GET)
	 public String sessionExpiredPage(final HttpServletRequest request,final Model model){
		 LOGGER.info("sessionExpired Page with GET");
		 
		 return "sessionExpired";
	 }
	
	 @RequestMapping(value="/invalidSessionUrl",method=RequestMethod.GET)
	 public String invalidSessionPage(final HttpServletRequest request,final Model model){
		 LOGGER.info("invalidSession Page with GET");
		 
		 return "invalidSessionUrl";
	 }
	 
	 @RequestMapping(value="/401page",method=RequestMethod.GET)
	 public String ge401UnAuthorized(final HttpServletRequest request,final HttpServletResponse response,final Model model) {
		 LOGGER.info(getPrincipal()+":401 page called");
		 
		 if(request.getHeader("Authorization") != null && model.containsAttribute("logoutAction") ){
			 String authorization = request.getHeader("Authorization").split(" ")[0];
			 LOGGER.info("Authorization="+authorization);
			 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			 if(authorization != null && auth.isAuthenticated()){
				 model.addAttribute("authorizationType", authorization);
				 model.addAttribute("result", "You have been successfully logout");
				 
				 doLogoutAction(request, response);
			 }
		 }else{
			 model.addAttribute("result","You are not authorized.Please provide correct credentials");
		 }
		
		 return "401page";
	 }
	 
	 @RequestMapping(value="/403")
	 public final String ge403denied() {
		 LOGGER.info("/403 ==>"+CmsPages.ACCESS_DENIED_PAGE+" , Access denied for user:"+getPrincipal());
		 
		 return CmsPages.ACCESS_DENIED_PAGE;
	 }
	 
	//NOTE: 404&500 - it is handled in web.xml by error-page tag.
	 
}
