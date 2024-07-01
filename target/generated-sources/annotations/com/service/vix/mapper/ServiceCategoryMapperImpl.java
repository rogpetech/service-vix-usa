package com.service.vix.mapper;

import com.service.vix.dto.ServiceCategoryDTO;
import com.service.vix.models.ServiceCategory;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-30T21:48:48-0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.2 (Oracle Corporation)"
)
public class ServiceCategoryMapperImpl implements ServiceCategoryMapper {

    @Override
    public ServiceCategory serviceCategoryDTOTosSrviceCategory(ServiceCategoryDTO serviceCategoryDTO) {
        if ( serviceCategoryDTO == null ) {
            return null;
        }

        ServiceCategory serviceCategory = new ServiceCategory();

        serviceCategory.setCreatedAt( serviceCategoryDTO.getCreatedAt() );
        serviceCategory.setUpdatedAt( serviceCategoryDTO.getUpdatedAt() );
        serviceCategory.setCreatedBy( serviceCategoryDTO.getCreatedBy() );
        serviceCategory.setUpdatedBy( serviceCategoryDTO.getUpdatedBy() );
        serviceCategory.setServiceCategoryName( serviceCategoryDTO.getServiceCategoryName() );
        serviceCategory.setServiceCategoryImage( serviceCategoryDTO.getServiceCategoryImage() );
        serviceCategory.setDiscription( serviceCategoryDTO.getDiscription() );
        serviceCategory.setActivationStatus( serviceCategoryDTO.isActivationStatus() );

        return serviceCategory;
    }

    @Override
    public ServiceCategoryDTO serviceCategoryToServiceCategoryDTO(ServiceCategory serviceCategory) {
        if ( serviceCategory == null ) {
            return null;
        }

        ServiceCategoryDTO serviceCategoryDTO = new ServiceCategoryDTO();

        serviceCategoryDTO.setServiceCategoryId( serviceCategory.getId() );
        serviceCategoryDTO.setServiceCategoryName( serviceCategory.getServiceCategoryName() );
        serviceCategoryDTO.setServiceCategoryImage( serviceCategory.getServiceCategoryImage() );
        serviceCategoryDTO.setDiscription( serviceCategory.getDiscription() );
        serviceCategoryDTO.setActivationStatus( serviceCategory.isActivationStatus() );
        serviceCategoryDTO.setCreatedAt( serviceCategory.getCreatedAt() );
        serviceCategoryDTO.setUpdatedAt( serviceCategory.getUpdatedAt() );
        serviceCategoryDTO.setCreatedBy( serviceCategory.getCreatedBy() );
        serviceCategoryDTO.setUpdatedBy( serviceCategory.getUpdatedBy() );

        return serviceCategoryDTO;
    }
}
