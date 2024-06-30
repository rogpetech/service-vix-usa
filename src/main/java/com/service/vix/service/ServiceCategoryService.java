package com.service.vix.service;

import java.security.Principal;
import java.util.List;

import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.ServiceCategoryDTO;

import jakarta.servlet.http.HttpSession;

@Component
public interface ServiceCategoryService {

	/**
	 * This method is used to add Service Category
	 * 
	 * @author hemantr
	 * @date Jun 8, 2023
	 * @return CommonResponse<ProductCategoryDTO>
	 * @param serviceCategoryDTO
	 * @param httpSession
	 * @return
	 * @exception Description
	 */
	 CommonResponse<ServiceCategoryDTO> addServiceCategory(ServiceCategoryDTO serviceCategoryDTO,
			HttpSession httpSession,Principal principal);

	/**
	 * This method is used to get all the service Categories
	 * 
	 * @author hemantr
	 * @date Jun 8, 2023
	 * @return CommonResponse<List<ServiceCategoryDTO>>
	 * @return
	 * @exception Description
	 */
	 CommonResponse<List<ServiceCategoryDTO>> getServiceCategory(Principal principal);

	/**
	 * This method is used to get Service Category By Id
	 * 
	 * @author hemantr
	 * @date Jun 8, 2023
	 * @return CommonResponse<ProductCategoryDTO>
	 * @param serviceCategoryId
	 * @return
	 * @exception Description
	 */
	 CommonResponse<ServiceCategoryDTO> getServiceCategoryById(Long serviceCategoryId);

	/**
	 * This method is used to remove Service Category by id
	 * 
	 * @author hemantr
	 * @date Jun 8, 2023
	 * @return CommonResponse<Boolean>
	 * @param serviceCategoryId
	 * @return
	 * @exception Description
	 */
	 CommonResponse<Boolean> removeServiceCategory(Long serviceCategoryId);

	/**
	 * This method is used to update Service Category
	 * 
	 * @author hemantr
	 * @date Jun 8, 2023
	 * @return CommonResponse<ProductCategoryDTO>
	 * @param serviceCategoryDTO
	 * @return
	 * @exception Description
	 */
	 CommonResponse<ServiceCategoryDTO> updateServiceCategory(ServiceCategoryDTO serviceCategoryDTO);
}
