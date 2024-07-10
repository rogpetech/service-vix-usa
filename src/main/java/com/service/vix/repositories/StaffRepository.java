/* 
 * ===========================================================================
 * File Name StaffRepository.java
 * 
 * Created on Aug 21, 2023
 * ===========================================================================
 */
/**
* Class Information
* @author rodolfopeixoto
* @version 1.2 - Aug 21, 2023
*/
package com.service.vix.repositories;

import java.util.List;

import org.springframework.context.annotation.Description;
import org.springframework.data.jpa.repository.JpaRepository;

import com.service.vix.models.Organization;
import com.service.vix.models.Staff;

public interface StaffRepository extends JpaRepository<Staff, Long> {

	/**
	 * This method is used to get staffs that is not deleted and also with
	 * organization id
	 * 
	 * @author rodolfopeixoto
	 * @date Sep 2, 2023
	 * @return List<Staff>
	 * @param organization
	 * @return
	 * @exception Description
	 */
	/*
	 * List<Staff> findAllByIsDeletedFalseAndOrganization(Organization
	 * organization);
	 */
	
	List<Staff> findAllByIsDeletedFalseAndOrganizationOrderByCreatedAtDesc(Organization organization);

}
