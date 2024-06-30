package com.service.vix.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Description;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.vix.models.Organization;
import com.service.vix.models.ProductCategory;

/**
 * This interface is used as repository or handle all the methods related to
 * product category
 */
@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

	/**
	 * This method is used to get Product Category By name
	 * 
	 * @author hemantr
	 * @date Jun 7, 2023
	 * @return Optional<ProductCategory>
	 * @param productCategoryName
	 * @return
	 * @exception Description
	 */
	Optional<ProductCategory> findByProductCategoryNameAndIsDeletedFalse(String productCategoryName);

	/**
	 * This method is used to get all products categories whose isDeleted is false and associate with organization
	 * 
	 * @author hemantr
	 * @date Jul 27, 2023
	 * @return List<ProductCategory>
	 * @param organization
	 * @return
	 * @exception Description
	 */
	List<ProductCategory> findAllByIsDeletedFalseAndOrganization(Organization organization);

}
