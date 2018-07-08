package com.chahar.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

public abstract class AbstractPageController {
	
	public static final String REDIRECT_PREFIX = "redirect:/";
	
	 protected void doLogoutAction(final HttpServletRequest request,final HttpServletResponse response){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null) {    
	        new SecurityContextLogoutHandler().logout(request, response, auth);
	    }
	    SecurityContextHolder.getContext().setAuthentication(null);
	    SecurityContextHolder.clearContext();
	 }
		 
	 protected String getPrincipal(){
		 String userName = null;
		 if(SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null
				 && SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {
			 
		    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		 
		    if (principal instanceof UserDetails) {
		        userName = ((UserDetails)principal).getUsername();
		    } else {
		        userName = principal.toString();
		    }
		}
      return userName;
	}
}
