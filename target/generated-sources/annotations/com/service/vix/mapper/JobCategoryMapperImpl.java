package com.service.vix.mapper;

import com.service.vix.dto.JobCategoryDTO;
import com.service.vix.models.JobCategory;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-30T21:48:49-0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.2 (Oracle Corporation)"
)
@Component
public class JobCategoryMapperImpl implements JobCategoryMapper {

    @Override
    public JobCategory jobCategoryDTOToJobCategory(JobCategoryDTO jobCategoryDTO) {
        if ( jobCategoryDTO == null ) {
            return null;
        }

        JobCategory jobCategory = new JobCategory();

        jobCategory.setId( jobCategoryDTO.getJobCategoryId() );
        jobCategory.setCreatedAt( jobCategoryDTO.getCreatedAt() );
        jobCategory.setUpdatedAt( jobCategoryDTO.getUpdatedAt() );
        jobCategory.setCreatedBy( jobCategoryDTO.getCreatedBy() );
        jobCategory.setUpdatedBy( jobCategoryDTO.getUpdatedBy() );
        jobCategory.setJobCategoryName( jobCategoryDTO.getJobCategoryName() );
        jobCategory.setActivationStatus( jobCategoryDTO.isActivationStatus() );

        return jobCategory;
    }

    @Override
    public JobCategoryDTO jobCategoryToJobCategoryDTO(JobCategory jobCategory) {
        if ( jobCategory == null ) {
            return null;
        }

        JobCategoryDTO jobCategoryDTO = new JobCategoryDTO();

        jobCategoryDTO.setJobCategoryId( jobCategory.getId() );
        jobCategoryDTO.setParentJobCategoryId( jobCategoryParentjobCategoryId( jobCategory ) );
        jobCategoryDTO.setCreatedBy( jobCategory.getCreatedBy() );
        jobCategoryDTO.setUpdatedBy( jobCategory.getUpdatedBy() );
        jobCategoryDTO.setJobCategoryName( jobCategory.getJobCategoryName() );
        jobCategoryDTO.setActivationStatus( jobCategory.isActivationStatus() );
        jobCategoryDTO.setCreatedAt( jobCategory.getCreatedAt() );
        jobCategoryDTO.setUpdatedAt( jobCategory.getUpdatedAt() );

        return jobCategoryDTO;
    }

    private Long jobCategoryParentjobCategoryId(JobCategory jobCategory) {
        if ( jobCategory == null ) {
            return null;
        }
        JobCategory parentjobCategory = jobCategory.getParentjobCategory();
        if ( parentjobCategory == null ) {
            return null;
        }
        Long id = parentjobCategory.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
