package com.service.vix.mapper;

import com.service.vix.dto.OrganizationPlanDTO;
import com.service.vix.models.OrganizationPlan;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-30T21:48:49-0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.2 (Oracle Corporation)"
)
public class OrganizationPlanMapperImpl implements OrganizationPlanMapper {

    @Override
    public OrganizationPlan organizationPlanDtoToOrganizationPlan(OrganizationPlanDTO organizationPlanDTO) {
        if ( organizationPlanDTO == null ) {
            return null;
        }

        OrganizationPlan organizationPlan = new OrganizationPlan();

        organizationPlan.setId( organizationPlanDTO.getId() );
        organizationPlan.setCreatedAt( organizationPlanDTO.getCreatedAt() );
        organizationPlan.setUpdatedAt( organizationPlanDTO.getUpdatedAt() );
        organizationPlan.setCreatedBy( organizationPlanDTO.getCreatedBy() );
        organizationPlan.setUpdatedBy( organizationPlanDTO.getUpdatedBy() );
        organizationPlan.setPlanCode( organizationPlanDTO.getPlanCode() );
        organizationPlan.setCoupan( organizationPlanDTO.getCoupan() );

        return organizationPlan;
    }

    @Override
    public OrganizationPlanDTO organizationPlanToOrganizationPlanDTO(OrganizationPlan organizationPlan) {
        if ( organizationPlan == null ) {
            return null;
        }

        OrganizationPlanDTO organizationPlanDTO = new OrganizationPlanDTO();

        organizationPlanDTO.setId( organizationPlan.getId() );
        organizationPlanDTO.setPlanCode( organizationPlan.getPlanCode() );
        organizationPlanDTO.setCoupan( organizationPlan.getCoupan() );
        organizationPlanDTO.setCreatedAt( organizationPlan.getCreatedAt() );
        organizationPlanDTO.setUpdatedAt( organizationPlan.getUpdatedAt() );
        organizationPlanDTO.setCreatedBy( organizationPlan.getCreatedBy() );
        organizationPlanDTO.setUpdatedBy( organizationPlan.getUpdatedBy() );

        return organizationPlanDTO;
    }
}
