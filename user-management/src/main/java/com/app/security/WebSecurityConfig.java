package com.app.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.app.usermanagement.Person;
import com.app.usermanagement.PersonService;
import com.app.usermanagement.Role;
import com.app.usermanagement.RoleService;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
		
		@Autowired
		PersonService personService;
		
		@Autowired
		RoleService roleService;
		
		private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		@Override
		protected void configure(HttpSecurity http) throws Exception{
			
			
			/*http.cors().and().csrf().disable()
			.authorizeRequests().anyRequest().authenticated().and().httpBasic().and().authorizeRequests()
			.antMatchers("/users").hasRole("CADMIN");*/
			//.antMatchers("/").access("hasRole('CADMIN') and hasRole('DBA')")
			
			
			/*** permit everyone **/
			http.csrf().disable();
	        http.authorizeRequests().antMatchers("/").permitAll();
			
		}	
		
	   @Override
	    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder);
	    }

	   
		@Bean
		protected 
		UserDetailsService userDetailsService() {
			return new UserDetailsService() {
							
				@Override
				public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
					
					System.out.println("Fetching user details..." + username);
					
					Person userCred = personService.getPersonByUserName(username);
					if(userCred!=null) {
						
						//Set<Role> userRoles = userCred.getRoles();						
						List<Role> userRoles = roleService.findRoleByPerson(userCred);
						
						ArrayList<String> userRolesNames = new ArrayList<String>();
						for(Role userRole : userRoles) {
							userRolesNames.add(userRole.getRoleName());
						}
						
						User usr = new User(userCred.getUserName(),userCred.getPassword(), true, true, true, true,
								AuthorityUtils.createAuthorityList("CADMIN"));
						return usr;
						
					}else {
						System.out.println("cannot find require user in database");
					}
					
					// TODO Auto-generated method stub
					return null;
				}
			};
	}
}

