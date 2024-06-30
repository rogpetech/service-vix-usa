package com.service.vix.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *This class is used as stored service location entity for application
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "stored_service_location")
@ToString
public class StoredServiceLocation extends AbstractModel {

	@Column(name = "customer_location_nick_name")
	private String locationNickName;
	
	private Boolean isPrimaryLocation = false;
	
	private Boolean isBillingAddresss = false;
	
	@Column(name = "customer_street_address_lat_long")
	private String streetAddressLatLong;
	
	@Column(name = "customer_unit")
	private String unit;
	
	@Column(name = "customer_address")
	private  String address;
	
	@Column(name = "customer_city")
	private String city;
	
	@Column(name = "customer_state")
	private String state;
		

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id")
	private Customer customer;

}
