package com.service.vix.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Description;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.vix.models.Customer;
import com.service.vix.models.Organization;

/**
 * This interface is used to handle all the methods for Customer Table
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	/**
	 * This method is used to get customer by customer name
	 * 
	 * @author rodolfopeixoto
	 * @date Jun 8, 2023
	 * @return Optional<Customer>
	 * @param customerName
	 * @return
	 * @exception Description
	 */
	Optional<Customer> findByCustomerName(String customerName);

	/**
	 * This method is used to find customer by keyword
	 * 
	 * @author rodolfopeixoto
	 * @date Jul 12, 2023
	 * @return List<Customer>
	 * @param customerName
	 * @param organization
	 * @return
	 * @exception Description
	 */
	List<Customer> findBycustomerNameLikeAndOrganization(String customerName, Organization organization);

	/**
	 * This method is used to find customer by keyword
	 * 
	 * @author rodolfopeixoto
	 * @date Jul 12, 2023
	 * @return List<Customer>
	 * @param customerName
	 * @param organization
	 * @return
	 * @exception Description
	 */
	List<Customer> findByOrganizationAndCustomerNameLikeOrderByIdDesc(Organization organization, String customerName);

	/**
	 * This method is used to get Deleted False Customers
	 * 
	 * @author rodolfopeixoto
	 * @date Aug 8, 2023
	 * @return List<Customer>
	 * @param organization
	 * @return
	 * @exception Description
	 */
	/*
	 * List<Customer> findAllByIsDeletedFalseAndOrganization(Organization
	 * organization);
	 */
	List<Customer> findAllByIsDeletedFalseAndOrganizationOrderByCreatedAtDesc(Organization organization);
	
	
	/**
	 * This method is 
	 * @author hemantr
	 * @date Sep 21, 2023
	 * @return Boolean 
	 * @param email
	 * @return
	 * @exception 
	 * Description
	 */
	Boolean existsByEmails_Email(String email);

}
