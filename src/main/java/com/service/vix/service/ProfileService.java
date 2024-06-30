/* 
 * ===========================================================================
 * File Name ProfileService.java
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
 * $Log: ProfileService.java,v $
 * ===========================================================================
 */
package com.service.vix.service;

import java.security.Principal;

import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.PasswordChangeDTO;
import com.service.vix.dto.UserDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * This class is used as Profile Service
 */
@Component
public interface ProfileService {

	/**
	 * This method is used to get logged in user details
	 * 
	 * @author ritiks
	 * @date Aug 16, 2023
	 * @param principal
	 * @return
	 */
	CommonResponse<UserDTO> loggedInUserDetails(Principal principal);

	/**
	 * This method is used to change password
	 * 
	 * @author ritiks
	 * @date Nov 6, 2023
	 * @return CommonResponse<PasswordChangeDTO>
	 * @param httpServletRequest
	 * @param httpSession
	 * @return
	 * @exception Description
	 */
	CommonResponse<PasswordChangeDTO> changePassword(HttpServletRequest httpServletRequest, HttpSession httpSession);

}
