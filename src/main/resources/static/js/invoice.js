// Navigation Active and Inactive JS
$(function() {
	var url = window.location;
	var element = $('.navbar_Sec ul a').filter(function() {
		return this.href == url;
	}).addClass('active').parent().addClass('active');
	while (true) {
		if (element.is('li')) {
			element = element.parent().addClass('ul_Active').slideDown(100).parent().addClass('activeCustome_Nav').children('a').addClass('active');
		} else {
			break;
		}
	}
	$('.navbar_Sec ul a').on('click', function(e) {
		if (!$(this).hasClass("active")) {
			// hide any open menus and remove all other classes
			$("ul", $(this).parents("ul:first")).slideUp(200);
			$("ul", $(this).parents("ul:first")).removeClass("ulBock");
			$("a", $(this).parents("ul:first")).removeClass("active");

			// open our new menu and add the open class
			$(this).next("ul").slideDown(100);
			$(this).next("ul").addClass("ulBock");
			$(this).addClass("active");
		} else if ($(this).hasClass("active")) {
			$(this).removeClass("active");
			$(this).parents("ul:first").removeClass("active");
			$(this).next("ul").slideUp(100);
			$(this).next("ul").removeClass("ulBock");
		}
	})
});

$(document).on("click", function() {
	$('.search-results').empty();
});

//Remove alert after showing
document.addEventListener("DOMContentLoaded", function() {
	setTimeout(function() {
		$(".alert").removeClass('show');
		$(".alert").addClass('d-none');
	}, 3000);
});

function convertIntoPriceFormat(input) {
	return "$" + input.toLocaleString('en-US');
}

$(document).ready(function() {

	$("#estimate-form").submit(function(event) {
		validateEstimateForm(event);
	});

});

function generateProductRow(tableId) {
	var lastTr = $("#invoice-table" + tableId + " tr:last");
	if (lastTr.find("input:first").val().length > 0) {
		var rowNo = $('#invoice-table' + tableId + ' tbody tr').length;
		lastTr.find("input:first").removeAttr("onclick");
		rowNo++;
		html = '<tr id="invoice-table' + tableId + 'tr' + rowNo + '">';
		html += '<td>' + rowNo + '</td>';
		html += '<td><div class="input-with-icon" style="position: relative;"><input type="text" onclick="generateProductRow(' + tableId + ')" autocomplete="off" placeholder="Type for search product/service.." class="form-control" name="invoice-table' + tableId + 'ProductName' + rowNo + '" id="invoice-table' + tableId + 'ProductName' + rowNo + '" value="" onKeyUp="onSearchType(event)" /></div><div id="invoice-table' + tableId + 'search_results' + rowNo + '" class="search-results"></td>';
		html += '<td><input type="text" class="form-control" id="invoice-table' + tableId + 'Description' + rowNo + '" name="invoice-table' + tableId + 'Description' + rowNo + '" value=""></td>';
		html += '<td><input type="number" class="form-control" id="invoice-table' + tableId + 'Quantity' + rowNo + '" name="invoice-table' + tableId + 'Quantity' + rowNo + '" value="1.0" onchange="calculateTotal($(\'#\'+this.id))" min="1"></td>';
		html += '<td><input type="text" class="form-control" readonly id="invoice-table' + tableId + 'Rate' + rowNo + '" name="invoice-table' + tableId + 'Rate' + rowNo + '" value="0" onchange="calculateTotal($(\'#\'+this.id))"></td>';
		html += '<td><input type="text" class="form-control" readonly id="invoice-table' + tableId + 'Total' + rowNo + '" name="invoice-table' + tableId + 'Total' + rowNo + '" value="0" readonly></td>';
		html += '<td ><span><button type="button"  style="cursor: pointer;" class="table-action-buttons del-btn del-row" name="' + rowNo + '" onclick="removeProductRow(' + tableId + ',' + rowNo + ')"><img src="/assest/trash.svg" class="img-fluid plan-carousel-btn"></button></span></td>';
		html += '</tr>';

		$('#invoice-table' + tableId + ' tbody').append(html);
	}
}


var isButtonAdded = false;

function addCustomerButton(customerName) {
	if (!isButtonAdded) {
		var addButtonContainer = $('<div class="col-md-6 position-relative plusCustomerButton" style="margin-top: 4%;">' +
			'<button type="button" class="add_estimates" data-bs-toggle="modal" data-bs-target="#addCustomerModal">' +
			'<img src="/assest/plus_circle.png" class="img-icon" alt="">' +
			'<span id="add_customer">Add Customer ' + customerName + '</span>' +
			'</button>' +
			'</div>');

		// Append the button container above the search results
		$('#customer_search_results').before(addButtonContainer);

		// Set the flag to true, so the button won't be added again
		isButtonAdded = true;
	}
}

function hideAddCustomerButton() {
	$('.plusCustomerButton').hide();
	isButtonAdded = false;
}

function searchCustomer(event) {
	var searchResultsContainer = $('#customer_search_results');
	searchResultsContainer.empty();
	var customerName = event.target.value;
	if (customerName.length > 0) {
		$("#customer_name").val(customerName);
		$.ajax({
			method: 'GET',
			url: '/customer/searchByName/' + customerName,
			success: function(response) {
				searchResultsContainer.empty();
				response.forEach(function(result) {
					var resultItem = document.createElement("div");
					resultItem.classList.add("result-item");
					resultItem.textContent = result.customerName;
					// Add click event listener to populate the selected result in the input field
					resultItem.addEventListener("click", function() {
						searchResultsContainer.empty(); // Clear the search results after selecting
						console.log(result);
						$('#customer_name_input').val(result.customerName);
						$('#paren_acc').val(result.customerParentAccount);
						$('#es_location_name').val(result.storedServiceLocations[0].locationNickName);
						$('#es_apt_suite').val(result.storedServiceLocations[0].unit);
						$('#es_loca_address').val(result.storedServiceLocations[0].address);
						$('#es_locat_city').val(result.storedServiceLocations[0].city);
						$('#es_locat_state').val(result.storedServiceLocations[0].state);
						//$('#locat_zip').val(result.storedServiceLocations[0].address); //Enable it for auto populate ZIP
						if (result.contactNumbers.length != 0) {
							$("#multi-mobile").val(result.contactNumbers[0].contactNumberCategory);
							$('#estimate_mobileID').val(result.contactNumbers[0].number);
						}
						if (result.emails.length != 0) {
							$("#multi-email-estimate").val(result.emails[0].emailCategory);
							$('#estimate_emailID').val(result.emails[0].email);
						}
					});
					searchResultsContainer.append(resultItem);
				});
				showAddCustomerBtn();
			},
			error: function(error) {
				console.log('Error while searching Customer :', error);
			},
		});
	}
}


function showAddServiceAddProductBtn(invoiceTableId, productRowNo) {
	var searchResultsContainer = $('#invoice-table' + invoiceTableId + 'search_results' + productRowNo);
	// Create the main wrapper div element and set its class
	var productServiceWrapper = $("<div>", {
		class: "product_service_suggestions_wrapper"
	});

	// Create the proservice_withbuttons div element and set its class
	var proserviceWithButtons = $("<div>", {
		class: "proservice_withbuttons"
	});

	// Create the proservice_buttons div element and set its class
	var proserviceButtons = $("<div>", {
		class: "proservice_buttons"
	});

	// Create the first button element and set its attributes
	var addProductButton = $("<button>", {
		type: "button",
		class: "add_estimates",
		"data-bs-toggle": "modal",
		"data-bs-target": "#addProductModal"
	}).append(
		$("<img>", {
			src: "/assest/plus_circle.png",
			class: "img-icon",
			alt: ""
		}),
		"Add Product " + $('#invoice-table' + invoiceTableId + 'ProductName' + productRowNo).val()
	);

	// Create the second button element and set its attributes
	var addServiceButton = $("<button>", {
		type: "button",
		class: "add_estimates",
		style: "margin-left: 8rem;",
		"data-bs-toggle": "modal",
		"data-bs-target": "#addServiceModal"
	}).append(
		$("<img>", {
			src: "/assest/plus_circle.png",
			class: "img-icon",
			alt: ""
		}),
		"Add Service " + $('#invoice-table' + invoiceTableId + 'ProductName' + productRowNo).val()
	);

	// Append the buttons to the proservice_buttons div
	proserviceButtons.append(addProductButton, addServiceButton);

	// Append the proservice_buttons div to the proservice_withbuttons div
	proserviceWithButtons.append(proserviceButtons);

	// Append the proservice_withbuttons div to the main wrapper div
	productServiceWrapper.append(proserviceWithButtons);

	// Append the statusSpan after the text content in the resultItem div
	searchResultsContainer.append(productServiceWrapper);
}

