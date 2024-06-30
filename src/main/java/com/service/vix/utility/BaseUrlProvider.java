/* 
 * ===========================================================================
 * File Name BaseUrlProvider.java
 * 
 * Created on 24-Jul-2023
 *
 * This code contains copyright information which is the proprietary property
 * of Chetu India Pvt. Ltd. No part of this code may be reproduced, stored or transmitted
 * in any form without the prior written permission of CHETU.
 *
 * Copyright (C) CHETU. 2023
 * All rights reserved.
 *
 * Modification history:
 * $Log: BaseUrlProvider.java,v $
 * ===========================================================================
 */
package com.service.vix.utility;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
 public class BaseUrlProvider {
	 
	     /**
	     * This method is used to provide the base url of the application.
	     * @author ritiks 
	     * @date 24-Jul-2023 
	     * @param request
	     * @return 
	     */
	    public String getBaseUrl(HttpServletRequest request) {
	         String scheme = request.getScheme();
	         String serverName = request.getServerName(); // host
	         int serverPort = request.getServerPort(); // port
	         StringBuilder baseUrlBuilder = new StringBuilder();
	         baseUrlBuilder.append(scheme).append("://").append(serverName);
	         if ((scheme.equals("http") && serverPort != 80) || (scheme.equals("https") && serverPort != 443)) {
	             baseUrlBuilder.append(":").append(serverPort);
	         }
	         return baseUrlBuilder.toString();
	     }
	 }
