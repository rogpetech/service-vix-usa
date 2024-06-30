package com.service.vix.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Description;

import com.service.vix.dto.CustomerDTO;
import com.service.vix.models.Customer;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

	CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

	/**
	 * This method is used to map Customer DTO to Customer Entity
	 * 
	 * @author hemantr
	 * @date Jun 8, 2023
	 * @return Customer
	 * @param customerDTO
	 * @return
	 * @exception Description
	 */
	@Mapping(target = "id", source = "customerId")
	Customer customerDTOToCustomer(CustomerDTO customerDTO);

	/**
	 * This method is used to map Customer Entity to Customer DTO
	 * 
	 * @author hemantr
	 * @date Jun 8, 2023
	 * @return CustomerDTO
	 * @param customer
	 * @return
	 * @exception Description
	 */
	@Mapping(target = "customerId", source = "id")
	@Mapping(target = "salesPerson", ignore = true)
	CustomerDTO customerToCustomerDTO(Customer customer);

}
