package com.service.vix.service;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.LoginRequest;
import com.service.vix.dto.SignupRequest;
import com.service.vix.models.User;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * This class is used as Service class for user of application not for Spring
 * Security User
 */
/**
 *
 */
@Component
public interface UserService {

	/**
	 * This method is used to save/register user
	 * 
	 * @author ritiks
	 * @date May 31, 2023
	 * @return CommonResponse<User>
	 * @param signupRequest
	 * @return
	 * @exception Description
	 */
	CommonResponse<User> registerUser(SignupRequest signupRequest);

	/**
	 * This method is used to login user
	 * 
	 * @author ritiks
	 * @date May 31, 2023
	 * @return CommonResponse<Object>
	 * @param loginRequest
	 * @return
	 * @exception Description
	 */
	CommonResponse<String> loginUser(LoginRequest loginRequest, HttpSession httpSession,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Model model);

	/**
	 * This method is used to get url from given role
	 * 
	 * @author ritiks
	 * @date Jun 6, 2023
	 * @return String
	 * @param strRole
	 * @return
	 * @exception Description
	 */
	String getUrlBasedOnUserRole(List<String> strRole);

	/**
	 * This method is used to rendered user if already logged in
	 * 
	 * @author ritiks
	 * @date Jun 6, 2023
	 * @return String
	 * @param principal
	 * @return
	 * @exception Description
	 */
	String renderUserIfAlreadyLoggedIn(Principal principal, HttpSession httpSession);

	/**
	 * @author ritiks
	 * @date Sep 14, 2023
	 * @return UserDTO
	 * @param userName
	 * @return
	 * @exception Description
	 */
	Map<Boolean, String> getUserByUserName(String userName);

	/**
	 * This method is used to generate a random password and send it on user's email
	 * 
	 * @author ritiks
	 * @date Nov 1, 2023
	 * @return CommonResponse<Boolean>
	 * @param httpServletRequest
	 * @param model
	 * @return
	 * @throws MessagingException
	 * @exception Description
	 */
	/**
	 * @author ritiks
	 * @date Nov 1, 2023
	 * @return CommonResponse<Boolean>
	 * @param httpServletRequest
	 * @param httpSession
	 * @return
	 * @throws MessagingException
	 * @exception Description
	 */
	CommonResponse<Boolean> forgotPassword(HttpServletRequest httpServletRequest, HttpSession httpSession)
			throws MessagingException;

}
