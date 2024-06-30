/* 
 * ===========================================================================
 * File Name EmailController.java
 * 
 * Created on 20-Jul-2023
 *
 * This code contains copyright information which is the proprietary property
 * of ServiceVix. No part of this code may be reproduced, stored or transmitted
 * in any form without the prior written permission of ServiceVix.
 *
 * Copyright (C) ServiceVix. 2023
 * All rights reserved.
 *
 * Modification history:
 * $Log: EmailController.java,v $
 * ===========================================================================
 */
/**
* Class Information
* @author rajeshy3 - Chetu
* @version 1.0 - 20-Jul-2023
*/
package com.service.vix.controller;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class EmailController extends BaseController{

	@PostMapping("/sendEmail")
	public String sendEmail(@RequestParam("recipient") String recipient, @RequestParam("subject") String subject,
			@RequestParam("message") String message, @RequestParam("attachment") MultipartFile attachmentFile) {
		try {
			File attachment = null;
			if (!attachmentFile.isEmpty()) {
				attachment = new File(attachmentFile.getOriginalFilename());
				FileUtils.writeByteArrayToFile(attachment, attachmentFile.getBytes());
			}

//             emailService.sendEmailWithAttachment(recipient, subject, message, attachment);

			// You can add a success message here and return to the success page.
			return "redirect:/success";
		} catch (Exception e) {
			// Handle exceptions and return to an error page or show an error message.
			return "redirect:/error";
		}
	}
}
