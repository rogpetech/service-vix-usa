package com.service.vix.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *This class is used as OrganizationPlan entity for application
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "organization_plan")
@ToString
public class OrganizationPlan extends AbstractModel {

	@Column(name = "organization_plan_code")
	private String planCode;
	@Column(name = "organization_coupan")
	private String coupan;

	@OneToOne(mappedBy = "organizationPlan")
	private Organization organization;

}