function showAddCustomerBtn() {
	var searchResultsContainer = $('#customer_search_results');
	// Create the main wrapper div element and set its class
	var productServiceWrapper = $("<div>", {
		class: "product_service_suggestions_wrapper"
	});

	// Create the proservice_withbuttons div element and set its class
	var proserviceWithButtons = $("<div>", {
		class: "proservice_withbuttons"
	});

	// Create the proservice_buttons div element and set its class
	var proserviceButtons = $("<div>", {
		class: "proservice_buttons"
	});

	// Create the first button element and set its attributes
	var addProductButton = $("<button>", {
		type: "button",
		class: "add_estimates",
		"data-bs-toggle": "modal",
		"data-bs-target": "#addCustomerModal"
	}).append(
		$("<img>", {
			src: "/assest/plus_circle.png",
			class: "img-icon",
			alt: ""
		}),
		"Add Customer " + $('#customer_name_input').val()
	);
	// Append the buttons to the proservice_buttons div
	proserviceButtons.append(addProductButton);

	// Append the proservice_buttons div to the proservice_withbuttons div
	proserviceWithButtons.append(proserviceButtons);

	// Append the proservice_withbuttons div to the main wrapper div
	productServiceWrapper.append(proserviceWithButtons);

	// Append the statusSpan after the text content in the resultItem div
	searchResultsContainer.append(productServiceWrapper);
}


function viewEstimate(estimateId) {
	$.ajax({
		type: 'GET',
		url: '/estimate/add',
		data: {
			estimateId: estimateId
		},
		success: function(response) {
			console.log('AJAX request successful. Response:', response);
			if (response && response.trim() !== '') {
				// Open the new page using the received URL
				window.location.href = response;
			}
		},
		error: function(xhr, status, error) {
			console.error('AJAX request failed. Status:', status);
		}
	});
}

function addProductPopup() {

	var form = $("#form_prodct");
	var formData = new FormData(form[0]);

	// Send the form data using AJAX
	$.ajax({
		type: "POST",
		url: "/product/process-popup-addProduct", // Replace with your server URL
		data: formData,
		processData: false,
		contentType: false,
		success: function(response) {
			// Handle the response from the server
			console.log("Form submitted successfully.");
			// You can perform any additional actions here after successful submission               
			$("#addProductPopupCloseBtn").click();

			var invoiceTableNumber = $("#clickedInvoiceTableNumber").val();
			var invoiceTableRowNumber = $("#clickedInvoiceTableRowNumber").val();
			$("#invoice-table" + invoiceTableNumber + "ProductName" + invoiceTableRowNumber).val(response.productName);
		},
		error: function(xhr, status, error) {
			console.error("Error submitting form: " + error);
			$("#addProductPopupCloseBtn").click();
		}
	});

}


function addServicePopup() {

	var form = $("#form_service");
	var formData = new FormData(form[0]);

	// Send the form data using AJAX
	$.ajax({
		type: "POST",
		url: "/service/process-popup-addService", // Replace with your server URL
		data: formData,
		processData: false,
		contentType: false,
		success: function(response) {
			// Handle the response from the server
			console.log("Form submitted successfully.");
			// You can perform any additional actions here after successful submission               
			$("#addServicePopupCloseBtn").click();
		},
		error: function(xhr, status, error) {
			console.error("Error submitting form: " + error);
			$("#addServicePopupCloseBtn").click();
		}
	});


}

/* Validation for staff mobile*/
function validateMobile() {
	var mobile = document.getElementById("customer_mobileID").value;
	var mobilePattern = /^\d{10}$/;
	var mobileError = document.getElementById("mobile-error");
	if (!mobilePattern.test(mobile)) {
		mobileError.textContent = "Invalid mobile number";
	}
	else {
		mobileError.textContent = "";
	}
}

function addCustomerPopup() {
	var valid = true;
	$(".error-message").empty();

	var customerName = $("#customer_name").val();
	var mobileNumber = $("#customer_mobileID").val();
	var customerEmail = $("#customer_emailID").val();

	if (customerName === "") {
		$("#cp-name-error").text("Name is required");
		valid = false;
	}
	if (mobileNumber === "") {
		$("#mobile-error").text("Mobile number is required");
		valid = false;
	}
	if (customerEmail === "") {
		$("#msg").text("Email is required");
		valid = false;
	}

	if (!valid) {
		$("#customer_name").focus(function() {
			$("#name-error").empty();
		});
		$("#custMobile").focus(function() {
			$("#mobile-error").empty();
		});
		$("#customerEmail").focus(function() {
			$("#msg").empty();
		});
	} else {
		var form = $("#form_cust");
		var formData = new FormData(form[0]);
		// Send the form data using AJAX
		$.ajax({
			type: "POST",
			url: "/customer/process-popup-add", // Replace with your server URL
			data: formData,
			processData: false,
			contentType: false,
			success: function(response) {
				var customerName = $("#customer_name").val();
				if (customerName.length > 0) {
					$("#customer_name").val(customerName);
					$.ajax({
						method: 'GET',
						url: '/customer/searchByName/' + customerName,
						success: function(response) {
							response.forEach(function(result) {
								$('#customer_name_input').val(result.customerName);
								$('#paren_acc').val(result.customerParentAccount);
								$('#es_location_name').val(result.storedServiceLocations[0].locationNickName);
								$('#es_apt_suite').val(result.storedServiceLocations[0].unit);
								$('#es_loca_address').val(result.storedServiceLocations[0].address);
								$('#es_locat_city').val(result.storedServiceLocations[0].city);
								$('#es_locat_state').val(result.storedServiceLocations[0].state);
								//$('#locat_zip').val(result.storedServiceLocations[0].address); //Enable it for auto populate ZIP
								if (result.contactNumbers.length != 0) {
									$("#multi-mobile").val(result.contactNumbers[0].contactNumberCategory);
									$('#estimate_mobileID').val(result.contactNumbers[0].number);
								}
								if (result.emails.length != 0) {
									$("#multi-email-estimate").val(result.emails[0].emailCategory);
									$('#estimate_emailID').val(result.emails[0].email);
								}
							});
						},
						error: function(error) {
							console.log('Error while searching Customer :', error);
						},
					});
				}
				alert("Customer added successfully!");
				$('#form_cust')[0].reset();
				$(".btn-close").trigger('click');
			},
			error: function(xhr, status, error) {
				alert("Error !");
				$(".btn-close").trigger('click');
			}
		});
	}


}

