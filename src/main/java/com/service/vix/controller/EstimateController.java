
package com.service.vix.controller;

import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Description;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.EstimateDTO;
import com.service.vix.dto.EstimateEmailDTO;
import com.service.vix.dto.Message;
import com.service.vix.dto.ProductCategoryDTO;
import com.service.vix.dto.ServiceCategoryDTO;
import com.service.vix.enums.EstimateStatus;
import com.service.vix.models.Option;
import com.service.vix.models.Product;
import com.service.vix.models.Services;
import com.service.vix.service.CustomerService;
import com.service.vix.service.EstimateService;
import com.service.vix.service.ProductCategoryService;
import com.service.vix.service.SalesPersonService;
import com.service.vix.service.ServiceCategoryService;
import com.service.vix.utility.Currency;
import com.service.vix.utility.EstimateEmailBody;
import com.service.vix.utility.Industry;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is used as controller class that handle all the request for
 * estimate pages
 *
 */
@Controller
@Slf4j
@RequestMapping("/estimate")
public class EstimateController extends BaseController {

	@Autowired
	private CustomerService customerService;
	@Autowired
	private EstimateService estimateService;
	@Autowired
	private SalesPersonService salesPersonService;
	@Autowired
	private ProductCategoryService productCategoryService;
	@Autowired
	private ServiceCategoryService serviceCategoryService;

	@Autowired
	private Environment env;

	@Value("${spring.mail.username}")
	private String fromEmailAddress;

	@Value("${estimate.subject}")
	private String estimateEmailSubject;

	/**
	 * This method is used to open add-estimate page
	 * 
	 * @author ritiks
	 * @date Jun 20, 2023
	 * @return String
	 * @param model
	 * @param httpServletRequest
	 * @param principal
	 * @return
	 * @exception Description
	 */
	@GetMapping("/add")
	public String addEstimate(Model model, HttpServletRequest httpServletRequest, Principal principal) {
		log.info("Enter inside EstimateController.addEstimateForm() mthods.");
		List<String> customerName = new ArrayList<String>();
		this.customerService.getCustomers(principal).getData().stream()
				.forEach(c -> customerName.add(c.getCustomerName()));
		List<ProductCategoryDTO> productCategories = this.productCategoryService.getProductCategories(principal)
				.getData();
		model.addAttribute("productCategories", productCategories);
		List<ServiceCategoryDTO> serviceCategories = this.serviceCategoryService.getServiceCategory(principal)
				.getData();
		model.addAttribute("currencies", Currency.currencies);
		model.addAttribute("industries", Industry.industries);
		model.addAttribute("serviceCategories", serviceCategories);
		model.addAttribute("salesPersons", this.salesPersonService.salesPersons().getData());
		model.addAttribute("customerNames", customerName);
		model.addAttribute("estimateStatus", EstimateStatus.values());
		model.addAttribute("estimate", new EstimateDTO());
		return "estimate/add-estimate";
	}

	/**
	 * This method is used to add estimate
	 * 
	 * @author ritiks
	 * @date Jun 22, 2023
	 * @return String
	 * @param httpSession
	 * @param servletRequest
	 * @return
	 * @exception Description
	 */
	@PostMapping("/process-add")
	public String processAddEstimate(HttpSession httpSession, HttpServletRequest servletRequest, Principal principal) {
		log.info("Enter inside EstimateController.processAddEstimate() method.");
		EstimateDTO extractEstimateFormObject = this.estimateService.extractEstimateFormObject(servletRequest);
		this.estimateService.saveEstimate(extractEstimateFormObject, httpSession, principal).getData();
		return "redirect:/estimate/estimates";
	}

	/**
	 * This method is used to show estimate listing page
	 * 
	 * @author ritiks
	 * @date Jun 27, 2023
	 * @return String
	 * @param model
	 * @param principal
	 * @return
	 * @exception Description
	 */
	@GetMapping("/estimates")
	public String showEstimates(Model model, Principal principal) {
		List<EstimateDTO> estimates = this.estimateService.getOrganizationEstimates(principal).getData();
		List<EstimateDTO> userEstimates = this.estimateService.getUserEstimates(principal).getData();
		model.addAttribute("estimates", estimates);
		model.addAttribute("userEstimates", userEstimates);
		model.addAttribute("estimateStatus", EstimateStatus.values());
		return "estimate/estimates";
	}

