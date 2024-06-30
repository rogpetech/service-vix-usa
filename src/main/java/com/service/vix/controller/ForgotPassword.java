/* 
 * ===========================================================================
 * File Name ForgotPassword.java
 * 
 * Created on Nov 1, 2023
 * ===========================================================================
 */
/**
* Class Information
* @author ritiks
* @version 1.2 - Nov 1, 2023
*/
package com.service.vix.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.service.vix.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * This method is used to handle all the requests that are coming for
 * Forgot-Password
 */
@Controller
@RequestMapping("/forgot-password")
public class ForgotPassword extends BaseController {

	@Autowired
	private UserService userService;

	/**
	 * This method is used to open page
	 * 
	 * @author ritiks
	 * @date Nov 1, 2023
	 * @return String
	 * @return
	 * @exception Description
	 */
	@GetMapping("/")
	public String forgotPasswordPage() {
		return "/password/forgot-password";
	}

	/**
	 * This method is used to send generated password on given email
	 * 
	 * @author ritiks
	 * @date Nov 1, 2023
	 * @return String
	 * @param request
	 * @param model
	 * @return
	 * @throws MessagingException
	 * @exception Description
	 */
	@PostMapping("/update-password")
	public String updatePassword(HttpServletRequest request, HttpSession httpSession) throws MessagingException {
		this.userService.forgotPassword(request, httpSession);
		return "login";
	}

}
