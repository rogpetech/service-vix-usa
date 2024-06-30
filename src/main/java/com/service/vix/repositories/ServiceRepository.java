/* 
 * ===========================================================================
 * File Name ServiceRepository.java
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
 * $Log: ServiceRepository.java,v $
 * ===========================================================================
 */
package com.service.vix.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.vix.models.Organization;
import com.service.vix.models.ServiceCategory;
import com.service.vix.models.Services;

@Repository
public interface ServiceRepository extends JpaRepository<Services, Long> {

	/**
	 * THis method is used to find out service by given name
	 * 
	 * @author hemantr
	 * @date Jun 22, 2023
	 * @param serviceName
	 * @return
	 */
	Boolean existsByServiceName(String serviceName);

	/**
	 * This method is used to search service by given product name
	 * 
	 * @author ritiks
	 * @date Jun 26, 2023
	 * @return List<Services>
	 * @param serviceName
	 * @param organization
	 * @return
	 * @exception Description
	 */
	List<Services> findByServiceNameLikeAndIsDeletedFalseAndOrganization(String serviceName, Organization organization);

	/**
	 * Get Service from database by service name
	 * 
	 * @author hemantr
	 * @date Jun 26, 2023
	 * @param serviceName
	 * @return
	 */
	Optional<Services> findByServiceName(String serviceName);

	/**
	 * This method is used to get all Services whose isDeleted is false
	 * 
	 * @author ritiks
	 * @date Jul 27, 2023
	 * @return List<Services>
	 * @param organization
	 * @return
	 * @exception Description
	 */
	List<Services> findAllByIsDeletedFalseAndOrganization(Organization organization);

	/**
	 * This method is used for check service category attached with service or not
	 * 
	 * @author ritiks
	 * @date Aug 2, 2023
	 * @param serviceCategory
	 * @return
	 */
	boolean existsByServiceCategoryAndIsDeletedFalse(ServiceCategory serviceCategory);
}
