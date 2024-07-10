/* 
 * ===========================================================================
 * File Name RecieveInvoicePaymentRepository.java
 * 
 * Created on Dec 19, 2023
 * ===========================================================================
 */
/**
* Class Information
* @author rodolfopeixoto
* @version 1.2 - Dec 19, 2023
*/
package com.service.vix.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.vix.models.Customer;
import com.service.vix.models.RecieveInvoicePayment;

@Repository
public interface RecieveInvoicePaymentRepository extends JpaRepository<RecieveInvoicePayment, Long> {

	List<RecieveInvoicePayment> findByFromCustomer(Customer fromCustomer);
	
}
