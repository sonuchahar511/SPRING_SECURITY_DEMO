package com.chahar.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;


/*
 * 	this represents a low level, granular privilege/authority in the system
 */

@Entity
@Table(name="priviledges")
public class Priviledge implements GrantedAuthority {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="name")
	private String name;
	
	@Column(name="description")
	private String description;
	
	@ManyToMany(mappedBy="priviledges")
	private List<Role> roles;
	
	public Priviledge(String name){
		this.name=name;
	}
	
	public Priviledge(){}
	
	public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Priviledge [name=");
        builder.append(name);
        builder.append("]");
        return builder.toString();
    }

	@Override
	public String getAuthority() {
		return name;
	}

}
