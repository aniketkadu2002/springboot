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
		
		@Autowired
	    private JwtAuthenticationEntryPoint unauthorizedHandler;
		
		private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		@Override
		protected void configure(HttpSecurity http) throws Exception{
			
			/** Basic HTTP Authentication ***/
			
			/*http.cors().and().csrf().disable()
			.authorizeRequests().anyRequest().authenticated().and().httpBasic().and().authorizeRequests()
			.antMatchers("/users").hasRole("CADMIN")
			.antMatchers("/").access("hasRole('CADMIN') and hasRole('DBA')");*/
			
			/*http.cors().and().csrf().disable()
			.authorizeRequests()
			.antMatchers("/generate-token").permitAll()
			.antMatchers("/users").hasRole("USER")
			.anyRequest().authenticated().and()
			.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
			
			http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);*/
			
			http.cors().and().csrf().disable().
			authorizeRequests()
			.antMatchers("/generate-token").permitAll()
			.antMatchers("/users").hasRole("CADMIN")
			.anyRequest().authenticated().and()
			//.anyRequest().hasAuthority("USER").and()
			.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
			
			http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
			
			/*** permit everyone **/
			/*http.csrf().disable();
	        http.authorizeRequests().antMatchers("/").permitAll();*/
			
		}	
		
	    @Override
	    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder);
	    }
	    
		@Bean
		public JwtAuthentiationFilter authenticationTokenFilterBean() throws Exception {
			return new JwtAuthentiationFilter();
		}	    

		/*@Override
	    public void configure(WebSecurity web) throws Exception {
	        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
	    }*/
		
		@Override
		@Bean
		public AuthenticationManager authenticationManagerBean() throws Exception {
			return super.authenticationManagerBean();
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
						
						Set<Role> userRoles = userCred.getRoles();						
						//List<Role> userRoles = roleService.findRoleByPerson(userCred);
						
						ArrayList<String> userRolesNames = new ArrayList<String>();
						for(Role userRole : userRoles) {
							userRolesNames.add(JwtConstants.ROLE_PREFIX + userRole.getRoleName());
						}
						
						User usr = new User(userCred.getUserName(),userCred.getPassword(), true, true, true, true,
								AuthorityUtils.createAuthorityList(userRolesNames.toArray(new String[userRolesNames.size()])));
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

