package com.service.vix.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This class is used as user entity for application
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer", uniqueConstraints = { @UniqueConstraint(columnNames = "name") })
@ToString
public class Customer extends AbstractModel {

	@Column(name = "name")
	private String customerName;
	@Column(name = "parent_account")
	private String customerParentAccount;
	@Column(name = "account_number")
	private Long customerAccountNumber;

	// Primary contact
	@Column(name = "p_c_prefix")
	private String primaryContactPrefix;
	@Column(name = "p_c_first_name")
	private String primaryContactFirstName;
	@Column(name = "p_c_last_name")
	private String primaryContactLastName;
	@Column(name = "p_c_suffix")
	private String primaryContactSuffix;
	@Column(name = "p_c_department")
	private String primaryContactDepartment;
	@Column(name = "p_c_job_title")
	private String primaryContactJobTitle;
	@Column(name = "p_c_is_billing_contact")
	private Boolean primaryContactIsBillingContact = false;
	@Column(name = "p_c_is_booking_contact")
	private Boolean primaryContactIsBookingContact = false;
	@Column(name = "p_c_anniversary")
	private String primaryContactAnniversary;

	// Variable used to save or get list of Stored Service Locations. There we
	// doon't create any other table to store ids we are using only two tables but
	// we have mapped StoredServiceLocation table with Customer
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinTable(name = "customer_stored_service_location", joinColumns = @JoinColumn(name = "customer_id"), inverseJoinColumns = @JoinColumn(name = "stored_service_location_id"))
	private List<StoredServiceLocation> storedServiceLocations;

	// Variable used to save or get list of Customer Contact Numbers. There we
	// are creating an another table customer_contact_number to store ids for both
	// records.
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinTable(name = "customer_contact_number", joinColumns = @JoinColumn(name = "customer_id"), inverseJoinColumns = @JoinColumn(name = "contact_number_id"))
	private List<ContactNumber> contactNumbers;

	// Variable used to save or get list of Customer Email Addresses. There we
	// are creating an another table customer_email to store ids for noth records.
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinTable(name = "customer_email", joinColumns = @JoinColumn(name = "customer_id"), inverseJoinColumns = @JoinColumn(name = "email_id"))
	private List<Email> emails;

	@Column(name = "sales_person_id")
	private Long salesPersonId;

	@Column(name = "customer_indistry")
	private String customerIndustry;

	@Column(name = "customer_currency")
	private String currency;

	@Column(name = "customer_referalSource")
	private String referalSource;

	@Column(name = "customer_buisnessNumber")
	private String buisnessNumber;

	@Column(name = "customer_privateNotes")
	private String privateNotes;

	private boolean isDeleted = false;

	@JoinColumn(name = "org_id")
	@ManyToOne
	private Organization organization;

}
