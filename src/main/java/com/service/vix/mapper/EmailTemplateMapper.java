/* 
 * ===========================================================================
 * File Name EmailTemplateMapper.java
 * 
 * Created on Nov 22, 2023
 * ===========================================================================
 */
/**
* Class Information
* @author rodolfopeixoto
* @version 1.2 - Nov 22, 2023
*/
package com.service.vix.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.service.vix.dto.EmailTemplateDTO;
import com.service.vix.models.EmailTemplate;

/**
 * This mapper is used to map EmailTemplate to EmailTemplateDTO and vice-versa
 */
@Mapper(componentModel = "spring")
public interface EmailTemplateMapper {

	EmailTemplateMapper INSTANCE = Mappers.getMapper(EmailTemplateMapper.class);

	/**
	 * This method is used to map EmailTemplate to EmailTemplateDTO
	 * 
	 * @author rodolfopeixoto
	 * @date Nov 22, 2023
	 * @return EmailTemplateDTO
	 * @param emailTemplate
	 * @return
	 * @exception Description
	 */
	@Mapping(target = "organizationDTO", ignore = true)
	public EmailTemplateDTO emailTemplateToEmailTemplateDTO(EmailTemplate emailTemplate);

	/**
	 * This method is used to map EmailTemplateDTO to EmailTemplate
	 * 
	 * @author rodolfopeixoto
	 * @date Nov 22, 2023
	 * @return EmailTemplate
	 * @param emailTemplateDTO
	 * @return
	 * @exception Description
	 */
	@Mapping(target = "organization", ignore = true)
	public EmailTemplate emailTemplateDTOToEmailTemplate(EmailTemplateDTO emailTemplateDTO);

}
