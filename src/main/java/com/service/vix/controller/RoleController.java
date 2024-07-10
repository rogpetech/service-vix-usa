/* 
 * ===========================================================================
 * File Name RoleController.java
 * 
 * Created on Aug 18, 2023
 *
 * This code contains copyright information which is the proprietary property
 * of Chetu India Pvt. Ltd. No part of this code may be reproduced, stored or transmitted
 * in any form without the prior written permission of CHETU.
 *
 * Copyright (C) CHETU. 2023
 * All rights reserved.
 *
 * Modification history:
 * $Log: RoleController.java,v $
 * ===========================================================================
 */
package com.service.vix.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.RoleDTO;
import com.service.vix.service.RoleService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/role")
public class RoleController extends BaseController {

	@Autowired
	private RoleService roleService;

	/**
	 * This method is used as controller method to save Role
	 * 
	 * @author rodolfopeixoto
	 * @date Aug 19, 2023
	 * @param roleDTO
	 * @return
	 */
	@ResponseBody
	@GetMapping("/addRole/{roleName}")
	public RoleDTO processAddRole(@PathVariable String roleName, HttpSession httpSession) {
		log.info("Enter inside RoleController.processAddRole() method.");
		RoleDTO roleDTO = this.roleService.saveRole(new RoleDTO(roleName), httpSession).getData();
		return roleDTO;
	}

	/**
	 * This method is used as controller method to Delete Role
	 * 
	 * @author rodolfopeixoto
	 * @date Aug 19, 2023
	 * @param roleId
	 * @param httpSession
	 * @return
	 */
	@GetMapping("/deleteRole/{roleId}")
	public String removeRole(@PathVariable Long roleId, HttpSession httpSession) {
		log.info("Enter inside RoleController.removeRole() method.");
		this.roleService.removeRole(roleId, httpSession);
		return "redirect:/workforce/staffList";
	}

	/**
	 * This method is used to get Role Details by id
	 * 
	 * @author rodolfopeixoto
	 * @date Aug 19, 2023
	 * @return RoleDTO
	 * @param roleId
	 * @return
	 * @exception Description
	 */
	@GetMapping("/get/{roleId}")
	@ResponseBody
	public RoleDTO roleDetailsById(@PathVariable Long roleId) {
		return this.roleService.getRoleById(roleId).getData();
	}

	/**
	 * This method is used to update Role
	 * 
	 * @author rodolfopeixoto
	 * @date Aug 19, 2023
	 * @return CommonResponse<RoleDTO>
	 * @param httpServletRequest
	 * @return
	 * @return
	 * @exception Description
	 */
	@GetMapping("/update")
	@ResponseBody
	public CommonResponse<RoleDTO> roleUpdate(HttpServletRequest httpServletRequest, HttpSession httpSession) {
		RoleDTO extractedRoleObj = this.roleService.extractRoleFormObject(httpServletRequest);
		CommonResponse<RoleDTO> updatedRole = this.roleService.updateRole(extractedRoleObj, httpSession);
		return updatedRole;
	}

}