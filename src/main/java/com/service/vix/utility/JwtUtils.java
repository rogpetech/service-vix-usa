package com.service.vix.utility;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Description;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.service.vix.service.impl.UserDetailsImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is used as Utility class that handles all the methods for JWT
 * token
 */
@Component
@Slf4j
public class JwtUtils {

	@Value("${jwt.secret.key}")
	private String jwtSecret;

	@Value("${jwt.expiration.time.ms}")
	private int jwtExpirationMs;

	/**
	 * This method is used to generate JWT token by given authentication
	 * 
	 * @author ritiks
	 * @date May 31, 2023
	 * @return String
	 * @param authentication
	 * @return
	 * @exception Description
	 */
	public String generateJwtToken(Authentication authentication) {
		log.info("Enter inside JwtUtils.generateJwtToken() method");
		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

		// Get the necessary user details from the UserPrincipal object
		String username = userPrincipal.getUsername();
		// ... Get other user details as needed

		// Build the JWT claims
		Claims claims = Jwts.claims().setSubject(username);
		// ... Set other claims as needed

		// Generate the JWT token
		return Jwts.builder().setClaims(claims).signWith(key(), SignatureAlgorithm.HS256).setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)).compact();
	}

	/**
	 * This method is used to generate Secret Key for build JWT token. It will be
	 * used our secret key that access from application.properties file
	 * 
	 * @author ritiks
	 * @date May 31, 2023
	 * @return Key
	 * @return
	 * @exception Description
	 */
	private Key key() {
		log.info("Enter inside JwtUtils.key() method");
		byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	/**
	 * This method is used to extract username from given JWT tooken
	 * 
	 * @author ritiks
	 * @date May 31, 2023
	 * @return String
	 * @param token
	 * @return
	 * @exception Description
	 */
	public String getUserNameFromJwtToken(String token) {
		log.info("Enter inside JwtUtils.getUserNameFromJwtToken() method.");
		return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody().getSubject();
	}

	/**
	 * This method is used to validate JWT token
	 * 
	 * @author ritiks
	 * @date May 31, 2023
	 * @return boolean
	 * @param authToken
	 * @return
	 * @exception Description
	 */
	public boolean validateJwtToken(String authToken) {
		log.info("Enter inside JwtUtils.validateJwtToken( method");
		if (TokenRevocationList.getInstance().isTokenRevoked(authToken)) {
			return false;
		}
		try {
			Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
			return true;
		} catch (MalformedJwtException e) {
			log.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			log.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			log.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			log.error("JWT claims string is empty: {}", e.getMessage());
		}
		return false;
	}
}
