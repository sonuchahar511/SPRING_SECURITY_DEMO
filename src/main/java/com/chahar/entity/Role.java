package com.chahar.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

/*
This represents the high level roles of the user in the system; each role will have a set of low level privileges
*/

@Entity
@Table(name="roles")
public class Role implements GrantedAuthority {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="name")
	private String name;
	
	@Column(name="description")
	private String description;
	
	@ManyToMany(cascade=CascadeType.MERGE,fetch=FetchType.EAGER,targetEntity=Priviledge.class)
	@JoinTable(name="role_priviledges",joinColumns=@JoinColumn(name="rolename"), inverseJoinColumns=@JoinColumn(name="priviledgename"))
    private List<Priviledge> priviledges;
	
	@ManyToMany(targetEntity=User.class,mappedBy="roles")
	private List<User> users;
	
    public Role(String name) {
		super();
		this.name = name;
	}
    
    public Role() {}
    
	@Override
	public String getAuthority() {
		return this.name;
	}
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<Priviledge> getPriviledges() {
        return priviledges;
    }
 
    public void setPriviledges(List<Priviledge> priviledges) {
        this.priviledges = priviledges;
    }
    
    public boolean equals(Object obj) {
    	if(obj == null) return false;
    	if(this == obj) return true;
    	if(!(obj instanceof Role) ) return false;
    	
    	Role roleObj = (Role) obj;
    	if(!this.getName().equals(roleObj.getName()))
    		return false;
    	
    	return true;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Role [name=");
        builder.append(name);
        /*builder.append(", privileges=");
        builder.append(priviledges);*/
        builder.append("]");
        return builder.toString();
    }
}
