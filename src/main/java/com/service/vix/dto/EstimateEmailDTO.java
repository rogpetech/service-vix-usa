/* 
 * ===========================================================================
 * File Name EstimateEmailDTO.java
 * 
 * Created on Jul 20, 2023
 *
 * This code contains copyright information which is the proprietary property
 * of Chetu India Pvt. Ltd. No part of this code may be reproduced, stored or transmitted
 * in any form without the prior written permission of CHETU.
 *
 * Copyright (C) CHETU. 2023
 * All rights reserved.
 *
 * Modification history:
 * $Log: EstimateEmailDTO.java,v $
 * ===========================================================================
 */
package com.service.vix.dto;

import java.util.List;

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
public class EstimateEmailDTO {

	private String fromEmail;
	private String toEmail;
	private String subject;
	private String body;
	private List<Long> optionIds;
	private Long estimateId;
	private Long invoiceId;

	// Attach handle
	private Boolean itemPrice = false;
	private Boolean itemTotal = false;
	private Boolean itemQuantity = false;
	private Boolean grandTotal = false;

}
