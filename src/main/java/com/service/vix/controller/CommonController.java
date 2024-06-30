/* 
 * ===========================================================================
 * File Name CommonController.java
 * 
 * Created on Jul 31, 2023
 *
 * This code contains copyright information which is the proprietary property
 * of Chetu India Pvt. Ltd. No part of this code may be reproduced, stored or transmitted
 * in any form without the prior written permission of CHETU.
 *
 * Copyright (C) CHETU. 2023
 * All rights reserved.
 *
 * Modification history:
 * $Log: CommonController.java,v $
 * ===========================================================================
 */
package com.service.vix.controller;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.vix.service.CommonService;

/**
 * This is common controller that handle all common methods
 */
@RestController
@RequestMapping("/common")
public class CommonController extends BaseController {

	@Autowired
	private CommonService commonService;

	/**
	 * This method is used to search product and service
	 * 
	 * @author ritiks
	 * @date Jul 31, 2023
	 * @param productAndServiceName
	 * @return
	 */
	@GetMapping("/{productAndServiceName}")
	public Map<String, Object> searchProductAndServices(@PathVariable String productAndServiceName,
			Principal principal) {
		Map<String, Object> productAndServices = this.commonService
				.searchProductAndServicesByName(productAndServiceName, principal);
		return productAndServices;
	}

}
