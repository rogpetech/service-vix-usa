/* 
 * ===========================================================================
 * File Name EstimateMapper.java
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
 * $Log: EstimateMapper.java,v $
 * ===========================================================================
 */
package com.service.vix.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.service.vix.dto.EstimateDTO;
import com.service.vix.models.Estimate;

/**
 * This interface is used to map Estimate to Estimate DTO and Vise Versa
 */
@Mapper(componentModel = "spring")
public interface EstimateMapper {

	EstimateMapper INSTANCE = Mappers.getMapper(EstimateMapper.class);

	/**
	 * This method is used to map Estimate to Estimate DTO
	 * 
	 * @author hemantr
	 * @date Jun 21, 2023
	 * @param estimate
	 * @return
	 */
	@Mapping(target = "contactNumbers", ignore = true)
	@Mapping(target = "emails", ignore = true)
	@Mapping(target = "customerName", ignore = true)
	@Mapping(target = "jobCategoryName", ignore = true)
	@Mapping(target = "customerDTO", ignore = true)
	@Mapping(target = "jobCategoryDTO", ignore = true)
	EstimateDTO estimateToEstimateDTO(Estimate estimate);

	/**
	 * This method is used to map Estimate DTO to Estimate
	 * 
	 * @author hemantr
	 * @date Jun 21, 2023
	 * @param estimateDTO
	 * @return
	 */
	@Mapping(target = "customerId", ignore = true)
	@Mapping(target = "jobCategoryId", ignore = true)
	@Mapping(target = "isDeleted", ignore = true)
	@Mapping(target = "organization", ignore = true)
	Estimate estimateDTOToEstimate(EstimateDTO estimateDTO);

}