	/**
	 * This method is used to view estimate
	 * 
	 * @author ritiks
	 * @date Jul 18, 2023
	 * @return String
	 * @param operationType
	 * @param estimateId
	 * @param model
	 * @param principal
	 * @return
	 * @exception Description
	 */
	@GetMapping("/operation/{operationType}/{estimateId}")
	public String viewEstimate(@PathVariable String operationType, @PathVariable Long estimateId, Model model,
			Principal principal) {
		log.info("Enter inside EstimateController.addEstimateForm() mthods.");
		List<String> customerName = new ArrayList<String>();
		this.customerService.getCustomers(principal).getData().stream()
				.forEach(c -> customerName.add(c.getCustomerName()));
		List<ProductCategoryDTO> productCategories = this.productCategoryService.getProductCategories(principal)
				.getData();
		EstimateDTO estimateDTO = this.estimateService.getEstimatesById(estimateId).getData();
		Boolean requestForView = false;
		if (operationType.equals("view"))
			requestForView = true;
		model.addAttribute("requestForView", requestForView);
		model.addAttribute("fromEmailAddress", fromEmailAddress);
		model.addAttribute("estimateEmailSubject", estimateEmailSubject);
		model.addAttribute("productCategories", productCategories);
		model.addAttribute("salesPersons", this.salesPersonService.salesPersons().getData());
		model.addAttribute("customerNames", customerName);
		model.addAttribute("estimateStatus", EstimateStatus.values());
		model.addAttribute("bodyContent", EstimateEmailBody.getBody(estimateDTO.getCustomerDTO().getCustomerName()));
		model.addAttribute("options", estimateDTO.getOptions());
		if (estimateId != null && estimateId > 0)
			model.addAttribute("estimate", estimateDTO);
		return "estimate/add-estimate";
	}

	/**
	 * This method is used to generate PDF and send email to customer
	 * 
	 * @author ritiks
	 * @date Jul 20, 2023
	 * @return String
	 * @param estimateEmailDTO
	 * @param httpServletResponse
	 * @param httpSession
	 * @return
	 * @exception Description
	 */
	@PostMapping("/send")
	public String sendEstimateEmail(@ModelAttribute EstimateEmailDTO estimateEmailDTO,
			HttpServletResponse httpServletResponse, HttpSession httpSession) {
		log.info("Enter inside EstimateController.sendEstimateEmail() method.");
		CommonResponse<Boolean> sendEstimateEmail = this.estimateService.sendEstimateEmail(estimateEmailDTO,
				httpServletResponse, httpSession);
		if (!sendEstimateEmail.getResult().booleanValue() == true)
			return "redirect:/estimate/estimates";
		return "redirect:/estimate/estimates";
	}

	/**
	 * This method is used to remove estimate
	 * 
	 * @author ritiks
	 * @date Jul 26, 2023
	 * @return String
	 * @param estimateId
	 * @param model
	 * @param httpSession
	 * @return
	 * @exception Description
	 */
	@GetMapping("/remove/{estimateId}")
	public String deleteEstimate(@PathVariable Long estimateId, Model model, HttpSession httpSession) {
		log.info("Enter inside EstimateController.deleteEstimate() method.");
		this.estimateService.removeEstimate(estimateId, httpSession);
		return "redirect:/estimate/estimates";
	}

	/**
	 * This method is used to open update estimate page
	 * 
	 * @author ritiks
	 * @date Jul 26, 2023
	 * @return String
	 * @param estimateId
	 * @param model
	 * @param principal
	 * @return
	 * @exception Description
	 */
	@GetMapping("/update/{estimateId}")
	public String updateEstimate(@PathVariable Long estimateId, Model model, Principal principal) {
		log.info("Enter inside EstimateController.addEstimateForm() mthods.");
		List<String> customerName = new ArrayList<String>();
		this.customerService.getCustomers(principal).getData().stream()
				.forEach(c -> customerName.add(c.getCustomerName()));
		List<ProductCategoryDTO> productCategories = this.productCategoryService.getProductCategories(principal)
				.getData();
		EstimateDTO estimateDTO = this.estimateService.getEstimatesById(estimateId).getData();
		Boolean requestForView = true;
		model.addAttribute("productCategories", productCategories);
		model.addAttribute("salesPersons", this.salesPersonService.salesPersons().getData());
		model.addAttribute("customerNames", customerName);
		model.addAttribute("estimateStatus", EstimateStatus.values());
		model.addAttribute("requestForView", requestForView);
		model.addAttribute("fromEmailAddress", fromEmailAddress);
		model.addAttribute("estimateEmailSubject", estimateEmailSubject);
		model.addAttribute("bodyContent", EstimateEmailBody.getBody(estimateDTO.getCustomerDTO().getCustomerName()));
		model.addAttribute("options", estimateDTO.getOptions());
		if (estimateId != null && estimateId > 0)
			model.addAttribute("estimate", estimateDTO);
		return "estimate/edit-estimate";
	}

