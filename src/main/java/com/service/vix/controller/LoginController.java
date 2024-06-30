package com.service.vix.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.service.vix.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class LoginController extends BaseController {

	@Autowired
	private UserService userService;

	/**
	 * This method is used as a handler that handle welcome request or open welcome
	 * page
	 * 
	 * @author hemantr
	 * @date May 31, 2023
	 * @return String
	 * @param principal
	 * @return
	 * @exception Description
	 */
	@GetMapping("/welcome")
	@ResponseBody
	public String welcome(Principal principal) {
		return "welcome sir    |   " + principal.getName();
	}

	/**
	 * This method is used as a handler that handle login request or open login page
	 * 
	 * @author hemantr
	 * @date May 31, 2023
	 * @return String
	 * @return
	 * @exception Description
	 */
	@GetMapping("/login")
	public String loginPage(Principal principal, HttpSession httpSession, Model model) {
		log.info("Enter inside PageController.loginPage() method.");
		if (principal != null)
			return userService.renderUserIfAlreadyLoggedIn(principal, httpSession);
		Boolean isUserInvalid = (Boolean) httpSession.getAttribute("isUserInvalid");
		if (isUserInvalid != null && isUserInvalid) {
			model.addAttribute("emailUsername", httpSession.getAttribute("emailUsername"));
			model.addAttribute("password", httpSession.getAttribute("password"));
		}
		return "login";
	}

}
