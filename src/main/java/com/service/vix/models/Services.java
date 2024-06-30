package com.service.vix.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This class is used as Service entity for application that responsible to add
 * services
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "service")
public class Services extends AbstractModel {

	@Column(name = "service_name")
	private String serviceName;

	@Column(name = "discription")
	private String discription;

	@Column(name = "regular_price")
	private Float regularPrice;

	@Column(name = "internal_cost")
	private Float internalCost;

	@ManyToOne
	@JoinColumn(name = "service_category_id")
	private ServiceCategory serviceCategory;

	private String serviceImage;

	private Boolean isDeleted = false;

	@JoinColumn(name = "org_id")
	@ManyToOne
	private Organization organization;

}
