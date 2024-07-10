/* 
 * ===========================================================================
 * File Name PasswordChangeDTO.java
 * 
 * Created on Nov 3, 2023
 * ===========================================================================
 */
/**
* Class Information
* @author rodolfopeixoto
* @version 1.2 - Nov 3, 2023
*/
package com.service.vix.dto;

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
public class PasswordChangeDTO {

	private Boolean isPasswordChange;
	private String reason;

}
