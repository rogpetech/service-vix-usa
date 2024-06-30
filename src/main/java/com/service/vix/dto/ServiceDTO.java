/* 
 * ===========================================================================
 * File Name ServiceDTO.java
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
 * $Log: ServiceDTO.java,v $
 * ===========================================================================
 */
package com.service.vix.dto;

import java.time.LocalDateTime;
import com.service.vix.models.ServiceCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *This class is used as service  dto
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
 public class ServiceDTO {

	private Long id;
	
	private Long serviceCategoryId;
	
	private String serviceName;
	
	private String discription;
	
	private Float regularPrice;
	
	private Float internalCost;
	
	private boolean activationStatus;
	
	private String serviceImage;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;
	
	private String createdBy;

	private String updatedBy;

	private ServiceCategory serviceCategory;
}
