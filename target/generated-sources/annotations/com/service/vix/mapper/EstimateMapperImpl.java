package com.service.vix.mapper;

import com.service.vix.dto.EstimateDTO;
import com.service.vix.models.Estimate;
import com.service.vix.models.Option;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-30T21:48:48-0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.2 (Oracle Corporation)"
)
@Component
public class EstimateMapperImpl implements EstimateMapper {

    @Override
    public EstimateDTO estimateToEstimateDTO(Estimate estimate) {
        if ( estimate == null ) {
            return null;
        }

        EstimateDTO estimateDTO = new EstimateDTO();

        estimateDTO.setId( estimate.getId() );
        estimateDTO.setRequestedOn( estimate.getRequestedOn() );
        estimateDTO.setEstimateStatus( estimate.getEstimateStatus() );
        List<Option> list = estimate.getOptions();
        if ( list != null ) {
            estimateDTO.setOptions( new ArrayList<Option>( list ) );
        }
        estimateDTO.setCreatedAt( estimate.getCreatedAt() );
        estimateDTO.setUpdatedAt( estimate.getUpdatedAt() );
        estimateDTO.setCreatedBy( estimate.getCreatedBy() );
        estimateDTO.setUpdatedBy( estimate.getUpdatedBy() );

        return estimateDTO;
    }

    @Override
    public Estimate estimateDTOToEstimate(EstimateDTO estimateDTO) {
        if ( estimateDTO == null ) {
            return null;
        }

        Estimate estimate = new Estimate();

        estimate.setId( estimateDTO.getId() );
        estimate.setCreatedAt( estimateDTO.getCreatedAt() );
        estimate.setUpdatedAt( estimateDTO.getUpdatedAt() );
        estimate.setCreatedBy( estimateDTO.getCreatedBy() );
        estimate.setUpdatedBy( estimateDTO.getUpdatedBy() );
        estimate.setRequestedOn( estimateDTO.getRequestedOn() );
        estimate.setEstimateStatus( estimateDTO.getEstimateStatus() );
        List<Option> list = estimateDTO.getOptions();
        if ( list != null ) {
            estimate.setOptions( new ArrayList<Option>( list ) );
        }

        return estimate;
    }
}
