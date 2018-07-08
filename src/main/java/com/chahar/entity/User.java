package com.chahar.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.chahar.util.ProjectConstant;

@Entity
@Table(name="users")
public class User implements UserDetails,Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="username")
	private String username;
	
	@Column(name="password")
	private String password;
	
	@Column(name="email")
	private String email;
	
	@Column(name="enabled")
	private boolean enabled=true;
	
	@Column(name="isAccountNonExpired")
	private boolean isAccountNonExpired=true;
	
	@Column(name="isAccountNonLocked")
	private boolean isAccountNonLocked=true;
	
	@Column(name="isCredentialsNonExpired")
	private boolean isCredentialsNonExpired=true;
	
	@ManyToMany(cascade={CascadeType.MERGE},fetch=FetchType.EAGER,targetEntity=Role.class)
	@JoinTable(name="user_roles",joinColumns=@JoinColumn(name="username") , inverseJoinColumns=@JoinColumn(name="rolename"))
	private Set<Role> roles;
	
	@Transient
	private Set<Role> userRoles = null;
	
	public User(String username, String password, String email,
			boolean enabled, boolean isAccountNonExpired,boolean isAccountNonLocked, boolean isCredentialsNonExpired) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
		this.enabled = enabled;
		this.isAccountNonExpired = isAccountNonExpired;
		this.isAccountNonLocked = isAccountNonLocked;
		this.isCredentialsNonExpired = isCredentialsNonExpired;
	}
	public User(){}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	@Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof User))
            return false;
        
        User other = (User) obj;
        if(this.username == null || other.username == null)
        	return false;
        if(this.username.equals(other.username))
        	return true;
        
        return true;
    }
	@Override
	public int hashCode() {
		return username.hashCode()*1000+1;
	}
	
	 @Override
	    public String toString() {
	       /* return "User [username=" + username  
	        	   //+", password=" + password
	                + ", enabled=" + enabled 
	                + ",isAccountNonExpired="+isAccountNonExpired
	                +",isAccountNonLocked="+isAccountNonLocked
	                +",isCredentialsNonExpired"+isCredentialsNonExpired
	                +"]";*/
	         return "User [username=" + username+"]";
	    }
	 
	//Used by spring framework for authentication
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if(this.userRoles == null){
			userRoles = new HashSet<Role>();
			for(Role role : this.roles){
				//Role userRole = new Role(ProjectConstant.ROLE_PREFIX +role.getName());
				Role userRole = new Role(role.getName());
				
				userRoles.add(userRole);
			}
		}
		return this.userRoles;
	}
	@Override
	public boolean isAccountNonExpired() {
		return this.isAccountNonExpired;
	}
	@Override
	public boolean isAccountNonLocked() {
		return isAccountNonLocked;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return isCredentialsNonExpired;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setAccountNonExpired(boolean isAccountNonExpired) {
		this.isAccountNonExpired = isAccountNonExpired;
	}
	public void setAccountNonLocked(boolean isAccountNonLocked) {
		this.isAccountNonLocked = isAccountNonLocked;
	}
	public void setCredentialsNonExpired(boolean isCredentialsNonExpired) {
		this.isCredentialsNonExpired = isCredentialsNonExpired;
	}
	public void setAuthorities(Set<Role> roles) {
		this.roles = roles;
	}
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	public void addRoles(Set<Role> roles) {
		for(Role r : roles) {
			if(!this.roles.contains(r)){
				this.roles.addAll(roles);
			}
		}
		
	}
	public boolean hasPermission(Object permission) {
		boolean userHasGivenPermission = false;
		if(permission != null){
			Role role = new Role((String) permission);
			
			System.out.println("Checking Permission="+role+" in User="+this);
			
			if(this.getAuthorities().contains(role)){
				userHasGivenPermission=true;
				System.out.println("Permission="+role.getName()+" is found in User="+this.username);
			}else{
				System.out.println("Permission="+role.getName()+" NOT found in User="+this.username);
			}
		}
		
		return userHasGivenPermission;
	}
}
