/* 
 * ===========================================================================
 * File Name EmailTemplateRepository.java
 * 
 * Created on Nov 22, 2023
 * ===========================================================================
 */
/**
* Class Information
* @author ritiks
* @version 1.2 - Nov 22, 2023
*/
package com.service.vix.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.vix.enums.EmailTemplateType;
import com.service.vix.models.EmailTemplate;
import com.service.vix.models.Organization;

@Repository
public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Long> {

	/**
	 * This method is used to get Email Template by Organization Id and Email
	 * Template Type
	 * 
	 * @author ritiks
	 * @date Nov 24, 2023
	 * @return List<EmailTemplate>
	 * @param organization
	 * @param emailTemplateType
	 * @return
	 * @exception Description
	 */
	List<EmailTemplate> findByOrganizationAndEmailTemplateType(Organization organization,
			EmailTemplateType emailTemplateType);

}
