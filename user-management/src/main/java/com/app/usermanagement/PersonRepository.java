package com.app.usermanagement;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.app.usermanagement.Person;

import java.lang.String;
import java.util.Set;


@Repository
public interface PersonRepository extends CrudRepository<Person, Integer>{
	List<Person> findByName(String name); 
	List<Person> findByRoles(Set roles);
	List<Person> findByUserName(String username);
	
}
