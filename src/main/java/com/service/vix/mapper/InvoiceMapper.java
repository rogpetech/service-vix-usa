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
import com.service.vix.models.Invoice;

/**
 * This interface is used to convert invoice to invoice DTO and vies versa
 */
@Mapper(componentModel = "spring")
public interface InvoiceMapper {

	InvoiceMapper INSTANCE = Mappers.getMapper(InvoiceMapper.class);

	/**
	 * This method is used to convert invoiceDTO object to invoice
	 * 
	 * @author ritiks
	 * @date Aug 7, 2023
	 * @param invoiceDTO
	 * @return
	 */
	@Mapping(target = "estimate", source = "estimateDTO")
	Invoice invoiceDTOTOInvoice(InvoiceDTO invoiceDTO);

	/**
	 * This method is used to convert invoice object to invoice DTO
	 * 
	 * @author ritiks
	 * @date Aug 7, 2023
	 * @param invoice
	 * @return
	 */
	@Mapping(target = "estimateDTO", source = "estimate")
	InvoiceDTO invoiceToInvoiceDTO(Invoice invoice);

}
