/* 
 * ===========================================================================
 * File Name EstimateService.java
 * 
 * Created on Jun 21, 2023
 *
 * This code contains copyright information which is the proprietary property
 * of Chetu India Pvt. Ltd. No part of this code may be reproduced, stored or transmitted
 * in any form without the prior written permission of CHETU.
 *
 * Copyright (C) CHETU. 2023
 * All rights reserved.
 *
 * Modification history:
 * $Log: EstimateService.java,v $
 * ===========================================================================
 */
package com.service.vix.service;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.EstimateDTO;
import com.service.vix.dto.EstimateEmailDTO;
import com.service.vix.dto.InvoiceDTO;
import com.service.vix.dto.JobDTO;
import com.service.vix.models.Option;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * This class is used as service class for Estimate to declare all the methods
 * for Estimate
 */
@Component
public interface EstimateService {

	/**
	 * This method is used to save Estimate
	 * 
	 * @author ritiks
	 * @date Jun 21, 2023
	 * @return CommonResponse<EstimateDTO>
	 * @param estimateDTO
	 * @param httpSession
	 * @param principal
	 * @return
	 * @exception Description
	 */
	CommonResponse<EstimateDTO> saveEstimate(EstimateDTO estimateDTO, HttpSession httpSession, Principal principal);

	/**
	 * This method is used to extract Save Estimate form data like extract multiple
	 * Option value
	 * 
	 * @author ritiks
	 * @date Jun 21, 2023
	 * @return EstimateDTO
	 * @param servletRequest
	 * @return
	 * @exception Description
	 */
	EstimateDTO extractEstimateFormObject(HttpServletRequest servletRequest);

	/**
	 * This method is used to find enstimate by id
	 * 
	 * @author ritiks
	 * @date Jun 21, 2023
	 * @return CommonResponse<EstimateDTO>
	 * @param estimateId
	 * @return
	 * @exception Description
	 */
	CommonResponse<EstimateDTO> getEstimatesById(Long estimateId);

	/**
	 * This method is used to send estimate to email
	 * 
	 * @author ritiks
	 * @date Jul 20, 2023
	 * @return CommonResponse<Boolean>
	 * @param estimateEmailDTO
	 * @param response
	 * @param httpSession
	 * @return
	 * @exception Description
	 */
	CommonResponse<Boolean> sendEstimateEmail(EstimateEmailDTO estimateEmailDTO, HttpServletResponse response,
			HttpSession httpSession);

	/**
	 * This method is used to remove Estimate
	 * 
	 * @author ritiks
	 * @date Jul 26, 2023
	 * @return CommonResponse<Boolean>
	 * @param estimateId
	 * @param httpSession
	 * @return
	 * @exception Description
	 */
	CommonResponse<Boolean> removeEstimate(Long estimateId, HttpSession httpSession);

	/**
	 * This method is used to update the estimate
	 * 
	 * @author ritiks
	 * @date Jul 27, 2023
	 * @return CommonResponse<EstimateDTO>
	 * @param estimateDTO
	 * @param httpSession
	 * @return
	 * @exception Description
	 */
	CommonResponse<EstimateDTO> updateEstimate(EstimateDTO estimateDTO, HttpSession httpSession);

	/**
	 * This method is used to change status of estimate and option
	 * 
	 * @author ritiks
	 * @date Nov 17, 2023
	 * @return void
	 * @param estimateId
	 * @param requestType
	 * @param optionId
	 * @param principal
	 * @param httpSession
	 * @exception Description
	 */
	void optionEstimateStatusChange(Long estimateId, String requestType, Long optionId, Principal principal,
			HttpSession httpSession);

	/**
	 * Get estimates of User's associated Organization
	 * 
	 * @author ritiks
	 * @date Sep 4, 2023
	 * @return CommonResponse<List<EstimateDTO>>
	 * @param principal
	 * @return
	 * @exception Description
	 */
	CommonResponse<List<EstimateDTO>> getOrganizationEstimates(Principal principal);

	/**
	 * Get Logged In User Estimates
	 * 
	 * @author ritiks
	 * @date Aug 5, 2023
	 * @return CommonResponse<List<EstimateDTO>>
	 * @param userName
	 * @return
	 * @exception Description
	 */
	CommonResponse<List<EstimateDTO>> getUserEstimates(Principal principal);

	/**
	 * This method is used to convert Estimate to job after accept the request by
	 * client
	 * 
	 * @author ritiks
	 * @date Nov 17, 2023
	 * @return JobDTO
	 * @param estimateId
	 * @param optionId
	 * @param principal
	 * @param httpSession
	 * @return
	 * @exception Description
	 */
	JobDTO convertEstimateToJob(Long estimateId, Long optionId, Principal principal, HttpSession httpSession);

	/**
	 * This method is used to convert Estimate to invoice after accept the request
	 * by client
	 * 
	 * @author ritiks
	 * @date Sep 4, 2023
	 * @return InvoiceDTO
	 * @param jobDTO
	 * @return
	 * @exception Description
	 */
	InvoiceDTO generateEstimateInvoice(JobDTO jobDTO);

	/**
	 * This method is used to get option details by option id
	 * 
	 * @author ritiks
	 * @date Oct 18, 2023
	 * @return Option
	 * @param optionId
	 * @return
	 * @exception Description
	 */
	Option optionDetailsByOptionId(Long optionId);

}
