/* 
 * ===========================================================================
 * File Name DataHelper.java
 * 
 * Created on Aug 23, 2023
 * ===========================================================================
 */
/**
* Class Information
* @author rodolfopeixoto
* @version 1.2 - Aug 23, 2023
*/
package com.service.vix.utility;

import org.springframework.stereotype.Component;

import com.service.vix.models.Organization;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This class is used to help to carry data on UI side after login
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Component
public class DataHelper {

	private Long loggedInUserId;
	private String loggedInUserName;
	private String loggedInOrganizationName;
	private String loggedInUserProfileURL;
	private String loggedInOrganizationProfileURL;
	private Organization loggedInOrganizationDetails;

}
