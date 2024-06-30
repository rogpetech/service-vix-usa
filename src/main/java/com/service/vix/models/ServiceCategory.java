package com.service.vix.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This class is used as entity class that responsible to add service category
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "service_category")
@ToString
public class ServiceCategory extends AbstractModel {

	@Column(name = "service_category_name")
	private String serviceCategoryName;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "parent_service_category_id")
	private ServiceCategory parentServiceCategory;

	private String serviceCategoryImage;
	
	@Column(name = "service_discription")
	private String discription;

	private boolean activationStatus;
	
	private Boolean isDeleted = false;
	
	@JsonIgnore
	@JoinColumn(name = "org_id")
	@ManyToOne
	private Organization organization;
}
