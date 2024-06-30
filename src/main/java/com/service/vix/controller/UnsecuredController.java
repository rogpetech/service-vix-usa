/* 
 * ===========================================================================
 * File Name UnsecuredController.java
 * 
 * Created on Aug 4, 2023
 *
 * This code contains copyright information which is the proprietary property
 * of Chetu India Pvt. Ltd. No part of this code may be reproduced, stored or transmitted
 * in any form without the prior written permission of CHETU.
 *
 * Copyright (C) CHETU. 2023
 * All rights reserved.
 *
 * Modification history:
 * $Log: UnsecuredController.java,v $
 * ===========================================================================
 */
package com.service.vix.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.service.vix.service.EstimateService;

import jakarta.servlet.http.HttpSession;

/**
 * This is an un-authorized controller that haven't any type of security
 */
@Controller
@RequestMapping("/uauth")
public class UnsecuredController extends BaseController {

	@Autowired
	private EstimateService estimateService;

	/**
	 * This method is used to approve request of the option from client end
	 * 
	 * @author ritiks
	 * @date Nov 17, 2023
	 * @return String
	 * @param estimateId
	 * @param requestType
	 * @param optionId
	 * @param httpSession
	 * @param principal
	 * @return
	 * @exception Description
	 */
	@GetMapping("/{estimateId}/option/{requestType}/{optionId}")
	public String optionApproval(@PathVariable Long estimateId, @PathVariable String requestType,
			@PathVariable Long optionId, HttpSession httpSession, Principal principal) {
		this.estimateService.optionEstimateStatusChange(estimateId, requestType, optionId, principal, httpSession);
		return "other/thanksmail";
	}

}
