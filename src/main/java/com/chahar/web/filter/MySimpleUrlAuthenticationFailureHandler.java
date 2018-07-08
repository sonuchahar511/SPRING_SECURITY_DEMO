package com.chahar.web.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;

public class MySimpleUrlAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler{
	protected final Log logger = LogFactory.getLog(MySimpleUrlAuthenticationFailureHandler.class);
	
	private String defaultFailureUrl;
	
	private transient String returnedFailureUrl;
	
	public MySimpleUrlAuthenticationFailureHandler(String defaultFailureUrl) {
		super(defaultFailureUrl);
	}
	
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		if (defaultFailureUrl == null) {
			logger.debug("No failure URL set, sending 401 Unauthorized error");
			response.sendError(401, "Authentication Failed: " + exception.getMessage());
		} else {
			this.saveException(request, exception);
			
			///////// custom business logic
			String exceptionMessage = exception.getMessage();
			if(exception instanceof SessionAuthenticationException && exceptionMessage.startsWith("Maximum sessions of") && exceptionMessage.endsWith("for this principal exceeded")){
				returnedFailureUrl = defaultFailureUrl + "?login_error=" + exceptionMessage;
				
				logger.info(">>>>>>>>>>>>>>>>>> Authentication failed due to MAXIMUM SESSION EXCEEDS <<<<<<<<<<<<<<<<<<< ");
			} else if(exception instanceof BadCredentialsException){
				returnedFailureUrl = defaultFailureUrl + "?login_error=" + exceptionMessage;
				
				logger.info(">>>>>>>>>>>>>>>>>> Authentication failed due to Both WRONG PASSWORD/INVALID USER <<<<<<<<<<<<<<<<<<< ");
			} else {
				returnedFailureUrl = defaultFailureUrl; 
			}
			/////////////////////////////////////////////////////////////
			
			if (isUseForward()) {
				logger.debug("Forwarding to " + this.returnedFailureUrl);
				request.getRequestDispatcher(this.returnedFailureUrl).forward(request, response);
			} else {
				logger.debug("Redirecting to " + this.returnedFailureUrl);
				getRedirectStrategy().sendRedirect(request, response, this.returnedFailureUrl);
			}
		}

	}

	public String getDefaultFailureUrl() {
		return defaultFailureUrl;
	}

	public void setDefaultFailureUrl(String defaultFailureUrl) {
		super.setDefaultFailureUrl(defaultFailureUrl);
		this.defaultFailureUrl = defaultFailureUrl;
	}
}