function onSearchType(event) {
	const searchFieldName = event.target.id;
	var numericValues = searchFieldName.match(/\d+/g);
	var invoiceTableId = numericValues[0];
	var productRowNo = numericValues[1];
	var searchResultsContainer = $('#invoice-table' + invoiceTableId + 'search_results' + productRowNo);
	searchResultsContainer.empty();
	var productName = event.target.value;

	if (productName.length > 0) {
		$('#inpproduct').val(productName);
		$('#clickedinvoiceTableNumber').val(invoiceTableId);
		$('#clickedinvoiceTableRowNumber').val(productRowNo);

		$('#inp_service_name').val(productName);
		$.ajax({
			method: 'GET',
			url: '/common/' + productName,
			success: function(response) {
				response.products.forEach(function(result) {
					var resultItem = document.createElement("div");
					resultItem.classList.add("result-item");
					resultItem.textContent = result.productName;

					resultItem.addEventListener("click", function() {
						searchResultsContainer.empty();
						var productNameInputField = $('#invoice-table' + invoiceTableId + 'ProductName' + productRowNo);
						productNameInputField.val(result.productName);
						$('#invoice-table' + invoiceTableId + 'Description' + productRowNo).val(result.discription);
						$('#invoice-table' + invoiceTableId + 'Rate' + productRowNo).val(convertIntoPriceFormat(result.regularPrice));
						$('#invoice-table' + invoiceTableId + 'AverageCost' + productRowNo).val(convertIntoPriceFormat(result.averageCost));
						$('#invoice-table' + invoiceTableId + 'AverageCostIntial' + productRowNo).val(convertIntoPriceFormat(result.averageCost));
						calculateTotal($('#invoice-table' + invoiceTableId + 'Quantity' + productRowNo));
						generateProductRow(invoiceTableId);
					});

					const resultItems = $(".search-results .result-item");

					// Initialize an array to store the inner text of each div
					const innerTextArray = [];

					// Loop through each div and extract the inner text
					resultItems.each(function() {
						innerTextArray.push($(this).text());
					});

					if ($.inArray(result.productName, innerTextArray) === -1) {
						searchResultsContainer.append(resultItem);
					}
				});
				response.services.forEach(function(result) {
					var resultItem = document.createElement("div");
					resultItem.classList.add("result-item");
					resultItem.textContent = result.serviceName;

					resultItem.addEventListener("click", function() {
						searchResultsContainer.empty();
						var productNameInput = $('#invoice-table' + invoiceTableId + 'ProductName' + productRowNo);
						productNameInput.attr("name", "invoice-table" + invoiceTableId + "ServiceName" + productRowNo);
						productNameInput.val(result.serviceName);

						$('#invoice-table' + invoiceTableId + 'Description' + productRowNo).val(result.discription);
						var rateInput = $('#invoice-table' + invoiceTableId + 'Rate' + productRowNo);
						rateInput.val(convertIntoPriceFormat(result.regularPrice));
						var avrgCostInput = $('#invoice-table' + invoiceTableId + 'AverageCost' + productRowNo);
						avrgCostInput.val(convertIntoPriceFormat(result.internalCost));
						var avrgCostInt = $('#invoice-table' + invoiceTableId + 'AverageCostIntial' + productRowNo);
						avrgCostInt.val(convertIntoPriceFormat(result.internalCost));
						calculateTotal($('#invoice-table' + invoiceTableId + 'Quantity' + productRowNo));
						generateProductRow(invoiceTableId);
					});

					const resultItems = $(".search-results .result-item");

					// Initialize an array to store the inner text of each div
					const innerTextArray = [];

					// Loop through each div and extract the inner text
					resultItems.each(function() {
						innerTextArray.push($(this).text());
					});

					if ($.inArray(result.productName, innerTextArray) === -1) {
						searchResultsContainer.append(resultItem);
					}
				});
				showAddServiceAddProductBtn(invoiceTableId, productRowNo);
			},
			error: function(error) {
				// Error handling
				console.log('Error:', error);
			}
		});
	}
}

function showAddProductBtn(invoiceTableId, productRowNo) {
	var searchResultsContainer = $('#invoice-table' + invoiceTableId + 'search_results' + productRowNo);
	searchResultsContainer.append("<h5>Add Button</h5>");
}

function calculateTotal(input) {
	var tableRow = input.parents('tr');

	const inputId = input.attr('id');
	var numericValues = inputId.match(/\d+/g);
	var invoiceTableId = numericValues[0];
	var productRowNo = numericValues[1];

	var rateInput = tableRow.find('input[type="text"]').eq(2);
	var rateValue = rateInput.val();

	// Find the quantity input field within the table row
	var quantityInput = input;
	var quantityValue = quantityInput.val();


	rateValue = rateValue.replace("$", "");
	rateValue = rateValue.replace(",", "");
	rateValue = parseFloat(rateValue);
	// Calculate the total
	var totalValue = rateValue * quantityValue;

	// Find the total input field within the table row
	var totalInput = tableRow.find('input[type="text"]').eq(3);
	totalInput.val(convertIntoPriceFormat(totalValue));



	calculateSummary(invoiceTableId, productRowNo);

}

function calculateSummary(invoiceTableId, productRowNo) {
	var invoiceTotal = $('#invoice-table' + invoiceTableId + 'InvoiceTotal');
	var invoiceTotalTxt = $('#invoice-table' + invoiceTableId + 'InvoiceTotalTxt');

	let grandTotal = 0.00;

	var invoiceTblId = '#invoice-table' + invoiceTableId + ' tbody tr';

	$(invoiceTblId).each(function(index, row) {
		var totalTrId = '#invoice-table' + invoiceTableId + 'Total' + (index + 1);
		var totalVal = $(totalTrId).val();
		totalVal = totalVal.replace("$", "");
		totalVal = totalVal.replace(",", "");
		const totalTrVal = parseFloat(totalVal) || 0.00;
		grandTotal += totalTrVal;
	});

	invoiceTotal.val(grandTotal.toFixed(2));
	invoiceTotalTxt.text(convertIntoPriceFormat(grandTotal));
}



function togglePasswordVisibility() {
	const passwordInput = document.getElementById("password");
	const eyeIcon = document.getElementById("eyeIcon");

	if (passwordInput.type === "password") {
		passwordInput.type = "text";
		eyeIcon.classList.remove("fa-eye");
		eyeIcon.classList.add("fa-eye-slash");
	} else {
		passwordInput.type = "password";
		eyeIcon.classList.remove("fa-eye-slash");
		eyeIcon.classList.add("fa-eye");
	}
}

function togglePasswordVisibilityy() {
	const passwordInput = document.getElementById("confirm-password");
	const eyeIcon = document.getElementById("eyeIconn");

	if (passwordInput.type === "password") {
		passwordInput.type = "text";
		eyeIcon.classList.remove("fa-eye");
		eyeIcon.classList.add("fa-eye-slash");
	} else {
		passwordInput.type = "password";
		eyeIcon.classList.remove("fa-eye-slash");
		eyeIcon.classList.add("fa-eye");
	}
}

