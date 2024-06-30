package com.service.vix.models;

import com.service.vix.enums.ContactNumberCategory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This class is used as entity class for Contact Numbers
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contact_number")
@ToString
public class ContactNumber extends AbstractModel {

	@Column(name = "contact_number_type")
	@Enumerated(EnumType.STRING)
	private ContactNumberCategory contactNumberCategory;

	@Column(name = "contact_number")
	private String number;

}
