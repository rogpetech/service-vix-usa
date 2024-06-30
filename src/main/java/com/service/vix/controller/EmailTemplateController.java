/* 
 * ===========================================================================

 * File Name EmailTemplateController.java
 * 
 * Created on Nov 20, 2023
 * ===========================================================================
 */
/**
* Class Information
* @author ritiks
* @version 1.2 - Nov 20, 2023
*/
package com.service.vix.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.service.vix.dto.EmailTemplateDTO;
import com.service.vix.service.EmailTemplateService;
import com.service.vix.utility.EmailTemplateIdentifiers;

import jakarta.servlet.http.HttpSession;

/**
 * This controller is used as Email-Template Controller
 */
@Controller
@RequestMapping("/email-template")
public class EmailTemplateController extends BaseController {

	@Autowired
	private EmailTemplateService emailTemplateService;

	/**
	 * This method is used to open email-template page
	 * 
	 * @author ritiks
	 * @date Nov 20, 2023
	 * @return String
	 * @return
	 * @exception Description
	 */
	@GetMapping("/")
	public String emailTemplate(Model model) {
		model.addAttribute("generalIdentifiers", EmailTemplateIdentifiers.generalIdentifiers);
		model.addAttribute("customerIdentifiers", EmailTemplateIdentifiers.customerIdentifiers);
		model.addAttribute("companyIdentifiers", EmailTemplateIdentifiers.companyIdentifiers);
		return "/email-template/email-template";
	}

	/**
	 * @author ritiks
	 * @date Nov 22, 2023
	 * @return String
	 * @param emailTemplateDTO
	 * @param httpSession
	 * @param principal
	 * @return
	 * @exception Description
	 */
	@PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String saveEmailTemplate(@ModelAttribute("emailTemplate") EmailTemplateDTO emailTemplateDTO,
			HttpSession httpSession, Principal principal) {
		this.emailTemplateService.saveEmailTemplate(emailTemplateDTO, httpSession, principal);
		return "redirect:/email-template/";
	}

	/**
	 * This method is used to get email template details by organization id
	 * 
	 * @author ritiks
	 * @date Nov 24, 2023
	 * @return EmailTemplateDTO
	 * @param orgId
	 * @return
	 * @exception Description
	 */
	@ResponseBody
	@GetMapping("/{templateType}")
	public EmailTemplateDTO emailTemplateDetails(@PathVariable String templateType, Principal principal) {
		EmailTemplateDTO emailTemplateDTO = this.emailTemplateService.emailTemplateByOrgId(principal, templateType);
		return emailTemplateDTO;
	}

}
