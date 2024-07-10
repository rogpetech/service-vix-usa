/* 
 * ===========================================================================
 * File Name EmailTemplate.java
 * 
 * Created on Nov 22, 2023
 * ===========================================================================
 */
/**
* Class Information
* @author rodolfopeixoto
* @version 1.2 - Nov 22, 2023
*/
package com.service.vix.models;

import com.service.vix.enums.EmailTemplateType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This class is used to as Entity class for Email Template
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmailTemplate extends AbstractModel {

	@Enumerated(EnumType.STRING)
	private EmailTemplateType emailTemplateType;

	private String templateSubject;

	@Column(length = 10485760)
	private String templateText;

	@JoinColumn(name = "org_id")
	@ManyToOne
	private Organization organization;

}
