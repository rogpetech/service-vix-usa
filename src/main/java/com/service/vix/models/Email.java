package com.service.vix.models;

import com.service.vix.enums.EmailCategory;

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
@Table(name = "email")
@ToString
public class Email extends AbstractModel {

	@Column(name = "email_type")
	@Enumerated(EnumType.STRING)
	private EmailCategory emailCategory;

	@Column(name = "email_address")
	private String email;

}
