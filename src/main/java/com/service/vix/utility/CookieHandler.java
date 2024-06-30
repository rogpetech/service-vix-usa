/* 
 * ===========================================================================
 * File Name CookieHandler.java
 * 
 * Created on Jul 3, 2023
 *
 * This code contains copyright information which is the proprietary property
 * of Chetu India Pvt. Ltd. No part of this code may be reproduced, stored or transmitted
 * in any form without the prior written permission of CHETU.
 *
 * Copyright (C) CHETU. 2023
 * All rights reserved.
 *
 * Modification history:
 * $Log: CookieHandler.java,v $
 * ===========================================================================
 */
package com.service.vix.utility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CookieHandler {

	@Value("${jwt.expiration.time.ms}")
	private int jwtExpirationMs;

	public void addTokenToCookie(String token, HttpServletResponse response) {
		Cookie cookie = new Cookie("jwtToken", token);
		cookie.setPath("/");
		cookie.setMaxAge(jwtExpirationMs);
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
	}

	public void addValueToCookie(String value, HttpServletResponse response) {
		Cookie cookie = new Cookie(value, value);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
	}

	public String getTokenFromCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("jwtToken")) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	public void removeTokenFromCookie(HttpServletResponse response) {
		Cookie cookie = new Cookie("jwtToken", null);
		cookie.setPath("/");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}
}
