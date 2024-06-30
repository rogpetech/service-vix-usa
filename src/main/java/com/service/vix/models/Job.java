/* 
 * ===========================================================================
 * File Name Job.java
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
 * $Log: Job.java,v $
 * ===========================================================================
 */
package com.service.vix.models;

import java.sql.Timestamp;
import java.util.List;

import com.service.vix.enums.JobStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "job")
public class Job extends AbstractModel {

	private Long customerId;

	private Long jobCategoryId;

	@ElementCollection
	private List<Long> technicianId;

	@Column(name = "requested_on")
	private Timestamp requestedOn;

	@Column(name = "job_status")
	@Enumerated(EnumType.STRING)
	private JobStatus jobStatus;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinTable(name = "job_option", joinColumns = @JoinColumn(name = "job_id"), inverseJoinColumns = @JoinColumn(name = "option_id"))
	private List<Option> options;

	private Boolean isDeleted = false;

	@JoinColumn(name = "org_id")
	@ManyToOne
	private Organization organization;

	@JoinColumn(name = "estimate_id")
	@ManyToOne
	private Estimate estimate;

}
