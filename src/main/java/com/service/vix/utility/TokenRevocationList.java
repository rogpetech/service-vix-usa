package com.service.vix.utility;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * This class is used to revoked JWT token
 */
@Component
@Slf4j
public class TokenRevocationList {
	private Set<String> revokedTokens;
	private static TokenRevocationList instance;

	public TokenRevocationList() {
		this.revokedTokens = new HashSet<>();
	}

	public static synchronized TokenRevocationList getInstance() {
		log.info("Enter inside TokenRevocationList.getInstance() method");
		if (instance == null) {
			instance = new TokenRevocationList();
		}
		return instance;
	}

	public void revokeToken(String token) {
		log.info("Enter inside TokenRevocationList.revokeToken() method");
		revokedTokens.add(token);
	}

	public boolean isTokenRevoked(String token) {
		log.info("Enter inside TokenRevocationList.isTokenRevoked() method");
		return revokedTokens.contains(token);
	}
}