function generateOption() {

	let tableId = $('#myTab_custom > .nav-item').length;
	tableId++;

	$('#myTab_custom').find('li:last-child .invoice-close').remove();

	$('#myTab_custom').append($('<li class="nav-item" role="presentation"><button class="nav-link" id="tab_label' + tableId + '" data-bs-target="#tab' + tableId + '"  data-bs-toggle="tab" type="button" role="tab" aria-controls="tab' + tableId + '" aria-selected="false"><div class="row"><div class="col-md-9 input-items"><img src="/assest/document-signed.svg" class="img-icon me-2"><lable class="invoice-nav-item-label">Option ' + tableId + ' </lable></div><div class="col-md-3 input-items"><span class="dropdown"><div class="nav-user-img" role="button" data-bs-toggle="dropdown" aria-expanded="false"      id="jobMenu"><img src="/assest/estimate-menu-white.svg" class="img-icon" alt="profile name" id="jobMenuWhiteIcon"> <img src="/assest/estimate-menu-black.svg" class="img-icon d-none" alt="profile name" id="jobMenuBlackIcon"></div><ul class="dropdown-menu estimate-custom-dropdown"><li><a onclick="duplicateJob(' + tableId + ')" class="dropdown-item"><span>Duplicate</span></a><a class="dropdown-item job-close" type="button" id="deleteOptionBtn' + tableId + '"><span>Delete</span></a></li></ul></span></div></div></button></li>')).insertAfter;
	$('#myTabContent_custom').append($('<div class="tab-pane fade" id="tab' + tableId + '" role="tabpanel" aria-labelledby="tab_label' + tableId + '">Tab ' + tableId + ' content</div>'));
	let a = `<div class="estimate_report_container">
                                                                           <div class="container-fluid custome-data-table mt-5 p-0 overflow-auto">
                                                                                  <ul id="search-results"></ul><table id="invoice-table`+ tableId + `" name="invoice-table` + tableId + `" class="table custom-table custom-category-table dt-responsive nowrap w-100"><input type="hidden" name="invoice-table` + tableId + `-input" value=1>
                                                                                         <thead>
                                                                                                <tr>
                                                                                                       <th>                                                                                                          
                                                                                                       </th>
                                                                                                       <th width="40%">Product/Service</th>
                                                                                                       <th>Description</th>
                                                                                                       <th>Quantity</th>
                                                                                                       <th>Rate</th>
                                                                                                       <th>Total</th>
                                                                                                       <th>Cost</th>
                                                                                                       <th>Action</th>
                                                                                                </tr>
                                                                                         </thead>
                                                                                         <tbody>
                                                                                                <tr id="invoice-table`+ tableId + `tr1">
                                                                                                       <td>1</td>
                                                                                                       <td>
                                                                                                       <div class="input-with-icon" style="position: relative;">
                                                                                                       <input type="text" autocomplete="off" onclick="generateProductRow(`+ tableId + `)" placeholder="Type for search product.." class="form-control" name="invoice-table` + tableId + `ProductName1" id="invoice-table` + tableId + `ProductName1" value="" onkeyup="onSearchType(event)">
                                                                                                       <span class="icon-span status-capsule requested d-none"
                                                                                                                           style="border-radius: 10px !important;"
                                                                                                                           id="invoice-table` + tableId + `inp-cap1">Product</span>
                                                                                                              </div>
                                                                                                              <div id="invoice-table` + tableId + `search_results1" class="search-results"></div>
                                                                                                       </td>
                                                                                                       <td>
                                                                                                              <input type="text" class="form-control"
                                                                                                                     placeholder="" id="invoice-table`+ tableId + `Description1"
                                                                                                                     name="invoice-table`+ tableId + `Description1" value="" >
                                                                                                       </td>
                                                                                                       <td><input type="number" class="form-control" id="invoice-table` + tableId + `Quantity1" name="invoice-table` + tableId + `Quantity1" value="1.0" onchange="calculateTotal($(\'#\'+this.id))" min="1"></td>
                                                                                                       <td><input type="text" class="form-control"  id="invoice-table` + tableId + `Rate1" name="invoice-table` + tableId + `Rate1" onchange="calculateTotal($(\'#\'+this.id))" value="0" readonly></td>
                                                                                                       <td><input type="text" class="form-control" id="invoice-table` + tableId + `Total1" name="invoice-table` + tableId + `Total1" value="0" readonly></td>
                                                                                                       <td><input type="text" class="form-control" name="invoice-table` + tableId + `AverageCost1"  id="invoice-table` + tableId + `AverageCost1" value="0" readonly><input type="hidden" class="form-control" name="invoice-table` + tableId + `AverageCostIntial1"  id="invoice-table` + tableId + `AverageCostIntial1" value="0" ></td>
                                                                                                       <td>
                                                                                                              <span>
                                                                                                                     <button type="button" disabled="" class="table-action-buttons del-btn del-row">
                                                                                                                           <img src="/assest/trash.svg" class="img-fluid plan-carousel-btn" alt="icon">
                                                                                                                     </button>
                                                                                                              </span>
                                                                                                       </td>
                                                                                                </tr>
                                                                                         </tbody>
                                                                                  </table>
                                                                           </div>
                                                                           <div class="estimate_summary_container mt-5">
                                                                                  <div class="row">
                                                                                         <div class="col-lg-6 mb-3 mb-lg-0">
                                                                                                <label for="inpexpiry" class="form-label">Notes to
                                                                                                       Customer</label>
                                                                                                <textarea class="form-control custom-area" id="inpexpiry" rows="3"></textarea>
                                                                                         </div>
                                                                                         <div class="col-lg-6">
                                                                                                <div class="invoice_summary">
                                                                                                       <div class="order-summary ">
                                                                                                              <h4 class="text-end fs-5">Job Summary</h4>
                                                                                                              <hr>
                                                                                                              <div class="plan">
                                                                                                                     <p>Job Total :</p>
                                                                                                                     <input type="hidden" id="invoice-table`+ tableId + `JobTotal"  name="invoice-table` + tableId + `JobTotal" />
                                                                                                                     <h5 id="invoice-table`+ tableId + `JobTotalTxt">$00.00</h5>
                                                                                                              </div>
                                                                                                              <div class="plan">
                                                                                                                     <p>Job Cost :</p>
                                                                                                                     <input type="hidden" id="invoice-table`+ tableId + `JobCost"  name="invoice-table` + tableId + `JobCost" />
                                                                                                                     <h5 id="invoice-table`+ tableId + `JobCostTxt">$0.00</h5>
                                                                                                              </div>
                                                                                                              <div class="plan">
                                                                                                                     <p>
                                                                                                                           Gross Profit :
                                                                                                                     </p>
                                                                                                                     <input type="hidden" id="invoice-table`+ tableId + `GrossProfit"  name="invoice-table` + tableId + `GrossProfit"/>
                                                                                                                     <h5 id="invoice-table`+ tableId + `GrossProfitTxt">$00.00</h5>
                                                                                                              </div>
                                                                                                       </div>
                                                                                                </div>
                                                                                         </div>
                                                                                  </div>
                                                                           </div>
                                                                     </div>`;
	$(('#tab' + tableId + '')).append(a)
	$("#tab_label" + (tableId - 1)).removeClass("active");
	$("#tab" + (tableId - 1)).removeClass(" show active");
	$("#tab_label" + tableId).addClass("active");
	$("#tab" + tableId).addClass(" show active");
}


function removeProductRow(tableId, rowNo) {

	var pRowNo = rowNo - 1;
	$('#invoice-table' + tableId + 'tr' + pRowNo).find("input:first").attr('onclick', 'generateProductRow(' + tableId + ')');

	$('#invoice-table' + tableId + 'tr' + rowNo).remove();

	var l = $('#invoice-table' + tableId + ' tbody tr').length;

	var productRowCounter = 1;
	$('#invoice-table' + tableId + ' tbody tr').each(function index() {

		//Changing id of tr
		$(this).attr('id', "invoice-table" + tableId + "tr" + productRowCounter);
		//Changing text of td
		$(this).find('td:first').text(productRowCounter);
		//Changing attributes of Product Name input
		var productNameInput = $(this).find('input').eq(0);
		var productNameInputName = $(productNameInput).attr("name");
		if (productNameInputName.includes("ProductName")) {
			productNameInput.attr('id', "invoice-table" + tableId + "ProductName" + productRowCounter);
			productNameInput.attr('name', "invoice-table" + tableId + "ProductName" + productRowCounter);
		} else {
			productNameInput.attr('id', "invoice-table" + tableId + "ProductName" + productRowCounter);
			productNameInput.attr('name', "invoice-table" + tableId + "ServiceName" + productRowCounter);
		}

		//Changing cap id
		var capsule = productNameInput.next('span');
		capsule.attr('id', 'invoice-table' + tableId + 'inp-cap' + productRowCounter);

		var inpdiv = capsule.parent();
		var suggestionWrapperDiv = inpdiv.next();
		var searchResult = suggestionWrapperDiv.next();

		//Changing search suggestion div id
		searchResult.attr('id', 'invoice-table' + tableId + 'search_results' + productRowCounter);
		//Changing attributes of description input
		var quantityInput = $(this).find('input').eq(1);
		quantityInput.attr('id', "estimate-table" + tableId + "Description" + productRowCounter);
		quantityInput.attr('name', "estimate-table" + tableId + "Description" + productRowCounter);
		//Changing attributes of quantity input
		var quantityInput = $(this).find('input').eq(2);
		quantityInput.attr('id', "invoice-table" + tableId + "Quantity" + productRowCounter);
		quantityInput.attr('name', "invoice-table" + tableId + "Quantity" + productRowCounter);
		//Changing attributes of rate input
		var rateInpput = $(this).find('input').eq(3);
		rateInpput.attr('id', "invoice-table" + tableId + "Rate" + productRowCounter);
		rateInpput.attr('name', "invoice-table" + tableId + "Rate" + productRowCounter);
		//Changing attributes of total input
		var totalInput = $(this).find('input').eq(4);
		totalInput.attr('id', "invoice-table" + tableId + "Total" + productRowCounter);
		totalInput.attr('name', "invoice-table" + tableId + "Total" + productRowCounter);

		const button = $(this).find('.del-btn.del-row:last');
		if (button.length > 0) {
			// Get the current onclick attribute value
			const originalOnclick = button.attr('onclick');

			if (typeof originalOnclick !== 'undefined') {
				// Extract the parameters from the original onclick attribute
				const params = originalOnclick.match(/\d+/g);

				// Replace the parameters with the new values
				if (params.length >= 2) {
					params[0] = tableId;
					params[1] = productRowCounter;
				}

				// Create the updated onclick attribute value
				const updatedOnclick = `removeProductRow(${params.join(',')})`;

				// Set the updated onclick attribute for the last button in the current row
				button.attr('onclick', updatedOnclick);
			}
		}

		productRowCounter++;
	});
	calculateSummary(tableId, 1);
}


