package com.app.usermanagement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonController {
	
	@Autowired
	PersonService personService;
	
	@Autowired
	RoleService roleService;
	
	@RequestMapping("/users")
	public  List<Person> getAllColleges() {
		return personService.getAllUsers();
		//new ArrayList<Person>();
		
	}
	
	@RequestMapping("/users/{username}")
	public Person getUserByUserName(@PathVariable String username) {
		return personService.getPersonByUserName(username);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/users")
	public void addUsers(@RequestBody Person user) {
		Set<Role> dbRoleList = new HashSet<Role>();
		Set<Role> roleList = user.getRoles();
		roleList.forEach(role-> dbRoleList.add(roleService.findRolebyRoleName(role.getRoleName())));
		
		//create user using empty role list.
		user.setRoles(new HashSet<Role>());				
		Person createdUser = personService.saveToDb(user);
		
		//now populate the roles on user object.		
		if(dbRoleList.size()>0) {
			createdUser.setRoles(dbRoleList);
			personService.saveToDb(createdUser);
		}
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/users/{personId}")
	public void updateUser(@RequestBody Person user, @PathVariable int personId) {
		user.setPersonId(personId);
		personService.saveToDb(user);
	}
}
