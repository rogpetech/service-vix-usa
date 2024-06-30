/* 
 * ===========================================================================
 * File Name EmailTemplateService.java
 * 
 * Created on Nov 22, 2023
 * ===========================================================================
 */
/**
* Class Information
* @author ritiks
* @version 1.2 - Nov 22, 2023
*/
package com.service.vix.service;

import java.security.Principal;

import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.EmailTemplateDTO;
import com.service.vix.enums.EmailTemplateType;
import com.service.vix.models.Customer;
import com.service.vix.models.Organization;

import jakarta.servlet.http.HttpSession;

@Component
public interface EmailTemplateService {

	/**
	 * This method is used to save email template
	 * 
	 * @author ritiks
	 * @date Nov 22, 2023
	 * @return CommonResponse<EmailTemplateDTO>
	 * @param emailTemplateDTO
	 * @param httpSession
	 * @param principal
	 * @return
	 * @exception Description
	 */
	CommonResponse<EmailTemplateDTO> saveEmailTemplate(EmailTemplateDTO emailTemplateDTO, HttpSession httpSession,
			Principal principal);

	/**
	 * This method is used to get organization email template by template type
	 * 
	 * @author ritiks
	 * @date Nov 24, 2023
	 * @return EmailTemplateDTO
	 * @param principal
	 * @param templateType
	 * @return
	 * @exception Description
	 */
	EmailTemplateDTO emailTemplateByOrgId(Principal principal, String templateType);

	/**
	 * This method is used to get EmailTemplate Details by template type and
	 * organization
	 * 
	 * @author ritiks
	 * @date Nov 25, 2023
	 * @return EmailTemplateDTO
	 * @param emailTemplateType
	 * @param organization
	 * @param customer
	 * @return
	 * @exception Description
	 */
	EmailTemplateDTO emailTemplateDetailsByTemplateTypeAndOrganization(EmailTemplateType emailTemplateType,
			Organization organization, Customer customer);

}
