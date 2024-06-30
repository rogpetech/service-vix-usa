package com.service.vix.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.service.vix.dto.JobCategoryDTO;
import com.service.vix.models.JobCategory;

/**
 * 
 * This class is used as mapper that map Job Category Entity to Job Category DTO
 * and Vise Versa
 * 
 * @author hemantr
 *
 */
/**
 * @author hemantr
 *
 */
@Mapper(componentModel = "spring")
public interface JobCategoryMapper {

	JobCategoryMapper INSTANCE = Mappers.getMapper(JobCategoryMapper.class);

	/**
	 * This method is used to map Job Category DTO to Job Category Entity
	 * 
	 * @param jobCategoryDTO
	 * @return
	 */
	@Mapping(target = "id", source = "jobCategoryId")
	@Mapping(target = "parentjobCategory", ignore = true)
	JobCategory jobCategoryDTOToJobCategory(JobCategoryDTO jobCategoryDTO);

	/**
	 * This method is used to map Job Category Entity to Job Category DTO
	 * 
	 * @param jobCategory
	 * @return
	 */
	@Mapping(target = "jobCategoryId", source = "id")
	@Mapping(target = "parentJobCategoryId", source = "parentjobCategory.id")
	JobCategoryDTO jobCategoryToJobCategoryDTO(JobCategory jobCategory);
}
