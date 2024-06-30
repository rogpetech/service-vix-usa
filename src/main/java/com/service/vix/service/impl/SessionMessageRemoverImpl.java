package com.service.vix.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.service.vix.dto.SchedulerParameterHolder;
import com.service.vix.scheduler.SessionMessageRemover;

import jakarta.servlet.http.HttpSession;

/**
 * This class is used as implementation class to implement SessionMessageRemover
 * Scheduler method implementation
 */
@Service
//@Slf4j
public class SessionMessageRemoverImpl implements SessionMessageRemover {

	@Autowired
	private SchedulerParameterHolder parameterHolder;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.scheduler.SessionMessageRemover#
	 * removeMessageAttributeFromSession()
	 */
	@Override
	@Scheduled(fixedRate = 5000) // Runs every 5 seconds
	public void removeMessageAttributeFromSession() {
		Object schedulerParameter = parameterHolder.getSchedulerParameter();
		if (schedulerParameter != null) {
			HttpSession currentSession = (HttpSession) schedulerParameter;
			try {
				if (currentSession != null) {
					Object messageAttr = currentSession.getAttribute("message");
					if (messageAttr != null)
						currentSession.removeAttribute("message");
				}
			} catch (Exception e) {
				// Session already invalidate
			}
		}
	}

}
