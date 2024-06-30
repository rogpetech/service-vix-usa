package com.service.vix.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Description;

import com.service.vix.dto.ProductCategoryDTO;
import com.service.vix.models.ProductCategory;

/**
 * This class is used as mapper that map Product Category Entity to Product
 * Category DTO and Vies Versa
 */
@Mapper
public interface ProductCategoryMapper {

	ProductCategoryMapper INSTANCE = Mappers.getMapper(ProductCategoryMapper.class);

	/**
	 * This method is used to map Product Category DTO to Product Category Entity
	 * 
	 * @author hemantr
	 * @date Jun 7, 2023
	 * @return ProductCategory
	 * @param productCategoryDTO
	 * @return
	 * @exception Description
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "isDeleted", ignore = true)
	@Mapping(target = "parentProductCategory", ignore = true)
	ProductCategory productCategoryDTOToProductCategory(ProductCategoryDTO productCategoryDTO);

	/**
	 * This method is used to map Product Category Entity to Product Category DTO
	 * 
	 * @author hemantr
	 * @date Jun 7, 2023
	 * @return ProductCategoryDTO
	 * @param productCategory
	 * @return
	 * @exception Description
	 */
	@Mapping(target = "productCategoryId", source = "id")
	@Mapping(target = "parentProductCategoryId", ignore = true)
	ProductCategoryDTO productCategoryToProductCategoryDTO(ProductCategory productCategory);

}
