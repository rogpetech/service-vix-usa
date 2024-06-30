/* 
 * ===========================================================================
 * File Name EmailTemplateIdentifiers.java
 * 
 * Created on Nov 24, 2023
 * ===========================================================================
 */
/**
* Class Information
* @author ritiks
* @version 1.2 - Nov 24, 2023
*/
package com.service.vix.utility;

/**
 * This class is used as Email Template Identifiers
 */
public class EmailTemplateIdentifiers {

	/** String[] This array is used as General Identifiers */
	public static String generalIdentifiers[] = { "@CurrentDate", "@CurrentTime" };

	/** String[] This array is used as Company Identifiers */
	public static String companyIdentifiers[] = { "@Company:Name", "@Company:OwnerName", "@Company:EmailId",
			"@Company:MobileNumber", "@Company:Address", "@Company:Country" };

	/** String[] This array is used as Customer Identifiers */
	public static String customerIdentifiers[] = { "@Customer:Name", "@Customer:ContactPersonName",
			"@Customer:AccountNumber", "@Customer:MobileNumber1", "@Customer:EmailId1", "@Customer:Department",
			"@Customer:JobTitle", "@Customer:StoredServiceLocationLocationNickName1",
			"@Customer:StoredServiceLocationApp1/suit1/unit1", "@Customer:StoredServiceLocationAddress1",
			"@Customer:StoredServiceLocationCity1", "@Customer:StoredServiceLocationState1",
			"@Customer:ReferralSource", "@Customer:Industry", "@Customer:DefailtCurrency" };

}
