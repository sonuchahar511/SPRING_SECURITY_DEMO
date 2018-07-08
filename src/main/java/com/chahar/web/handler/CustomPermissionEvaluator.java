package com.chahar.web.handler;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import com.chahar.entity.User;


public class CustomPermissionEvaluator implements PermissionEvaluator {
	private static final Log LOGGER = LogFactory.getLog(CustomPermissionEvaluator.class);
	
	@Override
	public boolean hasPermission(Authentication authentication,
			Object targetDomainObject, Object permission) {
		LOGGER.info("==============================================================================================");
		LOGGER.info("BasePermissionEvaluator.hasPermission(Authentication,"+
			"Object targetDomainObject, Object permission) called");
		
		LOGGER.info("authentication="+authentication);
		LOGGER.info("targetDomainObject="+targetDomainObject);
		LOGGER.info("permission="+permission);
		
		boolean hasPermission = ((User)authentication.getPrincipal()).hasPermission(permission);
		
		LOGGER.info("==============================================================================================");
		
		return hasPermission;
	}

	@Override
	public boolean hasPermission(Authentication authentication,
			Serializable targetId, String targetType, Object permission) {
		LOGGER.info("==============================================================================================");
		
		LOGGER.info("BasePermissionEvaluator.hasPermission(Authentication authentication,"+
			"Serializable targetId, String targetType, Object permission) called");
		
		LOGGER.info("authentication="+authentication);
		LOGGER.info("targetId="+targetId);
		LOGGER.info("targetType="+targetType);
		LOGGER.info("permission="+permission);
		
		LOGGER.info("==============================================================================================");
		
		return true;
	}
	
	
	
}