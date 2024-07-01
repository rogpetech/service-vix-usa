package com.service.vix.mapper;

import com.service.vix.dto.ProductDTO;
import com.service.vix.models.Product;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-30T21:48:48-0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.2 (Oracle Corporation)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public Product productDTOToProduct(ProductDTO productDto) {
        if ( productDto == null ) {
            return null;
        }

        Product product = new Product();

        product.setCreatedAt( productDto.getCreatedAt() );
        product.setUpdatedAt( productDto.getUpdatedAt() );
        product.setCreatedBy( productDto.getCreatedBy() );
        product.setUpdatedBy( productDto.getUpdatedBy() );
        product.setProductName( productDto.getProductName() );
        product.setDiscription( productDto.getDiscription() );
        product.setRegularPrice( productDto.getRegularPrice() );
        product.setAverageCost( productDto.getAverageCost() );
        product.setMemberPrice( productDto.getMemberPrice() );
        product.setProductImage( productDto.getProductImage() );

        return product;
    }

    @Override
    public ProductDTO productToProductDTO(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductDTO productDTO = new ProductDTO();

        productDTO.setProductId( product.getId() );
        productDTO.setProductName( product.getProductName() );
        productDTO.setDiscription( product.getDiscription() );
        productDTO.setRegularPrice( product.getRegularPrice() );
        productDTO.setMemberPrice( product.getMemberPrice() );
        productDTO.setAverageCost( product.getAverageCost() );
        productDTO.setProductImage( product.getProductImage() );
        productDTO.setCreatedAt( product.getCreatedAt() );
        productDTO.setUpdatedAt( product.getUpdatedAt() );
        productDTO.setCreatedBy( product.getCreatedBy() );
        productDTO.setUpdatedBy( product.getUpdatedBy() );
        productDTO.setProductCategory( product.getProductCategory() );

        return productDTO;
    }
}
