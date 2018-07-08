package com.chahar.entity.builder;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.userdetails.UserDetails;

import com.chahar.entity.Role;
import com.chahar.entity.User;
import com.chahar.util.ProjectConstant;

public class UserDetailBuilder {

	public static UserDetails  buildUserDetail(final String username) {
		User user = new User(username,"123456",username+"@gmail.com",true,true,true,true);
		Set<Role> auths = new HashSet<Role>();
		auths.add(new Role(ProjectConstant.ROLE_PREFIX+"USER"));
		user.setAuthorities(auths);
		
		return user;
	}
	
	public static User getUser(final String username){
		User  user = (User) buildUserDetail(username);
        return user;
	}
	
}
