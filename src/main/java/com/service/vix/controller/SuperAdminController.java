package com.service.vix.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

/**
 * This is a class that is used for super admin dashboard
 */
@Controller
@Slf4j
@RequestMapping(("/super-admin-dashboard"))
public class SuperAdminController extends BaseController {

	@GetMapping("/")
	public String superAdminDashboard() {
		log.info("Enter inside SuperAdminController.superAdminDashboard() method.");
		return "super/super-admin-dashboard";
	}

}
