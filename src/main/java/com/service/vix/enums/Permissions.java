/* 
 * ===========================================================================
 * File Name Permissions.java
 * 
 * Created on Jun 21, 2023
 *
 * This code contains copyright information which is the proprietary property
 * of Chetu India Pvt. Ltd. No part of this code may be reproduced, stored or transmitted
 * in any form without the prior written permission of CHETU.
 *
 * Copyright (C) CHETU. 2023
 * All rights reserved.
 *
 * Modification history:
 * $Log: Permissions.java,v $
 * ===========================================================================
 */
package com.service.vix.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is used to define Permissions for Application
 */
public enum Permissions {

	PRODUCT, SERVICE, CUSTOMER, ESTIMATE, INVOICE, JOB, SERVICE_CATEGORY, PRODUCT_CATEGORY, USERS;

	/**
	 * This method is used to get Capitalize Permissions
	 * 
	 * @author ritiks
	 * @date Aug 19, 2023
	 * @return
	 */
	public static List<String> getCapitalisePermissions() {
		List<String> permissions = new ArrayList<String>();
		Permissions[] permissionsArr = Permissions.values();
		Arrays.stream(permissionsArr).forEach(pe -> {
			String peStr = pe.toString().substring(0, 1).toUpperCase().toString()
					+ pe.toString().substring(1, pe.toString().length()).toLowerCase();
			peStr = peStr.replaceAll("_", " ");
			if (peStr.contains(" "))
				peStr = peStr.split(" ")[0] + " " + peStr.split(" ")[1].substring(0, 1).toUpperCase() + ""
						+ peStr.split(" ")[1].substring(1, peStr.split(" ")[1].length());
			permissions.add(peStr.replaceAll("_", " "));
		});
		return permissions;
	}

}
