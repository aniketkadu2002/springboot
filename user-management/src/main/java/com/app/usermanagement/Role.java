package com.app.usermanagement;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Role {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int roleId;

	private String roleName;

/*	@ManyToOne(cascade = CascadeType.ALL)
	@JoinTable(name = "roleusermapping", joinColumns = @JoinColumn(name = "role_id", referencedColumnName="roleId"), 
	inverseJoinColumns = @JoinColumn(name = "person_id", referencedColumnName="personId"))*/
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "person_id", nullable  = false)	
	private Person person;

	public void setPerson(Person person) {
		this.person = person;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	
}