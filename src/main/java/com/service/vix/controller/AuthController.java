package com.service.vix.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.LoginRequest;
import com.service.vix.dto.SignupRequest;
import com.service.vix.models.User;
import com.service.vix.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@Slf4j
@RequestMapping("/api/auth")
public class AuthController extends BaseController {

	@Autowired
	private UserService userService;

	/**
	 * This method is used as a handler that handle signin/login request
	 * 
	 * @author rodolfopeixoto
	 * @date May 30, 2023
	 * @return String
	 * @param loginRequest
	 * @param httpSession
	 * @param model
	 * @return
	 * @exception Description
	 */
	@PostMapping("/signin")
	public String authenticateUser(@ModelAttribute("loginRequest") LoginRequest loginRequest, HttpSession httpSession,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Model model,
			Principal principal) {
		log.info("Enter inside AuthController.authenticateUser() method");
		CommonResponse<String> loginUserResponse = this.userService.loginUser(loginRequest, httpSession,
				httpServletRequest, httpServletResponse, model);
		log.info("Got login response..");
		return loginUserResponse.getData();
	}

	/**
	 * This method used as a handler that handle signup/register request
	 * 
	 * @author rodolfopeixoto
	 * @date May 31, 2023
	 * @return ResponseEntity<?>
	 * @param signUpRequest
	 * @return
	 * @exception Description
	 */
	@PostMapping("/signup")
	@ResponseBody
	public CommonResponse<User> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		log.info("Entered inside AuthController.registerUser() method");
		return this.userService.registerUser(signUpRequest);
	}

}
