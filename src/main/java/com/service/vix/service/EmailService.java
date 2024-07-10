/* 
 * ===========================================================================
 * File Name EmailService.java
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
 * $Log: EmailService.java,v $
 * ===========================================================================
 */
/**
* Class Information
* @author rajeshy3 - Chetu
* @version 1.0 - 20-Jul-2023
*/
package com.service.vix.service;

import jakarta.mail.MessagingException;

public interface EmailService {

	/**
	 * This method is used to send emails with attached attachments
	 * 
	 * @author rodolfopeixoto
	 * @date 20-Jul-2023
	 * @return boolean
	 * @param recipientEmail
	 * @param subject
	 * @param message
	 * @param attachmentBytes
	 * @param attachmentName
	 * @return
	 * @throws MessagingException
	 * @exception Description
	 */
	public boolean sendEmailWithAttachment(String recipientEmail, String subject, String message,
			byte[] attachmentBytes, String attachmentName,String orgLogoWithPath) throws MessagingException;

	/**
	 * This method is used to send simple email
	 * 
	 * @author rodolfopeixoto
	 * @date Nov 1, 2023
	 * @return boolean
	 * @param recipientEmail
	 * @param subject
	 * @param message
	 * @return
	 * @throws MessagingException
	 * @exception Description
	 */
	public boolean sendEmailWithoutAttachment(String recipientEmail, String subject, String message)
			throws MessagingException;

}
