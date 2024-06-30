/* 
 * ===========================================================================
 * File Name RecieveInvoicePaymentDTO.java
 * 
 * Created on Dec 5, 2023
 * ===========================================================================
 */
/**
* Class Information
* @author ritiks
* @version 1.2 - Dec 5, 2023
*/
package com.service.vix.dto;

import java.time.LocalDateTime;
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
public class RecieveInvoicePaymentDTO {

	private Long id;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private String createdBy;

	private String updatedBy;

	private String paymentMethod;

	private String reference;

	private LocalDateTime recievedOn;

	private String paymentNotes;

	private Float totalOutstanding;

	private List<InvoiceDTO> invoices;

	private CustomerDTO fromCustomer;

	private UserDTO recievedBy;

	private Long invoiceId;

	private Long fromCustomerId;

	private Long recievedById;

	private CustomerDTO customerDTO;

	private UserDTO loggedInUser;
	
	private String invoiceIdsStr;
	
	private Float invoiceTotal;
}
