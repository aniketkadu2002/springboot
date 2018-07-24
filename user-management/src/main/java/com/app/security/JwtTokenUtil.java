package com.app.security;

import java.io.Serializable;
import java.util.Date;
import java.util.function.Function;
import org.springframework.stereotype.Component;

import com.app.usermanagement.Person;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = 1L;

	public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public  <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return (T) claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(JwtConstants.SIGNING_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(Person user) {
        return doGenerateToken(user.getUserName());
    }

    private String doGenerateToken(String subject) {

        Claims claims = Jwts.claims().setSubject(subject);
        //claims.put("scopes", AuthorityUtils.createAuthorityList("ROLE_CADMIN"));//Arrays.asList(new SimpleGrantedAuthority("USER")));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer("http://localhost")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JwtConstants.ACCESS_TOKEN_VALIDITY_SECONDS*1000))
                .signWith(SignatureAlgorithm.HS256, JwtConstants.SIGNING_KEY)
                .compact();
    }

    public Boolean validateToken(String token, Person userDetails) {
        final String username = getUsernameFromToken(token);
        return (
              username.equals(userDetails.getUserName())
                    && !isTokenExpired(token));
    }

}