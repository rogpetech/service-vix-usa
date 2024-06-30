/* 
 * ===========================================================================
 * File Name RoleService.java
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
 * $Log: RoleService.java,v $
 * ===========================================================================
 */
package com.service.vix.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.RoleDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Component
public interface RoleService {

	/**
	 * This method is used to save roles
	 * 
	 * @author ritiks
	 * @date Aug 18, 2023
	 * @param roleDTO
	 * @return
	 */
	CommonResponse<RoleDTO> saveRole(RoleDTO roleDTO, HttpSession httpSession);

	/**
	 * This method is used to get all roles
	 * 
	 * @author ritiks
	 * @date Aug 18, 2023
	 * @return
	 */
	CommonResponse<List<RoleDTO>> getRoles();

	/**
	 * This method is used to find role by Id
	 * 
	 * @author ritiks
	 * @date Aug 18, 2023
	 * @param roleId
	 * @return
	 */
	CommonResponse<RoleDTO> getRoleById(Long roleId);

	/**
	 * This method is used to delete Role by Id
	 * 
	 * @author ritiks
	 * @date Aug 19, 2023
	 * @param roleId
	 * @param httpSession
	 * @return
	 */
	CommonResponse<Boolean> removeRole(Long roleId, HttpSession httpSession);

	/**
	 * This method is used to update Role
	 * 
	 * @author ritiks
	 * @date Aug 19, 2023
	 * @return CommonResponse<RoleDTO>
	 * @param roleDTO
	 * @param httpSession
	 * @return
	 * @exception Description
	 */
	CommonResponse<RoleDTO> updateRole(RoleDTO roleDTO, HttpSession httpSession);

	/**
	 * This method is used to extract Role form object from Request
	 * 
	 * @author ritiks
	 * @date Aug 21, 2023
	 * @return RoleDTO
	 * @param httpServletRequest
	 * @return
	 * @exception Description
	 */
	RoleDTO extractRoleFormObject(HttpServletRequest httpServletRequest);
	
          

}
