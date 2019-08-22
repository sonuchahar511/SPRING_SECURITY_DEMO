package com.chahar.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.CacheMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.chahar.entity.Role;
import com.chahar.entity.User;
import com.chahar.service.UserService;

public class DefaultUserService implements UserService,UserDetailsService{
	private static final Log LOGGER = LogFactory.getLog(DefaultUserService.class);
	private SessionFactory sessionFactory;
	
	@Override
	public boolean saveUser(User user) {
		boolean isCustomerAdded=false;
		
		Session session = null;
	    Transaction tx=null;
	    
	    try {
	        session = this.sessionFactory.openSession();
	        tx = session.beginTransaction();
	        
	        Object savedUserObj = session.get(User.class, user.getUsername());
	        if(savedUserObj != null ){
	        	User savedUser = (User) savedUserObj;
	        	user.addRoles(savedUser.getRoles());
	        	session.merge(user); 
	        } else{
	        	session.persist(user) ;
	        }
	        
	        tx.commit();
	        
	        isCustomerAdded=true;
	    } catch (Exception e) {
	        e.printStackTrace();
	        isCustomerAdded=false;
	    } finally {
	    	if (!tx.wasCommitted()) {
	    		tx.rollback();
	    	}//not much doing but a good practice
	     session.flush(); //this is where I think things will start working.
	     session.close();
	    }
	    
		LOGGER.info(user);
		
		return isCustomerAdded;
	}

	@Override
	public User getUserByUsername(final String username) {
		User user = null;
		try{
			Session session = this.sessionFactory.openSession();
			user = (User) session.get(User.class , username);
		}catch(Exception e){
			e.printStackTrace();
		}
		return user;
	}


	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		LOGGER.info("DefaultUserService.loadUserByUsername() called for user="+username);
		
		User user = getUserByUsername(username);
		
		return user;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> findAllUsers() {
		List<User> users = null;
		
		Session session = null;
	    try {
	        session = this.sessionFactory.openSession();
	        Query ALL_USERS_QUERY = session.createQuery("FROM User");
	        users = ALL_USERS_QUERY.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	        users = new ArrayList<User>();
	    } finally {
	    	if(session !=null){
	    		session.close();
	    	}
	    }
	    
	    return users;
	}

	@Override
	public boolean deleteUserByUsername(String username) {
		boolean result = false;
		
		LOGGER.info("<<<<<<<<<<<<<<< Got request to delete User:"+username+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		
		User user = getUserByUsername(username);
		Session session = null;
	    try {
	        session = this.sessionFactory.openSession();
	        CacheMode cachemode = session.getCacheMode();
	        
	        session.setCacheMode(CacheMode.IGNORE);
	        LOGGER.info("user="+username+" is deleted");
	        session.delete(user);
	        session.flush();
	        
	        session.setCacheMode(cachemode);
	    } catch (Exception e) {
	        e.printStackTrace();
	        
	    } finally {
	    	if(session !=null){
	    		session.close();
	    	}
	    }
		
		return result;
	}

	@Override
	public List<String> findAllRoles()
	{
		
		List<Role> roles = null;
		
		Session session = null;
	    try {
	        session = this.sessionFactory.openSession();
	        Query ALL_ROLES_QUERY = session.createQuery("FROM Role");
	        roles = ALL_ROLES_QUERY.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	        roles = new ArrayList<Role>();
	    } finally {
	    	if(session !=null){
	    		session.close();
	    	}
	    }
	    
	    List<String> userRoles = new ArrayList<String>();
	    for(Role role : roles){
	    	userRoles.add(role.getName());
	    }
	    
	    return userRoles;
	}

}
