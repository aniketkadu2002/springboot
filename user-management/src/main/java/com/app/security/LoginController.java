package com.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.usermanagement.Person;
import com.app.usermanagement.PersonService;


@RestController
public class LoginController {
	
	@Autowired
	private PersonService personService;
	
	@Autowired
    private AuthenticationManager authenticationManager;
	
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

	@RequestMapping(method=RequestMethod.POST, value ="/generate-token")
	public ResponseEntity<JwtAuthenticationResponse> register(@RequestBody LoginUser loginUser) throws AuthenticationException {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final Person person = personService.getPersonByUserName(loginUser.getUsername());
        final String token = jwtTokenUtil.generateToken(person);
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }	
	
}

class LoginUser{
	
	String username;
	String password;
	
	public LoginUser() {
		super();
	}
	
	public LoginUser(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
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
}