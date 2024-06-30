/* 
 * ===========================================================================
 * File Name ServiceMapper.java
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
 * $Log: ServiceMapper.java,v $
 * ===========================================================================
 */
package com.service.vix.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.service.vix.dto.ServiceDTO;
import com.service.vix.models.Services;

/**
 * This class is used as mapper that map Service Entity to Service DTO and Vise
 * Versa
 */
@Mapper(componentModel = "spring")
public interface ServiceMapper {

	ServiceMapper INSTANCE = Mappers.getMapper(ServiceMapper.class);

	/**
	 * This method is used to map serviceDTO to service entity
	 * 
	 * @author hemantr
	 * @date Jun 24, 2023
	 * @param serviceDto
	 * @return
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "isDeleted", ignore = true)
	@Mapping(target = "serviceCategory", ignore = true)
	Services serviceDTOToService(ServiceDTO serviceDto);

	/**
	 * This method is used to map service entity to serviceDTO
	 * 
	 * @author hemantr
	 * @date Jun 24, 2023
	 * @param service
	 * @return
	 */
	// @Mapping(target = "serviceId",ignore = true)
	@Mapping(target = "activationStatus", ignore = true)
	@Mapping(target = "serviceCategoryId", source = "id")
	ServiceDTO serviceTOServiceDTO(Services service);

}
