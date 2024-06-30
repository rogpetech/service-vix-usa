package com.service.vix.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.service.vix.models.SalesPerson;
import com.service.vix.utility.SalesPersons;

@Component
public interface SalesPersonsRepository {
	/**
	 * This method is used to get Sales persons from Utility
	 * 
	 * @author ritiks
	 * @date Jun 14, 2023
	 * @return List<SalesPerson>
	 * @return
	 * @exception Description
	 */
	public List<SalesPerson> findAll();

	/**
	 * This method is used to get sales person by id
	 * 
	 * @author ritiks
	 * @date Jun 14, 2023
	 * @return Optional<SalesPerson>
	 * @param salesPersonId
	 * @return
	 * @exception Description
	 */
	public Optional<SalesPerson> findbyId(Long salesPersonId);
}

/**
 * This class is used to give implementation for SalesPersonRepository methods.
 */
@Service
class SalesPersonRepositoryImpl implements SalesPersonsRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.repositories.SalesPersonsRepository#findAll()
	 */
	@Override
	public List<SalesPerson> findAll() {
		return SalesPersons.salesPersons;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.repositories.SalesPersonsRepository#findbyId(java.lang.Long)
	 */
	@Override
	public Optional<SalesPerson> findbyId(Long salesPersonId) {
		return SalesPersons.salesPersons.stream().filter(sp -> sp.getSalesPersonId().equals(salesPersonId)).findFirst();
	}

}