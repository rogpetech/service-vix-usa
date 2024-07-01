package com.service.vix.mapper;

import com.service.vix.dto.EmailTemplateDTO;
import com.service.vix.models.EmailTemplate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-30T21:48:49-0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.2 (Oracle Corporation)"
)
@Component
public class EmailTemplateMapperImpl implements EmailTemplateMapper {

    @Override
    public EmailTemplateDTO emailTemplateToEmailTemplateDTO(EmailTemplate emailTemplate) {
        if ( emailTemplate == null ) {
            return null;
        }

        EmailTemplateDTO emailTemplateDTO = new EmailTemplateDTO();

        emailTemplateDTO.setId( emailTemplate.getId() );
        emailTemplateDTO.setCreatedAt( emailTemplate.getCreatedAt() );
        emailTemplateDTO.setUpdatedAt( emailTemplate.getUpdatedAt() );
        emailTemplateDTO.setCreatedBy( emailTemplate.getCreatedBy() );
        emailTemplateDTO.setUpdatedBy( emailTemplate.getUpdatedBy() );
        emailTemplateDTO.setEmailTemplateType( emailTemplate.getEmailTemplateType() );
        emailTemplateDTO.setTemplateSubject( emailTemplate.getTemplateSubject() );
        emailTemplateDTO.setTemplateText( emailTemplate.getTemplateText() );

        return emailTemplateDTO;
    }

    @Override
    public EmailTemplate emailTemplateDTOToEmailTemplate(EmailTemplateDTO emailTemplateDTO) {
        if ( emailTemplateDTO == null ) {
            return null;
        }

        EmailTemplate emailTemplate = new EmailTemplate();

        emailTemplate.setId( emailTemplateDTO.getId() );
        emailTemplate.setCreatedAt( emailTemplateDTO.getCreatedAt() );
        emailTemplate.setUpdatedAt( emailTemplateDTO.getUpdatedAt() );
        emailTemplate.setCreatedBy( emailTemplateDTO.getCreatedBy() );
        emailTemplate.setUpdatedBy( emailTemplateDTO.getUpdatedBy() );
        emailTemplate.setEmailTemplateType( emailTemplateDTO.getEmailTemplateType() );
        emailTemplate.setTemplateSubject( emailTemplateDTO.getTemplateSubject() );
        emailTemplate.setTemplateText( emailTemplateDTO.getTemplateText() );

        return emailTemplate;
    }
}
