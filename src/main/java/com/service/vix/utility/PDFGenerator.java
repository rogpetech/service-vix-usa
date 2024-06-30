/* 
 * ===========================================================================
 * File Name PDFGenerator.java
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
 * $Log: PDFGenerator.java,v $
 * ===========================================================================
 */
/**
* Class Information
* @author rajeshy3 - Chetu
* @version 1.0 - 20-Jul-2023
*/
package com.service.vix.utility;

import java.io.ByteArrayOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;

@Component
public class PDFGenerator {
	@Autowired
	private TemplateEngine templateEngine;

	public byte[] generatePDF(String templateName, Context context) {
		String processedHtml = templateEngine.process(templateName, context);

		// Initialize the ByteArrayOutputStream to store the PDF content
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ITextRenderer renderer = new ITextRenderer();

		try {
			renderer.setDocumentFromString(processedHtml);
			renderer.layout();
			renderer.createPDF(outputStream);
		} catch (DocumentException e) {
			System.err.println("Error while generating PDF: " + e.getMessage());
			e.printStackTrace();
		}

		return outputStream.toByteArray();
	}
}
