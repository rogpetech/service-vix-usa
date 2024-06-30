/* 
 * ===========================================================================
 * File Name RoleMapper.java
 * 
 * Created on Aug 18, 2023
 *
 * This code contains copyright information which is the proprietary property
 * of Chetu India Pvt. Ltd. No part of this code may be reproduced, stored or transmitted
 * in any form without the prior written permission of CHETU.
 *
 * Copyright (C) CHETU. 2023
 * All rights reserved.
 *
 * Modification history:
 * $Log: RoleMapper.java,v $
 * ===========================================================================
 */
package com.service.vix.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.service.vix.dto.RoleDTO;
import com.service.vix.models.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {

	RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

	/**
	 * Add method description here
	 * 
	 * @author ritiks
	 * @date Aug 18, 2023
	 * @param role
	 * @return
	 */
	@Mapping(target = "permissionsMap", ignore = true)
	RoleDTO roleToRoleDTO(Role role);

	/**
	 * Add method description here
	 * 
	 * @author ritiks
	 * @date Aug 18, 2023
	 * @param roleDTO
	 * @return
	 */
	Role roleDTOToRole(RoleDTO roleDTO);

}
