package com.service.vix.controller;

import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * This class is used to render the upcoming request to related page
 */
@Controller
@RequestMapping("/dashboard")
public class ApplicationController extends BaseController {

	/**
	 * This method is used to handle the request related to super admin dashboard
	 * 
	 * @author ritiks
	 * @date May 31, 2023
	 * @return String
	 * @return
	 * @exception Description
	 */
	@GetMapping("/super")
	public String superAdminDashboard() {
		return "redirect:/super-admin-dashboard/";
	}

	/**
	 * This method is used to handle the request related to user dashboard
	 * 
	 * @author ritiks
	 * @date May 31, 2023
	 * @return String
	 * @return
	 * @exception Description
	 */
	@GetMapping("/user")
	public String userDashboard() {
		return "/user/user-dashboard";
	}

	/**
	 * This method is used to handle the request related to admin dashboard
	 * 
	 * @author ritiks
	 * @date May 31, 2023
	 * @return String
	 * @return
	 * @exception Description
	 */
	@GetMapping("/admin")
	public String adminDashboard() {
		return "/admin/admin-dashboard";
	}

	/**
	 * This method is used to handle the request related to mod dashboard
	 * 
	 * @author ritiks
	 * @date May 31, 2023
	 * @return String
	 * @return
	 * @exception Description
	 */
	@GetMapping("/mod")
	public String modDashboard() {
		return "/mod/mod-dashboard";
	}

	/**
	 * This method is used to handle the request related to organization dashboard
	 * 
	 * @author ritiks
	 * @date May 31, 2023
	 * @return String
	 * @return
	 * @exception Description
	 */
	@GetMapping("/organization")
	public String organizationDashboard() {
		return "organization/home";
	}
}
