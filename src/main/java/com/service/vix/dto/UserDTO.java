package com.service.vix.dto;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import com.service.vix.models.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This class is used as User DTO
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDTO {

	private Long id;

	private String username;

	private String email;

	private String password;

	private String firstName;

	private String lastName;

	private String mobileNum;

	private String gender;

	private Role role;

	private Boolean isActive;

	private OrganizationDTO organizationDTO;

	private String organizationName;

	private MultipartFile organizationLogoMultipart;

	private MultipartFile userProfileMultipart;

	private String userProfilePictureURL;

	private String organizationLogoURL;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private String createdBy;

	private String updatedBy;

	private StaffDTO staffDTO;

}
