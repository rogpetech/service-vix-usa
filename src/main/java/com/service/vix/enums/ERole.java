package com.service.vix.enums;

/**
 * This class is used to declare roles for the application
 */
public enum ERole {
	ROLE_SUPER_ADMIN(1), ROLE_ORGNAIZATION(2), ROLE_USER(3), ROLE_MODERATOR(4), ROLE_ADMIN(5);

	private final int constValue;

	/**
	 * @param constValue
	 * @Description This constructor is used to get Role by value
	 */
	private ERole(int constValue) {
		this.constValue = constValue;
	}

	public int constValue() {
		return constValue;
	}
}
