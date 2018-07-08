package com.chahar.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import com.chahar.entity.User;

public interface UserService {
	
	@PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
	public boolean saveUser(final User user);
	
	public User getUserByUsername(final String username);
	
	@PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
	public List<User> findAllUsers();
	
	//Only one user can delete itself only.
	//@PreAuthorize("#username == authentication.name")
	//Custom PermissionEvaluator will not called.
	
	//@PreAuthorize("hasPermission(#username,'ROLE_SUPER_ADMIN')")
	//Custom PermissionEvaluator will be called since hasPermission is used.
	//CustomPermissionEvaluator.hasPermission(Authentication,Object targetDomainObject=#username,Object permission='ROLE_SUPER_ADMIN')
	public boolean deleteUserByUsername(final String username);
	
	public List<String> findAllRoles();
}
