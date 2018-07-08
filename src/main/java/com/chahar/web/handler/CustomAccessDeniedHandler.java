package com.chahar.web.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

public class CustomAccessDeniedHandler implements AccessDeniedHandler{
	private static final Log LOGGER = LogFactory.getLog(CustomAccessDeniedHandler.class);
	
	private String errorPage;
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException arg2)
			throws IOException, ServletException {
		
		//do some business logic, then redirect to errorPage url
		
		LOGGER.info("AccessDenied due to: " + arg2.getMessage());
		
		response.sendRedirect(errorPage);
	}

	public String getErrorPage() {
		return errorPage;
	}
	public void setErrorPage(String errorPage) {
		this.errorPage = errorPage;
	}
}
