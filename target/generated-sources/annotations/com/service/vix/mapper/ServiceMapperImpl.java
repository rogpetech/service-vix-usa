package com.service.vix.mapper;

import com.service.vix.dto.ServiceDTO;
import com.service.vix.models.Services;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-30T21:48:49-0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.2 (Oracle Corporation)"
)
@Component
public class ServiceMapperImpl implements ServiceMapper {

    @Override
    public Services serviceDTOToService(ServiceDTO serviceDto) {
        if ( serviceDto == null ) {
            return null;
        }

        Services services = new Services();

        services.setCreatedAt( serviceDto.getCreatedAt() );
        services.setUpdatedAt( serviceDto.getUpdatedAt() );
        services.setCreatedBy( serviceDto.getCreatedBy() );
        services.setUpdatedBy( serviceDto.getUpdatedBy() );
        services.setServiceName( serviceDto.getServiceName() );
        services.setDiscription( serviceDto.getDiscription() );
        services.setRegularPrice( serviceDto.getRegularPrice() );
        services.setInternalCost( serviceDto.getInternalCost() );
        services.setServiceImage( serviceDto.getServiceImage() );

        return services;
    }

    @Override
    public ServiceDTO serviceTOServiceDTO(Services service) {
        if ( service == null ) {
            return null;
        }

        ServiceDTO serviceDTO = new ServiceDTO();

        serviceDTO.setServiceCategoryId( service.getId() );
        serviceDTO.setId( service.getId() );
        serviceDTO.setServiceName( service.getServiceName() );
        serviceDTO.setDiscription( service.getDiscription() );
        serviceDTO.setRegularPrice( service.getRegularPrice() );
        serviceDTO.setInternalCost( service.getInternalCost() );
        serviceDTO.setServiceImage( service.getServiceImage() );
        serviceDTO.setCreatedAt( service.getCreatedAt() );
        serviceDTO.setUpdatedAt( service.getUpdatedAt() );
        serviceDTO.setCreatedBy( service.getCreatedBy() );
        serviceDTO.setUpdatedBy( service.getUpdatedBy() );
        serviceDTO.setServiceCategory( service.getServiceCategory() );

        return serviceDTO;
    }
}