	/**
	 * This method is used to update estimate
	 * 
	 * @author ritiks
	 * @date Jul 26, 2023
	 * @return String
	 * @param httpSession
	 * @param servletRequest
	 * @return
	 * @exception Description
	 */
	@PostMapping("/process-update")
	public String processUpdateEstimate(HttpSession httpSession, HttpServletRequest servletRequest) {
		log.info("Enter inside EstimateController.processUpdateEstimate() method.");
		EstimateDTO extractEstimateFormObject = this.estimateService.extractEstimateFormObject(servletRequest);
		extractEstimateFormObject.setId(Long.valueOf(servletRequest.getParameter("estimateId")));
		this.estimateService.updateEstimate(extractEstimateFormObject, httpSession);
		return "redirect:/estimate/estimates";
	}

	/**
	 * This method is used to save and send estimate simultaneously
	 * 
	 * @author ritiks
	 * @date Jun 22, 2023
	 * @return Map<String,Object>
	 * @param httpSession
	 * @param servletRequest
	 * @param principal
	 * @return
	 * @exception Description
	 */
	@PostMapping("/process-save-send")
	@ResponseBody
	public Map<String, Object> processSaveAndSendEstimate(HttpSession httpSession, HttpServletRequest servletRequest,
			Principal principal) {
		log.info("Enter inside EstimateController.processAddEstimate() method.");
		EstimateDTO extractEstimateFormObject = this.estimateService.extractEstimateFormObject(servletRequest);
		EstimateDTO estimateDTO = new EstimateDTO();
		if (extractEstimateFormObject.getId() != null) {
			CommonResponse<EstimateDTO> updateEstimate = this.estimateService.updateEstimate(extractEstimateFormObject,
					httpSession);
			estimateDTO = updateEstimate.getData();
		} else
			estimateDTO = this.estimateService.saveEstimate(extractEstimateFormObject, httpSession, principal)
					.getData();
		httpSession.setAttribute("message", new Message(env.getProperty("estimate.save.send"), "success"));
		Map<String, Object> data = new HashMap<>();
		data.put("fromEmailAddress", fromEmailAddress);
		data.put("estimateEmailSubject", estimateEmailSubject);
		data.put("bodyContent", EstimateEmailBody.getBody(estimateDTO.getCustomerDTO().getCustomerName()));
		List<Long> optionIds = estimateDTO.getOptions().stream().map(op -> op.getId()).collect(Collectors.toList());
		data.put("options", optionIds);
		data.put("customerEmail", estimateDTO.getCustomerDTO().getEmails().get(0).getEmail());
		data.put("estimateId", estimateDTO.getId());
		data.put("estimateDTO", estimateDTO);
		return data;
	}

	/**
	 * This method is used to update and send estimate simultaneously
	 * 
	 * @author ritiks
	 * @date Aug 20, 2023
	 * @return Map<String,Object>
	 * @param httpSession
	 * @param servletRequest
	 * @return
	 * @exception Description
	 */
	@PostMapping("/process-update-send")
	@ResponseBody
	public Map<String, Object> processUpdateAndSendEstimate(HttpSession httpSession,
			HttpServletRequest servletRequest) {
		log.info("Enter inside EstimateController.processAddEstimate() method.");
		EstimateDTO extractEstimateFormObject = this.estimateService.extractEstimateFormObject(servletRequest);
		extractEstimateFormObject.setId(Long.valueOf(servletRequest.getParameter("estimateId")));
		EstimateDTO estimateDTO = new EstimateDTO();
		CommonResponse<EstimateDTO> updateEstimate = this.estimateService.updateEstimate(extractEstimateFormObject,
				httpSession);
		estimateDTO = updateEstimate.getData();
		httpSession.setAttribute("message", new Message(env.getProperty("estimate.update.send"), "success"));
		Map<String, Object> data = new HashMap<>();
		data.put("estimateId", estimateDTO.getId());
		data.put("fromEmailAddress", fromEmailAddress);
		data.put("estimateEmailSubject", estimateEmailSubject);
		data.put("bodyContent", EstimateEmailBody.getBody(estimateDTO.getCustomerDTO().getCustomerName()));
		List<Long> optionIds = estimateDTO.getOptions().stream().map(op -> op.getId()).collect(Collectors.toList());
		data.put("options", optionIds);
		data.put("customerEmail", estimateDTO.getCustomerDTO().getEmails().get(0).getEmail());
		return data;
	}

