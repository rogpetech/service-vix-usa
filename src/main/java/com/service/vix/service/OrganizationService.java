package com.service.vix.service;

import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.OrganizationDTO;
import com.service.vix.dto.UserDTO;

import jakarta.servlet.http.HttpSession;

/**
 * This interface is used as Organization Service
 */
@Component
public interface OrganizationService {

	/**
	 * This method is used to register/save organization
	 * 
	 * @author hemantr
	 * @date Jun 1, 2023
	 * @return CommonResponse<UserDTO>
	 * @param userDTO
	 * @return
	 * @exception Description
	 */
	CommonResponse<UserDTO> saveOrganization(UserDTO userDTO);

	/**
	 * This method is used to get organization by org name
	 * 
	 * @author hemantr
	 * @date Jun 1, 2023
	 * @return CommonResponse<OrganizationDTO>
	 * @param organizationName
	 * @return
	 * @exception Description
	 */
	CommonResponse<OrganizationDTO> getOrganizationByName(String organizationName);

	/**
	 * This method is used to update User and organization details
	 * 
	 * @author ritiks
	 * @date Aug 18, 2023
	 * @param userDTO
	 * @return
	 */
	CommonResponse<UserDTO> updateOrganization(UserDTO userDTO, HttpSession httpSession);

	/**
	 * This method is used to check Organization registered with given
	 * organization-name or not
	 * 
	 * @author ritiks
	 * @date Sep 19, 2023
	 * @return Boolean
	 * @param orgName
	 * @return
	 * @exception Description
	 */
	Boolean checkOrganizationWithOrganizationName(String orgName);

}
