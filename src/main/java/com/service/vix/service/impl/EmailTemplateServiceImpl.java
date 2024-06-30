/* 
 * ===========================================================================
 * File Name EmailTemplateServiceImpl.java
 * 
 * Created on Nov 22, 2023
 * ===========================================================================
 */
/**
* Class Information
* @author ritiks
* @version 1.2 - Nov 22, 2023
*/
package com.service.vix.service.impl;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.EmailTemplateDTO;
import com.service.vix.dto.Message;
import com.service.vix.enums.EmailTemplateType;
import com.service.vix.mapper.EmailTemplateMapper;
import com.service.vix.models.Customer;
import com.service.vix.models.EmailTemplate;
import com.service.vix.models.Organization;
import com.service.vix.repositories.EmailTemplateRepository;
import com.service.vix.service.CommonService;
import com.service.vix.service.EmailTemplateService;
import com.service.vix.utility.EmailTemplateIdentifiers;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailTemplateServiceImpl implements EmailTemplateService {

	@Autowired
	private CommonService commonService;

	@Autowired
	private EmailTemplateRepository emailTemplateRepository;

	@Autowired
	private Environment env;

	/**
	 * This method is used to convert all the identifiers of email template into
	 * it's actual value
	 * 
	 * @author ritiks
	 * @date Nov 25, 2023
	 * @return EmailTemplateDTO
	 * @param organization
	 * @param customer
	 * @param emailTemplateDTO
	 * @return
	 * @exception Description
	 */
	private EmailTemplateDTO sanitizeEmailTemplate(Organization organization, Customer customer,
			EmailTemplateDTO emailTemplateDTO) {
		log.info("Enter inside EmailTemplateServiceImpl.sanitizeEmailTemplate() method.");
		EmailTemplateDTO sanitizedEmailTemplateDTO = emailTemplateDTO;

		LocalDateTime currentDate = LocalDateTime.now();
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		String currentDateStr = currentDate.format(dateFormatter);
		String currentTimeStr = currentDate.format(timeFormatter);

		if (emailTemplateDTO.getTemplateText() != null) {

			String templateText = emailTemplateDTO.getTemplateText();

			// Set values for general identifiers
			String[] generalIdentifiers = EmailTemplateIdentifiers.generalIdentifiers;
			// Replace Date Identifier to Current Date
			templateText = templateText.replaceAll(Pattern.quote(generalIdentifiers[0]),
					Matcher.quoteReplacement(currentDateStr));
			// Replace Time Identifier to Current Time
			templateText = templateText.replaceAll(Pattern.quote(generalIdentifiers[1]),
					Matcher.quoteReplacement(currentTimeStr));

			// Set values for Company identifiers
			if (organization != null) {
				String[] companyIdentifiers = EmailTemplateIdentifiers.companyIdentifiers;
				// Replace Name Identifier to Company Name
				if (organization.getOrgName() != null)
					templateText = templateText.replaceAll(Pattern.quote(companyIdentifiers[0]),
							Matcher.quoteReplacement(organization.getOrgName()));
				// Replace Owner Name Identifier to Company Owner Name
				if (organization.getOrgOwnerName() != null)
					templateText = templateText.replaceAll(Pattern.quote(companyIdentifiers[1]),
							Matcher.quoteReplacement(organization.getOrgOwnerName()));
				// Replace Email Identifier to Company Email
				if (organization.getOrgEmail() != null)
					templateText = templateText.replaceAll(Pattern.quote(companyIdentifiers[2]),
							Matcher.quoteReplacement(organization.getOrgEmail()));
				// Replace Mobile Number Identifier to Company Mobile Number
				if (organization.getOrgMobNum() != null)
					templateText = templateText.replaceAll(Pattern.quote(companyIdentifiers[3]),
							Matcher.quoteReplacement(organization.getOrgMobNum()));
				// Replace Address Identifier to Company Address
				if (organization.getOrgAddress() != null)
					templateText = templateText.replaceAll(Pattern.quote(companyIdentifiers[4]),
							Matcher.quoteReplacement(organization.getOrgAddress()));
				// Replace Country Identifier to Company Country
				if (organization.getOrgCountry() != null)
					templateText = templateText.replaceAll(Pattern.quote(companyIdentifiers[5]),
							Matcher.quoteReplacement(organization.getOrgCountry()));
			}

			// Set values for Customer identifiers
			if (customer != null) {
				String[] customerIdentifiers = EmailTemplateIdentifiers.customerIdentifiers;
				if (customer.getCustomerName() != null)
					templateText = templateText.replaceAll(Pattern.quote(customerIdentifiers[0]),
							Matcher.quoteReplacement(customer.getCustomerName()));
				if (customer.getCustomerParentAccount() != null)
					templateText = templateText.replaceAll(Pattern.quote(customerIdentifiers[1]),
							Matcher.quoteReplacement(customer.getCustomerParentAccount()));
				if (customer.getCustomerAccountNumber() != null)
					templateText = templateText.replaceAll(Pattern.quote(customerIdentifiers[2]),
							Matcher.quoteReplacement(customer.getCustomerAccountNumber().toString()));
				if (customer.getContactNumbers() != null)
					templateText = templateText.replaceAll(Pattern.quote(customerIdentifiers[3]),
							Matcher.quoteReplacement(customer.getContactNumbers().get(0).getNumber().toString()));
				if (customer.getEmails() != null)
					templateText = templateText.replaceAll(Pattern.quote(customerIdentifiers[4]),
							Matcher.quoteReplacement(customer.getEmails().get(0).getEmail()));
				if (customer.getPrimaryContactDepartment() != null)
					templateText = templateText.replaceAll(Pattern.quote(customerIdentifiers[5]),
							Matcher.quoteReplacement(customer.getPrimaryContactDepartment()));
				if (customer.getPrimaryContactJobTitle() != null)
					templateText = templateText.replaceAll(Pattern.quote(customerIdentifiers[6]),
							Matcher.quoteReplacement(customer.getPrimaryContactJobTitle()));
				if (customer.getStoredServiceLocations() != null)
					templateText = templateText.replaceAll(Pattern.quote(customerIdentifiers[7]), Matcher
							.quoteReplacement(customer.getStoredServiceLocations().get(0).getLocationNickName()));
				if (customer.getStoredServiceLocations() != null)
					templateText = templateText.replaceAll(Pattern.quote(customerIdentifiers[8]),
							Matcher.quoteReplacement(customer.getStoredServiceLocations().get(0).getUnit()));
				if (customer.getStoredServiceLocations() != null)
					templateText = templateText.replaceAll(Pattern.quote(customerIdentifiers[9]),
							Matcher.quoteReplacement(customer.getStoredServiceLocations().get(0).getAddress()));
				if (customer.getStoredServiceLocations() != null)
					templateText = templateText.replaceAll(Pattern.quote(customerIdentifiers[10]),
							Matcher.quoteReplacement(customer.getStoredServiceLocations().get(0).getCity()));
				if (customer.getStoredServiceLocations() != null)
					templateText = templateText.replaceAll(Pattern.quote(customerIdentifiers[11]),
							Matcher.quoteReplacement(customer.getStoredServiceLocations().get(0).getState()));
				if (customer.getReferalSource() != null)
					templateText = templateText.replaceAll(Pattern.quote(customerIdentifiers[12]),
							Matcher.quoteReplacement(customer.getReferalSource()));
				if (customer.getCustomerIndustry() != null)
					templateText = templateText.replaceAll(Pattern.quote(customerIdentifiers[13]),
							Matcher.quoteReplacement(customer.getCustomerIndustry()));
				if (customer.getCurrency() != null)
					templateText = templateText.replaceAll(Pattern.quote(customerIdentifiers[14]),
							Matcher.quoteReplacement(customer.getCurrency()));
			}

			sanitizedEmailTemplateDTO.setTemplateText(templateText);
		}
		return sanitizedEmailTemplateDTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.EmailTemplateService#saveEmailTemplate(com.service.
	 * vix.dto.EmailTemplateDTO)
	 */
	@Override
	public CommonResponse<EmailTemplateDTO> saveEmailTemplate(EmailTemplateDTO emailTemplateDTO,
			HttpSession httpSession, Principal principal) {
		log.info("Enter inside EmailTemplateServiceImpl.saveEmailTemplate() method.");
		CommonResponse<EmailTemplateDTO> response = new CommonResponse<EmailTemplateDTO>();
		Organization organization = this.commonService.getLoggedInUserOrganization(principal);
		EmailTemplate emailTemplate = EmailTemplateMapper.INSTANCE.emailTemplateDTOToEmailTemplate(emailTemplateDTO);
		emailTemplate.setOrganization(organization);
		EmailTemplate savedEmailTemplate = this.emailTemplateRepository.save(emailTemplate);
		EmailTemplateDTO savedEmailTemplateDTO = EmailTemplateMapper.INSTANCE
				.emailTemplateToEmailTemplateDTO(savedEmailTemplate);
		String msg = "";
		if (emailTemplateDTO.getId() == null)
			msg = env.getProperty("email.template.save.success");
		else
			msg = env.getProperty("email.template.update.success");
		httpSession.setAttribute("message", new Message(msg, "success"));
		response.setData(savedEmailTemplateDTO);
		response.setResult(true);
		response.setStatus(HttpStatus.CREATED.value());
		response.setMessage(msg);
		return response;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.EmailTemplateService#emailTemplateByOrgId(java.lang.
	 * Long, java.lang.String)
	 */
	@Override
	public EmailTemplateDTO emailTemplateByOrgId(Principal principal, String templateType) {
		log.info("Enter inside EmailTemplateServiceImpl.emailTemplateByOrgId() method.");

		Organization organization = this.commonService.getLoggedInUserOrganization(principal);
		EmailTemplateDTO emailTemplateDTO = new EmailTemplateDTO();
		EmailTemplateType emailTemplateType = EmailTemplateType.ESTIMATE_TEMPLATE;
		try {
			emailTemplateType = EmailTemplateType.valueOf(templateType);
		} catch (IllegalArgumentException e) {
			log.error(
					"EmailTemplateType not found by given EmailTemplateType String. So EmailTemplateType will be set by ESTIMATE_TEMPLATE.");
		}

		if (organization != null) {
			List<EmailTemplate> orgEmailTemplates = this.emailTemplateRepository
					.findByOrganizationAndEmailTemplateType(organization, emailTemplateType);
			if (orgEmailTemplates.size() > 0) {
				EmailTemplate emailTemplate = orgEmailTemplates.stream().findFirst().get();
				emailTemplateDTO = EmailTemplateMapper.INSTANCE.emailTemplateToEmailTemplateDTO(emailTemplate);
			}
		}
		return emailTemplateDTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.EmailTemplateService#
	 * emailTemplateDetailsByTemplateTypeAndOrganization(com.service.vix.enums.
	 * EmailTemplateType, com.service.vix.models.Organization)
	 */
	@Override
	public EmailTemplateDTO emailTemplateDetailsByTemplateTypeAndOrganization(EmailTemplateType emailTemplateType,
			Organization organization, Customer customer) {
		log.info("Enter inside EmailTemplateServiceImpl.emailTemplateDetailsByTemplateTypeAndOrganization() method.");
		EmailTemplateDTO emailTemplateDTO = new EmailTemplateDTO();
		List<EmailTemplate> orgEmailTemplates = this.emailTemplateRepository
				.findByOrganizationAndEmailTemplateType(organization, emailTemplateType);
		if (orgEmailTemplates.size() > 0) {
			EmailTemplate emailTemplate = orgEmailTemplates.stream().findFirst().get();
			emailTemplateDTO = EmailTemplateMapper.INSTANCE.emailTemplateToEmailTemplateDTO(emailTemplate);
			emailTemplateDTO = this.sanitizeEmailTemplate(organization, customer, emailTemplateDTO);
		}
		return emailTemplateDTO;
	}

}
