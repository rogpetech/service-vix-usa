/* 
 * ===========================================================================
 * File Name EmailServiceImpl.java
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
 * $Log: EmailServiceImpl.java,v $
 * ===========================================================================
 */
/**
* Class Information
* @author rajeshy3 - Chetu
* @version 1.0 - 20-Jul-2023
*/
package com.service.vix.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.service.vix.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is used to
 */
@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender mailSender;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.EmailService#sendEmailWithAttachment(java.lang.
	 * String, java.lang.String, java.lang.String, byte[], java.lang.String)
	 */
	public boolean sendEmailWithAttachment(String recipientEmail, String subject, String message,
			byte[] attachmentBytes, String attachmentName, String orgLogoWithPath) {
		log.info("Enter inside EmailServiceImpl.sendEmailWithAttachment() method.");
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			List<String> recipientEmailsList = new ArrayList<>();
			if (recipientEmail.contains(",")) {
				String[] se = recipientEmail.split(",");
				for (String re : se)
					recipientEmailsList.add(re);
			} else {
				recipientEmailsList.add(recipientEmail);
			}
			String[] recipientEmails = recipientEmailsList.toArray(new String[0]);

			helper = new MimeMessageHelper(mimeMessage, true);
			helper.setFrom("raj05378@yopmail.com");
			helper.setTo(recipientEmails);
			helper.setSubject(subject);
			helper.setText(message, true);

			FileSystemResource inlineImage = new FileSystemResource(orgLogoWithPath);
			helper.addInline("image", inlineImage);

			// Add the PDF attachment
			if (attachmentBytes != null && attachmentName != null && attachmentName.length() > 0)
				helper.addAttachment(attachmentName, new ByteArrayDataSource(attachmentBytes, "application/pdf"));
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.EmailService#sendEmailWithoutAttachment(java.lang.
	 * String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean sendEmailWithoutAttachment(String recipientEmail, String subject, String message)
			throws MessagingException {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			List<String> recipientEmailsList = new ArrayList<>();
			if (recipientEmail.contains(",")) {
				String[] se = recipientEmail.split(",");
				for (String re : se)
					recipientEmailsList.add(re);
			} else {
				recipientEmailsList.add(recipientEmail);
			}
			String[] recipientEmails = recipientEmailsList.toArray(new String[0]);
			helper = new MimeMessageHelper(mimeMessage, true);
			helper.setTo(recipientEmails);
			helper.setSubject(subject);
			helper.setText(message, false);
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
