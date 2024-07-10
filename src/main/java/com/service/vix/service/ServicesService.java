/* 
 * ===========================================================================
 * File Name ServiceService.java
 * 
 * Created on Jun 24, 2023
 *
 * This code contains copyright information which is the proprietary property
 * of Chetu India Pvt. Ltd. No part of this code may be reproduced, stored or transmitted
 * in any form without the prior written permission of CHETU.
 *
 * Copyright (C) CHETU. 2023
 * All rights reserved.
 *
 * Modification history:
 * $Log: ServiceService.java,v $
 * ===========================================================================
 */
package com.service.vix.service;

import java.security.Principal;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.ServiceDTO;

import jakarta.servlet.http.HttpSession;

public interface ServicesService {

	/**
	 * This method is used to add Service
	 * 
	 * @author rodolfopeixoto
	 * @date Jun 24, 2023
	 * @return CommonResponse<ServiceDTO>
	 * @param serviceDTO
	 * @param file
	 * @param httpSession
	 * @param principal
	 * @return
	 * @exception Description
	 */
	CommonResponse<ServiceDTO> saveService(ServiceDTO serviceDTO, MultipartFile file, HttpSession httpSession,
			Principal principal);

	/**
	 * This method is used to get all the Services
	 * 
	 * @author hemantr
	 * @date Jun 24, 2023
	 * @return
	 */
	CommonResponse<List<ServiceDTO>> getServices(Principal principal);

	/**
	 * This method is used to get Service By Id
	 * 
	 * @author hemantr
	 * @date Jun 24, 2023
	 * @param serviceId
	 * @return
	 */
	CommonResponse<ServiceDTO> getServiceById(Long serviceId);

	/**
	 * This method is used to remove Service by id
	 * 
	 * @author hemantr
	 * @date Jun 24, 2023
	 * @param serviceId
	 * @return
	 */
	CommonResponse<Boolean> removeService(Long serviceId);

	/**
	 * This method is used to update Service
	 * 
	 * @author hemantr
	 * @date Jun 24, 2023
	 * @param serviceDTO
	 * @return
	 */
	CommonResponse<ServiceDTO> updateService(ServiceDTO serviceDTO);

	/**
	 * This method is used to find service by given search alphabet
	 * 
	 * @author hemantr
	 * @date Jun 24, 2023
	 * @param serviceName
	 * @return
	 */
	List<ServiceDTO> searchService(String serviceName, Principal principal);

	/**
	 * This method is used to get service details by service name
	 * 
	 * @author hemantr
	 * @date Jun 26, 2023
	 * @param serviceName
	 * @return
	 */
	CommonResponse<ServiceDTO> getServiceByServiceName(String serviceName);

}