function duplicateInvoice(optionId) {
	var newOpt = $("#myTab_custom .nav-item").length;
	//Generate Option Grid
	generateOption();
	//Remove blank product row
	$("#invoice-table" + (newOpt + 1) + "tr1").remove();
	var counter = 1;
	//Iterate all the product row of option that has requested for duplicate
	$("#invoice-table" + (optionId) + " tbody > tr").each(function() {
		//Cloning product row
		var productRow = $(this).clone();
		//Append generated product row with new option              
		$("#invoice-table" + (newOpt + 1)).append(productRow);
		//Change id of generated product row > tr
		$(productRow).attr("id", "invoice-table" + (optionId + 1) + "tr" + counter);
		var prRowTd = $(productRow).find("td");
		//Get all inputs of the tr
		var prRowInps = prRowTd.find("input");
		//Get product name input and change all the attributes
		var prNameInp = prRowInps[0];
		var prNameName = $(prNameInp).attr("name");
		if (prNameName.includes("ProductName")) {
			$(prNameInp).attr("id", "invoice-table" + (newOpt + 1) + "ProductName" + counter);
			$(prNameInp).attr("name", "invoice-table" + (newOpt + 1) + "ProductName" + counter);
		} else {
			$(prNameInp).attr("id", "invoice-table" + (newOpt + 1) + "ProductName" + counter);
			$(prNameInp).attr("name", "invoice-table" + (newOpt + 1) + "ServiceName" + counter);
		}

		//Add onclick if product row is last row
		if ($(prNameInp).attr('onclick'))
			$(prNameInp).attr("onclick", "generateProductRow(" + (newOpt + 1) + ")");
		//Change capsule id
		$(prNameInp).next().attr("id", "invoice-table" + (newOpt + 1) + "inp-cap" + counter);
		//Change search result id
		$(prRowTd).find(".search-results").attr("id", "invoice-table" + (newOpt + 1) + "search_results" + counter);
		//Get product quantity input and change all the attributes
		var prQuantityInp = prRowInps[2];
		$(prQuantityInp).attr("id", "invoice-table" + (newOpt + 1) + "Quantity" + counter);
		$(prQuantityInp).attr("name", "invoice-table" + (newOpt + 1) + "Quantity" + counter);
		//Get product rate input and change all the attributes
		var prRateInp = prRowInps[3];
		$(prRateInp).attr("id", "invoice-table" + (newOpt + 1) + "Rate" + counter);
		$(prRateInp).attr("name", "invoice-table" + (newOpt + 1) + "Rate" + counter);
		//Get product total input and change all the attributes
		var prTotalInp = prRowInps[4];
		$(prTotalInp).attr("id", "invoice-table" + (newOpt + 1) + "Total" + counter);
		$(prTotalInp).attr("name", "invoice-table" + (newOpt + 1) + "Total" + counter);
		//Get product Average Cost input and change all the attributes
		var prAvgCostInp = prRowInps[5];
		$(prAvgCostInp).attr("id", "invoice-table" + (newOpt + 1) + "AverageCost" + counter);
		$(prAvgCostInp).attr("name", "invoice-table" + (newOpt + 1) + "AverageCost" + counter);
		//Get product Average Cost input and change all the attributes
		var prAvgCostIntInp = prRowInps[6];
		$(prAvgCostIntInp).attr("id", "invoice-table" + (newOpt + 1) + "AverageCostIntial" + counter);
		var dltBtn = $(productRow).find(".del-row");
		if ($(dltBtn).attr("onclick"))
			$(dltBtn).attr("onclick", "removeProductRow(" + (newOpt + 1) + "," + counter + ")");

		/*Estimate Summary Section*/
		/*Estimate Total*/
		$("#invoice-table" + (newOpt + 1) + "JobTotal").val($("#invoice-table" + (optionId) + "JobTotal").val());
		$("#invoice-table" + (newOpt + 1) + "JobTotalTxt").text("$" + $("#invoice-table" + (optionId) + "JobTotal").val());
		/*Job Cost*/
		$("#invoice-table" + (newOpt + 1) + "JobCost").val($("#invoice-table" + (optionId) + "JobCost").val());
		$("#invoice-table" + (newOpt + 1) + "JobCostTxt").text("$" + $("#invoice-table" + (optionId) + "JobCost").val());
		/*Gross Profit*/
		$("#invoice-table" + (newOpt + 1) + "GrossProfit").val($("#invoice-table" + (optionId) + "GrossProfit").val());
		$("#invoice-table" + (newOpt + 1) + "GrossProfitTxt").text("$" + $("#invoice-table" + (optionId) + "GrossProfit").val());

		$("#tab_label" + (optionId)).removeClass("active");
		$("#tab" + (optionId)).removeClass(" show active");

		$("#tab_label" + (newOpt + 1)).addClass("active");
		$("#tab" + (newOpt + 1)).addClass(" show active");

		counter++;
	});
}


function updateEstimateHTMLView(event) {
	var checkBox = event.target;
	var hiddenOptionId;

	var checkBoxId = checkBox.id;
	if (checkBoxId.includes("optionCheck")) {
		hiddenOptionId = "option" + checkBoxId.substring(11, checkBoxId.length) + "HTMLView";
	}

	if (checkBox.checked) {
		$("#" + hiddenOptionId).removeClass("d-none");
	} else {
		$("#" + hiddenOptionId).removeClass("d-none");
	}
	if (checkBoxId === "optionQuantityCheck") {
		if (checkBox.checked) {
			$(".optionQuantityHeadHTMLView").removeClass("d-none");
			$(".optionQuantityTextHTMLView").removeClass("d-none");
		} else {
			$(".optionQuantityHeadHTMLView").addClass("d-none");
			$(".optionQuantityTextHTMLView").addClass("d-none");
		}
	}
	if (checkBoxId === "optionTotalCheck") {
		if (checkBox.checked) {
			$(".optionTotalHeadHTMLView").removeClass("d-none");
			$(".optionTotalTextHTMLView").removeClass("d-none");
		} else {
			$(".optionTotalHeadHTMLView").addClass("d-none");
			$(".optionTotalTextHTMLView").addClass("d-none");
		}
	}
	if (checkBoxId === "optionPriceCheck") {
		if (checkBox.checked) {
			$(".optionRateHeadHTMLView").removeClass("d-none");
			$(".optionRateTextHTMLView").removeClass("d-none");
		} else {
			$(".optionRateHeadHTMLView").addClass("d-none");
			$(".optionRateTextHTMLView").addClass("d-none");
		}
	}
	if (checkBoxId === "optionGrandTotalCheck") {
		if (checkBox.checked) {
			$(".optionGrandTotal").removeClass("d-none");
		} else {
			$(".optionGrandTotal").addClass("d-none");
		}
	}
}

function validateEstimateForm(event) {
	if ($("#estimate_emailID").val() === "") {
		$("#alertModal").modal("show");
		event.preventDefault();
		return;
	}
	var estiTblCounter = 1;
	$('#myTab_custom > .nav-item').each(function() {
		var prodRowCounter = 1;
		$("#estimate-table" + estiTblCounter + " tbody > tr").each(function() {
			var prodRowLenght = $("#estimate-table" + estiTblCounter + " tbody > tr").length;
			if (prodRowCounter != prodRowLenght) {
				if ($("#estimate-table" + estiTblCounter + "Rate" + prodRowCounter).val() === "0") {
					$("#alertModal").modal("show");
					event.preventDefault();
					return;
				}
			} else if (prodRowLenght > 1 && prodRowCounter === prodRowLenght) {
				if ($("#estimate-table" + estiTblCounter + "Rate" + prodRowCounter).val() === "0") {
					$("#estimate-table" + estiTblCounter + "tr" + prodRowCounter).remove();
				}
			}
			else if (prodRowCounter === 1) {
				if ($("#estimate-table" + estiTblCounter + "Rate" + 1).val() === "0") {
					$("#alertModal").modal("show");
					event.preventDefault();
					return;
				}
			}
			prodRowCounter++;
		});
		estiTblCounter++;
	});
}

