/* 
 * ===========================================================================
 * File Name JobDTO.java
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
 * $Log: JobDTO.java,v $
 * ===========================================================================
 */
package com.service.vix.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import com.service.vix.enums.JobStatus;
import com.service.vix.models.ContactNumber;
import com.service.vix.models.Email;
import com.service.vix.models.Option;

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
public class JobDTO {

	private Long id;
	
	private List<Long> technicianId;

	private String customerName;

	private String jobCategoryName;

	private CustomerDTO customerDTO;

	private JobCategoryDTO jobCategoryDTO;

	private Timestamp requestedOn;

	private JobStatus jobStatus;

	private List<Option> options;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private String createdBy;

	private String updatedBy;

	private List<ContactNumber> contactNumbers;

	private List<Email> emails;

	private Long salesPersonId;

	private EstimateDTO estimateDTO;
	
	private List<UserDTO> technicians;
}
