package com.service.vix.controller;

import org.springframework.context.annotation.Description;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is used as a handler that handle all test requests (ONLY FOR
 * TESTING)
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController extends BaseController {

	/**
	 * This method is used to handle request for all and also access for all roles.
	 * If user havn't any role, also access this request
	 * 
	 * @author hemantr
	 * @date May 31, 2023
	 * @return String
	 * @return
	 * @exception Description
	 */
	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}

	/**
	 * This method is used to handle request for super admin and also access only
	 * for super admin role. If user havn't super admin role then they are not able
	 * to access this url
	 * 
	 * @author hemantr
	 * @date May 31, 2023
	 * @return String
	 * @return
	 * @exception Description
	 */
	@GetMapping("/super_admin")
	@PreAuthorize("hasRole('SUPER_ADMIN')")
	public String superAdminAccess() {
		return "Super Admin Board.";
	}

	/**
	 * This method is used to handle request for user and also access only for user
	 * role. If user havn't user role then they are not able to access this url.But
	 * if they have any role then they wil able to access this url
	 * 
	 * @author hemantr
	 * @date May 31, 2023
	 * @return String
	 * @return
	 * @exception Description
	 */
	@GetMapping("/user")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
	public String userAccess() {
		return "User Content.";
	}

	/**
	 * This method is used to handle request for mod and also access only for mod
	 * role. If user havn't mod role then they are not able to access this url
	 * 
	 * @author hemantr
	 * @date May 31, 2023
	 * @return String
	 * @return
	 * @exception Description
	 */
	@GetMapping("/mod")
	@PreAuthorize("hasRole('MODERATOR')")
	public String moderatorAccess() {
		return "Moderator Board.";
	}

	/**
	 * This method is used to handle request for admin and also access only for
	 * admin role. If user havn't admin role then they are not able to access this
	 * url
	 * 
	 * @author hemantr
	 * @date May 31, 2023
	 * @return String
	 * @return
	 * @exception Description
	 */
	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return "Admin Board.";
	}
}