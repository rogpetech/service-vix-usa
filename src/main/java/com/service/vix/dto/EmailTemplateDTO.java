/* 
 * ===========================================================================
 * File Name EmailTemplateDTO.java
 * 
 * Created on Nov 22, 2023
 * ===========================================================================
 */
/**
* Class Information
* @author rodolfopeixoto
* @version 1.2 - Nov 22, 2023
*/
package com.service.vix.dto;

import java.time.LocalDateTime;

import com.service.vix.enums.EmailTemplateType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmailTemplateDTO {

	private Long id;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private String createdBy;

	private String updatedBy;

	private EmailTemplateType emailTemplateType;

	private String templateSubject;

	private String templateText;

	private OrganizationDTO organizationDTO;

}
