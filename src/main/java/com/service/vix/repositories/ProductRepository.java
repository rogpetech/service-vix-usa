package com.service.vix.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Description;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.vix.models.Organization;
import com.service.vix.models.Product;
import com.service.vix.models.ProductCategory;

/**
 *
 * This interface is used as repository or handle all the methods related to
 * product
 *
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	/**
	 * Add method description here
	 * 
	 * @author rodolfopeixoto
	 * @date Jun 22, 2023
	 * @param productName
	 * @return
	 */
	Boolean existsByProductName(String productName);

	/**
	 * This method is used to search product by given product name
	 * 
	 * @author rodolfopeixoto
	 * @date Jun 22, 2023
	 * @return List<Product>
	 * @param productName
	 * @param organization
	 * @return
	 * @exception Description
	 */
	List<Product> findByProductNameLikeAndIsDeletedFalseAndOrganization(String productName, Organization organization);

	/**
	 * Get Product from database by product name
	 * 
	 * @author hemantr
	 * @date Jun 24, 2023
	 * @param productName
	 * @return
	 */
	Optional<Product> findByProductName(String productName);

	/**
	 * This method is used to get all products whose isDeleted is false
	 * 
	 * @author rodolfopeixoto
	 * @date Sep 4, 2023
	 * @return List<Product>
	 * @param organization
	 * @return
	 * @exception Description
	 */
	List<Product> findAllByIsDeletedFalseAndOrganization(Organization organization);

	/**
	 * This method is used to check product exists by product category
	 * 
	 * @author rodolfopeixoto
	 * @date Aug 2, 2023
	 * @param productCategory
	 * @return
	 */
	Boolean existsByProductCategoryAndIsDeletedFalse(ProductCategory productCategory);

}
