/* 
 * ===========================================================================
 * File Name InvoiceEmailDTO.java
 * 
 * Created on Nov 27, 2023
 * ===========================================================================
 */
 /**
 * Class Information
 * @author hemantr
 * @version 1.2 - Nov 27, 2023
 */
 package com.service.vix.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InvoiceEmailDTO {
		private String fromEmail;
		private String toEmail;
		private String subject;
		private String body;
		private List<Long> optionIds;
		private Long invoiceId;

		// Attach handle
		private Boolean itemPrice = false;
		private Boolean itemTotal = false;
		private Boolean itemQuantity = false;
		private Boolean grandTotal = false;
}
