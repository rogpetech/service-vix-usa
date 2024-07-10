/* 
 * ===========================================================================
 * File Name RecieveInvoicePayment.java
 * 
 * Created on Dec 19, 2023
 * ===========================================================================
 */
/**
* Class Information
* @author rodolfopeixoto
* @version 1.2 - Dec 19, 2023
*/
package com.service.vix.models;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "invoice_payment")
public class RecieveInvoicePayment extends AbstractModel {

	private String paymentMethod;

	private String reference;

	private LocalDateTime recievedOn;

	private String paymentNotes;

	private Float totalOutstanding;

	@JoinTable(name = "recieve_invoice_payment_invoice", joinColumns = @JoinColumn(name = "recieve_invoice_payment_id"), inverseJoinColumns = @JoinColumn(name = "invoice_id"))
	@ManyToMany
	private List<Invoice> invoices;

	@JoinColumn(name = "from_customer_id")
	@ManyToOne
	private Customer fromCustomer;

	@JoinColumn(name = "recieved_user_id")
	@ManyToOne
	private User recievedBy;
}
