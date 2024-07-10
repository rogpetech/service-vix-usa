/* 
 * ===========================================================================
 * File Name WorkForceController.java
 * 
 * Created on Aug 9, 2023
 *
 * This code contains copyright information which is the proprietary property
 * of Chetu India Pvt. Ltd. No part of this code may be reproduced, stored or transmitted
 * in any form without the prior written permission of CHETU.
 *
 * Copyright (C) CHETU. 2023
 * All rights reserved.
 *
 * Modification history:
 * $Log: WorkForceController.java,v $
 * ===========================================================================
 */
package com.service.vix.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.service.vix.dto.RoleDTO;
import com.service.vix.dto.StaffDTO;
import com.service.vix.enums.Permissions;
import com.service.vix.service.RoleService;
import com.service.vix.service.StaffService;
import com.service.vix.utility.USStates;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/workforce")
public class WorkForceController extends BaseController {

	@Autowired
	private RoleService roleService;

	@Autowired
	private StaffService staffService;

	@Value("${role.organization}")
	private String organizationRole;

	/**
	 * This Controller is used to open Add work force form
	 * 
	 * @author rodolfopeixoto
	 * @date Aug 9, 2023
	 * @param model
	 * @return
	 */
	@GetMapping("/add")
	public String addWorkForceForm(Model model) {
		log.info("Enter inside WorkForceController.addWorkForceForm(). method");
		List<RoleDTO> roles = this.roleService.getRoles().getData();
		roles.removeIf(role -> (role.getName().equals("ORGANIZATION")));
		model.addAttribute("roles", roles);
		model.addAttribute("usStates", USStates.usStates);
		return "workforce/add-staff";
	}

	/**
	 * This method is used to show staff listing page
	 * 
	 * @author rodolfopeixoto
	 * @date Aug 9, 2023
	 * @param model
	 * @param principal
	 * @return
	 */
	@GetMapping("/staffList")
	public String showWorkForceList(Model model, Principal principal) {
		log.info("Enter inside WorkForceController.addWorkForceForm(). method");
		List<RoleDTO> roles = this.roleService.getRoles().getData();
		roles.removeIf(rd -> (rd.getName().equals(organizationRole)));
		model.addAttribute("roles", roles);
		model.addAttribute("permissions", Permissions.getCapitalisePermissions());
		List<StaffDTO> staffs = this.staffService.getAllStaff(principal).getData();
		staffs.removeIf(staff -> (staff.getUserDTO().getUsername().equals(principal.getName())));
		model.addAttribute("allStaff", staffs);
		return "workforce/staffList";
	}

}
