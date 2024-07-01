package com.service.vix.mapper;

import com.service.vix.dto.JobDTO;
import com.service.vix.models.Job;
import com.service.vix.models.Option;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-30T21:48:49-0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.2 (Oracle Corporation)"
)
@Component
public class JobMapperImpl implements JobMapper {

    @Override
    public JobDTO jobToJobDTO(Job job) {
        if ( job == null ) {
            return null;
        }

        JobDTO jobDTO = new JobDTO();

        jobDTO.setId( job.getId() );
        List<Long> list = job.getTechnicianId();
        if ( list != null ) {
            jobDTO.setTechnicianId( new ArrayList<Long>( list ) );
        }
        jobDTO.setRequestedOn( job.getRequestedOn() );
        jobDTO.setJobStatus( job.getJobStatus() );
        List<Option> list1 = job.getOptions();
        if ( list1 != null ) {
            jobDTO.setOptions( new ArrayList<Option>( list1 ) );
        }
        jobDTO.setCreatedAt( job.getCreatedAt() );
        jobDTO.setUpdatedAt( job.getUpdatedAt() );
        jobDTO.setCreatedBy( job.getCreatedBy() );
        jobDTO.setUpdatedBy( job.getUpdatedBy() );

        return jobDTO;
    }

    @Override
    public Job jobDTOToJob(JobDTO jobDTO) {
        if ( jobDTO == null ) {
            return null;
        }

        Job job = new Job();

        job.setId( jobDTO.getId() );
        job.setCreatedAt( jobDTO.getCreatedAt() );
        job.setUpdatedAt( jobDTO.getUpdatedAt() );
        job.setCreatedBy( jobDTO.getCreatedBy() );
        job.setUpdatedBy( jobDTO.getUpdatedBy() );
        List<Long> list = jobDTO.getTechnicianId();
        if ( list != null ) {
            job.setTechnicianId( new ArrayList<Long>( list ) );
        }
        job.setRequestedOn( jobDTO.getRequestedOn() );
        job.setJobStatus( jobDTO.getJobStatus() );
        List<Option> list1 = jobDTO.getOptions();
        if ( list1 != null ) {
            job.setOptions( new ArrayList<Option>( list1 ) );
        }

        return job;
    }
}
