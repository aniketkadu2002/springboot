package com.app.usermanagement;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.app.usermanagement.Role;

import java.lang.String;


@Repository
public interface RoleRepository extends CrudRepository<Role, Integer>{
	List<Role> findByRoleName(String rolename);
	List<Role> findByRoleId(int roleId);
	List<Role> findByPerson(Person person);
}

