package com.service.vix.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.service.vix.dto.Message;
import com.service.vix.dto.SchedulerParameterHolder;
import com.service.vix.utility.CookieHandler;
import com.service.vix.utility.JwtUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is used as authentication entry point if some exception occuring
 * within spring security then it will be triggered
 */
@Component
@Slf4j
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

	@Autowired
	private Environment env;
	@Autowired
	private SchedulerParameterHolder parameterHolder;
	
	@Autowired
	private CookieHandler cookieHandler;
	@Autowired
	private JwtUtils jwtUtils;


	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		log.info("Entered inside AuthEntryPointJwt.commence() method");
		
		HttpSession httpSession = request.getSession();

		log.info("Give session object to scheduler parameter holder.");
		parameterHolder.setSchedulerParameter(httpSession);
		log.info("Going to fetch token from session");
		
		String tokenFromCookie = cookieHandler.getTokenFromCookie(request);
        
        if (tokenFromCookie == null) {
             log.info("No JWT Token found in session. Means User is an unauthenticated user.");
             httpSession.setAttribute("message", new Message(env.getProperty("unauthenticated.user"), "error"));
             httpSession.setMaxInactiveInterval(60);
             response.sendRedirect("/login");
        }else if(tokenFromCookie != null && !jwtUtils.validateJwtToken(tokenFromCookie)) {
        	log.info("Session Time Out. Please login again");
            httpSession.setAttribute("message", new Message(env.getProperty("jwt.session.timeout"), "error"));
            httpSession.setMaxInactiveInterval(60);
            response.sendRedirect("/login");
        }
}
}