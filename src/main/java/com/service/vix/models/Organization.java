package com.service.vix.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;

/**
 * This class is used as organization entity for application
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "organization")
@ToString
public class Organization extends AbstractModel {

	@Column(name = "organization_name")
	private String orgName;
	@Column(name = "organization_owner_name")
	private String orgOwnerName;
	@Column(name = "organization_email")
	private String orgEmail;
	@Column(name = "organization_mobile_number")
	private String orgMobNum;
	@Column(name = "organization_address")
	private String orgAddress;
	@Column(name = "organization_country")
	private String orgCountry;
	@Column(name = "organization_buisness_licence")
	private String orgBusinessLicence;
	@Column(name = "organization_logo")
	private String orgLogo;

	@OneToOne(mappedBy = "organizationDetails")
	@JsonIgnore
	@Exclude
	private User user;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "plan_id", updatable = false)
	private OrganizationPlan organizationPlan;

}
