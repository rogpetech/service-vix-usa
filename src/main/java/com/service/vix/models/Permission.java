/* 
 * ===========================================================================
 * File Name Permission.java
 * 
 * Created on Aug 19, 2023
 *
 * This code contains copyright information which is the proprietary property
 * of Chetu India Pvt. Ltd. No part of this code may be reproduced, stored or transmitted
 * in any form without the prior written permission of CHETU.
 *
 * Copyright (C) CHETU. 2023
 * All rights reserved.
 *
 * Modification history:
 * $Log: Permission.java,v $
 * ===========================================================================
 */
package com.service.vix.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.service.vix.enums.Permissions;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This class is used as Permission Class for application
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "`permission`")
@ToString
public class Permission extends AbstractModel {

	@Column(name = "name")
	@Enumerated(EnumType.STRING)
	private Permissions permissionName;

	private Boolean listView = false;

	private Boolean individualView = false;

	private Boolean addNew = false;

	private Boolean updateExisting = false;

	@Column(name = "delete_permission")
	private Boolean delete = false;

	@JsonIgnore
	@ManyToOne
	private Role role;

}
