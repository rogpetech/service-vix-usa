/* 
 * ===========================================================================
 * File Name InvoiceRepository.java
 * 
 * Created on Aug 5, 2023
 *
 * This code contains copyright information which is the proprietary property
 * of Chetu India Pvt. Ltd. No part of this code may be reproduced, stored or transmitted
 * in any form without the prior written permission of CHETU.
 *
 * Copyright (C) CHETU. 2023
 * All rights reserved.
 *
 * Modification history:
 * $Log: InvoiceRepository.java,v $
 * ===========================================================================
 */
package com.service.vix.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Description;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.service.vix.models.Customer;
import com.service.vix.models.Invoice;
import com.service.vix.models.Organization;

/**
 * This class is used as Repository for Invoice
 */
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

	/**
	 * This method is used to get invoice by user name
	 * 
	 * @author hemantr
	 * @date Nov 2, 2023
	 * @return Optional<List<Invoice>>
	 * @param organization
	 * @return
	 * @exception Description
	 */
	Optional<List<Invoice>> findByOrganizationAndIsDeletedFalse(Organization organization);

	/**
	 * This method is used to get non-deleted invoice
	 * 
	 * @author hemantr
	 * @date Nov 2, 2023
	 * @return List<Invoice>
	 * @param username
	 * @return
	 * @exception Description
	 */
	List<Invoice> findByCreatedByAndIsDeletedFalse(String username);

	/**
	 * This method is used to get max invoice number
	 * 
	 * @author rodolfopeixoto
	 * @date Nov 21, 2023
	 * @return Long
	 * @return
	 * @exception Description
	 */
	@Query(value = "SELECT max(e.invoiceNumber) FROM Invoice e")
	Long findMaxInvoiceNumber();

	/**
	 * This method is used to get max invoice ifd
	 * 
	 * @author rodolfopeixoto
	 * @date Nov 27, 2023
	 * @return Long
	 * @return
	 * @exception Description
	 */
	@Query(value = "SELECT max(e.id) FROM Invoice e")
	Long findMaxInvoiceId();

	/**
	 * @author hemantr
	 * @date Nov 21, 2023
	 * @return Long
	 * @param invoiceId
	 * @return
	 * @exception Description
	 */
	Boolean existsByInvoiceNumber(Long invoiceId);

	/**
	 * @author hemantr
	 * @date Nov 21, 2023
	 * @return List<Invoice>
	 * @return
	 * @exception Description
	 */
	List<Invoice> findAllByIsDeletedFalseOrderByCreatedAtDesc();

	/**
	 * This method is used to get All invoices of customer
	 * 
	 * @author rodolfopeixoto
	 * @date Nov 28, 2023
	 * @return List<Invoice>
	 * @param customer
	 * @return
	 * @exception Description
	 */
	List<Invoice> findByCustomerAndIsDeleted(Customer customer,Boolean isDeleted);
	
	/**
	 * @author hemantr
	 * @date Nov 21, 2023
	 * @return Long
	 * @param invoiceId
	 * @return
	 * @exception Description
	 */
	Boolean existsByEstimateId(Long estimateId);

}
