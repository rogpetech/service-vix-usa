package com.service.vix.service;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.CustomerDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * This class is used to declare all the methods to handle for Customer
 */
@Component
public interface CustomerService {

	/**
	 * This method is used to add customer
	 * 
	 * @author hemantr
	 * @date Jun 8, 2023
	 * @return CommonResponse<CustomerDTO>
	 * @param customerDTO
	 * @param httpSession
	 * @return
	 * @exception Description
	 */
	CommonResponse<CustomerDTO> addCustomer(CustomerDTO customerDTO, HttpSession httpSession, Principal principal);

	/**
	 * This method is used to get all customers
	 * 
	 * @author hemantr
	 * @date Jun 8, 2023
	 * @return CommonResponse<List<CustomerDTO>>
	 * @return
	 * @exception Description
	 */
	CommonResponse<List<CustomerDTO>> getCustomers(Principal principal);

	/**
	 * This method is used to get customer by customer id
	 * 
	 * @author hemantr
	 * @date Jun 8, 2023
	 * @return CommonResponse<CustomerDTO>
	 * @param customerId
	 * @return
	 * @exception Description
	 */
	CommonResponse<CustomerDTO> getCustomerById(Long customerId);

	/**
	 * This method is used to remove Customer
	 * 
	 * @author hemantr
	 * @date Jun 8, 2023
	 * @return CommonResponse<Boolean>
	 * @param customerId
	 * @return
	 * @exception Description
	 */
	CommonResponse<Boolean> removeCustomer(Long customerId);

	/**
	 * This method is used to update customer
	 * 
	 * @author hemantr
	 * @date Jun 8, 2023
	 * @return CommonResponse<CustomerDTO>
	 * @param customerDTO
	 * @return
	 * @exception Description
	 */
	CommonResponse<CustomerDTO> updateCustomer(CustomerDTO customerDTO);

	/**
	 * This method is used to extract customer form object and create a proper
	 * CustomerDTO object
	 * 
	 * @author hemantr
	 * @date Jun 8, 2023
	 * @return CustomerDTO
	 * @param customerDTO
	 * @param httpServletRequest
	 * @return extractAddCustomerFormObject
	 * @exception Description
	 */
	CustomerDTO extractAddCustomerFormObject(CustomerDTO customerDTO, HttpServletRequest httpServletRequest);

	/**
	 * This method is used to get customer by customer Name
	 * 
	 * @author hemantr
	 * @date Jun 20, 2023
	 * @param customer
	 * @return findByCustomerName
	 */
	CommonResponse<CustomerDTO> findByCustomerName(String customer);

	/**
	 * 
	 * @author rodolfopeixoto
	 * @date Aug 8, 2023
	 * @param customer
	 * @return
	 */
	CommonResponse<List<CustomerDTO>> searchCustomer(String customer, Principal principal);

	/**
	 * This method is used to get customers for any organization
	 * 
	 * @author rodolfopeixoto
	 * @date Aug 8, 2023
	 * @return CommonResponse<List<CustomerDTO>>
	 * @param principal
	 * @return
	 * @exception Description
	 */
	CommonResponse<List<CustomerDTO>> getOrganisationCustomers(Principal principal);

	/**
	 * This method is used to check exist customer by customer Email.
	 * 
	 * @author hemantr
	 * @date Sep 21, 2023
	 * @return Boolean
	 * @param mail
	 * @return
	 * @exception Description
	 */
	Map<Boolean, String> serachCustomerByEmail(String mail);

	/**
	 * This method is used to get customer details with invoices
	 * 
	 * @author rodolfopeixoto
	 * @date Nov 28, 2023
	 * @return CommonResponse<List<CustomerDTO>>
	 * @param principal
	 * @return
	 * @exception Description
	 */
	CommonResponse<List<CustomerDTO>> getOrganisationCustomersWithInvoices(Principal principal);

}
