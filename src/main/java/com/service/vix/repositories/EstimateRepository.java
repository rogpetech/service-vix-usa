/* 
 * ===========================================================================
 * File Name EstimateRepository.java
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
 * $Log: EstimateRepository.java,v $
 * ===========================================================================
 */
package com.service.vix.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.service.vix.models.Estimate;
import com.service.vix.models.Organization;

/**
 * This interface is used as Repository for Estimate
 */
@Repository
public interface EstimateRepository extends JpaRepository<Estimate, Long> {
	/**
	 * This method is used to find estimate by name
	 * 
	 * @author ritiks
	 * @date Jun 21, 2023
	 * @return Optional<Estimate>
	 * @param customerId
	 * @return
	 * @exception Description
	 */
	Optional<Estimate> findByCustomerId(Long customerId);

	/**
	 * This method is used to get Estimates by user name
	 * 
	 * @author ritiks
	 * @date Aug 5, 2023
	 * @return Optional<List<Estimate>>
	 * @param organization
	 * @return
	 * @exception Description
	 */
	
	
	//Optional<List<Estimate>> findByOrganizationAndIsDeletedFalse(Organization organization);
	Optional<List<Estimate>> findByOrganizationAndIsDeletedFalseOrderByCreatedAtDesc(Organization organization);

	/**
	 * This method is used to find product associted with estimate.
	 * 
	 * @author ritiks
	 * @date Aug 4, 2023
	 * @return Optional<Estimate>
	 * @param productId
	 * @return
	 * @exception Description
	 */
	@Query("SELECT e FROM Estimate e JOIN e.options o JOIN o.optionProducts op WHERE op.productName = :productName")
	Optional<Estimate> findByProductName(@Param("productName") String productName);

	/**
	 * This method is used to find service associated with estimate.
	 * 
	 * @author ritiks
	 * @date Aug 4, 2023
	 * @return Optional<Estimate>
	 * @param serviceId
	 * @return
	 * @exception Description
	 */
	@Query("SELECT e FROM Estimate e JOIN e.options o JOIN o.optionProducts op WHERE op.serviceName = :serviceName")
	Optional<Estimate> findByServiceName(@Param("serviceName") String serviceName);

	/**
	 * This method is used to get non-deleted estimates
	 * 
	 * @author ritiks
	 * @date Sep 7, 2023
	 * @return List<Estimate>
	 * @param username
	 * @return
	 * @exception Description
	 */
	//List<Estimate> findByCreatedByAndIsDeletedFalse(String username);
	
	List<Estimate> findByCreatedByAndIsDeletedFalseOrderByCreatedAtDesc(String username);
}