	/**
	 * This method is used to populate option send email details on popup
	 * 
	 * @author ritiks
	 * @date Sep 8, 2023
	 * @return Map<String,Object>
	 * @param estimateId
	 * @return
	 * @exception Description
	 */
	@GetMapping("/estimate-options/{estimateId}")
	@ResponseBody
	public Map<String, Object> populateEstimateDetailsOnSendMailPopup(@PathVariable Long estimateId) {
		log.info("Enter inside EstimateController.processSaveAndSendEstimate() method.");
		EstimateDTO estimateDTO = this.estimateService.getEstimatesById(estimateId).getData();
		Map<String, Object> data = new HashMap<>();
		data.put("fromEmailAddress", fromEmailAddress);
		data.put("estimateEmailSubject", estimateEmailSubject);
		data.put("bodyContent", EstimateEmailBody.getBody(estimateDTO.getCustomerDTO().getCustomerName()));
		List<Long> optionIds = estimateDTO.getOptions().stream().map(op -> op.getId()).collect(Collectors.toList());
		data.put("options", optionIds);
		data.put("customerEmail", estimateDTO.getCustomerDTO().getEmails().get(0).getEmail());
		data.put("estimateId", estimateDTO.getId());
		data.put("estimateDTO", estimateDTO);
		return data;
	}

	/**
	 * This method is used to get details related to given estimate and populate it
	 * on Estimate PDF HTML view
	 * 
	 * @author ritiks
	 * @date Sep 15, 2023
	 * @return Map<String,Object>
	 * @param estimateId
	 * @return
	 * @exception Description
	 */
	@GetMapping("/estimate-options-html-view/{estimateId}")
	@ResponseBody
	public Map<String, Object> populateEstimateDetailsOnEstimateHTMLPDFView(@PathVariable Long estimateId) {
		log.info("Enter inside EstimateController.populateEstimateDetailsOnEstimateHTMLPDFView() method.");
		EstimateDTO estimateDTO = this.estimateService.getEstimatesById(estimateId).getData();
		Map<String, Object> data = new HashMap<>();
		data.put("estimateId", estimateDTO.getId());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		data.put("estimateDate", estimateDTO.getCreatedAt().format(formatter));
		data.put("estimateCustomerName", estimateDTO.getCustomerDTO().getCustomerName());
		List<Option> options = estimateDTO.getOptions();
		options.stream().forEach(op -> {
			op.getOptionProducts().stream().forEach(opPro -> {
				if (opPro.getProductName() != null && !opPro.getProductName().equals("")) {
					Product product = new Product();
					product.setProductName(opPro.getProductName());
					product.setAverageCost(opPro.getProductCost());
					product.setDiscription(opPro.getProductDiscription());
					product.setRegularPrice(opPro.getRate());
					opPro.setProduct(product);
				} else {
					Services services = new Services();
					services.setServiceName(opPro.getServiceName());
					services.setInternalCost(opPro.getServiceCost());
					services.setDiscription(opPro.getServiceDiscription());
					services.setRegularPrice(opPro.getRate());
					opPro.setServices(services);
				}
			});
		});
		data.put("option", options);
		data.put("showItemPrice", false);
		data.put("showItemTotal", false);
		data.put("showItemQuantity", false);
		data.put("showItemGrandTotal", true);
		return data;
	}

	@ResponseBody
	@GetMapping("/option/details/{optionId}")
	public Option optionDetails(@PathVariable Long optionId) {
		return this.estimateService.optionDetailsByOptionId(optionId);
	}

}