/*Get Option Pop-up details for print*/
function getOptionDetailsForPrint(optionId) {
	$.ajax({
		url: "/estimate/option/details/" + optionId,
		type: 'GET',
		success: function(response) {
			$("#jobPrintView").empty();
			var jobProductServiceTbl = `
                     <table class="table-main" style="width: 100%;border-collapse: collapse;" id="jobTablePDFView">
                           <thead>
                           <tr class="tabletitle">
                              <th style="font-size: 0.85em;text-align: left;padding: 5px 10px;border-bottom: 2px solid #ddd;">
                                 Product/Service
                              </th>
                              <th class="optionQuantityHeadHTMLView" style="font-size: 0.85em;text-align: left;padding: 5px 10px;border-bottom: 2px solid #ddd;">
                                 Quantity
                              </th>
                              <th class="optionRateHeadHTMLView" style="font-size: 0.85em;text-align: left;padding: 5px 10px;border-bottom: 2px solid #ddd;">
                                 Rate
                              </th>
                              <th class="optionTotalHeadHTMLView" style="font-size: 0.85em;text-align: left;padding: 5px 10px;border-bottom: 2px solid #ddd;">
                                 Total
                              </th>
                           </tr>
                        </thead>
                 </table>`;
			var formatter = new Intl.NumberFormat('en-US', {
				style: 'currency',
				currency: 'USD'
			});
			$("#jobPrintView").append(jobProductServiceTbl);
			$.each(response.optionProducts, function(key, value) {
				console.log(value);
				if (value.productCost) {

					$("#jobTablePDFView").append(`
                                  <tr class="list-item">
                                 <td data-label="Type" class="tableitem" style="padding: 10px;border-bottom: 1px solid #cccaca;font-size: 0.85em;text-align: left;"><span>`+ value.productName + `</span></td>
                                 <td data-label="Quantity" class="optionQuantityTextHTMLView" style="padding: 10px;border-bottom: 1px solid #cccaca;font-size: 0.85em;text-align: left;">`+ value.quantity + `</td>
                                 <td data-label="Rate" class="optionRateTextHTMLView" style="padding: 10px;border-bottom: 1px solid #cccaca;font-size: 0.85em;text-align: left;">`+ formatter.format(value.rate) + `</td>
                                 <td data-label="Total" class="optionTotalTextHTMLView" style="padding: 10px;border-bottom: 1px solid #cccaca;font-size: 0.85em;text-align: left;">`+ formatter.format(value.total) + `</td>
                              </tr>`);
				}
				if (value.serviceCost) {
					$("#jobTablePDFView").append(`
                                  <tr class="list-item">
                                 <td data-label="Type" class="tableitem" style="padding: 10px;border-bottom: 1px solid #cccaca;font-size: 0.85em;text-align: left;"><span>`+ value.serviceName + `</span></td>
                                 <td data-label="Quantity" class="optionQuantityTextHTMLView" style="padding: 10px;border-bottom: 1px solid #cccaca;font-size: 0.85em;text-align: left;">`+ value.quantity + `</td>
                                 <td data-label="Rate" class="optionRateTextHTMLView" style="padding: 10px;border-bottom: 1px solid #cccaca;font-size: 0.85em;text-align: left;">`+ formatter.format(value.rate) + `</td>
                                 <td data-label="Total" class="optionTotalTextHTMLView" style="padding: 10px;border-bottom: 1px solid #cccaca;font-size: 0.85em;text-align: left;">`+ formatter.format(value.total) + `</td>
                              </tr>`);
				}
			});
		},
		error: function(error) {
			console.log(error);
		}
	});
}

/*print invoice*/
/*Get Option Pop-up details for print*/
function getOptionDetailsForPrint(optionId) {
	$.ajax({
		url: "/estimate/option/details/" + optionId,
		type: 'GET',
		success: function(response) {
			$("#jobPrintView").empty();
			var invoiceProductServiceTbl = `
                     <table class="table-main" style="width: 100%;border-collapse: collapse;" id="jobTablePDFView">
                           <thead>
                           <tr class="tabletitle">
                              <th style="font-size: 0.85em;text-align: left;padding: 5px 10px;border-bottom: 2px solid #ddd;">
                                 Product/Service
                              </th>
                              <th class="optionRateHeadHTMLView" style="font-size: 0.85em;text-align: left;padding: 5px 10px;border-bottom: 2px solid #ddd;">
                                 Rate
                              </th>
                              <th class="optionTotalHeadHTMLView" style="font-size: 0.85em;text-align: left;padding: 5px 10px;border-bottom: 2px solid #ddd;">
                                 Total
                              </th>
                           </tr>
                        </thead>
                 </table>`;
			$("#jobPrintView").append(invoiceProductServiceTbl);
			$.each(response.optionProducts, function(key, value) {
				console.log(value);
				if (value.productCost) {
					$("#jobTablePDFView").append(`
                                  <tr class="list-item">
                                 <td data-label="Type" class="tableitem" style="padding: 10px;border-bottom: 1px solid #cccaca;font-size: 0.85em;text-align: left;"><span>`+ value.productName + `</span></td>
                                 <td data-label="Rate" class="optionRateTextHTMLView" style="padding: 10px;border-bottom: 1px solid #cccaca;font-size: 0.85em;text-align: left;">`+ value.rate.toLocaleString('en-US', { style: 'currency', currency: 'USD' }) + `</td>
                                 <td data-label="Total" class="optionTotalTextHTMLView" style="padding: 10px;border-bottom: 1px solid #cccaca;font-size: 0.85em;text-align: left;">`+ value.total.toLocaleString('en-US', { style: 'currency', currency: 'USD' }) + `</td>
                              </tr>`);
				}
				if (value.serviceCost) {
					$("#jobTablePDFView").append(`
                                  <tr class="list-item">
                                 <td data-label="Type" class="tableitem" style="padding: 10px;border-bottom: 1px solid #cccaca;font-size: 0.85em;text-align: left;"><span>`+ value.serviceName + `</span></td>
                                 <td data-label="Rate" class="optionRateTextHTMLView" style="padding: 10px;border-bottom: 1px solid #cccaca;font-size: 0.85em;text-align: left;">`+ value.rate.toLocaleString('en-US', { style: 'currency', currency: 'USD' }) + `</td>
                                 <td data-label="Total" class="optionTotalTextHTMLView" style="padding: 10px;border-bottom: 1px solid #cccaca;font-size: 0.85em;text-align: left;">`+ value.total.toLocaleString('en-US', { style: 'currency', currency: 'USD' }) + `</td>
                              </tr>`);
				}
			});
		},
		error: function(error) {
			console.log(error);
		}
	});
}


function saveAndSendInvoice() {
	if ($('#customer_name_input').val().length === 0 && $('#invoice-table1ProductName1').val().length === 0) {
		$('#alertModal').modal('show');

	} else {
		var estimateForm = $('#invoice-form');
		estimateForm.attr('action', '/invoice/process-save-send');
		// Get form data
		const formData = estimateForm.serialize();

		// Make an AJAX POST request
		$.ajax({
			url: estimateForm.attr('action'),
			type: 'POST',
			data: formData,
			success: function(response) {
				// Handle the response from the server, if needed
				console.log('AJAX request successful');
				console.log(response);
				getInvoiceDetailsForInvoicePDFView(response.invoiceId);
				$('#sendEstimateModal').modal('show');
				$('#inputfrom').val(response.fromEmailAddress);
				$('#inputTo').val(response.customerEmail);
				$('#inputsub').val(response.estimateEmailSubject);
				$('#inputBody').text(response.bodyContent);
				$('#invoiceId').val(response.invoiceId);
				var mySelect = $('#optionsdiv');
				mySelect.empty();
				var options = response.options;
				$.each(options, function(index) {
					mySelect.append("<input type='checkbox' style='margin-left: 3%;' class='checkbox-validation' value='" + options[index] + "' name='optionIds' id='optionCheck" + (index + 1) + "' onchange=updateEstimateHTMLView(event)>Option <span>" + (index + 1) + "</span>");
				});
			},
			error: function(xhr, status, error) {
				// Handle errors, if any
				console.log('AJAX request failed');
				console.log(error);
			}
		});
	}
}





$('#sendEmailEstimateForm').submit(function(event) {
	// Count the number of checked checkboxes
	var checkedCount = $('.checkbox-validation:checked').length;

	// Check if at least one checkbox is checked
	if (checkedCount === 0) {
		// Show the error message
		$('#error').show();
		event.preventDefault();
	} else {
		// Validation passes, proceed with the form submission or other actions
		$('#error').hide();
		// Add your code here for form submission or other actions
	}
});



function updateAndSendEstimate() {
	if ($('#customer_name_input').val().length === 0 && $('#invoice-table1ProductName1').val().length === 0) {
		$('#alertModal').modal('show');
	} else {
		var estimateForm = $('#invoice-form');
		estimateForm.attr('action', '/invoice/process-update-send');
		// Get form data
		const formData = estimateForm.serialize();

		// Make an AJAX POST request
		$.ajax({
			url: estimateForm.attr('action'),
			type: 'POST',
			data: formData,
			success: function(response) {
				debugger
				// Handle the response from the server, if needed
				getInvoiceDetailsForInvoicePDFView(response.invoiceId);
				$('#sendEstimateModal').modal('show');
				$('#inputfrom').val(response.fromEmailAddress);
				$('#inputTo').val(response.customerEmail);
				$('#inputsub').val(response.estimateEmailSubject);
				$('#inputBody').text(response.bodyContent);
				$('#invoiceId').val(response.invoiceId);
				var mySelect = $('#optionsdiv');
				mySelect.empty();
				var options = response.options;
				$.each(options, function(index) {
					mySelect.append("<input type='checkbox' style='margin-left: 3%;' class='checkbox-validation' value='" + options[index] + "' name='optionIds' id='optionCheck" + (index + 1) + "' onchange=updateEstimateHTMLView(event)>Option <span>" + (index + 1) + "</span>");
				});
				
				$("#PDFViewCustomerName").text(response.InvoiceDTO.customerDTO.customerName);
				/*$("#PDFViewAddress").text(response.InvoiceDTO.customerDTO.storedServiceLocations[0].locationNickName);
				$("#PDFViewCity").text(response.InvoiceDTO.customerDTO.storedServiceLocations[0].city);
				$("#PDFViewZip").text(response.InvoiceDTO.customerDTO.storedServiceLocations[0].address);
				$("#PDFViewTaxNum").text(response.InvoiceDTO.customerDTO.contactNumbers[0].number);
				$("#PDFViewEstimateId").text(response.InvoiceDTO.id);
				$("#PDFViewOptionId").text(response.InvoiceDTO.options[0].id);
				$("#PDFViewEstimateIdU").text(response.InvoiceDTO.id);
				$("#PDFViewOptionIdU").text(response.InvoiceDTO.options[0].id);
				$("#PDFViewCustomerName").text(response.InvoiceDTO.customerDTO.customerName);*/
			},
			error: function(xhr, status, error) {
				// Handle errors, if any
				console.log('AJAX request failed');
				console.log(error);
			}
		});
	}
}


