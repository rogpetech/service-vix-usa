/* 
 * ===========================================================================
 * File Name EstimateDTO.java
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
 * $Log: EstimateDTO.java,v $
 * ===========================================================================
 */
package com.service.vix.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.service.vix.enums.EstimateStatus;
import com.service.vix.models.ContactNumber;
import com.service.vix.models.Email;
import com.service.vix.models.Option;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This class is used as estimate dto
 */

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InvoiceDTO {

	private Long id;

	private String customerName;

	private Long invoiceNumber;

	private String sentBy;

	private Timestamp sentOn;

	private String jobCategoryName;

	private CustomerDTO customerDTO;

	private JobCategoryDTO jobCategoryDTO;

	private Timestamp requestedOn;

	private EstimateStatus estimateStatus;

	private List<Option> options;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private String createdBy;

	private String updatedBy;

	private List<ContactNumber> contactNumbers;

	private List<Email> emails;

	private EstimateDTO estimateDTO;

}
