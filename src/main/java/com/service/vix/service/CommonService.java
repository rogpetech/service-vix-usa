/* 
 * ===========================================================================
 * File Name CommonService.java
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
 * $Log: CommonService.java,v $
 * ===========================================================================
 */
package com.service.vix.service;

import java.security.Principal;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.service.vix.models.Organization;

/**
 * This is an interface that have all the common method of the application
 */
@Component
public interface CommonService {

	/**
	 * This method is a service method that have declaration of the search product
	 * and service method
	 * 
	 * @author ritiks
	 * @date Sep 4, 2023
	 * @return Map<String,Object>
	 * @param productServiceName
	 * @param principal
	 * @return
	 * @exception Description
	 */
	Map<String, Object> searchProductAndServicesByName(String productServiceName, Principal principal);

	/**
	 * This method is used to get Logged in user Organization
	 * 
	 * @author ritiks
	 * @date Sep 4, 2023
	 * @return Organization
	 * @param principal
	 * @return
	 * @exception Description
	 */
	Organization getLoggedInUserOrganization(Principal principal);

}