/*Get Estimate Pop-up details*/
function getInvoiceDetailsForMailPopup(invoiceId, event) {
	$.ajax({
		url: "/invoice/invoice-options/" + invoiceId,
		type: 'GET',
		success: function(response) {
			console.log('AJAX request successful');

			console.log(response);
			getInvoiceDetailsForInvoicePDFView(invoiceId);
			$('#inputfrom').val(response.fromEmailAddress);
			$('#inputTo').val(response.customerEmail);
			$('#inputsub').val(response.estimateEmailSubject);
			$('#inputBody').text(response.bodyContent);
			$('#estId').val(response.estimateId);
			var mySelect = $('#optionsdiv');
			mySelect.empty();
			var options = response.options;
			$.each(options, function(index) {
				mySelect.append("<input type='checkbox' checked style='margin-left: 3%;' class='checkbox-validation d-none' value='" + options[index] + "' name='optionIds' id='optionCheck" + (index + 1) + "' onchange=updateEstimateHTMLView(event)>Option <span>" + (index + 1) + "</span>");
			});
		},
		error: function(xhr, status, error) {
			console.log('AJAX request failed');
			console.log(error);
		}
	});
}

function updateEstimateHTMLView(event) {
	var checkBox = event.target;
	var hiddenOptionId;

	var checkBoxId = checkBox.id;
	if (checkBoxId.includes("optionCheck")) {
		hiddenOptionId = "option" + checkBoxId.substring(11, checkBoxId.length) + "HTMLView";
	}

	if (checkBox.checked) {
		$("#" + hiddenOptionId).removeClass("d-none");
	} else {
		$("#" + hiddenOptionId).addClass("d-none");
	}
	if (checkBoxId === "optionQuantityCheck") {
		if (checkBox.checked) {
			$(".optionQuantityHeadHTMLView").removeClass("d-none");
			$(".optionQuantityTextHTMLView").removeClass("d-none");
		} else {
			$(".optionQuantityHeadHTMLView").addClass("d-none");
			$(".optionQuantityTextHTMLView").addClass("d-none");
		}
	}
	if (checkBoxId === "optionTotalCheck") {
		if (checkBox.checked) {
			$(".optionTotalHeadHTMLView").removeClass("d-none");
			$(".optionTotalTextHTMLView").removeClass("d-none");
		} else {
			$(".optionTotalHeadHTMLView").addClass("d-none");
			$(".optionTotalTextHTMLView").addClass("d-none");
		}
	}
	if (checkBoxId === "optionPriceCheck") {
		if (checkBox.checked) {
			$(".optionRateHeadHTMLView").removeClass("d-none");
			$(".optionRateTextHTMLView").removeClass("d-none");
		} else {
			$(".optionRateHeadHTMLView").addClass("d-none");
			$(".optionRateTextHTMLView").addClass("d-none");
		}
	}
	if (checkBoxId === "optionGrandTotalCheck") {
		if (checkBox.checked) {
			$(".optionGrandTotal").removeClass("d-none");
		} else {
			$(".optionGrandTotal").addClass("d-none");
		}
	}
}

/*Get Invoice Details for Invoice PDF view HTML*/
function getInvoiceDetailsForInvoicePDFView(invoiceId) {
	$.ajax({
		url: "/invoice/invoice-options-html-view/" + invoiceId,
		type: 'GET',
		success: function(response) {
			console.log(response);

			$("#invoiceOptionsHTMLView").empty();
			$("#estimateIdHTMLView").text(response.invoiceId);
			$("#estimateDateHTMLView").text(response.estimateDate);
			$("#estimateCustomerNameHTMLView").text(response.estimateCustomerName);
			$("#estimateId2HTMLView").text(response.invoiceId);
			$("#invoiceNumberHTMLView").text(response.invoiceNumber);
			address
			$("#addressHTMLView").text(response.address);
			console.log(response.invoiceId);
			$.each(response.option, function(key, value) {
				let formatter = new Intl.NumberFormat('en-US', {
					style: 'currency',
					currency: 'USD'
				});
				console.log("\n\nvalue: ")
				str = JSON.stringify(value);
				console.log(str)
				var generatedOption = `<div id="option` + (key + 1) + `HTMLView" style="padding: 20px;min-height: 240px;"><h3 style="font-weight: bold;font-size: 1.2em;line-height: 2em;margin-top: 6%;">Option #<span>` + (key + 1) + `</span></h3><div id="table"><table class="table-main" style="width: 100%;border-collapse: collapse;" id="invoiceOptionsTblHTMLView` + (key + 1) + `"><thead>
					<tr class="tabletitle">
						<th style="font-size: 0.85em;text-align: left;padding: 5px 10px;border-bottom: 2px solid #ddd;">
							Product/Service</th>
						<th class="optionTotalHeadHTMLView" style="font-size: 0.85em;text-align: left;padding: 5px 10px;border-bottom: 2px solid #ddd;">
							Description</th>
						<th class="optionQuantityHeadHTMLView d-none" style="font-size: 0.85em;text-align: left;padding: 5px 10px;border-bottom: 2px solid #ddd;">
							Quantity</th>
						<th class="optionRateHeadHTMLView d-none" style="font-size: 0.85em;text-align: left;padding: 5px 10px;border-bottom: 2px solid #ddd;">
							Rate</th>
						<th class="optionTotalHeadHTMLView d-none" style="font-size: 0.85em;text-align: left;padding: 5px 10px;border-bottom: 2px solid #ddd;">
							Total</th>
					</tr>
				</thead>
				</table>
				</div>
					<!--End Table-->
					<div class="optionGrandTotal"
						style="text-align: right;padding: 20px 0;font-weight: 600;">
						<span>Grand Total: </span>
						<span>`+ formatter.format(value.invoiceTotal) + `</span>
					</div>
					
				</div>`;
				$("#invoiceOptionsHTMLView").append(generatedOption);
				var invoiceTbl = $("#invoiceOptionsTblHTMLView" + (key + 1));
				var opt = value.optionProducts;
				$.each(opt, function(index, element) {
					var psname;
					var desc;
					str = JSON.stringify(element);
					console.log("................................" + str)
					if (element.product) {

						psname = element.product.productName;
						desc = element.product.discription;
					} else {
						psname = element.services.serviceName;
						desc = element.services.discription;
					}
					var generatedTr = $("<tr class='list-item'><td data-label='Type' class='tableitem' style='padding: 10px;border-bottom: 1px solid #cccaca;font-size: 0.85em;text-align: left;'><span>" + psname + "</span></td><td data-label='Type' class='tableitem' style='padding: 10px;border-bottom: 1px solid #cccaca;font-size: 0.85em;text-align: left;'><span>" + desc + "</span></td><td data-label='Quantity' class='optionQuantityTextHTMLView d-none' style='padding: 10px;border-bottom: 1px solid #cccaca;font-size: 0.85em;text-align: left;'>" + element.quantity + "</td><td data-label='Rate' class='optionRateTextHTMLView d-none' style='padding: 10px;border-bottom: 1px solid #cccaca;font-size: 0.85em;text-align: left;'>" + formatter.format(element.rate) + "</td><td data-label='Total' class='optionTotalTextHTMLView d-none' style='padding: 10px;border-bottom: 1px solid #cccaca;font-size: 0.85em;text-align: left;'>" + formatter.format(element.total) + "</td></tr>");
					invoiceTbl.append(generatedTr);
				});
			});
		},
		error: function(xhr, status, error) {
			console.log('AJAX request failed');
			console.log(error);
		}
	});
}

























