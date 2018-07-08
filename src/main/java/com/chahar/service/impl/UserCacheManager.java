package com.chahar.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;

public class UserCacheManager  implements UserCache{
	
	private Map<String,UserDetails> userCache = new HashMap<String,UserDetails>();
	
	@Override
	public UserDetails getUserFromCache(String username) {
		return userCache.get(username);
	}

	@Override
	public void putUserInCache(UserDetails user) {
		userCache.put(user.getUsername(), user);
	}

	@Override
	public void removeUserFromCache(String username) {
		userCache.remove(username);
	}
	
}
