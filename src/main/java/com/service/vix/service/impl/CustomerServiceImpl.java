package com.service.vix.service.impl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.CustomerDTO;
import com.service.vix.dto.InvoiceDTO;
import com.service.vix.dto.Message;
import com.service.vix.enums.ContactNumberCategory;
import com.service.vix.enums.EmailCategory;
import com.service.vix.mapper.CustomerMapper;
import com.service.vix.mapper.InvoiceMapper;
import com.service.vix.models.ContactNumber;
import com.service.vix.models.Customer;
import com.service.vix.models.Email;
import com.service.vix.models.Estimate;
import com.service.vix.models.Invoice;
import com.service.vix.models.Organization;
import com.service.vix.models.RecieveInvoicePayment;
import com.service.vix.models.StoredServiceLocation;
import com.service.vix.repositories.CustomerRepository;
import com.service.vix.repositories.EstimateRepository;
import com.service.vix.repositories.InvoiceRepository;
import com.service.vix.repositories.RecieveInvoicePaymentRepository;
import com.service.vix.service.CommonService;
import com.service.vix.service.CustomerService;
import com.service.vix.service.SalesPersonService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is used to define all the methods for Customer
 */
@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private SalesPersonService salesPersonService;
	@Autowired
	private EstimateRepository estimateRepository;
	@Autowired
	private InvoiceRepository invoiceRepository;
	@Autowired
	private RecieveInvoicePaymentRepository recieveInvoicePaymentRepository;

	@Autowired
	private CommonService commonService;

	@Autowired
	private Environment env;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.CustomerService#addCustomer(com.service.vix.dto.
	 * CustomerDTO)
	 */
	@Override
	public CommonResponse<CustomerDTO> addCustomer(CustomerDTO customerDTO, HttpSession httpSession,
			Principal principal) {
		log.info("Enter inside CustomerServiceImpl.addCustomer() method.");
		CommonResponse<CustomerDTO> response = new CommonResponse<>();
		Optional<Customer> customerByName = this.customerRepository.findByCustomerName(customerDTO.getCustomerName());
		if (customerByName.isPresent()) {
			String msg = env.getProperty("customer.already.exists");
			httpSession.setAttribute("message", new Message(msg, "error"));
			log.info(msg);
			response.setMessage(msg);
			response.setData(null);
			response.setResult(true);
			response.setStatus(HttpStatus.ALREADY_REPORTED.value());
			return response;
		} else {
			log.info("Going to save customer");
			Customer customer = CustomerMapper.INSTANCE.customerDTOToCustomer(customerDTO);
			Organization organization = this.commonService.getLoggedInUserOrganization(principal);
			customer.setOrganization(organization);
			Customer savedCustomer = this.customerRepository.save(customer);
			CustomerDTO savedCustomerDTO = CustomerMapper.INSTANCE.customerToCustomerDTO(savedCustomer);
			String msg = env.getProperty("customer.add.success");
			log.info(msg);
			httpSession.setAttribute("message", new Message(msg, "success"));
			response.setMessage(msg);
			response.setData(savedCustomerDTO);
			response.setResult(true);
			response.setStatus(HttpStatus.OK.value());
			return response;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.CustomerService#getCustomers()
	 */
	@Override
	public CommonResponse<List<CustomerDTO>> getCustomers(Principal principal) {
		log.info("Enter inside CustomerServiceImpl.getCustomers() method.");
		CommonResponse<List<CustomerDTO>> response = new CommonResponse<>();
		Organization organization = this.commonService.getLoggedInUserOrganization(principal);
		List<Customer> customers = this.customerRepository
				.findAllByIsDeletedFalseAndOrganizationOrderByCreatedAtDesc(organization);
		List<CustomerDTO> customerDTOs = new ArrayList<CustomerDTO>();
		customers.forEach(c -> {
			CustomerDTO customerDTO = CustomerMapper.INSTANCE.customerToCustomerDTO(c);
			if (c.getSalesPersonId() != null)
				customerDTO.setSalesPerson(this.salesPersonService.getSalesPersonById(c.getSalesPersonId()).getData());
			customerDTOs.add(customerDTO);
		});
		String msg = env.getProperty("record.found.success");
		log.info(msg);
		response.setMessage(msg);
		response.setData(customerDTOs);
		response.setResult(true);
		response.setStatus(HttpStatus.OK.value());
		return response;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.CustomerService#getCustomerById(java.lang.Long)
	 */
	@Override
	public CommonResponse<CustomerDTO> getCustomerById(Long customerId) {
		log.info("Enter inside CustomerServiceImpl.getCustomerById() method.");
		CommonResponse<CustomerDTO> response = new CommonResponse<>();
		if (customerId != null && customerId > 0) {
			CustomerDTO customerDTO = new CustomerDTO();
			Optional<Customer> customerOpt = this.customerRepository.findById(customerId);
			if (customerOpt.isPresent())
				customerDTO = CustomerMapper.INSTANCE.customerToCustomerDTO(customerOpt.get());
			String msg = env.getProperty("record.found.success");
			log.info(msg);
			response.setMessage(msg);
			response.setData(customerDTO);
			response.setResult(true);
			response.setStatus(HttpStatus.OK.value());
			return response;
		} else {
			String msg = env.getProperty("record.not.found");
			log.info(msg);
			response.setMessage(msg);
			response.setData(null);
			response.setResult(true);
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return response;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.CustomerService#removeCustomer(com.service.vix.dto.
	 * CustomerDTO)
	 */
	@Override
	public CommonResponse<Boolean> removeCustomer(Long customerId) {
		log.info("Enter inside CustomerServiceImpl.removeCustomer() method.");
		CommonResponse<Boolean> response = new CommonResponse<>();
		if (customerId != null && customerId > 0) {
			Optional<Customer> customerOpt = this.customerRepository.findById(customerId);
			String msg = "";
			if (customerOpt.isPresent()) {

				Optional<Estimate> customerEstimate = this.estimateRepository.findByCustomerId(customerId);
				if (customerEstimate.isEmpty()) {
					Customer customer = customerOpt.get();
					customer.setDeleted(true);
					this.customerRepository.save(customer);
					/* this.customerRepository.delete(customer); */
					msg = env.getProperty("customer.delete.success");
					log.info(env.getProperty("record.delete.success") + " 1.");
				} else {
					msg = env.getProperty("customer.associated.estimate");
					log.info(msg);
					response.setMessage(msg);
					response.setData(false);
					response.setResult(false);
					response.setStatus(HttpStatus.ALREADY_REPORTED.value());
					return response;
				}
			} else {
				msg = env.getProperty("record.delete.success") + " 0.";
			}
			log.info(msg);
			response.setMessage(msg);
			response.setData(true);
			response.setResult(true);
			response.setStatus(HttpStatus.OK.value());
			return response;
		} else {
			String msg = env.getProperty("record.not.found");
			log.info(msg);
			response.setMessage(msg);
			response.setData(false);
			response.setResult(true);
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return response;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.CustomerService#updateCustomer(com.service.vix.dto.
	 * CustomerDTO)
	 */
	@Override
	public CommonResponse<CustomerDTO> updateCustomer(CustomerDTO customerDTO) {
		log.info("Enter inside CustomerServiceImpl.updateCustomer() method.");
		CommonResponse<CustomerDTO> response = new CommonResponse<>();
		Long customerId = customerDTO.getCustomerId();
		if (customerId != null && customerId > 0) {
			log.info("Customer found by given Customer details.");
			Optional<Customer> customerOpt = this.customerRepository.findById(customerId);
			if (customerOpt.isPresent()) {
				Customer customer = CustomerMapper.INSTANCE.customerDTOToCustomer(customerDTO);
				Customer DBCustomer = customerOpt.get();
				Long DBCustomerId = DBCustomer.getId();
				customer.setId(DBCustomerId);
				customer.setOrganization(DBCustomer.getOrganization());
				Customer savedCustomer = this.customerRepository.save(customer);
				CustomerDTO savedCustomerDTO = CustomerMapper.INSTANCE.customerToCustomerDTO(savedCustomer);
				String msg = env.getProperty("customer.update.success");
				log.info(msg);
				response.setMessage(msg);
				response.setData(savedCustomerDTO);
				response.setResult(true);
				response.setStatus(HttpStatus.OK.value());
				return response;
			} else {
				String msg = env.getProperty("record.not.found");
				log.info(msg);
				response.setMessage(msg);
				response.setData(null);
				response.setResult(true);
				response.setStatus(HttpStatus.NOT_FOUND.value());
				return response;
			}
		} else {
			String msg = env.getProperty("record.not.found");
			log.info(msg);
			response.setMessage(msg);
			response.setData(null);
			response.setResult(true);
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return response;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.CustomerService#extractAddCustomerFormObject(com.
	 * service.vix. dto.CustomerDTO, jakarta.servlet.http.HttpServletRequest)
	 */
	@Override
	public CustomerDTO extractAddCustomerFormObject(CustomerDTO customerDTO, HttpServletRequest httpServletRequest) {
		log.info("Enter inside CustomerServiceImpl.calculateFormObject() method.");
		int counter = 0;

		// Get values for locationNickName form
		log.info("Going to calculate storedServiceLocation values from form.");
		List<StoredServiceLocation> storedServiceLocations = new ArrayList<StoredServiceLocation>();
		while (true) {
			log.info("Going to get values from " + (counter + 1) + " storedServiceLocation form.");
			StoredServiceLocation storedServiceLocation = new StoredServiceLocation();

			String locationNickNameVal = httpServletRequest.getParameter("locationNickName" + counter);
			String isPrimaryLocationVal = httpServletRequest.getParameter("isPrimaryLocation" + counter);
			String isBillingAddressVal = httpServletRequest.getParameter("isBillingAddress" + counter);
			String streetAddressLatLongVal = httpServletRequest.getParameter("streetAddressLatLong" + counter);
			String sslUnit = httpServletRequest.getParameter("unit" + counter);
			String sslAddress = httpServletRequest.getParameter("address" + counter);
			String sslCIty = httpServletRequest.getParameter("city" + counter);
			String sslState = httpServletRequest.getParameter("state" + counter);

			if (locationNickNameVal != null) {
				log.info("Got locationNickName value (" + locationNickNameVal + ") for " + (counter + 1)
						+ " storedServiceLocation form.");
				boolean isPrimaryLocation = false;
				boolean isBillingAddress = false;
				storedServiceLocation.setLocationNickName(locationNickNameVal);
				if (isPrimaryLocationVal != null)
					if (isPrimaryLocationVal.equals("on"))
						isPrimaryLocation = true;
				if (isBillingAddressVal != null)
					if (isBillingAddressVal.equals("on"))
						isBillingAddress = true;
				storedServiceLocation.setIsPrimaryLocation(isPrimaryLocation);
				storedServiceLocation.setIsBillingAddresss(isBillingAddress);
				if (streetAddressLatLongVal != null)
					storedServiceLocation.setStreetAddressLatLong(streetAddressLatLongVal);
				if (sslUnit != null) {
					storedServiceLocation.setUnit(sslUnit);
				}
				if (sslAddress != null) {
					storedServiceLocation.setAddress(sslAddress);
				}
				if (sslCIty != null) {

					storedServiceLocation.setCity(sslCIty);
				}
				if (sslState != null) {
					storedServiceLocation.setState(sslState);
				}
				storedServiceLocations.add(storedServiceLocation);
			} else {
				log.info("No value present in " + (counter + 1) + " locationNickName field.");
				break;
			}

			counter++;
		}

		// Re-intialize counter
		counter = 0;
		log.info("(1)Re-intialize counter value.");

		// Get values for contact number
		log.info("Going to calculate contact number values from form.");
		List<ContactNumber> contactNumbers = new ArrayList<ContactNumber>();
		while (true) {
			log.info("Going to get value from " + (counter + 1) + " mobile field.");
			ContactNumber contactNumber = new ContactNumber();

			String contactNumberVal = httpServletRequest.getParameter("contactNumber" + counter);
			String contactNumberCategoryVal = httpServletRequest.getParameter("contactNumberCategory" + counter);
			ContactNumberCategory contactNumberCategory = ContactNumberCategory.MOBILE;
			if (contactNumberVal != null) {
				log.info("Got contact number (" + contactNumberVal + ") for " + (counter + 1)
						+ " contact number field.");
				contactNumber.setNumber(contactNumberVal);

				if (contactNumberCategoryVal != null) {
					log.info("Contact number category given by user.");
					contactNumberCategory = Arrays.stream(ContactNumberCategory.values())
							.filter(category -> contactNumberCategoryVal.equals(category.name())).findFirst()
							.orElse(null);
				} else
					log.info("Given contact number category in null so set it to MOBILE category.");

				contactNumber.setContactNumberCategory(contactNumberCategory);

				contactNumbers.add(contactNumber);
			} else {
				log.info("No value present in " + (counter + 1) + " mobile field.");
				break;
			}
			counter++;
		}

		// Re-intialize counter
		counter = 0;
		log.info("(2)Re-intialize counter value.");

		// Get values for contact number
		log.info("Going to calculate contact number values from form.");
		List<Email> emails = new ArrayList<Email>();
		while (true) {
			log.info("Going to get value from " + (counter + 1) + " email field.");
			Email email = new Email();

			String emailVal = httpServletRequest.getParameter("email" + counter);
			String emailCategoryVal = httpServletRequest.getParameter("emailCategory" + counter);
			EmailCategory emailCategory = EmailCategory.PERSONAL;
			if (emailVal != null) {
				log.info("Got email (" + emailVal + ") for " + (counter + 1) + " email field.");
				email.setEmail(emailVal);

				if (emailCategoryVal != null) {
					log.info("Email category given by user.");
					Arrays.stream(EmailCategory.values()).filter(category -> emailCategoryVal.equals(category.name()))
							.findFirst().orElse(null);
				} else
					log.info("Given email category in null so set it to PERSONAL category.");

				email.setEmailCategory(emailCategory);

				emails.add(email);
			} else {
				log.info("No value present in " + (counter + 1) + " mobile field.");
				break;
			}
			counter++;
		}

		// Set calculated variables in CustomerDTO
		log.info("Going to set calculated variables inside CustomerDTO object");
		customerDTO.setStoredServiceLocations(storedServiceLocations);
		customerDTO.setContactNumbers(contactNumbers);
		customerDTO.setEmails(emails);

		return customerDTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.CustomerService#findByCustomerName(java.lang.String)
	 */
	@Override
	public CommonResponse<CustomerDTO> findByCustomerName(String customerName) {
		log.info("Enter inside CustomerServiceImpl.findByCustomerName() method.");
		CommonResponse<CustomerDTO> response = new CommonResponse<>();
		if (customerName != null) {
			CustomerDTO customerDTO = new CustomerDTO();
			Optional<Customer> customerOpt = this.customerRepository.findByCustomerName(customerName);
			if (customerOpt.isPresent())
				customerDTO = CustomerMapper.INSTANCE.customerToCustomerDTO(customerOpt.get());
			String msg = env.getProperty("record.found.success");
			log.info(msg);
			response.setMessage(msg);
			response.setData(customerDTO);
			response.setResult(true);
			response.setStatus(HttpStatus.OK.value());
			return response;
		} else {
			String msg = env.getProperty("record.not.found");
			log.info(msg);
			response.setMessage(msg);
			response.setData(new CustomerDTO());
			response.setResult(true);
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return response;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.CustomerService#searchCustomer(java.lang.String)
	 */
	@Override
	public CommonResponse<List<CustomerDTO>> searchCustomer(String customerNameChar, Principal principal) {
		log.info("Enter inside CustomerServiceImpl.getCustomers() method.");
		CommonResponse<List<CustomerDTO>> response = new CommonResponse<>();
		Organization organization = this.commonService.getLoggedInUserOrganization(principal);
		List<Customer> customers = this.customerRepository
				.findByOrganizationAndCustomerNameLikeOrderByIdDesc(organization, "%" + customerNameChar + "%");
		List<CustomerDTO> customerDTOs = new ArrayList<CustomerDTO>();
		customers.forEach(c -> {
			CustomerDTO customerDTO = CustomerMapper.INSTANCE.customerToCustomerDTO(c);
			if (c.getSalesPersonId() != null)
				customerDTO.setSalesPerson(this.salesPersonService.getSalesPersonById(c.getSalesPersonId()).getData());
			customerDTOs.add(customerDTO);
		});
		String msg = env.getProperty("record.found.success");
		log.info(msg);
		response.setMessage(msg);
		response.setData(customerDTOs);
		response.setResult(true);
		response.setStatus(HttpStatus.OK.value());
		return response;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.CustomerService#getOrganisationCustomers(java.lang.
	 * String)
	 */
	@Override
	public CommonResponse<List<CustomerDTO>> getOrganisationCustomers(Principal principal) {
		log.info("Enter inside CustomerServiceImpl.getCustomers() method.");
		CommonResponse<List<CustomerDTO>> response = new CommonResponse<>();
		Organization organization = this.commonService.getLoggedInUserOrganization(principal);
		List<Customer> customers = this.customerRepository
				.findAllByIsDeletedFalseAndOrganizationOrderByCreatedAtDesc(organization);
		List<CustomerDTO> customerDTOs = new ArrayList<CustomerDTO>();
		customers.forEach(c -> {
			CustomerDTO customerDTO = CustomerMapper.INSTANCE.customerToCustomerDTO(c);
			if (c.getSalesPersonId() != null)
				customerDTO.setSalesPerson(this.salesPersonService.getSalesPersonById(c.getSalesPersonId()).getData());
			customerDTOs.add(customerDTO);
		});
		String msg = env.getProperty("record.found.success");
		log.info(msg);
		response.setMessage(msg);
		response.setData(customerDTOs);
		response.setResult(true);
		response.setStatus(HttpStatus.OK.value());
		return response;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.CustomerService#serachCustomerByEmail(java.lang.
	 * String)
	 */
	@Override
	public Map<Boolean, String> serachCustomerByEmail(String mail) {

		HashMap<Boolean, String> result = new HashMap<>();
		Boolean existsByEmails_Email = this.customerRepository.existsByEmails_Email(mail);

		if (existsByEmails_Email)
			result.put(true, "Email already exists");
		else
			result.put(false, "Email available");
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.CustomerService#getOrganisationCustomersWithInvoices(
	 * java.security.Principal)
	 */
	@Override
	public CommonResponse<List<CustomerDTO>> getOrganisationCustomersWithInvoices(Principal principal) {
		log.info("Enter inside CustomerServiceImpl.getOrganisationCustomersWithInvoices() method.");
		CommonResponse<List<CustomerDTO>> organisationCustomers = this.getOrganisationCustomers(principal);
		List<CustomerDTO> organizationCustomer = new ArrayList<CustomerDTO>();
		organisationCustomers.getData().stream().forEach(oc -> {
			List<Invoice> customerInvoices = this.invoiceRepository
					.findByCustomerAndIsDeleted(CustomerMapper.INSTANCE.customerDTOToCustomer(oc), false);
			List<InvoiceDTO> customerInvoiceDTOs = new ArrayList<InvoiceDTO>();
			Double totalOutstanding = customerInvoices.stream().mapToDouble(ci -> {
				customerInvoiceDTOs.add(InvoiceMapper.INSTANCE.invoiceToInvoiceDTO(ci));
				return ci.getOptions().get(0).getInvoiceTotal();
			}).sum();

			oc.setInvoices(customerInvoiceDTOs);

			List<RecieveInvoicePayment> customerRecievedInvoices = this.recieveInvoicePaymentRepository
					.findByFromCustomer(CustomerMapper.INSTANCE.customerDTOToCustomer(oc));

			if (customerRecievedInvoices != null && customerRecievedInvoices.size() > 0)
				oc.setTotalInvoiceAmount(
						totalOutstanding.floatValue() - customerRecievedInvoices.get(0).getTotalOutstanding());
			else
				oc.setTotalInvoiceAmount(0f);
			if (customerInvoiceDTOs.size() > 0)
				organizationCustomer.add(oc);
		});
		organisationCustomers.setData(organizationCustomer);
		return organisationCustomers;
	}

}
