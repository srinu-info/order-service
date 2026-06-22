package com.web.store.order.security;


import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;


@Component
public class JwtUtil {
private static final String SECRET_KEY="veryconfidentialsecretekeyveryconfidentialsecretekey123";
//@Value("${jwt.secrete}")
//private static String secrete; 

private static final SecretKey key=Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

public String generateToken(String email, String role) {
    return Jwts.builder()
            .setSubject(email)
            .claim("role", role)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
}


public String extractUserName(String token) {
	return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();	
}
public String extractRole(String token) {
	return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("role",String.class)	;
}
public boolean validateToken(String token) {
	try {
		Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
		return true;
	}
	catch(Exception e){
		return false;
	}
}
}