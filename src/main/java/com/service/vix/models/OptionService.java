/* 
 * ===========================================================================
 * File Name OptionProduct.java
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
 * $Log: OptionProduct.java,v $
 * ===========================================================================
 */
package com.service.vix.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This class is used as entity that hold option product details.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "option_service")
@ToString
public class OptionService extends AbstractModel {

	private Long serviceId;

	@Column(name = "service_qantity_hrs")
	private Float quantity;

	@Column(name = "service_total_cost")
	private Float total;

	@Transient
	private String serviceName;

	@Transient
	private Double serviceCost;
	
	@Transient
	private Services services;

}
