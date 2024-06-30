/* 
 * ===========================================================================
 * File Name JobService.java
 * 
 * Created on Aug 5, 2023
 *
 * This code contains copyright information which is the proprietary property
 * of Chetu India Pvt. Ltd. No part of this code may be reproduced, stored or transmitted
 * in any form without the prior written permission of CHETU.
 *
 * Copyright (C) CHETU. 2023
 * All rights reserved.
 *
 * Modification history:
 * $Log: JobService.java,v $
 * ===========================================================================
 */
package com.service.vix.service;

import java.security.Principal;
import java.util.List;

import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.JobDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * This class is used as Service for Job
 */
@Component
public interface JobService {

	/**
	 * This method is used to create Job
	 * 
	 * @author ritiks
	 * @date Nov 2, 2023
	 * @return CommonResponse<JobDTO>
	 * @param jobDTO
	 * @param httpSession
	 * @param principal
	 * @return
	 * @exception Description
	 */
	CommonResponse<JobDTO> saveJob(JobDTO jobDTO, HttpSession httpSession, Principal principal);

	/**
	 * This method is used to extract Save Estimate form data like extract multiple
	 * Option value
	 * 
	 * @author ritiks
	 * @date Nov 2, 2023
	 * @return JobDTO
	 * @param servletRequest
	 * @return
	 * @exception Description
	 */
	JobDTO extractJobFormObject(HttpServletRequest servletRequest);

	/**
	 * This method is used to find estimate by id
	 * 
	 * @author ritiks
	 * @date Nov 2, 2023
	 * @return CommonResponse<JobDTO>
	 * @param jobId
	 * @return
	 * @exception Description
	 */
	CommonResponse<JobDTO> getJobsById(Long jobId);

	/**
	 * This method is used to remove Job
	 * 
	 * @author ritiks
	 * @date Nov 2, 2023
	 * @return CommonResponse<Boolean>
	 * @param jobId
	 * @param httpSession
	 * @return
	 * @exception Description
	 */
	CommonResponse<Boolean> removeJob(Long jobId, HttpSession httpSession);

	/**
	 * This method is used to update the job
	 * 
	 * @author ritiks
	 * @date Nov 2, 2023
	 * @return CommonResponse<JobDTO>
	 * @param jobDTO
	 * @param httpSession
	 * @return
	 * @exception Description
	 */
	CommonResponse<JobDTO> updateJob(JobDTO jobDTO, HttpSession httpSession);

	/**
	 * Get jobs of User's associated Organization
	 * 
	 * @author ritiks
	 * @date Nov 2, 2023
	 * @return CommonResponse<List<JobDTO>>
	 * @param principal
	 * @return
	 * @exception Description
	 */
	CommonResponse<List<JobDTO>> getOrganizationJobs(Principal principal);

	/**
	 * Get Logged In User Jobs
	 * 
	 * @author ritiks
	 * @date Nov 2, 2023
	 * @return CommonResponse<List<JobDTO>>
	 * @param principal
	 * @return
	 * @exception Description
	 */
	CommonResponse<List<JobDTO>> getUserJobs(Principal principal);

}
