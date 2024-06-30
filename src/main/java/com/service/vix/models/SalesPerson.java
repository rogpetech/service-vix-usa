package com.service.vix.models;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This class is used as dummy class or as a model class for Sales Person
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
@ToString
public class SalesPerson {

	private Long salesPersonId;
	private String salesPersonName;

}
