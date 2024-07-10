/* 
 * ===========================================================================
 * File Name UpdatePasswordDTO.java
 * 
 * Created on Sep 18, 2023
 * ===========================================================================
 */
/**
* Class Information
* @author rodolfopeixoto
* @version 1.2 - Sep 18, 2023
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
public class UpdatePasswordDTO {

	private String email;
	private String password;

}
