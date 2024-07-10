/* 
 * ===========================================================================
 * File Name ProfileController.java
 * 
 * Created on Aug 16, 2023
 *
 * This code contains copyright information which is the proprietary property
 * of Chetu India Pvt. Ltd. No part of this code may be reproduced, stored or transmitted
 * in any form without the prior written permission of CHETU.
 *
 * Copyright (C) CHETU. 2023
 * All rights reserved.
 *
 * Modification history:
 * $Log: ProfileController.java,v $
 * ===========================================================================
 */
package com.service.vix.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.UserDTO;
import com.service.vix.enums.ERole;
import com.service.vix.service.ProfileService;
import com.service.vix.utility.Countries;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/**
 * This controller is used as Profile Controller
 */
@Slf4j
@Controller
@RequestMapping("/profile")
public class ProfileController extends BaseController {

	@Autowired
	private ProfileService profileService;

	/**
	 * This method is used to Show User profile Details
	 * 
	 * @author rodolfopeixoto
	 * @date Aug 16, 2023
	 * @return
	 */
	@GetMapping("/details")
	public String userProfileDetails(Principal principal, Model model) {
		log.info("Enter inside ProfileController.userProfileDetails() method.");
		CommonResponse<UserDTO> loggedInUserDetails = this.profileService.loggedInUserDetails(principal);
		model.addAttribute("user", loggedInUserDetails.getData());
		model.addAttribute("countries", Countries.countries);
		model.addAttribute("roles", ERole.values());
		return "/profile/user_profile";
	}

	/**
	 * This method is used to change password
	 * 
	 * @author rodolfopeixoto
	 * @date Nov 6, 2023
	 * @return String
	 * @param request
	 * @param httpSession
	 * @param model
	 * @param principal
	 * @return
	 * @exception Description
	 */
	@PostMapping("/change-password")
	public String changePassword(HttpServletRequest request, HttpSession httpSession, Model model,
			Principal principal) {
		this.profileService.changePassword(request, httpSession);
		CommonResponse<UserDTO> loggedInUserDetails = this.profileService.loggedInUserDetails(principal);
		model.addAttribute("user", loggedInUserDetails.getData());
		model.addAttribute("countries", Countries.countries);
		model.addAttribute("roles", ERole.values());
		return "profile/edit-profile";
	}

}
