package com.service.vix.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import com.service.vix.dto.ProductDTO;
import com.service.vix.models.Product;

/**
 * This class is used as mapper that map Product Entity to Product DTO and Vise
 * Versa
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {

	ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

	/**
	 * This method is used to map Product DTO to Product Entity
	 * 
	 * @author hemantr
	 * @date Jun 19, 2023
	 * @param productDto
	 * @return
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "productCategory", ignore = true)
	@Mapping(target = "activationStatus", ignore = true)
	@Mapping(target = "isDeleted", ignore = true)
	Product productDTOToProduct(ProductDTO productDto);

	/**
	 * This method is used to map Product Entity to Product DTO
	 * 
	 * @author hemantr
	 * @date Jun 22, 2023
	 * @param product
	 * @return
	 */
	@Mapping(target = "productId", source = "id")
	@Mapping(target = "productCategoryId", ignore = true)
	ProductDTO productToProductDTO(Product product);
}
