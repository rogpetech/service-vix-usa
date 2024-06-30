/* 
 * ===========================================================================
 * File Name invoice.java
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
 * $Log: invoice.java,v $
 * ===========================================================================
 */
package com.service.vix.models;

import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This class is used as as entity class for invoice
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "invoice")
public class Invoice extends AbstractModel {

//	private Long customerId;
	

	private Long invoiceNumber;

	private String sentBy;

	private Timestamp sentOn;

	private Long jobCategoryId;

	private String custome_invoice_Id;

	private Long jobId;

	@Column(name = "requested_on")
	private Timestamp requestedOn;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinTable(name = "invoice_option", joinColumns = @JoinColumn(name = "invoice_id"), inverseJoinColumns = @JoinColumn(name = "option_id"))
	private List<Option> options;

	private Boolean isDeleted = false;

	@JoinColumn(name = "org_id")
	@ManyToOne
	private Organization organization;

	@JoinColumn(name = "estimate_id")
	@ManyToOne
	private Estimate estimate;

	@JoinColumn(name = "customer_id")
	@ManyToOne
	private Customer customer;
	
}
