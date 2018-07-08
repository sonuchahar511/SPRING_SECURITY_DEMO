package com.chahar.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

public class DefaultPasswordEncoderService implements PasswordEncoder{
	private static final Log LOGGER = LogFactory.getLog(DefaultPasswordEncoderService.class);
	private PasswordEncoder passwordEncoder;
	
	@Override
	public String encode(CharSequence password) {
		String encodedPassword = this.passwordEncoder.encode(password);
		
		LOGGER.info("Encoding Password="+password+">>> encodedPassword="+encodedPassword);
		
		return encodedPassword;
	}
	
	@Override
	public boolean matches(final CharSequence rawPassword , final String encodedPassword) {
		LOGGER.info("Comparing encoding Password="+rawPassword+">>> encodedPassword="+encodedPassword);
		
		boolean result = this.passwordEncoder.matches(rawPassword, encodedPassword);
		
		LOGGER.info("Matched="+result);
		
		return result;
	}

	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

}
