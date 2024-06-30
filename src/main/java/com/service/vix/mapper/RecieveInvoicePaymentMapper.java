/* 
 * ===========================================================================
 * File Name InvoiceMapper.java
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
 * $Log: InvoiceMapper.java,v $
 * ===========================================================================
 */
package com.service.vix.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.service.vix.dto.InvoiceDTO;
import com.service.vix.dto.RecieveInvoicePaymentDTO;
import com.service.vix.models.Invoice;
import com.service.vix.models.RecieveInvoicePayment;

/**
 * This interface is used to convert invoice to invoice DTO and vies versa
 */
@Mapper(componentModel = "spring")
public interface RecieveInvoicePaymentMapper {

	RecieveInvoicePaymentMapper INSTANCE = Mappers.getMapper(RecieveInvoicePaymentMapper.class);

	/**
	 * This method is used to convert RecieveInvoicePayment DTO object to
	 * RecieveInvoicePayment
	 * 
	 * @author ritiks
	 * @date Dec 19, 2023
	 * @return Invoice
	 * @param invoiceDTO
	 * @return
	 * @exception Description
	 */
	RecieveInvoicePayment recieveInvoicePaymentDTOToRecieveInvoicePayment(RecieveInvoicePaymentDTO invoiceDTO);

	/**
	 * This method is used to convert RecieveInvoicePayment object to
	 * RecieveInvoicePayment DTO
	 * 
	 * @author ritiks
	 * @date Dec 19, 2023
	 * @return RecieveInvoicePaymentDTO
	 * @param invoice
	 * @return
	 * @exception Description
	 */
	RecieveInvoicePaymentDTO recieveInvoicePaymentToRecieveInvoicePaymentDTO(RecieveInvoicePayment invoice);

}
