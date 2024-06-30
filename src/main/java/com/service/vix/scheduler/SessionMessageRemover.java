package com.service.vix.scheduler;

import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

/**
 * This class is used as a scheduler that will be responsible to remove message
 * attribute from session
 */
@Component
public interface SessionMessageRemover {

	/**
	 * This method used to remove message attribute from current session
	 * 
	 * @author hemantr
	 * @date Jun 13, 2023
	 * @return void
	 * @exception Description
	 */
	public void removeMessageAttributeFromSession();
}
