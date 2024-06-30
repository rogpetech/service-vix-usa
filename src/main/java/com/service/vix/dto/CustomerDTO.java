package com.service.vix.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.service.vix.models.ContactNumber;
import com.service.vix.models.Email;
import com.service.vix.models.SalesPerson;
import com.service.vix.models.StoredServiceLocation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This class is used as Customer DTO
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Component
public class CustomerDTO {

	private String createdBy;

	private String updatedBy;

	private Long customerId;

	private String customerName;

	private String customerParentAccount;

	private Integer customerAccountNumber;

	// Primary contact
	private String primaryContactPrefix;

	private String primaryContactFirstName;

	private String primaryContactLastName;

	private String primaryContactSuffix;

	private String primaryContactDepartment;

	private String primaryContactJobTitle;

	private Boolean primaryContactIsBillingContact;

	private Boolean primaryContactIsBookingContact;

	private String primaryContactAnniversary;

	private List<StoredServiceLocation> storedServiceLocations;

	private List<ContactNumber> contactNumbers;

	private List<Email> emails;

	private Long salesPersonId;

	private SalesPerson salesPerson;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private String customerIndustry;

	private String currency;

	private String referalSource;

	private String buisnessNumber;

	private String privateNotes;

	private List<InvoiceDTO> invoices;
	
	private Float totalInvoiceAmount;

}
