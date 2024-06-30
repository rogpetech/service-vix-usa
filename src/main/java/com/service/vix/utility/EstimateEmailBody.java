/* 
 * ===========================================================================
 * File Name EstimateEmailBody.java
 * 
 * Created on Jul 20, 2023
 *
 * This code contains copyright information which is the proprietary property
 * of Chetu India Pvt. Ltd. No part of this code may be reproduced, stored or transmitted
 * in any form without the prior written permission of CHETU.
 *
 * Copyright (C) CHETU. 2023
 * All rights reserved.
 *
 * Modification history:
 * $Log: EstimateEmailBody.java,v $
 * ===========================================================================
 */
package com.service.vix.utility;

public class EstimateEmailBody {

	/**
	 * This method is used for providing the body of the email message.
	 * 
	 * @author rajeshy3
	 * @date 21-Jul-2023
	 * @param customerName
	 * @return
	 */
	public static String getBody(String customerName) {
		String body = "Dear " + customerName + ",\r\n"
				+ "Thank you for considering ServiceVix for your product/service needs. We appreciate the opportunity to provide you with a customized estimate based on your requirements. We have carefully reviewed your project details and prepared an estimate tailored specifically to meet your needs.\r\n"
				+ "                                                \r\n"
				+ "Please find in attachment a breakdown of our estimate:\r\n"
				+ "We believe our estimate accurately reflects the time, effort, and expertise required to successfully complete your project/service. Should you have any questions or require further clarification, please do not hesitate to reach out to our team. We are more than happy to discuss any aspects of the estimate with you.\r\n"
				+ "\r\n"
				+ "Please note that this estimate is valid for a period of 5 from the date of this email. If you wish to proceed with our services, kindly inform us within this timeframe, and we will be glad to initiate the next steps.\r\n"
				+ "\r\n"
				+ "We sincerely appreciate your consideration and look forward to the possibility of working with you. If you have any additional requirements or if there are any changes to your project/service, please let us know, and we will be happy to revise the estimate accordingly.\r\n"
				+ "\r\n"
				+ "Thank you once again for considering ServiceVix. We are committed to providing you with exceptional [project/service] and ensuring your utmost satisfaction.\r\n"
				+ "\r\n" + "Best regards,\r\n" + "Service Vix";
		return body;
	}

	/**
	 * This method is used for convert plan text to HTML formated content
	 * 
	 * @author rajeshy3
	 * @date 21-Jul-2023
	 * @param plainText
	 * @return
	 */
	public static String convertToHtml(String plainText) {
		StringBuilder htmlContent = new StringBuilder();
		String[] lines = plainText.split("\\r\\n");
		for (String line : lines) {
			htmlContent.append("<p>").append(line).append("</p>");
		}
		return htmlContent.toString();
	}

}
