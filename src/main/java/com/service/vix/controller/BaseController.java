package com.service.vix.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.core.env.Environment;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.service.vix.dto.Message;
import com.service.vix.service.UserService;
import com.service.vix.utility.CookieHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is used as Parent class for all the controllers
 */
@ControllerAdvice
@Slf4j
public class BaseController {

	@Autowired
	private Environment env;
	@Autowired
	private UserService userService;
	@Autowired
	private CookieHandler cookieHandler;

	/**
	 * This method is used to handle exceptions for all the controllers
	 * 
	 * @author ritiks
	 * @date Jun 15, 2023
	 * @return ResponseEntity<String>
	 * @param ex
	 * @return
	 * @exception Description
	 */
	@ExceptionHandler(Exception.class)
	public String handleException(Exception ex, Model model, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Principal principal) {
		log.info("Enter inside BaseController.handleException() method.");
		HttpSession httpSession = httpServletRequest.getSession();
		log.info("Going to fetch token from session");
		String jwtToken = this.cookieHandler.getTokenFromCookie(httpServletRequest);
		if (jwtToken == null) {
			log.info("No JWT Token found in session. Means User is an unauthenticated user.");
			httpSession.setAttribute("message", new Message(env.getProperty("something.went.wrong"), "danger"));
			log.error("An exception occure :: " + ex.getMessage());
			return "redirect:/login";
		} else {
			log.info("JWT Token found in cookies. Means User is an authenticated user.");
			String renderURL = this.userService.renderUserIfAlreadyLoggedIn(principal, httpSession);
			httpSession.removeAttribute("message");
			httpSession.setAttribute("message", new Message(env.getProperty("something.went.wrong"), "danger"));
			log.error("An exception occure :: " + ex.getMessage());
			return renderURL;
		}
	}

}
