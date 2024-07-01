package com.service.vix.mapper;

import com.service.vix.dto.CustomerDTO;
import com.service.vix.models.ContactNumber;
import com.service.vix.models.Customer;
import com.service.vix.models.Email;
import com.service.vix.models.StoredServiceLocation;
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
public class CustomerMapperImpl implements CustomerMapper {

    @Override
    public Customer customerDTOToCustomer(CustomerDTO customerDTO) {
        if ( customerDTO == null ) {
            return null;
        }

        Customer customer = new Customer();

        customer.setId( customerDTO.getCustomerId() );
        customer.setCreatedAt( customerDTO.getCreatedAt() );
        customer.setUpdatedAt( customerDTO.getUpdatedAt() );
        customer.setCreatedBy( customerDTO.getCreatedBy() );
        customer.setUpdatedBy( customerDTO.getUpdatedBy() );
        customer.setCustomerName( customerDTO.getCustomerName() );
        customer.setCustomerParentAccount( customerDTO.getCustomerParentAccount() );
        if ( customerDTO.getCustomerAccountNumber() != null ) {
            customer.setCustomerAccountNumber( customerDTO.getCustomerAccountNumber().longValue() );
        }
        customer.setPrimaryContactPrefix( customerDTO.getPrimaryContactPrefix() );
        customer.setPrimaryContactFirstName( customerDTO.getPrimaryContactFirstName() );
        customer.setPrimaryContactLastName( customerDTO.getPrimaryContactLastName() );
        customer.setPrimaryContactSuffix( customerDTO.getPrimaryContactSuffix() );
        customer.setPrimaryContactDepartment( customerDTO.getPrimaryContactDepartment() );
        customer.setPrimaryContactJobTitle( customerDTO.getPrimaryContactJobTitle() );
        customer.setPrimaryContactIsBillingContact( customerDTO.getPrimaryContactIsBillingContact() );
        customer.setPrimaryContactIsBookingContact( customerDTO.getPrimaryContactIsBookingContact() );
        customer.setPrimaryContactAnniversary( customerDTO.getPrimaryContactAnniversary() );
        List<StoredServiceLocation> list = customerDTO.getStoredServiceLocations();
        if ( list != null ) {
            customer.setStoredServiceLocations( new ArrayList<StoredServiceLocation>( list ) );
        }
        List<ContactNumber> list1 = customerDTO.getContactNumbers();
        if ( list1 != null ) {
            customer.setContactNumbers( new ArrayList<ContactNumber>( list1 ) );
        }
        List<Email> list2 = customerDTO.getEmails();
        if ( list2 != null ) {
            customer.setEmails( new ArrayList<Email>( list2 ) );
        }
        customer.setSalesPersonId( customerDTO.getSalesPersonId() );
        customer.setCustomerIndustry( customerDTO.getCustomerIndustry() );
        customer.setCurrency( customerDTO.getCurrency() );
        customer.setReferalSource( customerDTO.getReferalSource() );
        customer.setBuisnessNumber( customerDTO.getBuisnessNumber() );
        customer.setPrivateNotes( customerDTO.getPrivateNotes() );

        return customer;
    }

    @Override
    public CustomerDTO customerToCustomerDTO(Customer customer) {
        if ( customer == null ) {
            return null;
        }

        CustomerDTO customerDTO = new CustomerDTO();

        customerDTO.setCustomerId( customer.getId() );
        customerDTO.setCreatedBy( customer.getCreatedBy() );
        customerDTO.setUpdatedBy( customer.getUpdatedBy() );
        customerDTO.setCustomerName( customer.getCustomerName() );
        customerDTO.setCustomerParentAccount( customer.getCustomerParentAccount() );
        if ( customer.getCustomerAccountNumber() != null ) {
            customerDTO.setCustomerAccountNumber( customer.getCustomerAccountNumber().intValue() );
        }
        customerDTO.setPrimaryContactPrefix( customer.getPrimaryContactPrefix() );
        customerDTO.setPrimaryContactFirstName( customer.getPrimaryContactFirstName() );
        customerDTO.setPrimaryContactLastName( customer.getPrimaryContactLastName() );
        customerDTO.setPrimaryContactSuffix( customer.getPrimaryContactSuffix() );
        customerDTO.setPrimaryContactDepartment( customer.getPrimaryContactDepartment() );
        customerDTO.setPrimaryContactJobTitle( customer.getPrimaryContactJobTitle() );
        customerDTO.setPrimaryContactIsBillingContact( customer.getPrimaryContactIsBillingContact() );
        customerDTO.setPrimaryContactIsBookingContact( customer.getPrimaryContactIsBookingContact() );
        customerDTO.setPrimaryContactAnniversary( customer.getPrimaryContactAnniversary() );
        List<StoredServiceLocation> list = customer.getStoredServiceLocations();
        if ( list != null ) {
            customerDTO.setStoredServiceLocations( new ArrayList<StoredServiceLocation>( list ) );
        }
        List<ContactNumber> list1 = customer.getContactNumbers();
        if ( list1 != null ) {
            customerDTO.setContactNumbers( new ArrayList<ContactNumber>( list1 ) );
        }
        List<Email> list2 = customer.getEmails();
        if ( list2 != null ) {
            customerDTO.setEmails( new ArrayList<Email>( list2 ) );
        }
        customerDTO.setSalesPersonId( customer.getSalesPersonId() );
        customerDTO.setCreatedAt( customer.getCreatedAt() );
        customerDTO.setUpdatedAt( customer.getUpdatedAt() );
        customerDTO.setCustomerIndustry( customer.getCustomerIndustry() );
        customerDTO.setCurrency( customer.getCurrency() );
        customerDTO.setReferalSource( customer.getReferalSource() );
        customerDTO.setBuisnessNumber( customer.getBuisnessNumber() );
        customerDTO.setPrivateNotes( customer.getPrivateNotes() );

        return customerDTO;
    }
}
