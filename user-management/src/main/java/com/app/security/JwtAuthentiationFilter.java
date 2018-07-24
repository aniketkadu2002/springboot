package com.app.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.app.usermanagement.Person;
import com.app.usermanagement.PersonService;
import com.app.usermanagement.Role;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

public class JwtAuthentiationFilter extends OncePerRequestFilter {

	@Autowired
	PersonService personService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
        String header = req.getHeader(JwtConstants.HEADER_STRING);
        String username = null;
        String authToken = null;
        if (header != null && header.startsWith(JwtConstants.TOKEN_PREFIX)) {
            authToken = header.replace(JwtConstants.TOKEN_PREFIX,"");
            try {
                username = jwtTokenUtil.getUsernameFromToken(authToken);
            } catch (IllegalArgumentException e) {
                logger.error("an error occured during getting username from token", e);
            } catch (ExpiredJwtException e) {
                logger.warn("the token is expired and not valid anymore", e);
            } catch(SignatureException e){
                logger.error("Authentication Failed. Username or Password not valid.");
            }
        } else {
            logger.warn("couldn't find bearer string, will ignore the header");
        }
        
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            Person userDetails = personService.getPersonByUserName(username);

            if (jwtTokenUtil.validateToken(authToken, userDetails)) {
				Set<Role> userRoles = userDetails.getRoles();						
				//List<Role> userRoles = roleService.findRoleByPerson(userCred);
				
				ArrayList<String> userRolesNames = new ArrayList<String>();
				for(Role userRole : userRoles) {
					userRolesNames.add(JwtConstants.ROLE_PREFIX + userRole.getRoleName());
				}
				
                UsernamePasswordAuthenticationToken authentication = 
                		new UsernamePasswordAuthenticationToken(userDetails, null, 
                				AuthorityUtils.createAuthorityList(userRolesNames.toArray(new String[userRolesNames.size()])));
                //Arrays.asList(new SimpleGrantedAuthority("USER")));
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                logger.info("authenticated user " + username + ", setting security context");
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(req, response);
	}

}
