package com.service.vix.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Description;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.vix.models.Organization;
import com.service.vix.models.ServiceCategory;

/**
 * This interface is used as repository or handle all the methods related to
 * service category
 */
@Repository
public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Long> {
	
	
	/**
	 * This method is used to get Service Category By name
	 * 
	 * @param serviceCategoryName
	 * @return
	 */
	Optional<ServiceCategory> findByServiceCategoryNameAndIsDeletedFalse(String serviceCategoryName);
	
	/**
     * This method is used to get all service categories whose isDeleted is false
     * @author hemantr 
     * @date Jul 27, 2023 
     * @return 
     */
    List<ServiceCategory> findAllByIsDeletedFalse();
    
    /**
     * This method is used to get all service categories whose isDeleted is false and associate with organization
     * @author rodolfopeixoto
     * @date Sep 5, 2023
     * @return List<ProductCategory> 
     * @param organization
     * @return
     * @exception 
     * Description
     */
    List<ServiceCategory> findAllByIsDeletedFalseAndOrganization(Organization organization);
    
    
   
}
