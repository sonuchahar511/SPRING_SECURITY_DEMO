package com.chahar.web.dto.populator;

import java.util.HashSet;
import java.util.Set;

import com.chahar.entity.Role;
import com.chahar.entity.User;
import com.chahar.web.dto.UserDto;

public class UserReversePopulator implements ReversePopulator<UserDto, User>{

	@Override
	public User populate(final UserDto source) {
		final User target = new User();
		target.setUsername(source.getUsername());
		target.setPassword(source.getPassword());
		target.setEmail(source.getEmail());
		
		target.setEnabled(true);
		target.setAccountNonExpired(true);
		target.setAccountNonLocked(true);
		target.setCredentialsNonExpired(true);
		
		Set<Role> auths = new HashSet<Role>();
		auths.add(new Role(source.getRole()));
		target.setAuthorities(auths);
		
		return target;
	}

}
