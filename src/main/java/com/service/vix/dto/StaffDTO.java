/* 
 * ===========================================================================
 * File Name StaffDTO.java
 * 
 * Created on Aug 21, 2023
 * ===========================================================================
 */
/**
* Class Information
* @author ritiks
* @version 1.2 - Aug 21, 2023
*/
package com.service.vix.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StaffDTO {

	private Long id;

	private Long staffOrgId;

	private String address;

	private String city;

	private String state;

	private String ssn;

	private String dateOfBirthStr;

	private String drivingLicienceNumber;

	private String drivingLicienceExpiryStr;

	private String profilePicture;

	/* Employment Information */

	private String jobTitle;

	private String employmentType;

	private String department;

	private String manager;

	private String hireDateStr;

	private String releaseDateStr;

	private String groupAssignment;

	private String createdAt;

	private String updatedAt;

	private Long roleId;

	private MultipartFile staffProfileMultipart;

	@Exclude
	private UserDTO userDTO;

	private Boolean isPasswordChange = false;

	private String createdBy;

	private String updatedBy;

}
