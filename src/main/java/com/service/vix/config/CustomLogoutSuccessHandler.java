package com.service.vix.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.service.vix.dto.Message;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/**
 * Thic class is used to handle custom logout functionality
 */
@Component
@Slf4j
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

	@Autowired
	private Environment env;

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		log.info("Enter inside CustomLogoutSuccessHandler.onLogoutSuccess() method ");
		HttpSession session = request.getSession();
		session.setAttribute("message", new Message(env.getProperty("user.logout.successfully"), "success"));
		log.info("Request comming for logout");
		response.sendRedirect("/login");
	}
}
