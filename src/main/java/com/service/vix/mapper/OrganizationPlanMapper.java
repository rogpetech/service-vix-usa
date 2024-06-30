package com.service.vix.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Description;

import com.service.vix.dto.OrganizationPlanDTO;
import com.service.vix.models.OrganizationPlan;

@Mapper
public interface OrganizationPlanMapper {

	OrganizationPlanMapper INSTANCE = Mappers.getMapper(OrganizationPlanMapper.class);

	/**
	 * This method is used to map Organization Plan DTO to Organization Plan Entity
	 * 
	 * @author hemantr
	 * @date Jun 6, 2023
	 * @return OrganizationPlan
	 * @param organizationPlanDTO
	 * @return
	 * @exception Description
	 */
	@Mapping(target = "organization",ignore = true)
	OrganizationPlan organizationPlanDtoToOrganizationPlan(OrganizationPlanDTO organizationPlanDTO);

	/**
	 * This method is used to map Organization Entity to Organization DTO
	 * 
	 * @author hemantr
	 * @date Jun 6, 2023
	 * @return OrganizationPlanDTO
	 * @param organizationPlan
	 * @return
	 * @exception Description
	 */
	OrganizationPlanDTO organizationPlanToOrganizationPlanDTO(OrganizationPlan organizationPlan);

}
