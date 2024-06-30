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
@Table(name = "option_product_invoice")
@ToString
public class OptionProductInvoice extends AbstractModel {

//	private Long productId;
//
//	private Long serviceId;

	private Float quantity;

	private Float rate;

	private Float total;

	private String productName;

	private String productDiscription;

	private Float productRegularPrice;

	private Float productCost;

	private String serviceName;

	private String serviceDiscription;

	private Float serviceRegularPrice;

	private Float serviceCost;

	@Transient
	private Product product;

	@Transient
	private Services services;

}
