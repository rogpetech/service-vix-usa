/* 
 * ===========================================================================
 * File Name RoleDTO.java
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
 * $Log: RoleDTO.java,v $
 * ===========================================================================
 */
package com.service.vix.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.service.vix.models.Permission;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RoleDTO {

	private Long id;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private String createdBy;

	private String updatedBy;

	private String name;

	private List<Permission> permissions;

	private Map<String, Permission> permissionsMap;

	private Boolean isDeleted = false;

	public RoleDTO(String name) {
		super();
		this.name = name;
	}

}
