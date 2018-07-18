package com.app.usermanagement;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
public class Person {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int personId;

	private String userName;

	private String password;
	
	private String name;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "roleusermapping", joinColumns = @JoinColumn(name = "person_id", referencedColumnName="personId"), 
	inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName="roleId"))
	private Set<Role> roles;
	
	public int getPersonId() {
		return personId;
	}

	public void setPersonId(int personId) {
		this.personId = personId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