$(document).ready(function() {

	/*disableSaveAndSendBtn();*/

	var originalURL = "";
	$('a').on('click', function(e) {
		if (!window.location.href.includes('/view/') && !event.target.id.includes('mainPageCancel')) {
			var elementType = this.tagName.toLowerCase();
			var isInsideMainDiv = $(this).closest('#add-job-parent-div').length > 0;

			if (elementType === "a" && !isInsideMainDiv) {
				originalURL = $(this).attr('href'); // Get the URL of the clicked link
			}

			if ($('#customer_name_input').val() || $('#estimate-table1ProductName1').val()) {
				// Open the modal and prevent the link or button from being followed
				$('#leaveModal').modal('show');
				e.preventDefault();
			}
		}
	});

	// When Leave button in the modal is clicked, redirect to the original link URL
	$('#leaveButton').on('click', function() {
		if (originalURL !== "") {
			window.location.href = originalURL;
		} else {
			// Handle the case when the Leave button is clicked without a previous link
			console.log("Redirect URL not available.");
		}
	});

	// When Cancel button in the modal is clicked, dismiss the modal
	$('#cancelButton').on('click', function() {
		$('#leaveModal').modal('hide');
	});

	// When the modal is hidden, reset the original URL
	$('#leaveModal').on('hide.bs.modal', function() {
		originalURL = "";
	});

	// Listen for browser back button click
	$(window).on('popstate', function() {
		// If the modal is currently open, close it
		if ($('#leaveModal').hasClass('show')) {
			$('#leaveModal').modal('hide');
		}
	});
});

//--------Add Customer Pop-up Validation---------//
$("#customer_emailID").on("keyup", function(e) {
	var email = $('#customer_emailID').val();
	if ($('#customer_emailID').val() == null || $('#customer_emailID').val() == "") {
		$('#msg').show();
		$("#msg").html("email is a required field.").css("color", "red");
	} else {
		var regex =
			/^([a-zA-Z0-9_\.\-\+])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
		if (!regex.test(email)) {
			$('#msg').show();
			$("#msg").html("Invalid Email").css("color", "red");
			return;
		}
		$.ajax({
			type: 'GET',
			url: '/customer/searchByEmail/' + email,
			cache: false,
			success: function(msg) {
				$('#msg').show();
				$.each(msg, function(index, value) {
					console.log(value)
					console.log(index)
					if (value === 'Email already exists') {
						$("#msg").html(value).css("color", "red");
					} else {
						$("#msg").html(value).css("color", "green");
					}
				});


			},
			error: function(jqXHR, textStatus, errorThrown) {
				alert("fail");
				$('#msg').show();
				$("#msg").html(textStatus + " " + errorThrown).css("color", "red");
			}
		});
	}
});


var tables = document.getElementsByTagName('table');
for (var i = 0; i < tables.length; i++) {
	resizableGrid(tables[i]);
}

function resizableGrid(table) {
	var row = table.getElementsByTagName('tr')[0],
		cols = row ? row.children : undefined;
	if (!cols) return;

	table.style.overflow = 'hidden';

	var tableHeight = table.offsetHeight;

	for (var i = 0; i < cols.length; i++) {
		var div = createDiv(tableHeight);
		cols[i].appendChild(div);
		cols[i].style.position = 'relative';
		setListeners(div);
	}

	function setListeners(div) {
		var pageX, curCol, nxtCol, curColWidth, nxtColWidth;

		div.addEventListener('mousedown', function(e) {
			curCol = e.target.parentElement;
			nxtCol = curCol.nextElementSibling;
			pageX = e.pageX;

			var padding = paddingDiff(curCol);

			curColWidth = curCol.offsetWidth - padding;
			if (nxtCol)
				nxtColWidth = nxtCol.offsetWidth - padding;
		});

		div.addEventListener('mouseover', function(e) {
			e.target.style.borderRight = '2px solid #0000ff';
		})

		div.addEventListener('mouseout', function(e) {
			e.target.style.borderRight = '';
		})

		document.addEventListener('mousemove', function(e) {
			if (curCol) {
				var diffX = e.pageX - pageX;

				if (nxtCol)
					nxtCol.style.width = (nxtColWidth - (diffX)) + 'px';

				curCol.style.width = (curColWidth + diffX) + 'px';
			}
		});

		document.addEventListener('mouseup', function(e) {
			curCol = undefined;
			nxtCol = undefined;
			pageX = undefined;
			nxtColWidth = undefined;
			curColWidth = undefined;
		});
	}

	function createDiv(height) {
		var div = document.createElement('div');
		div.style.top = 0;
		div.style.right = 0;
		div.style.width = '5px';
		div.style.position = 'absolute';
		div.style.cursor = 'col-resize';
		div.style.userSelect = 'none';
		div.style.height = height + 'px';
		return div;
	}

	function paddingDiff(col) {

		if (getStyleVal(col, 'box-sizing') == 'border-box') {
			return 0;
		}

		var padLeft = getStyleVal(col, 'padding-left');
		var padRight = getStyleVal(col, 'padding-right');
		return (parseInt(padLeft) + parseInt(padRight));

	}

	function getStyleVal(elm, css) {
		return (window.getComputedStyle(elm, null).getPropertyValue(css))
	}
};

/*Get Invoice Pop-up details*/
function getInvoiceDetailsForPrint(invoiceId) {
	$('#printIModal').modal('show');
	/*getInvoiceDetailsForInvoicePrint(invoiceId);*/
}

const handlePrint = () => {
	html2canvas(document.querySelector("#print-invoice")).then(canvas => {
		var imgData = canvas.toDataURL('image/png');
		var pdf = new jsPDF('p', 'pt', 'a4');
		pdf.addImage(imgData, 'PNG', 12, 10);
		pdf.save('Test.pdf');
		$('#printIModal').modal('hide');
	});
}


/*Get Invoice Details for Invoice Print*/
function getInvoiceDetailsForInvoicePrint(invoiceId) {

	var printContents = document.getElementById("print-invoice").innerHTML;
	var originalContents = document.body.innerHTML;

	document.body.innerHTML = printContents;

	window.print();

	document.body.innerHTML = originalContents;
}

function showInvoiceTable(id) {
	$("#invoiceTableArrowRight" + id).addClass("d-none");
	$("#invoiceTableArrowDown" + id).removeClass("d-none");
	$("#invoicePaymenttable" + id).removeClass("d-none");
}

function hideInvoiceTable(id) {
	$("#invoiceTableArrowDown" + id).addClass("d-none");
	$("#invoiceTableArrowRight" + id).removeClass("d-none");
	$("#invoicePaymenttable" + id).addClass("d-none");
}
var totalSelected = 0;
function showHideRecievePaymentBtn(rowId, invoiceTotal, index) {
	var checkedInvoice = $('input[type="checkbox"][name="selectedInvoiceIds' + rowId + '"]:checked').length;
	var recievePaymentBtn = $("#recievePaymentBtn" + rowId);

	if (parseInt(checkedInvoice) >= 1) {
		$(recievePaymentBtn).css("opacity", "1.0");
		$(recievePaymentBtn).css("pointer-events", "all");
		$(recievePaymentBtn).removeAttr("disabled");
	} else {
		$(recievePaymentBtn).css("opacity", "0.5");
		$(recievePaymentBtn).css("pointer-events", "none");
		$(recievePaymentBtn).prop('disabled', true);
	}

	if ($('#invoiceCheck' + rowId + '' + index).is(':checked')) {
		totalSelected = totalSelected + invoiceTotal;
	} else {
		totalSelected = totalSelected - invoiceTotal;
	}
	var tsv;
	if (totalSelected === 0 || isNaN(totalSelected) || totalSelected < 0) {
		tsv = "$0.00";
	} else {
		tsv = "$" + totalSelected.toLocaleString('en-US');
	}

	$("#totalSelectedInvoice" + rowId).text(tsv);
}

function recievePayment(customerId) {

	$("#selectedInvoices" + customerId).val('');
	$("#selectedInvoiceCustomerId" + customerId).val('');

	var selectedInvoices = [];
	$('input[type="checkbox"][name="selectedInvoiceIds' + customerId + '"]:checked').map(function() {
		selectedInvoices.push($(this).val());
	});

	$("#selectedInvoices" + customerId).val(selectedInvoices.join(","));
	$("#selectedInvoiceCustomerId" + customerId).val(customerId);

	$("#recievePaymentForm" + customerId).submit();

}


