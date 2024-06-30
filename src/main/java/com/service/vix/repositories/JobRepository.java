/* 
 * ===========================================================================
 * File Name JobRepository.java
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
 * $Log: JobRepository.java,v $
 * ===========================================================================
 */
package com.service.vix.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Description;
import org.springframework.data.jpa.repository.JpaRepository;

import com.service.vix.models.Job;
import com.service.vix.models.Organization;

/**
 * This class is used as Repository for Job
 */
public interface JobRepository extends JpaRepository<Job, Long> {

	/**
	 * This method is used to get User Jobs
	 * 
	 * @author ritiks
	 * @date Aug 5, 2023
	 * @param userName
	 * @return
	 */
	List<Job> findByCreatedBy(String userName);

	/**
	 * This method is used to get non-deleted jobs
	 * 
	 * @author ritiks
	 * @date Nov 2, 2023
	 * @return List<Job>
	 * @param username
	 * @return
	 * @exception Description
	 */
	List<Job> findByCreatedByAndIsDeletedFalse(String username);

	/**
	 * This method is used to get organization jobs
	 * 
	 * @author ritiks
	 * @date Nov 2, 2023
	 * @return Optional<List<Estimate>>
	 * @param organization
	 * @return
	 * @exception Description
	 */
	Optional<List<Job>> findByOrganizationAndIsDeletedFalse(Organization organization);

}
