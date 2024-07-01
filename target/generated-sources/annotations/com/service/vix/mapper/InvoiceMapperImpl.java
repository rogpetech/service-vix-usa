package com.service.vix.mapper;

import com.service.vix.dto.EstimateDTO;
import com.service.vix.dto.InvoiceDTO;
import com.service.vix.models.Estimate;
import com.service.vix.models.Invoice;
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
public class InvoiceMapperImpl implements InvoiceMapper {

    @Override
    public Invoice invoiceDTOTOInvoice(InvoiceDTO invoiceDTO) {
        if ( invoiceDTO == null ) {
            return null;
        }

        Invoice invoice = new Invoice();

        invoice.setEstimate( estimateDTOToEstimate( invoiceDTO.getEstimateDTO() ) );
        invoice.setId( invoiceDTO.getId() );
        invoice.setCreatedAt( invoiceDTO.getCreatedAt() );
        invoice.setUpdatedAt( invoiceDTO.getUpdatedAt() );
        invoice.setCreatedBy( invoiceDTO.getCreatedBy() );
        invoice.setUpdatedBy( invoiceDTO.getUpdatedBy() );
        invoice.setInvoiceNumber( invoiceDTO.getInvoiceNumber() );
        invoice.setSentBy( invoiceDTO.getSentBy() );
        invoice.setSentOn( invoiceDTO.getSentOn() );
        invoice.setRequestedOn( invoiceDTO.getRequestedOn() );
        List<Option> list = invoiceDTO.getOptions();
        if ( list != null ) {
            invoice.setOptions( new ArrayList<Option>( list ) );
        }

        return invoice;
    }

    @Override
    public InvoiceDTO invoiceToInvoiceDTO(Invoice invoice) {
        if ( invoice == null ) {
            return null;
        }

        InvoiceDTO invoiceDTO = new InvoiceDTO();

        invoiceDTO.setEstimateDTO( estimateToEstimateDTO( invoice.getEstimate() ) );
        invoiceDTO.setId( invoice.getId() );
        invoiceDTO.setInvoiceNumber( invoice.getInvoiceNumber() );
        invoiceDTO.setSentBy( invoice.getSentBy() );
        invoiceDTO.setSentOn( invoice.getSentOn() );
        invoiceDTO.setRequestedOn( invoice.getRequestedOn() );
        List<Option> list = invoice.getOptions();
        if ( list != null ) {
            invoiceDTO.setOptions( new ArrayList<Option>( list ) );
        }
        invoiceDTO.setCreatedAt( invoice.getCreatedAt() );
        invoiceDTO.setUpdatedAt( invoice.getUpdatedAt() );
        invoiceDTO.setCreatedBy( invoice.getCreatedBy() );
        invoiceDTO.setUpdatedBy( invoice.getUpdatedBy() );

        return invoiceDTO;
    }

    protected Estimate estimateDTOToEstimate(EstimateDTO estimateDTO) {
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

    protected EstimateDTO estimateToEstimateDTO(Estimate estimate) {
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
}
