package com.service.vix.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.service.vix.dto.CommonResponse;
import com.service.vix.models.SalesPerson;
import com.service.vix.repositories.SalesPersonsRepository;
import com.service.vix.service.SalesPersonService;

import lombok.extern.slf4j.Slf4j;

/**
 * This class is used to implement Sales Person Serivce Implimentation
 */
@Slf4j
@Service
public class SalesPersonServiceImpl implements SalesPersonService {

	@Autowired
	private SalesPersonsRepository salesPersonsRepository;
	@Autowired
	private Environment env;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.SalesPersonService#salesPersons()
	 */
	@Override
	public CommonResponse<List<SalesPerson>> salesPersons() {
		log.info("Enter inside SalesPersonServiceImpl.salesPersons() method.");
		CommonResponse<List<SalesPerson>> result = new CommonResponse<>();
		result.setData(this.salesPersonsRepository.findAll());
		result.setMessage(env.getProperty("record.found.success"));
		result.setResult(true);
		result.setStatus(HttpStatus.OK.value());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.SalesPersonService#getSalesPersonById(java.lang.Long)
	 */
	@Override
	public CommonResponse<SalesPerson> getSalesPersonById(Long salesPersonId) {
		log.info("Enter inside SalesPersonServiceImpl.getSalesPersonById() method.");
		CommonResponse<SalesPerson> result = new CommonResponse<>();
		Optional<SalesPerson> salesPersonOpt = this.salesPersonsRepository.findbyId(salesPersonId);
		if (!salesPersonOpt.isEmpty()) {
			result.setData(salesPersonOpt.get());
			result.setResult(true);
			result.setStatus(HttpStatus.OK.value());
			result.setMessage(env.getProperty("record.found.success"));
		} else {
			result.setData(salesPersonOpt.get());
			result.setResult(true);
			result.setStatus(HttpStatus.NOT_FOUND.value());
			result.setMessage(env.getProperty("record.not.found"));
		}
		return result;
	}
}
