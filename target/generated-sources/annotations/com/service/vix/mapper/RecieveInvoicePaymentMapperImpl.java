package com.service.vix.mapper;

import com.service.vix.dto.CustomerDTO;
import com.service.vix.dto.InvoiceDTO;
import com.service.vix.dto.RecieveInvoicePaymentDTO;
import com.service.vix.dto.UserDTO;
import com.service.vix.models.ContactNumber;
import com.service.vix.models.Customer;
import com.service.vix.models.Email;
import com.service.vix.models.Invoice;
import com.service.vix.models.Option;
import com.service.vix.models.RecieveInvoicePayment;
import com.service.vix.models.StoredServiceLocation;
import com.service.vix.models.User;
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
public class RecieveInvoicePaymentMapperImpl implements RecieveInvoicePaymentMapper {

    @Override
    public RecieveInvoicePayment recieveInvoicePaymentDTOToRecieveInvoicePayment(RecieveInvoicePaymentDTO invoiceDTO) {
        if ( invoiceDTO == null ) {
            return null;
        }

        RecieveInvoicePayment recieveInvoicePayment = new RecieveInvoicePayment();

        recieveInvoicePayment.setId( invoiceDTO.getId() );
        recieveInvoicePayment.setCreatedAt( invoiceDTO.getCreatedAt() );
        recieveInvoicePayment.setUpdatedAt( invoiceDTO.getUpdatedAt() );
        recieveInvoicePayment.setCreatedBy( invoiceDTO.getCreatedBy() );
        recieveInvoicePayment.setUpdatedBy( invoiceDTO.getUpdatedBy() );
        recieveInvoicePayment.setPaymentMethod( invoiceDTO.getPaymentMethod() );
        recieveInvoicePayment.setReference( invoiceDTO.getReference() );
        recieveInvoicePayment.setRecievedOn( invoiceDTO.getRecievedOn() );
        recieveInvoicePayment.setPaymentNotes( invoiceDTO.getPaymentNotes() );
        recieveInvoicePayment.setTotalOutstanding( invoiceDTO.getTotalOutstanding() );
        recieveInvoicePayment.setInvoices( invoiceDTOListToInvoiceList( invoiceDTO.getInvoices() ) );
        recieveInvoicePayment.setFromCustomer( customerDTOToCustomer( invoiceDTO.getFromCustomer() ) );
        recieveInvoicePayment.setRecievedBy( userDTOToUser( invoiceDTO.getRecievedBy() ) );

        return recieveInvoicePayment;
    }

    @Override
    public RecieveInvoicePaymentDTO recieveInvoicePaymentToRecieveInvoicePaymentDTO(RecieveInvoicePayment invoice) {
        if ( invoice == null ) {
            return null;
        }

        RecieveInvoicePaymentDTO recieveInvoicePaymentDTO = new RecieveInvoicePaymentDTO();

        recieveInvoicePaymentDTO.setId( invoice.getId() );
        recieveInvoicePaymentDTO.setCreatedAt( invoice.getCreatedAt() );
        recieveInvoicePaymentDTO.setUpdatedAt( invoice.getUpdatedAt() );
        recieveInvoicePaymentDTO.setCreatedBy( invoice.getCreatedBy() );
        recieveInvoicePaymentDTO.setUpdatedBy( invoice.getUpdatedBy() );
        recieveInvoicePaymentDTO.setPaymentMethod( invoice.getPaymentMethod() );
        recieveInvoicePaymentDTO.setReference( invoice.getReference() );
        recieveInvoicePaymentDTO.setRecievedOn( invoice.getRecievedOn() );
        recieveInvoicePaymentDTO.setPaymentNotes( invoice.getPaymentNotes() );
        recieveInvoicePaymentDTO.setTotalOutstanding( invoice.getTotalOutstanding() );
        recieveInvoicePaymentDTO.setInvoices( invoiceListToInvoiceDTOList( invoice.getInvoices() ) );
        recieveInvoicePaymentDTO.setFromCustomer( customerToCustomerDTO( invoice.getFromCustomer() ) );
        recieveInvoicePaymentDTO.setRecievedBy( userToUserDTO( invoice.getRecievedBy() ) );

        return recieveInvoicePaymentDTO;
    }

    protected Invoice invoiceDTOToInvoice(InvoiceDTO invoiceDTO) {
        if ( invoiceDTO == null ) {
            return null;
        }

        Invoice invoice = new Invoice();

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

    protected List<Invoice> invoiceDTOListToInvoiceList(List<InvoiceDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<Invoice> list1 = new ArrayList<Invoice>( list.size() );
        for ( InvoiceDTO invoiceDTO : list ) {
            list1.add( invoiceDTOToInvoice( invoiceDTO ) );
        }

        return list1;
    }

    protected Customer customerDTOToCustomer(CustomerDTO customerDTO) {
        if ( customerDTO == null ) {
            return null;
        }

        Customer customer = new Customer();

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

    protected User userDTOToUser(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        User user = new User();

        user.setId( userDTO.getId() );
        user.setCreatedAt( userDTO.getCreatedAt() );
        user.setUpdatedAt( userDTO.getUpdatedAt() );
        user.setCreatedBy( userDTO.getCreatedBy() );
        user.setUpdatedBy( userDTO.getUpdatedBy() );
        user.setUsername( userDTO.getUsername() );
        user.setEmail( userDTO.getEmail() );
        user.setPassword( userDTO.getPassword() );
        user.setFirstName( userDTO.getFirstName() );
        user.setLastName( userDTO.getLastName() );
        user.setMobileNum( userDTO.getMobileNum() );
        user.setGender( userDTO.getGender() );
        user.setIsActive( userDTO.getIsActive() );
        user.setRole( userDTO.getRole() );

        return user;
    }

    protected InvoiceDTO invoiceToInvoiceDTO(Invoice invoice) {
        if ( invoice == null ) {
            return null;
        }

        InvoiceDTO invoiceDTO = new InvoiceDTO();

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

    protected List<InvoiceDTO> invoiceListToInvoiceDTOList(List<Invoice> list) {
        if ( list == null ) {
            return null;
        }

        List<InvoiceDTO> list1 = new ArrayList<InvoiceDTO>( list.size() );
        for ( Invoice invoice : list ) {
            list1.add( invoiceToInvoiceDTO( invoice ) );
        }

        return list1;
    }

    protected CustomerDTO customerToCustomerDTO(Customer customer) {
        if ( customer == null ) {
            return null;
        }

        CustomerDTO customerDTO = new CustomerDTO();

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

    protected UserDTO userToUserDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId( user.getId() );
        userDTO.setUsername( user.getUsername() );
        userDTO.setEmail( user.getEmail() );
        userDTO.setPassword( user.getPassword() );
        userDTO.setFirstName( user.getFirstName() );
        userDTO.setLastName( user.getLastName() );
        userDTO.setMobileNum( user.getMobileNum() );
        userDTO.setGender( user.getGender() );
        userDTO.setRole( user.getRole() );
        userDTO.setIsActive( user.getIsActive() );
        userDTO.setCreatedAt( user.getCreatedAt() );
        userDTO.setUpdatedAt( user.getUpdatedAt() );
        userDTO.setCreatedBy( user.getCreatedBy() );
        userDTO.setUpdatedBy( user.getUpdatedBy() );

        return userDTO;
    }
}
