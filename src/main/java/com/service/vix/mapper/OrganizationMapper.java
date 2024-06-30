package com.service.vix.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Description;

import com.service.vix.dto.OrganizationDTO;
import com.service.vix.models.Organization;

/**
 * This class is used as mapper that map Organization Entity to Organization DTO
 * and Vise Versa
 */
@Mapper
public interface OrganizationMapper {

	OrganizationMapper INSTANCE = Mappers.getMapper(OrganizationMapper.class);

	/**
	 * This method is used to map Organization DTO to Organization Entity
	 * 
	 * @author hemantr
	 * @date Jun 1, 2023
	 * @return Organization
	 * @param organizationDTO
	 * @return
	 * @exception Description
	 */
	@Mapping(target = "organizationPlan", ignore = true)
	@Mapping(target = "user", ignore = true)
	Organization organizationDtoToOrganization(OrganizationDTO organizationDTO);

	/**
	 * This method is used to map Organization Entity to Organization DTO
	 * 
	 * @author hemantr
	 * @date Jun 1, 2023
	 * @return OrganizationDTO
	 * @param organizationDTO
	 * @return
	 * @exception Description
	 */
	@Mapping(target = "organizationPlanDTO", ignore = true)
	OrganizationDTO organizationToOrganizationDTO(Organization organizationDTO);

}
