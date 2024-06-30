/* 
 * ===========================================================================
 * File Name Option.java
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
 * $Log: Option.java,v $
 * ===========================================================================
 */
package com.service.vix.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "`option`")
@ToString
public class Option extends AbstractModel {

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "option_option_product", joinColumns = @JoinColumn(name = "option_id"), inverseJoinColumns = @JoinColumn(name = "option_product_id"))
	private List<OptionProduct> optionProducts = new ArrayList<>();

	private Float jobTotal;
	
	private Float invoiceTotal;

	private Float estimateTotal;

	private Float jobCost;

	private Float grossProfit;
}
