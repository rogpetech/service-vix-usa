package com.service.vix.models;

import jakarta.persistence.CascadeType;
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
 * This class is used as Product entity for application that responsible to add
 * product
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "products")
public class Product extends AbstractModel {

	@Column(name = "product_name")
	private String productName;

	@Column(name = "discription")
	private String discription;

	@Column(name = "regular_price")
	private Float regularPrice;

	@Column(name = "average_cost")
	private Float averageCost;

	@Column(name = "member_price")
	private Float memberPrice;

	private boolean activationStatus;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "product_category_id")
	private ProductCategory productCategory;

	private String productImage;

	private Boolean isDeleted = false;

	@JoinColumn(name = "org_id")
	@ManyToOne
	private Organization organization;

}
