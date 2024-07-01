package com.service.vix.mapper;

import com.service.vix.dto.ProductCategoryDTO;
import com.service.vix.models.ProductCategory;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-30T21:48:48-0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.2 (Oracle Corporation)"
)
public class ProductCategoryMapperImpl implements ProductCategoryMapper {

    @Override
    public ProductCategory productCategoryDTOToProductCategory(ProductCategoryDTO productCategoryDTO) {
        if ( productCategoryDTO == null ) {
            return null;
        }

        ProductCategory productCategory = new ProductCategory();

        productCategory.setCreatedAt( productCategoryDTO.getCreatedAt() );
        productCategory.setUpdatedAt( productCategoryDTO.getUpdatedAt() );
        productCategory.setCreatedBy( productCategoryDTO.getCreatedBy() );
        productCategory.setUpdatedBy( productCategoryDTO.getUpdatedBy() );
        productCategory.setProductCategoryName( productCategoryDTO.getProductCategoryName() );
        productCategory.setDiscription( productCategoryDTO.getDiscription() );
        productCategory.setProductCategoryImage( productCategoryDTO.getProductCategoryImage() );
        productCategory.setActivationStatus( productCategoryDTO.isActivationStatus() );

        return productCategory;
    }

    @Override
    public ProductCategoryDTO productCategoryToProductCategoryDTO(ProductCategory productCategory) {
        if ( productCategory == null ) {
            return null;
        }

        ProductCategoryDTO productCategoryDTO = new ProductCategoryDTO();

        productCategoryDTO.setProductCategoryId( productCategory.getId() );
        productCategoryDTO.setProductCategoryName( productCategory.getProductCategoryName() );
        productCategoryDTO.setProductCategoryImage( productCategory.getProductCategoryImage() );
        productCategoryDTO.setDiscription( productCategory.getDiscription() );
        productCategoryDTO.setActivationStatus( productCategory.isActivationStatus() );
        productCategoryDTO.setCreatedAt( productCategory.getCreatedAt() );
        productCategoryDTO.setUpdatedAt( productCategory.getUpdatedAt() );
        productCategoryDTO.setCreatedBy( productCategory.getCreatedBy() );
        productCategoryDTO.setUpdatedBy( productCategory.getUpdatedBy() );

        return productCategoryDTO;
    }
}
