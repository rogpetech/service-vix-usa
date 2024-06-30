package com.service.vix.dto;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This class is used as Organization DTO
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Component
public class OrganizationDTO {

	private Long id;

	private String orgName;

	private String orgOwnerName;

	private String orgEmail;

	private String orgMobNum;

	private String orgAddress;

	private String orgCountry;

	private String orgBusinessLicence;

	private String orgLogo;

	private OrganizationPlanDTO organizationPlanDTO;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private String createdBy;

	private String updatedBy;

}
