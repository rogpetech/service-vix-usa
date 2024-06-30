package com.service.vix.dto;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class is used as holder class that will be hold scheduler variables
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class SchedulerParameterHolder {

	private Object schedulerParameter;

}
