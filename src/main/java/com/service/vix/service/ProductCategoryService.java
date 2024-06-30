package com.service.vix.service;

import java.security.Principal;
import java.util.List;

import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.ProductCategoryDTO;

import jakarta.servlet.http.HttpSession;

/**
 * This class is used as service class that have all the method declaration for
 * Product Category Entity
 */
@Component
public interface ProductCategoryService {

	/**
	 * This method is used to add Product Category
	 * 
	 * @author hemantr
	 * @date Jun 7, 2023
	 * @return CommonResponse<ProductCategoryDTO>
	 * @param productCategoryDTO
	 * @param httpSession
	 * @return
	 * @exception Description
	 */
	public CommonResponse<ProductCategoryDTO> addProductCategory(ProductCategoryDTO productCategoryDTO,
			HttpSession httpSession, Principal principal);

	/**
	 * This method is used to get all the Product Categories
	 * 
	 * @author hemantr
	 * @date Jun 7, 2023
	 * @return CommonResponse<List<ProductCategoryDTO>>
	 * @param principal
	 * @return
	 * @exception Description
	 */
	public CommonResponse<List<ProductCategoryDTO>> getProductCategories(Principal principal);

	/**
	 * This method is used to get Product Category By Id
	 * 
	 * @author hemantr
	 * @date Jun 7, 2023
	 * @return CommonResponse<ProductCategoryDTO>
	 * @param productCategoryId
	 * @return
	 * @exception Description
	 */
	public CommonResponse<ProductCategoryDTO> getProductCategoryById(Long productCategoryId);

	/**
	 * This method is used to remove Product Category by id
	 * 
	 * @author hemantr
	 * @date Jun 7, 2023
	 * @return CommonResponse<Boolean>
	 * @param productCategoryId
	 * @return
	 * @exception Description
	 */
	public CommonResponse<Boolean> removeProductCategory(Long productCategoryId);

	/**
	 * This method is used to update Product Category
	 * 
	 * @author hemantr
	 * @date Jun 7, 2023
	 * @return CommonResponse<ProductCategoryDTO>
	 * @param productCategoryDTO
	 * @return
	 * @exception Description
	 */
	public CommonResponse<ProductCategoryDTO> updateProductCategory(ProductCategoryDTO productCategoryDTO);

}
