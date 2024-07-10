/* 
 * ===========================================================================
 * File Name Staff.java
 * 
 * Created on Aug 21, 2023
 * ===========================================================================
 */
/**
* Class Information
* @author rodolfopeixoto
* @version 1.2 - Aug 21, 2023
*/
package com.service.vix.models;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;

/**
 * This class is used as staff entity for application
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "staff")
@ToString
public class Staff extends AbstractModel {

	private String address;

	private String city;

	private String state;

	private String ssn;

	private LocalDate dateOfBirth;

	private String drivingLicienceNumber;

	private LocalDate drivingLicienceExpiry;

	/* Employment Information */
	private String jobTitle;

	private String employmentType;

	private String department;

	private String manager;

	private LocalDate hireDate;

	private LocalDate releaseDate;

	private String groupAssignment;

	private Boolean isDeleted = false;

	@JoinColumn(name = "org_id")
	@ManyToOne
	private Organization organization;

	@OneToOne(mappedBy = "staff")
	@JsonIgnore
	@Exclude
	private User user;
	
	private Boolean isPasswordChange=false;

}
