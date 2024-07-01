package com.service.vix.mapper;

import com.service.vix.dto.OrganizationDTO;
import com.service.vix.models.Organization;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-30T21:48:49-0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.2 (Oracle Corporation)"
)
public class OrganizationMapperImpl implements OrganizationMapper {

    @Override
    public Organization organizationDtoToOrganization(OrganizationDTO organizationDTO) {
        if ( organizationDTO == null ) {
            return null;
        }

        Organization organization = new Organization();

        organization.setId( organizationDTO.getId() );
        organization.setCreatedAt( organizationDTO.getCreatedAt() );
        organization.setUpdatedAt( organizationDTO.getUpdatedAt() );
        organization.setCreatedBy( organizationDTO.getCreatedBy() );
        organization.setUpdatedBy( organizationDTO.getUpdatedBy() );
        organization.setOrgName( organizationDTO.getOrgName() );
        organization.setOrgOwnerName( organizationDTO.getOrgOwnerName() );
        organization.setOrgEmail( organizationDTO.getOrgEmail() );
        organization.setOrgMobNum( organizationDTO.getOrgMobNum() );
        organization.setOrgAddress( organizationDTO.getOrgAddress() );
        organization.setOrgCountry( organizationDTO.getOrgCountry() );
        organization.setOrgBusinessLicence( organizationDTO.getOrgBusinessLicence() );
        organization.setOrgLogo( organizationDTO.getOrgLogo() );

        return organization;
    }

    @Override
    public OrganizationDTO organizationToOrganizationDTO(Organization organizationDTO) {
        if ( organizationDTO == null ) {
            return null;
        }

        OrganizationDTO organizationDTO1 = new OrganizationDTO();

        organizationDTO1.setId( organizationDTO.getId() );
        organizationDTO1.setOrgName( organizationDTO.getOrgName() );
        organizationDTO1.setOrgOwnerName( organizationDTO.getOrgOwnerName() );
        organizationDTO1.setOrgEmail( organizationDTO.getOrgEmail() );
        organizationDTO1.setOrgMobNum( organizationDTO.getOrgMobNum() );
        organizationDTO1.setOrgAddress( organizationDTO.getOrgAddress() );
        organizationDTO1.setOrgCountry( organizationDTO.getOrgCountry() );
        organizationDTO1.setOrgBusinessLicence( organizationDTO.getOrgBusinessLicence() );
        organizationDTO1.setOrgLogo( organizationDTO.getOrgLogo() );
        organizationDTO1.setCreatedAt( organizationDTO.getCreatedAt() );
        organizationDTO1.setUpdatedAt( organizationDTO.getUpdatedAt() );
        organizationDTO1.setCreatedBy( organizationDTO.getCreatedBy() );
        organizationDTO1.setUpdatedBy( organizationDTO.getUpdatedBy() );

        return organizationDTO1;
    }
}
