package com.app.usermanagement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

	@Autowired
	private PersonRepository personRepository;
	
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	public Person saveToDb(Person user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return personRepository.save(user);
	}

	public List<Person> getAllUsers() {
		List<Person> personList = new ArrayList<Person>();
		personRepository.findAll().forEach(personList::add);
		
		//we don't want to display the password.
		personList.forEach(person->{
			person.setPassword("");
		});
		
		return personList;		
	}
	
	public Person getPersonByUserName(String userName) {
		List<Person> personList = personRepository.findByUserName(userName);
		return personList.size()>0?personList.get(0):null;
	}
}
