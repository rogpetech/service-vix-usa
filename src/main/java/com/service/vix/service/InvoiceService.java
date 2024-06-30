/* 
 * ===========================================================================
 * File Name InvoiceService.java
 * 
 * Created on Aug 7, 2023
 *
 * This code contains copyright information which is the proprietary property
 * of Chetu India Pvt. Ltd. No part of this code may be reproduced, stored or transmitted
 * in any form without the prior written permission of CHETU.
 *
 * Copyright (C) CHETU. 2023
 * All rights reserved.
 *
 * Modification history:
 * $Log: InvoiceService.java,v $
 * ===========================================================================
 */
package com.service.vix.service;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.InvoiceDTO;
import com.service.vix.dto.InvoiceEmailDTO;
import com.service.vix.dto.RecieveInvoicePaymentDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * This interface is used as Invoice Service
 */
@Component
public interface InvoiceService {

	/**
	 * This method is used for create Invoice
	 * 
	 * @author ritiks
	 * @date Aug 7, 2023
	 * @param invoiceDTO
	 * @return
	 */
	CommonResponse<InvoiceDTO> createInvoice(InvoiceDTO invoiceDTO);

	/**
	 * This method is used to get all invoices from the database
	 * 
	 * @author hemantr
	 * @date Oct 11, 2023
	 * @return CommonResponse<List<InvoiceDTO>>
	 * @return
	 * @exception Description
	 */
	CommonResponse<List<InvoiceDTO>> getInvoices();

	/**
	 * This method is used to find invoice by id
	 * 
	 * @author hemantr
	 * @date Nov 2, 2023
	 * @return CommonResponse<EstimateDTO>
	 * @param invoiceId
	 * @return
	 * @exception Description
	 */
	CommonResponse<InvoiceDTO> getInvoiceById(Long invoiceId);

	/**
	 * This method is used to extract Save invoice form data like extract multiple
	 * 
	 * @author hemantr
	 * @date Nov 2, 2023
	 * @return InvoiceDTO
	 * @return
	 * @exception Description
	 */
	InvoiceDTO extractInvoiceFormObject(HttpServletRequest servletRequest);

	/**
	 * This method is used to save invoice
	 * 
	 * @author hemantr
	 * @date Nov 2, 2023
	 * @return CommonResponse<InvoiceDTO>
	 * @param invoiceDTO
	 * @param httpSession
	 * @param principal
	 * @return
	 * @exception Description
	 */
	CommonResponse<InvoiceDTO> saveInvoice(InvoiceDTO invoiceDTO, HttpSession httpSession, Principal principal);

	/**
	 * This method is used to remove invoice
	 * 
	 * @author hemantr
	 * @date Nov 2, 2023
	 * @param invoiceId
	 * @param requestType
	 * @param optionId
	 * @exception Description
	 */
	CommonResponse<Boolean> removeInvoice(Long invoiceId, HttpSession httpSession);

	/**
	 * This method is used to update the invoice
	 * 
	 * @author hemantr
	 * @date Nov 2, 2023
	 * @return CommonResponse<InvoiceDTO>
	 * @param invoiceDTO
	 * @param httpSession
	 * @return
	 * @exception Description
	 */
	CommonResponse<InvoiceDTO> updateInvoice(InvoiceDTO invoiceDTO, HttpSession httpSession);

	/**
	 * Get invoice of User's associated Organization
	 * 
	 * @author hemantr
	 * @date Nov 2, 2023
	 * @return CommonResponse<List<InvoiceDTO>>
	 * @param principal
	 * @return
	 * @exception Description
	 */
	CommonResponse<List<InvoiceDTO>> getOrganizationInvoices(Principal principal);

	/**
	 * Get Logged In User Estimates
	 * 
	 * @author hemantr
	 * @date Nov 2, 2023
	 * @return CommonResponse<List<InvoiceDTO>>
	 * @param userName
	 * @return
	 * @exception Description
	 */
	CommonResponse<List<InvoiceDTO>> getUserInvoices(Principal principal);

	/**
	 * This method is used to generate selected estimate products invoice
	 * 
	 * @author ritiks
	 * @date Nov 9, 2023
	 * @return CommonResponse<InvoiceDTO>
	 * @param datas
	 * @return
	 * @exception Description
	 */
	CommonResponse<InvoiceDTO> generateSelectedInvoice(Map<String, List<String>> data, HttpSession httpSession,
			Principal principal);

	/**
	 * @author hemantr
	 * @date Nov 13, 2023
	 * @return CommonResponse<Boolean>
	 * @param estimateEmailDTO
	 * @param response
	 * @param httpSession
	 * @return
	 * @exception Description
	 */

	CommonResponse<Boolean> sendInvoiceEmail(InvoiceEmailDTO invoiceEmailDTO, HttpServletResponse response,
			HttpSession httpSession);

	/**
	 * @author hemantr
	 * @date Nov 21, 2023
	 * @return Boolean
	 * @param invoiceId
	 * @return
	 * @exception Description
	 */
	Boolean existsByInvoiceNumber(Long invoiceId);

	/**
	 * @author hemantr
	 * @date Nov 27, 2023
	 * @return Boolean
	 * @param estimateId
	 * @return
	 * @exception Description
	 */
	Boolean existsByEstimateId(Long estimateId);

	/**
	 * This method is used to get details for Receive Invoice Payment Details
	 * 
	 * @author ritiks
	 * @date Dec 5, 2023
	 * @return CommonResponse<RecieveInvoicePaymentDTO>
	 * @param selectedInvoices
	 * @param selectedInvoiceCustomerId
	 * @param principal
	 * @return
	 * @exception Description
	 */
	CommonResponse<RecieveInvoicePaymentDTO> recievePayment(String selectedInvoices, String selectedInvoiceCustomerId,
			Principal principal);

}
