package com.service.vix.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.vix.models.Organization;

/**
 * This class is used as a repository to handle all the database methods related
 * to Organization
 */
@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

	/**
	 * This method is used to check organization with given name
	 * 
	 * @author rodolfopeixoto
	 * @date Sep 4, 2023
	 * @return Optional<Organization>
	 * @param orgName
	 * @return
	 * @exception Description
	 */
	Optional<Organization> findByOrgName(String orgName);

	/**
	 * This method is used to check organization with given name
	 * 
	 * @author rodolfopeixoto
	 * @date Sep 19, 2023
	 * @return Boolean
	 * @param orgName
	 * @return
	 * @exception Description
	 */
	Boolean existsByOrgName(String orgName);

}
