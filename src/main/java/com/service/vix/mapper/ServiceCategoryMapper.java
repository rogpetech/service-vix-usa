package com.service.vix.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Description;

import com.service.vix.dto.ServiceCategoryDTO;
import com.service.vix.models.ServiceCategory;

/**
 * This class is used as mapper that map Service Category Entity to Product
 * Categpry DTO and Vise Versa
 */

@Mapper
public interface ServiceCategoryMapper {

	ServiceCategoryMapper INSTANCE = Mappers.getMapper(ServiceCategoryMapper.class);
	
	/**
	 * This method is used to map Service Category DTO to Service Category Entity
	 * @author hemantr
	 * @date Jun 8, 2023
	 * @return serviceCategory
	 * @param ServiceCategoryDTO
	 * @return
	 * @exception Description
	 */
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "isDeleted", ignore = true)
	@Mapping(target = "parentServiceCategory", ignore = true)
	ServiceCategory serviceCategoryDTOTosSrviceCategory(ServiceCategoryDTO serviceCategoryDTO);
	
	/**
	 * This method is used to map Service Category Entity to Service Category DTO
	 * @author hemantr
	 * @date Jun 8, 2023
	 * @return ServiceCategoryDTO
	 * @param serviceCategory
	 * @return
	 * @exception Description
	 */
	@Mapping(target = "serviceCategoryId", source = "id")
	@Mapping(target = "parentServiceCategoryId", ignore = true)
	ServiceCategoryDTO serviceCategoryToServiceCategoryDTO(ServiceCategory serviceCategory);
}
