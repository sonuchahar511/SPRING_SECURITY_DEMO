package com.chahar.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.chahar.entity.User;
import com.chahar.service.UserService;

public class CustomAuthenticationProvider implements AuthenticationProvider{
	
	private static final Log LOGGER = LogFactory.getLog(CustomAuthenticationProvider.class);
	
	private UserService userService;
	private DefaultPasswordEncoderService passwordEncoderService;
	
	@Override
	public Authentication authenticate(final Authentication authentication)throws AuthenticationException {
		String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        
        User user =  userService.getUserByUsername(username);
        
        if(user == null){
        	LOGGER.info("NO user username="+username);
        	throw new BadCredentialsException("Username not found.");
        }
        String encodedPassword = passwordEncoderService.encode(password);
        if(!passwordEncoderService.matches(password, user.getPassword())){
        	LOGGER.info("Password mismatch for username="+username);
        	throw new BadCredentialsException("Bad password");
        }
        
        LOGGER.info("User Authenticated for username="+username+" . Returning UsernamePasswordAuthenticationToken");
        
		return new UsernamePasswordAuthenticationToken(user, encodedPassword ,user.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public DefaultPasswordEncoderService getPasswordEncoderService() {
		return passwordEncoderService;
	}

	public void setPasswordEncoderService(DefaultPasswordEncoderService passwordEncoderService) {
		this.passwordEncoderService = passwordEncoderService;
	}

}
