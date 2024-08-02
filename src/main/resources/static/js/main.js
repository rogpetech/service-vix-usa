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

const handlePrint = () => {
    html2canvas(document.querySelector("#print-estimate")).then(canvas => {
        var imgData = canvas.toDataURL('image/png');
        var pdf = new jsPDF('p', 'pt', 'a4');
        pdf.addImage(imgData, 'PNG', 12, 10);
        pdf.save('Test.pdf');
        $('#printEModal').modal('hide');
    });
}




function convertIntoPriceFormat(input) {
	return "$" + input.toLocaleString('en-US');
}

$(document).ready(function() {

	$("#clickMe").click(function() {
		num1 = 10000;
		num2 = 1234.56789;
		output1 = num1.toLocaleString('en-US');
		output2 = num2.toLocaleString('en-US');
		console.log();
	});

	$("#estimate-form").submit(function(event) {
		validateEstimateForm(event);
	});

	/*Estimate Menu Hover on/off*/
	$("#estimateMenu").hover(function() {
		$("#estimateMenuWhiteIcon").addClass("d-none");
		$("#estimateMenuBlackIcon").removeClass("d-none");
	}, function() {
		$("#estimateMenuBlackIcon").addClass("d-none");
		$("#estimateMenuWhiteIcon").removeClass("d-none");
	});

	/* Active Product And Service Tab and Area*/
	$("#nav-tab").find("button").first().trigger("click");

	/*Hide filter and pagination for Permissions view table*/
	$("#user-permissions-table_filter").addClass("d-none");
	$("#user-permissions-table_info").addClass("d-none");
	$("#user-permissions-table_paginate").addClass("d-none");
	/*Update Role's Permission button action*/
	$("#update_role_permission_btn").click(function() {
		var selectedRole = $("#selected_role_name").val();
		if (selectedRole.length === 0) {
			$("#alertModal").modal("show");
		} else {
			var formAction = $("#permission_form").attr("action");
			var form = $('#permission_form');
			const formData = form.serialize();
			$.ajax({
				method: 'GET',
				url: formAction,
				data: formData,
				success: function(response) {
					console.log(response.result);
					if (response.result) {
						$("#success_msg").removeClass("d-none");
						$("#success_msg").addClass("show");
						$("#successMessage").text(response.message);
					} else {
						$("#error_msg").removeClass("d-none");
						$("#errorMessage").text(response.message);
					}
				},
				error: function(error) {
				},
			});
		}
	});

	/*Permission Role Drop Down Event Handler*/
	$("#permissions_role_select").change(function() {
		var selectedRole = $("option:selected", this).text();
		$("#selected_role_name").val(selectedRole);
		$("#user-permissions-table > tbody > tr > td > input[type=checkbox]").each(function(index, element) {
			$(element).prop("checked", false);
		});
		$.ajax({
			method: 'GET',
			url: '/role/get/' + $("#permissions_role_select").val(),
			success: function(response) {
				var pm = response.permissionsMap;
				$.each(pm, function(key, value) {
					/* For List View Check */
					var listViewDBValue = value.listView;
					if (listViewDBValue) {
						/*var lvc = $("#" + key + "_list_view_check");*/
						var lvc = document.getElementById(key + "_list_view_check");
						lvc.checked = true;
					}
					/* For Individual View Check */
					var individualViewDBValue = value.individualView;
					if (individualViewDBValue) {
						/*$("#" + key + "_individual_view_check").prop("checked", true);*/

						var ivc = document.getElementById(key + "_individual_view_check");
						ivc.checked = true;
					}
					/* For Add New Check */
					var addNewDBValue = value.addNew;
					if (addNewDBValue) {
						/*$("#" + key + "_add_new_check").prop("checked", true);	*/

						var anc = document.getElementById(key + "_add_new_check");
						anc.checked = true;
					}
					/* For Update Existing Check */
					var updateExistingDBValue = value.updateExisting;
					if (updateExistingDBValue) {
						/* $("#" + key + "_update_existing_check").prop("checked", true); */

						var uec = document.getElementById(key + "_update_existing_check");
						uec.checked = true;
					}
					/* For Delete Check */
					var deleteDBValue = value.delete;
					if (deleteDBValue) {
						/*	$("#" + key + "_delete_check").prop("checked", true);*/

						var dc = document.getElementById(key + "_delete_check");
						dc.checked = true;
					}
				});
			},
			error: function(error) {

			},
		});
	});



	/*Add Role Button click event handler*/
// $("#add_role_btn").click(function() {
// 	var roleName = $("#role_name").val().trim();
// 	if (!roleName) {
// 			$("#role_name_alert").removeClass("d-none");
// 			return;
// 	} else {
// 			$("#role_name_alert").addClass("d-none");
// 	}

// 	// Verificar duplicidade no lado do cliente
// 	var duplicateRole = false;
// 	$('#user-roles-table td').each(function() {
// 			if ($(this).text().trim().toLowerCase() === roleName.toLowerCase()) {
// 					duplicateRole = true;
// 					return false; // Sair do loop
// 			}
// 	});

// 	if (duplicateRole) {
// 			$("#already_role_ex").removeClass("d-none");
// 			$("#already_role_ex").addClass("show");
// 			return; // Não prosseguir com a requisição AJAX
// 	}

// 	// Desabilitar o botão para evitar múltiplas requisições
// 	var $button = $(this);
// 	$button.prop('disabled', true);
// 	$button.text('Adding...'); // Indicador visual

// 	$.ajax({
// 			url: '/role/addRole/' + roleName, // Ajuste o endpoint conforme necessário
// 			method: 'POST',
// 			success: function(response) {
// 					if (response && response.name) {
// 							var newRole = `<tr>
// 									<th class="dtr-control sorting_1" tabindex="0"><input type="checkbox"></th>
// 									<td>` + response.name + `</td>
// 									<td>
// 											<span>
// 													<button class="table-action-buttons del-btn" type="button" data-bs-toggle="modal" data-bs-target="#deleteConfirmationModalForRole-` + response.id + `">
// 															<img src="/assest/trash.svg" class="img-fluid plan-carousel-btn" alt="">
// 													</button>
// 													<div class="modal fade" id="deleteConfirmationModalForRole-` + response.id + `" tabindex="-1" aria-labelledby="deleteConfirmationLabelForService" aria-hidden="true">
// 															<div class="modal-dialog modal-dialog-centered">
// 																	<div class="modal-content">
// 																			<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
// 																			<div class="modal-body">
// 																					<h4 class="modal-title" id="deleteConfirmationLabel">Delete Role</h4>
// 																					<p>Are you sure you want to delete this Role?</p>
// 																			</div>
// 																			<div class="modal-footer">
// 																					<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
// 																					<a href="/role/deleteRole/` + response.id + `" class="btn btn-danger">Delete</a>
// 																			</div>
// 																	</div>
// 															</div>
// 													</div>
// 											</span>
// 									</td>
// 							</tr>`;
// 							$('#user-roles-table tr:last').after(newRole);
// 							$("#success_msg").removeClass("d-none");
// 							$("#success_msg").addClass("show");
// 							$("#successMessage").html('Role Created Successfully.');
// 					} else {
// 							$("#already_role_ex").removeClass("d-none");
// 							$("#already_role_ex").addClass("show");
// 					}
// 			},
// 			error: function(error) {
// 					console.error('Error', error);
// 					$("#errorMessage").text('Error adding role: ' + error.responseText);
// 					$("#error_msg").removeClass("d-none");
// 			},
// 			complete: function() {
// 					// Reabilitar o botão após a conclusão da requisição
// 					$button.prop('disabled', false);
// 					$button.text('Add Role');
// 			}
// 	});
// });


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

	// Custom filter function
	$.fn.dataTable.ext.search.push(function(settings, data, dataIndex) {

		var statusFilter = $('#select_est_status').val();
		var status = data[4]; // Column index for "Status" is 4

		if (statusFilter === '' || status === statusFilter) {
			return true;
		}

		return false;
	});

	$.fn.dataTable.ext.search.push(function(settings, data, dataIndex) {

		var myStatusFilter = $('#select_est_status_my').val();
		var status = data[4]; // Column index for "Status" is 4

		if (myStatusFilter === '' || status === myStatusFilter) {
			return true;
		}

		return false;
	});



	$("#select_est_status_my").change(function() {
		var table = $('#my-estimates-table').DataTable();
		table.draw();

		// Check if there are no matching records and display a message
		var statusFilter = $(this).val();
		var filteredData = table.column(4, { search: 'applied' }).data();

		if (statusFilter !== '' && filteredData.indexOf(statusFilter) === -1) {
			/*$('#estimates-table_info').hide();   // Remove the existing <tfoot> if it exists
			$('#estimates-table_paginate').hide();  
			$('#estimates-table_filter').hide();*/
		} else {
			// Remove the message if it was displayed previously
			/*$('#estimates-table_info').hide();
			$('#estimates-table_paginate').hide();
			$('#estimates-table_filter').hide();*/
		}
	});

	$("#select_est_status").change(function() {
		var table = $('#estimates-table').DataTable();
		table.draw();

		// Check if there are no matching records and display a message
		var statusFilter = $(this).val();
		var filteredData = table.column(4, { search: 'applied' }).data();

		if (statusFilter !== '' && filteredData.indexOf(statusFilter) === -1) {
			/*$('#estimates-table_info').hide();   // Remove the existing <tfoot> if it exists
			$('#estimates-table_paginate').hide();  
			$('#estimates-table_filter').hide();*/
		} else {
			// Remove the message if it was displayed previously
			/*$('#estimates-table_info').hide();
			$('#estimates-table_paginate').hide();
			$('#estimates-table_filter').hide();*/
		}
	});

	// ----------------activecard------------------//
	$(".plan-card").click(function() {
		$(".plan-card").removeClass("plan-card-selected");
		$(this).addClass("plan-card-selected");
	});
	// ----------------activesidebar_menu------------------//
	$(".leftnavbar .navlist a").click(function() {

		$(".leftnavbar .navlist a").removeClass("active_menu");
		$(this).addClass("active_menu");
	});


	$('.profiletoggle').click(function() {
		if ($('#takeonebar').hasClass('slamdown')) {
			$('#takeonebar').removeClass('slamedown');
		} else {
			$('#takeonebar').addClass('slamdown');
		}
	});

	// -----------------wizard-----------------//
	var current_fs, next_fs, previous_fs;


	// No BACK button on first screen
	if ($(".show").hasClass("first-screen")) {
		$(".prev").css({ 'display': 'none' });
	}
	if ($(".show").hasClass("hide-btn")) {
		alert('test')
	}

	// Next button
	$(".next-button").click(function() {

		current_fs = document.querySelector('.card2.show')
		next_fs = $(current_fs).next();


		$(".prev").css({ 'display': 'block' });

		$(current_fs).removeClass("show");
		$(next_fs).addClass("show");

		$("#progressbar li").eq($(".card2").index(next_fs)).addClass("active");

		current_fs.animate({}, {
			step: function() {

				current_fs.css({
					'display': 'none',
					'position': 'relative'
				});

				next_fs.css({
					'display': 'block'
				});
			}
		});

		//----Added by Ritik
		if ($(".card2").hasClass("show")) {
			$(".next-button").css({
				'display': 'none'
			});
			$(".add-org-button").removeClass("d-none");
		}

	});

	// Previous button
	$(".prev").click(function() {

		current_fs = $(".show");
		previous_fs = $(".show").prev();

		$(current_fs).removeClass("show");
		$(previous_fs).addClass("show");

		$(".prev").css({ 'display': 'block' });

		if ($(".show").hasClass("first-screen")) {
			$(".prev").css({ 'display': 'none' });
		}

		$("#progressbar li").eq($(".card2").index(current_fs)).removeClass("active");

		current_fs.animate({}, {
			step: function() {

				current_fs.css({
					'display': 'none',
					'position': 'relative'
				});

				previous_fs.css({
					'display': 'block'
				});
			}
		});

		//----Added by Ritik
		$(".next-button").css({
			'display': 'block'
		});
		$(".add-org-button").addClass("d-none");

	});


	// -----------sidebarnavtoggle---------------//
	/*$('li.has-ul').click(function() {
		if ($(".leftnavbar").width() === 90 || $(".leftnavbar-toggle").width() === 90) {
			$(".sub-ul").css({ 'display': 'none' });
		} else {

			$(this).children('.sub-ul').slideToggle(500);
			$(this).toggleClass('active');

		}
	});*/

	// -----------addinputfieldsonclick---------------//

	$('.multi-field-wrapper').each(function() {
		var $wrapper = $('.multi-fields', this);
		$(".add-field", $(this)).click(function(e) {

			$('.multi-field:first-child', $wrapper).clone(true).appendTo($wrapper).find('input').val('').focus();

		});
		$('.multi-field .remove-field', $wrapper).click(function() {
			if ($('.multi-field', $wrapper).length > 1)
				$(this).parent('.multi-field').remove();
		});
	});

	// ------------------addlocation----------------//
	var clonelocationCount = 2;
	$(".add_loaction").click(function() {
		$('#location-fields')
			.clone(true)
			.attr('id', 'location-fields-' + clonelocationCount++)
			.insertAfter($('[id^=location-fields]:last'))
			.prepend('<hr>')

	});

	$(".delete-location").click(function() {
		if ($('.location-fields').length > 1)
			$(this).parent('.location-fields').remove();
		clonelocationCount--
		reset()

	});

	function reset() {
		clonelocationCount = 1;//start count from 1
		//loop 
		$(".location-fields").each(function() {
			$(this).attr('id', 'location-fields' + clonelocationCount);//change id
			clonelocationCount++;//increment
		})
	}

});

// -----------------dashboadtoggle--------------//
function togglesidebar() {
	var element = document.getElementsByClassName("leftnavbar");
	element[0].classList.toggle("leftnavbar-toggle");
}

// --------------imageuploadJS------------------//
function readURL(input) {
	if (input.files && input.files[0]) {
		var reader = new FileReader();
		reader.onload = function(e) {
			$('#imagePreview').css('background-image', 'url(' + e.target.result + ')');
			$('#imagePreview').hide();
			$('#imagePreview').removeClass("d-none");
			$('#imagePreviewImg').hide();
			$('#imagePreview').fadeIn(650);
		}
		reader.readAsDataURL(input.files[0]);
	}
}
$("#imageUpload").change(function() {
	readURL(this);
});

function readURLforUser(input) {
	if (input.files && input.files[0]) {
		var reader = new FileReader();
		reader.onload = function(e) {
			$('#imagePreview0').css('background-image', 'url(' + e.target.result + ')');
			$('#imagePreview0').hide();
			$('#imagePreview0').removeClass("d-none");
			$('#imagePreviewImg0').hide();
			$('#imagePreview0').fadeIn(650);
		}
		reader.readAsDataURL(input.files[0]);
	}
}
$("#imageUpload0").change(function() {
	readURLforUser(this);
});

// -----------uploadmultipleimages-------------//
jQuery(document).ready(function() {
	ImgUpload();
});

function ImgUpload() {
	var imgWrap = "";
	var imgArray = [];

	$('.upload__inputfile').each(function() {
		$(this).on('change', function(e) {
			imgWrap = $(this).closest('.upload__box').find('.upload__img-wrap');
			var maxLength = $(this).attr('data-max_length');

			var files = e.target.files;
			var filesArr = Array.prototype.slice.call(files);
			var iterator = 0;
			filesArr.forEach(function(f, index) {

				if (!f.type.match('image.*')) {
					return;
				}

				if (imgArray.length > maxLength) {
					return false
				} else {
					var len = 0;
					for (var i = 0; i < imgArray.length; i++) {
						if (imgArray[i] !== undefined) {
							len++;
						}
					}
					if (len > maxLength) {
						return false;
					} else {
						imgArray.push(f);

						var reader = new FileReader();
						reader.onload = function(e) {
							var html = "<div class='upload__img-box'><div style='background-image: url(" + e.target.result + ")' data-number='" + $(".upload__img-close").length + "' data-file='" + f.name + "' class='img-bg'><div class='upload__img-close'></div></div></div>";
							imgWrap.append(html);
							iterator++;
						}
						reader.readAsDataURL(f);
					}
				}
			});
		});
	});

	$('body').on('click', ".upload__img-close", function(e) {
		var file = $(this).parent().data("file");
		for (var i = 0; i < imgArray.length; i++) {
			if (imgArray[i].name === file) {
				imgArray.splice(i, 1);
				break;
			}
		}
		$(this).parent().parent().remove();
	});
}

// ---------multiple_selector----------//

new SlimSelect({
	select: '#multiple'
});


// -----------addnewrowindatatable-----------------//


var faqs_row = 0;
function addfaqs() {

	html = '<tr id="faqs-row' + faqs_row + '">';
	html += '<td><input type="checkbox"></td>';
	html += '<td><input type="text" class="form-control" placeholder="Category"></td>';
	html += '<td><input type="text" placeholder="Description" class="form-control"></td>';
	html += '<td ><span class="status-capsule">Active</span></td>';
	html += '<td > <span><button class="table-action-buttons edit-row " ><img src="/assest/pencil.svg" class="img-fluid edit-icon"></button></span><span><button class="table-action-buttons del-btn" onclick="$(\'#faqs-row' + faqs_row + '\').remove();"><img src="/assest/trash.svg" class="img-fluid plan-carousel-btn"></button></span></td>';

	html += '</tr>';

	$('#product-category-table tbody').prepend(html);

	faqs_row++;
}

var faqs_row2 = 0;
function addfaqs2() {
	html = '<tr id="faqs-row2' + faqs_row2 + '">';
	html += '<td><input type="checkbox"></td>';
	html += '<td><input type="text" class="form-control" placeholder="Category"></td>';
	html += '<td><input type="text" placeholder="Description" class="form-control"></td>';
	html += '<td ><span class="status-capsule">Active</span></td>';
	html += '<td > <span><button type="button" class="table-action-buttons edit-row " ><img src="/assest/pencil.svg" class="img-fluid edit-icon"></button></span><span><button type="button" class="table-action-buttons del-btn" onclick="$(\'#faqs-row2' + faqs_row2 + '\').remove();"><img src="/assest/trash.svg" class="img-fluid plan-carousel-btn"></button></span></td>';



	html += '</tr>';

	$('#service-category-table tbody').prepend(html);

	faqs_row2++;
}

// estimate_table//

var faqs_row3 = 0;
var counter = 0;


function addfaqs3(tableId) {
	if (faqs_row3 === undefined)
		faqs_row3 = 1;
	if (counter === undefined)
		counter = 2;


	html = '<tr id="faqs-row3' + faqs_row3 + '">';
	html += '<td>' + counter + '</td>';
	html += '<td><input type="text" onclick="generateProductRow(1)" placeholder="Type for search product.." class="form-control" name="productName' + faqs_row3 + '" id="estimate-table' + tableId + 'productName' + faqs_row3 + '" value="" onKeyUp="onSearchType(event)"><div id="search_results' + faqs_row3 + '" class="search-results"></td>';
	html += '<td><input type="number" class="form-control" placeholder="1" id="quantity" name="quantity' + faqs_row3 + '" value="1" onchange="calculateTotal(this)"></td>';
	html += '<td><input type="number" class="form-control" placeholder="2.80" id="rate" name="rate' + faqs_row3 + '" onchange="calculateTotal(this)"></td>';
	html += '<td><input type="number" class="form-control" placeholder="2800.00" id="total" name="total' + faqs_row3 + '" readonly></td>';
	html += '<td><input type="number" class="form-control" placeholder="1480.00" name="averageCost' + faqs_row3 + '" id="averageCost"></td>';
	html += '<td ><span><button type="button" class="table-action-buttons del-btn del-row" name="' + faqs_row3 + '" onclick="removefaqs3(' + faqs_row3 + ')"><img src="/assest/trash.svg" class="img-fluid plan-carousel-btn"></button></span></td>';
	html += '</tr>';

	$('#estimate-table' + tableId + ' tbody').prepend(html);

	faqs_row3++;
	counter++;
}

function generateProductRow(tableId) {

	/*	$("#estimate-table" + tableId + " tbody").find('tr:first-child .del-btn').remove();*/
	var lastTr = $("#estimate-table" + tableId + " tr:last");
	if (lastTr.find("input:first").val().length > 0) {
		var rowNo = $('#estimate-table' + tableId + ' tbody tr').length;

		lastTr.find("input:first").removeAttr("onclick");

		/*$('estimate-table' + tableId + 'tr' + rowNo).find('button.del-btn').removeAttr('onclick');*/

		rowNo++;
		html = '<tr id="estimate-table' + tableId + 'tr' + rowNo + '">';
		html += '<td>' + rowNo + '</td>';
		html += '<td><div class="input-with-icon" style="position: relative;"><input type="text" onclick="generateProductRow(' + tableId + ')" autocomplete="off" placeholder="Type for search product.." class="form-control" name="estimate-table' + tableId + 'ProductName' + rowNo + '" id="estimate-table' + tableId + 'ProductName' + rowNo + '" value="" onKeyUp="onSearchType(event)" /></div><div class="product_service_suggestions_wrapper" id="productServicSuggestionsWrapper"></div><div id="estimate-table' + tableId + 'search_results' + rowNo + '" class="search-results"></td>';
		html += '<td><input type="text" class="form-control" id="estimate-table' + tableId + 'Description' + rowNo + '" name="estimate-table' + tableId + 'Description' + rowNo + '" value=""></td>';
		html += '<td><input type="number" class="form-control" id="estimate-table' + tableId + 'Quantity' + rowNo + '" name="estimate-table' + tableId + 'Quantity' + rowNo + '" value="1.0" onchange="calculateTotal($(\'#\'+this.id))" min="1"></td>';
		html += '<td><input type="text" class="form-control" readonly id="estimate-table' + tableId + 'Rate' + rowNo + '" name="estimate-table' + tableId + 'Rate' + rowNo + '" value="0" onchange="calculateTotal($(\'#\'+this.id))"></td>';
		html += '<td><input type="text" class="form-control" readonly id="estimate-table' + tableId + 'Total' + rowNo + '" name="estimate-table' + tableId + 'Total' + rowNo + '" value="0" readonly></td>';
		html += '<td><input type="text" class="form-control" readonly name="estimate-table' + tableId + 'AverageCost' + rowNo + '"  id="estimate-table' + tableId + 'AverageCost' + rowNo + '" value="0" readonly><input type="hidden" class="form-control" name="estimate-table' + tableId + 'AverageCostIntial' + rowNo + '"  id="estimate-table' + tableId + 'AverageCostIntial' + rowNo + '" value="0"></td>';
		html += '<td ><span><button type="button"  style="cursor: pointer;" class="table-action-buttons del-btn del-row" name="' + rowNo + '" onclick="removeProductRow(' + tableId + ',' + rowNo + ')"><img src="/assest/trash.svg" class="img-fluid plan-carousel-btn"></button></span></td>';
		html += '</tr>';

		$('#estimate-table' + tableId + ' tbody').append(html);
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


function showAddServiceAddProductBtn(estimateTableId, productRowNo) {
	var searchResultsContainer = $('#estimate-table' + estimateTableId + 'search_results' + productRowNo);
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
		"Add Product " + $('#estimate-table' + estimateTableId + 'ProductName' + productRowNo).val()
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
		"Add Service " + $('#estimate-table' + estimateTableId + 'ProductName' + productRowNo).val()
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

	var valid = true;
	$(".error-message").empty();

	var productName = $("#inpproduct").val();
	var productCategoryName = $("#select_product_cat").val();
	var description = $("#inpwork").val();
	var regularPrice = $("#reg_price").val();
	var memberPrice = $("#mem_price").val();
	var averageCost = $("#avg_cost").val();

	let avgCostStr = document.getElementById("avg_cost");
	let mem_privceStr = document.getElementById("mem_price");
	let regPriceStr = document.getElementById("reg_price");
	avgCostStr.value = avgCostStr.value.replace(/[$,]/g, '');
	mem_privceStr.value = mem_privceStr.value.replace(/[$,]/g, '');
	regPriceStr.value = regPriceStr.value.replace(/[$,]/g, '');


	if (productName === "") {
		$("#productName-error").text("product name is required");
		valid = false;
	}
	if (description === "") {
		$("#description-error").text("description is required");
		valid = false;
	}
	if (productCategoryName === "") {
		$("#productCat-Name-error").text("product category is required");
		valid = false;
	}
	if (regularPrice === "") {
		$("#regularPrice-error").text("regular price is required");
		valid = false;
	}
	if (memberPrice === "") {
		$("#memberPrice-error").text("member price is required");
		valid = false;
	}
	if (averageCost === "") {
		$("#averageCost-error").text("average cost is required");
		valid = false;
	}
	if (!valid) {
		$("#inpproduct").focus(function() {
			$("#productName-error").empty();
		});
		$("#inpwork").focus(function() {
			$("#description-error").empty();
		});
		$("#select_product_cat").focus(function() {
			$("#productCat-Name-error").empty();
		});
		$("#reg_price").focus(function() {
			$("#regularPrice-error").empty();
		});
		$("#mem_price").focus(function() {
			$("#memberPrice-error").empty();
		});
		$("#avg_cost").focus(function() {
			$("#averageCost-error").empty();
		});
	}
	else {
		var form = $("#form_prodct_add");
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

				var estimateTableNumber = $("#clickedEstimateTableNumber").val();
				var estimateTableRowNumber = $("#clickedEstimateTableRowNumber").val();
				$("#estimate-table" + estimateTableNumber + "ProductName" + estimateTableRowNumber).val(response.productName);
			},
			error: function(xhr, status, error) {
				console.error("Error submitting form: " + error);
				$("#addProductPopupCloseBtn").click();
			}
		});
	}
}
function addServicePopup() {

	var valid = true;
	$(".error-message").empty();
	var serviceName = $("#inp_service_name").val();
	var description = $("#inp_ser_dec").val();
	var regularPrice = $("#reg_rate").val();
	var memberPrice = $("#mem_rate").val();

	let avgCostStr = document.getElementById("mem_rate");
	let mem_privceStr = document.getElementById("reg_rate");
	avgCostStr.value = avgCostStr.value.replace(/[$,]/g, '');
	mem_privceStr.value = mem_privceStr.value.replace(/[$,]/g, '');



	if (serviceName === "") {
		$("#serviceName-error").text("service name is required");
		valid = false;
	}
	if (description === "") {
		$("#discription-error").text("service description is required");
		valid = false;
	}
	if (regularPrice === "") {
		$("#regularPrice-error").text("regular price is required");
		valid = false;
	}
	if (memberPrice === "") {
		$("#mem_rate-error").text("member price is required");
		valid = false;
	}

	if (!valid) {

		$("#inp_service_name").focus(function() {
			$("#serviceName-error").empty();
		});
		$("#inp_ser_dec").focus(function() {
			$("#discription-error").empty();
		});
		$("#select_product_cat").focus(function() {
			$("#productCat-Name-error").empty();
		});
		$("#reg_rate").focus(function() {
			$("#regularPrice-error").empty();
		});
		$("#mem_rate").focus(function() {
			$("#mem_rate-error").empty();
		});
	}
	else {


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

function saveAndSendEstimate() {
	if ($('#customer_name_input').val().length === 0 && $('#estimate-table1ProductName1').val().length === 0) {
		$('#alertModal').modal('show');
	} else {
		var estimateForm = $('#estimate-form');
		estimateForm.attr('action', '/estimate/process-save-send');
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
				getEstimateDetailsForEstimatePDFView(response.estimateId);
				$('#sendEstimateModal').modal('show');
				$('#inputfrom').val(response.fromEmailAddress);
				$('#inputTo').val(response.customerEmail);
				$('#inputsub').val(response.estimateEmailSubject);
				$('#inputBody').text(response.bodyContent);
				$('#estId').val(response.estimateId);
				var mySelect = $('#optionsdiv');
				mySelect.empty();
				var options = response.options;
				$.each(options, function(index) {
					mySelect.append("<input type='checkbox' style='margin-left: 3%;' class='checkbox-validation' value='" + options[index] + "' name='optionIds' id='optionCheck" + (index + 1) + "' onchange=updateEstimateHTMLView(event)>Option <span>" + (index + 1) + "</span>");
				});


				$("#PDFViewCustomerName").text(response.estimateDTO.customerDTO.customerName);
				$("#PDFViewAddress").text(response.estimateDTO.customerDTO.storedServiceLocations[0].locationNickName);
				$("#PDFViewCity").text(response.estimateDTO.customerDTO.storedServiceLocations[0].city);
				$("#PDFViewZip").text(response.estimateDTO.customerDTO.storedServiceLocations[0].address);
				$("#PDFViewTaxNum").text(response.estimateDTO.customerDTO.contactNumbers[0].number);
				$("#PDFViewEstimateId").text(response.estimateDTO.id);
				$("#PDFViewOptionId").text(response.estimateDTO.options[0].id);
				$("#PDFViewEstimateIdU").text(response.estimateDTO.id);
				$("#PDFViewOptionIdU").text(response.estimateDTO.options[0].id);
				$("#PDFViewCustomerName").text(response.estimateDTO.customerDTO.customerName);
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
function getEsimateDetailsForMailPopup(estimateId) {
	$.ajax({
		url: "/estimate/estimate-options/" + estimateId,
		type: 'GET',
		success: function(response) {
			console.log('AJAX request successful');
			console.log(response);
			getEstimateDetailsForEstimatePDFView(estimateId);
			$('#inputfrom').val(response.fromEmailAddress);
			$('#inputTo').val(response.customerEmail);
			$('#inputsub').val(response.estimateEmailSubject);
			$('#inputBody').text(response.bodyContent);
			$('#estId').val(response.estimateId);
			var mySelect = $('#optionsdiv');
			mySelect.empty();
			var options = response.options;
			$.each(options, function(index) {
				mySelect.append("<input type='checkbox' style='margin-left: 3%;' class='checkbox-validation' value='" + options[index] + "' name='optionIds' id='optionCheck" + (index + 1) + "' onchange=updateEstimateHTMLView(event)>Option <span>" + (index + 1) + "</span>");
			});


			$("#PDFViewCustomerName").text(response.estimateDTO.customerDTO.customerName);
			$("#PDFViewAddress").text(response.estimateDTO.customerDTO.storedServiceLocations[0].locationNickName);
			$("#PDFViewCity").text(response.estimateDTO.customerDTO.storedServiceLocations[0].city);
			$("#PDFViewZip").text(response.estimateDTO.customerDTO.storedServiceLocations[0].address);
			$("#PDFViewTaxNum").text(response.estimateDTO.customerDTO.contactNumbers[0].number);
			$("#PDFViewEstimateId").text(response.estimateDTO.id);
			$("#PDFViewOptionId").text(response.estimateDTO.options[0].id);
			$("#PDFViewEstimateIdU").text(response.estimateDTO.id);
			$("#PDFViewOptionIdU").text(response.estimateDTO.options[0].id);
			$("#PDFViewCustomerName").text(response.estimateDTO.customerDTO.customerName);

		},
		error: function(xhr, status, error) {
			console.log('AJAX request failed');
			console.log(error);
		}
	});
}

/*Get Estimate Pop-up details*/
function getEsimateDetailsForPrint(estimateId) {
	$('#printEModal').modal('show');
}

/*Get Estimate Details for Estimate PDF view HTML*/
function getEstimateDetailsForEstimatePDFView(estimateId) {
	$.ajax({
		url: "/estimate/estimate-options-html-view/" + estimateId,
		type: 'GET',
		success: function(response) {
			console.log(response);
			/*$("#estimateOptionsHTMLView").empty();*/
			$("#estimateIdHTMLView").text(response.estimateId);
			$("#estimateDateHTMLView").text(response.estimateDate);
			$("#estimateCustomerNameHTMLView").text(response.estimateCustomerName);
			$("#estimateId2HTMLView").text(response.estimateId);
			$.each(response.option, function(key, value) {
				let formatter = new Intl.NumberFormat('en-US', {
					style: 'currency',
					currency: 'USD'
				});
				console.log(value)
				var generatedOption = `<div id="option` + (key + 1) + `HTMLView" class="d-none" style="padding: 20px;min-height: 240px;"><h3 style="font-weight: bold;font-size: 1.2em;line-height: 2em;margin-top: 6%;">Option <span>` + (key + 1) + `</span></h3><div id="table"><table class="table-main" style="width: 100%;border-collapse: collapse;" id="estimateOptionsTblHTMLView` + (key + 1) + `"><thead>
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
						<span>`+ formatter.format(value.estimateTotal) + `</span>
					</div>					
				</div>`;
				$("#estimateOptionsHTMLView").append(generatedOption);
				var estimateTbl = $("#estimateOptionsTblHTMLView" + (key + 1));
				var opt = value.optionProducts;
				$.each(opt, function(index, element) {
					var psname;
					var desc;
					if (element.product) {
						psname = element.product.productName;
						desc = element.product.discription;
					} else {
						psname = element.services.serviceName;
						desc = element.services.discription;
					}
					var generatedTr = $("<tr class='list-item'><td data-label='Type' class='tableitem' style='padding: 10px;border-bottom: 1px solid #cccaca;font-size: 0.85em;text-align: left;'><span>" + psname + "</span></td><td data-label='Type' class='tableitem' style='padding: 10px;border-bottom: 1px solid #cccaca;font-size: 0.85em;text-align: left;'><span>" + desc + "</span></td><td data-label='Quantity' class='optionQuantityTextHTMLView d-none' style='padding: 10px;border-bottom: 1px solid #cccaca;font-size: 0.85em;text-align: left;'>" + element.quantity + "</td><td data-label='Rate' class='optionRateTextHTMLView d-none' style='padding: 10px;border-bottom: 1px solid #cccaca;font-size: 0.85em;text-align: left;'>" + formatter.format(element.rate) + "</td><td data-label='Total' class='optionTotalTextHTMLView d-none' style='padding: 10px;border-bottom: 1px solid #cccaca;font-size: 0.85em;text-align: left;'>" + formatter.format(element.total) + "</td></tr>");
					estimateTbl.append(generatedTr);
				});
			});
		},
		error: function(xhr, status, error) {
			console.log('AJAX request failed');
			console.log(error);
		}
	});
}

function updateAndSendEstimate() {
	if ($('#customer_name_input').val().length === 0 && $('#estimate-table1ProductName1').val().length === 0) {
		$('#alertModal').modal('show');
	} else {
		var estimateForm = $('#estimate-form');
		estimateForm.attr('action', '/estimate/process-update-send');
		// Get form data
		const formData = estimateForm.serialize();

		// Make an AJAX POST request
		$.ajax({
			url: estimateForm.attr('action'),
			type: 'POST',
			data: formData,
			success: function(response) {
				// Handle the response from the server, if needed
				getEstimateDetailsForEstimatePDFView(response.estimateId);
				$('#sendEstimateModal').modal('show');
				$('#inputfrom').val(response.fromEmailAddress);
				$('#inputTo').val(response.customerEmail);
				$('#inputsub').val(response.estimateEmailSubject);
				$('#inputBody').text(response.bodyContent);
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

function onSearchType(event) {
	const searchFieldName = event.target.id;
	var numericValues = searchFieldName.match(/\d+/g);
	var estimateTableId = numericValues[0];
	var productRowNo = numericValues[1];

	console.log("Estimate Table id : " + estimateTableId + "  :  Product Row No. : " + productRowNo);
	var searchResultsContainer = $('#estimate-table' + estimateTableId + 'search_results' + productRowNo);
	searchResultsContainer.empty();
	var productName = event.target.value;

	if (productName.length > 0) {
		$('#inpproduct').val(productName);
		$('#clickedEstimateTableNumber').val(estimateTableId);
		$('#clickedEstimateTableRowNumber').val(productRowNo);

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
						var productNameInputField = $('#estimate-table' + estimateTableId + 'ProductName' + productRowNo);
						productNameInputField.val(result.productName);
						$('#estimate-table' + estimateTableId + 'Description' + productRowNo).val(result.discription);
						$('#estimate-table' + estimateTableId + 'Rate' + productRowNo).val(convertIntoPriceFormat(result.regularPrice));
						$('#estimate-table' + estimateTableId + 'AverageCost' + productRowNo).val(convertIntoPriceFormat(result.averageCost));
						$('#estimate-table' + estimateTableId + 'AverageCostIntial' + productRowNo).val(convertIntoPriceFormat(result.averageCost));
						calculateTotal($('#estimate-table' + estimateTableId + 'Quantity' + productRowNo));
						generateProductRow(estimateTableId);
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
						var productNameInput = $('#estimate-table' + estimateTableId + 'ProductName' + productRowNo);
						productNameInput.attr("name", "estimate-table" + estimateTableId + "ServiceName" + productRowNo);
						productNameInput.val(result.serviceName);
						$('#estimate-table' + estimateTableId + 'Description' + productRowNo).val(result.discription);
						var rateInput = $('#estimate-table' + estimateTableId + 'Rate' + productRowNo);
						rateInput.val(convertIntoPriceFormat(result.regularPrice));
						var avrgCostInput = $('#estimate-table' + estimateTableId + 'AverageCost' + productRowNo);
						avrgCostInput.val(convertIntoPriceFormat(result.internalCost));
						var avrgCostInt = $('#estimate-table' + estimateTableId + 'AverageCostIntial' + productRowNo);
						avrgCostInt.val(convertIntoPriceFormat(result.internalCost));
						calculateTotal($('#estimate-table' + estimateTableId + 'Quantity' + productRowNo));
						generateProductRow(estimateTableId);
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
				showAddServiceAddProductBtn(estimateTableId, productRowNo);
			},
			error: function(error) {
				console.log('Error:', error);
			}
		});
	}
}

function calculateTotal(input) {
	var tableRow = input.parents('tr');
	const inputId = input.attr('id');
	var numericValues = inputId.match(/\d+/g);
	var estimateTableId = numericValues[0];
	var productRowNo = numericValues[1];

	var rateInput = tableRow.find('input[type="text"]').eq(2);
	var rateValue = rateInput.val();

	// Find the quantity input field within the table row
	var quantityInput = input;
	var quantityValue = quantityInput.val();

	var costInitialInput = tableRow.find('input[type="hidden"]').eq(0);
	var costInitialValue = costInitialInput.val();
	costInitialValue = costInitialValue.replace("$", "");
	costInitialValue = costInitialValue.replace(",", "");
	costInitialValue = parseFloat(costInitialValue);
	rateValue = rateValue.replace("$", "");
	rateValue = rateValue.replace(",", "");
	rateValue = parseFloat(rateValue);
	// Calculate the total cost
	var totalCost = costInitialValue * quantityValue;
	// Calculate the total
	var totalValue = rateValue * quantityValue;

	// Find the total input field within the table row
	var totalInput = tableRow.find('input[type="text"]').eq(3);
	totalInput.val(convertIntoPriceFormat(totalValue));

	var costInput = tableRow.find('input[type="text"]').eq(4);
	costInput.val(convertIntoPriceFormat(totalCost));


	calculateEstimateSummary(estimateTableId, productRowNo);

}

function calculateEstimateSummary(estimateTableId, productRowNo) {
	var estimateTotal = $('#estimate-table' + estimateTableId + 'EstimateTotal');
	var estimateTotalTxt = $('#estimate-table' + estimateTableId + 'EstimateTotalTxt');

	var jobCost = $('#estimate-table' + estimateTableId + 'JobCost');
	var jobCostTxt = $('#estimate-table' + estimateTableId + 'JobCostTxt');

	var grossProfit = $('#estimate-table' + estimateTableId + 'GrossProfit');
	var grossProfitTxt = $('#estimate-table' + estimateTableId + 'GrossProfitTxt');


	let grandCost = 0.00;
	let grandTotal = 0.00;
	let grossProfitVal = 0.00;

	var estimateTblId = '#estimate-table' + estimateTableId + ' tbody tr';

	$(estimateTblId).each(function(index, row) {
		var totalTrId = '#estimate-table' + estimateTableId + 'Total' + (index + 1);
		var totalVal = $(totalTrId).val();
		totalVal = totalVal.replace("$", "");
		totalVal = totalVal.replace(",", "");
		const totalTrVal = parseFloat(totalVal) || 0.00;
		grandTotal += totalTrVal;

		var costTrId = '#estimate-table' + estimateTableId + 'AverageCost' + (index + 1);
		var costVal = $(costTrId).val();
		costVal = costVal.replace("$", "");
		costVal = costVal.replace(",", "");
		const costTrVal = parseFloat(costVal) || 0.00;
		grandCost += costTrVal;
	});

	estimateTotal.val(grandTotal.toFixed(2));
	estimateTotalTxt.text("$" + grandTotal.toLocaleString());

	jobCost.val(grandCost.toFixed(2));
	jobCostTxt.text("$" + grandCost.toLocaleString());

	grossProfitVal = grandTotal - grandCost;

	grossProfit.val(grossProfitVal.toFixed(2));
	grossProfitTxt.text("$" + grossProfitVal.toLocaleString());

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

	$('#myTab_custom').find('li:last-child .estimate-close').remove();

	$('#myTab_custom').append($('<li class="nav-item" role="presentation"><button class="nav-link" id="tab_label' + tableId + '" data-bs-target="#tab' + tableId + '"  data-bs-toggle="tab" type="button" role="tab" aria-controls="tab' + tableId + '" aria-selected="false"><div class="row"><div class="col-md-9 input-items"><img src="/assest/document-signed.svg" class="img-icon me-2"><lable class="estimate-nav-item-label">Option ' + tableId + ' </lable></div><div class="col-md-3 input-items"><span class="dropdown"><div class="nav-user-img" role="button" data-bs-toggle="dropdown" aria-expanded="false"	id="estimateMenu"><img src="/assest/estimate-menu-white.svg" class="img-icon" alt="profile name" id="estimateMenuWhiteIcon"> <img src="/assest/estimate-menu-black.svg" class="img-icon d-none" alt="profile name" id="estimateMenuBlackIcon"></div><ul class="dropdown-menu estimate-custom-dropdown"><li><a onclick="duplicateEstimate(' + tableId + ')" class="dropdown-item"><span>Duplicate</span></a><a class="dropdown-item estimate-close" type="button" id="deleteOptionBtn' + tableId + '"><span>Delete</span></a></li></ul></span></div></div></button></li>')).insertAfter;
	$('#myTabContent_custom').append($('<div class="tab-pane fade" id="tab' + tableId + '" role="tabpanel" aria-labelledby="tab_label' + tableId + '">Tab ' + tableId + ' content</div>'));
	/*let a = $('.estimate_report_container').first().clone()*/
	let a = `<div class="estimate_report_container">
											<div class="container-fluid custome-data-table mt-5 p-0 overflow-auto">
												<ul id="search-results"></ul><table id="estimate-table`+ tableId + `" name="estimate-table` + tableId + `" class="table custom-table custom-category-table dt-responsive nowrap w-100"><input type="hidden" name="estimate-table` + tableId + `-input" value=1>
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
														<tr id="estimate-table`+ tableId + `tr1">
															<td>1</td>
															<td>
															<div class="input-with-icon" style="position: relative;">
															<input type="text" autocomplete="off" onclick="generateProductRow(`+ tableId + `)" placeholder="Type for search product.." class="form-control" name="estimate-table` + tableId + `ProductName1" id="estimate-table` + tableId + `ProductName1" value="" onkeyup="onSearchType(event)">															
																</div>
																<div id="estimate-table` + tableId + `search_results1" class="search-results"></div>
															</td>
															<td>
																<input type="text" class="form-control"
																	placeholder="" id="estimate-table`+ tableId + `Description1"
																	name="estimate-table`+ tableId + `Description1" value="" >
															</td>
															<td><input type="number" class="form-control" id="estimate-table` + tableId + `Quantity1" name="estimate-table` + tableId + `Quantity1" value="1.0" onchange="calculateTotal($(\'#\'+this.id))" min="1"></td>
															<td><input type="text" class="form-control"  id="estimate-table` + tableId + `Rate1" name="estimate-table` + tableId + `Rate1" onchange="calculateTotal($(\'#\'+this.id))" value="0" readonly></td>
															<td><input type="text" class="form-control" id="estimate-table` + tableId + `Total1" name="estimate-table` + tableId + `Total1" value="0" readonly></td>
															<td><input type="text" class="form-control" name="estimate-table` + tableId + `AverageCost1"  id="estimate-table` + tableId + `AverageCost1" value="0" readonly><input type="hidden" class="form-control" name="estimate-table` + tableId + `AverageCostIntial1"  id="estimate-table` + tableId + `AverageCostIntial1" value="0" ></td>
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
														<div class="estimate_summary">
															<div class="order-summary ">
																<h4 class="text-end fs-5">Estimate Summary</h4>
																<hr>
																<div class="plan">
																	<p>Esrimate Total :</p>
																	<input type="hidden" id="estimate-table`+ tableId + `EstimateTotal"  name="estimate-table` + tableId + `EstimateTotal" />
																	<h5 id="estimate-table`+ tableId + `EstimateTotalTxt">$00.00</h5>
																</div>
																<div class="plan">
																	<p>Job Cost :</p>
																	<input type="hidden" id="estimate-table`+ tableId + `JobCost"  name="estimate-table` + tableId + `JobCost" />
																	<h5 id="estimate-table`+ tableId + `JobCostTxt">$0.00</h5>
																</div>
																<div class="plan">
																	<p>
																		Gross Profit :
																	</p>
																	<input type="hidden" id="estimate-table`+ tableId + `GrossProfit"  name="estimate-table` + tableId + `GrossProfit"/>
																	<h5 id="estimate-table`+ tableId + `GrossProfitTxt">$00.00</h5>
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
	$('#estimate-table' + tableId + 'tr' + pRowNo).find("input:first").attr('onclick', 'generateProductRow(' + tableId + ')');

	$('#estimate-table' + tableId + 'tr' + rowNo).remove();

	var l = $('#estimate-table' + tableId + ' tbody tr').length;

	var productRowCounter = 1;
	$('#estimate-table' + tableId + ' tbody tr').each(function index() {

		//Changing id of tr
		$(this).attr('id', "estimate-table" + tableId + "tr" + productRowCounter);
		//Changing text of td
		$(this).find('td:first').text(productRowCounter);
		//Changing attributes of Product Name input
		var productNameInput = $(this).find('input').eq(0);
		var productNameInputName = $(productNameInput).attr("name");
		if (productNameInputName.includes("ProductName")) {
			productNameInput.attr('id', "estimate-table" + tableId + "ProductName" + productRowCounter);
			productNameInput.attr('name', "estimate-table" + tableId + "ProductName" + productRowCounter);
		} else {
			productNameInput.attr('id', "estimate-table" + tableId + "ProductName" + productRowCounter);
			productNameInput.attr('name', "estimate-table" + tableId + "ServiceName" + productRowCounter);
		}

		//Changing cap id
		var capsule = productNameInput.next('span');
		capsule.attr('id', 'estimate-table' + tableId + 'inp-cap' + productRowCounter);

		var inpdiv = capsule.parent();
		var suggestionWrapperDiv = inpdiv.next();
		var searchResult = suggestionWrapperDiv.next();

		//Changing search suggestion div id
		searchResult.attr('id', 'estimate-table' + tableId + 'search_results' + productRowCounter);
		//Changing attributes of description input
		var quantityInput = $(this).find('input').eq(1);
		quantityInput.attr('id', "estimate-table" + tableId + "Description" + productRowCounter);
		quantityInput.attr('name', "estimate-table" + tableId + "Description" + productRowCounter);
		//Changing attributes of quantity input
		var quantityInput = $(this).find('input').eq(2);
		quantityInput.attr('id', "estimate-table" + tableId + "Quantity" + productRowCounter);
		quantityInput.attr('name', "estimate-table" + tableId + "Quantity" + productRowCounter);
		//Changing attributes of rate input
		var rateInpput = $(this).find('input').eq(3);
		rateInpput.attr('id', "estimate-table" + tableId + "Rate" + productRowCounter);
		rateInpput.attr('name', "estimate-table" + tableId + "Rate" + productRowCounter);
		//Changing attributes of total input
		var totalInput = $(this).find('input').eq(4);
		totalInput.attr('id', "estimate-table" + tableId + "Total" + productRowCounter);
		totalInput.attr('name', "estimate-table" + tableId + "Total" + productRowCounter);
		//Changing attributes of average cost input
		var avgInput = $(this).find('input').eq(5);
		avgInput.attr('id', "estimate-table" + tableId + "AverageCost" + productRowCounter);
		avgInput.attr('name', "estimate-table" + tableId + "AverageCost" + productRowCounter);
		//Changing attributes of average cost intial input
		var avgIntialInput = $(this).find('input').eq(6);
		avgIntialInput.attr('id', "estimate-table" + tableId + "AverageCostIntial" + productRowCounter);

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
	calculateEstimateSummary(tableId, 1);
}


function duplicateEstimate(optionId) {
	var newOpt = $("#myTab_custom .nav-item").length;
	//Generate Option Grid
	generateOption();
	//Remove blank product row
	$("#estimate-table" + (newOpt + 1) + "tr1").remove();
	var counter = 1;
	//Iterate all the product row of option that has requested for duplicate
	$("#estimate-table" + (optionId) + " tbody > tr").each(function() {
		//Cloning product row
		var productRow = $(this).clone();
		//Append generated product row with new option		
		$("#estimate-table" + (newOpt + 1)).append(productRow);
		//Change id of generated product row > tr
		$(productRow).attr("id", "estimate-table" + (optionId + 1) + "tr" + counter);
		var prRowTd = $(productRow).find("td");
		//Get all inputs of the tr
		var prRowInps = prRowTd.find("input");
		//Get product name input and change all the attributes
		var prNameInp = prRowInps[0];
		var prNameName = $(prNameInp).attr("name");
		if (prNameName.includes("ProductName")) {
			$(prNameInp).attr("id", "estimate-table" + (newOpt + 1) + "ProductName" + counter);
			$(prNameInp).attr("name", "estimate-table" + (newOpt + 1) + "ProductName" + counter);
		} else {
			$(prNameInp).attr("id", "estimate-table" + (newOpt + 1) + "ProductName" + counter);
			$(prNameInp).attr("name", "estimate-table" + (newOpt + 1) + "ServiceName" + counter);
		}

		//Add onclick if product row is last row
		if ($(prNameInp).attr('onclick'))
			$(prNameInp).attr("onclick", "generateProductRow(" + (newOpt + 1) + ")");
		//Change search result id
		$(prRowTd).find(".search-results").attr("id", "estimate-table" + (newOpt + 1) + "search_results" + counter);
		//Get product quantity input and change all the attributes
		var prQuantityInp = prRowInps[1];
		$(prQuantityInp).attr("id", "estimate-table" + (newOpt + 1) + "Description" + counter);
		$(prQuantityInp).attr("name", "estimate-table" + (newOpt + 1) + "Description" + counter);
		//Get product quantity input and change all the attributes
		var prQuantityInp = prRowInps[2];
		$(prQuantityInp).attr("id", "estimate-table" + (newOpt + 1) + "Quantity" + counter);
		$(prQuantityInp).attr("name", "estimate-table" + (newOpt + 1) + "Quantity" + counter);
		//Get product rate input and change all the attributes
		var prRateInp = prRowInps[3];
		$(prRateInp).attr("id", "estimate-table" + (newOpt + 1) + "Rate" + counter);
		$(prRateInp).attr("name", "estimate-table" + (newOpt + 1) + "Rate" + counter);
		//Get product total input and change all the attributes
		var prTotalInp = prRowInps[4];
		$(prTotalInp).attr("id", "estimate-table" + (newOpt + 1) + "Total" + counter);
		$(prTotalInp).attr("name", "estimate-table" + (newOpt + 1) + "Total" + counter);
		//Get product Average Cost input and change all the attributes
		var prAvgCostInp = prRowInps[5];
		$(prAvgCostInp).attr("id", "estimate-table" + (newOpt + 1) + "AverageCost" + counter);
		$(prAvgCostInp).attr("name", "estimate-table" + (newOpt + 1) + "AverageCost" + counter);
		//Get product Average Cost input and change all the attributes
		var prAvgCostIntInp = prRowInps[6];
		$(prAvgCostIntInp).attr("id", "estimate-table" + (newOpt + 1) + "AverageCostIntial" + counter);
		var dltBtn = $(productRow).find(".del-row");
		if ($(dltBtn).attr("onclick"))
			$(dltBtn).attr("onclick", "removeProductRow(" + (newOpt + 1) + "," + counter + ")");

		/*Estimate Summary Section*/
		/*Estimate Total*/
		$("#estimate-table" + (newOpt + 1) + "EstimateTotal").val($("#estimate-table" + (optionId) + "EstimateTotal").val());
		$("#estimate-table" + (newOpt + 1) + "EstimateTotalTxt").text("$" + $("#estimate-table" + (optionId) + "EstimateTotal").val());
		/*Job Cost*/
		$("#estimate-table" + (newOpt + 1) + "JobCost").val($("#estimate-table" + (optionId) + "JobCost").val());
		$("#estimate-table" + (newOpt + 1) + "JobCostTxt").text("$" + $("#estimate-table" + (optionId) + "JobCost").val());
		/*Gross Profit*/
		$("#estimate-table" + (newOpt + 1) + "GrossProfit").val($("#estimate-table" + (optionId) + "GrossProfit").val());
		$("#estimate-table" + (newOpt + 1) + "GrossProfitTxt").text("$" + $("#estimate-table" + (optionId) + "GrossProfit").val());

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
			$("#jobPrintView").append(jobProductServiceTbl);
			$.each(response.optionProducts, function(key, value) {
				console.log(value);
				if (value.productCost) {
					$("#jobTablePDFView").append(`
					<tr class="list-item">
				      <td data-label="Type" class="tableitem" style="padding: 10px;border-bottom: 1px solid #cccaca;font-size: 0.85em;text-align: left;"><span>`+ value.productName + `</span></td>
				      <td data-label="Quantity" class="optionQuantityTextHTMLView" style="padding: 10px;border-bottom: 1px solid #cccaca;font-size: 0.85em;text-align: left;">`+ value.quantity + `</td>
				      <td data-label="Rate" class="optionRateTextHTMLView" style="padding: 10px;border-bottom: 1px solid #cccaca;font-size: 0.85em;text-align: left;">`+ value.rate + `</td>
				      <td data-label="Total" class="optionTotalTextHTMLView" style="padding: 10px;border-bottom: 1px solid #cccaca;font-size: 0.85em;text-align: left;">`+ value.total + `</td>
				   </tr>`);
				}
				if (value.serviceCost) {
					$("#jobTablePDFView").append(`
					<tr class="list-item">
				      <td data-label="Type" class="tableitem" style="padding: 10px;border-bottom: 1px solid #cccaca;font-size: 0.85em;text-align: left;"><span>`+ value.serviceName + `</span></td>
				      <td data-label="Quantity" class="optionQuantityTextHTMLView" style="padding: 10px;border-bottom: 1px solid #cccaca;font-size: 0.85em;text-align: left;">`+ value.quantity + `</td>
				      <td data-label="Rate" class="optionRateTextHTMLView" style="padding: 10px;border-bottom: 1px solid #cccaca;font-size: 0.85em;text-align: left;">`+ value.rate + `</td>
				      <td data-label="Total" class="optionTotalTextHTMLView" style="padding: 10px;border-bottom: 1px solid #cccaca;font-size: 0.85em;text-align: left;">`+ value.total + `</td>
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
			$("#jobPrintView").append(invoiceProductServiceTbl);
			$.each(response.optionProducts, function(key, value) {
				console.log(value);
				if (value.productCost) {
					$("#jobTablePDFView").append(`
					<tr class="list-item">
				      <td data-label="Type" class="tableitem" style="padding: 10px;border-bottom: 1px solid #cccaca;font-size: 0.85em;text-align: left;"><span>`+ value.productName + `</span></td>
				      <td data-label="Quantity" class="optionQuantityTextHTMLView" style="padding: 10px;border-bottom: 1px solid #cccaca;font-size: 0.85em;text-align: left;">`+ value.quantity + `</td>
				      <td data-label="Rate" class="optionRateTextHTMLView" style="padding: 10px;border-bottom: 1px solid #cccaca;font-size: 0.85em;text-align: left;">`+ value.rate + `</td>
				      <td data-label="Total" class="optionTotalTextHTMLView" style="padding: 10px;border-bottom: 1px solid #cccaca;font-size: 0.85em;text-align: left;">`+ value.total + `</td>
				   </tr>`);
				}
				if (value.serviceCost) {
					$("#jobTablePDFView").append(`
					<tr class="list-item">
				      <td data-label="Type" class="tableitem" style="padding: 10px;border-bottom: 1px solid #cccaca;font-size: 0.85em;text-align: left;"><span>`+ value.serviceName + `</span></td>
				      <td data-label="Quantity" class="optionQuantityTextHTMLView" style="padding: 10px;border-bottom: 1px solid #cccaca;font-size: 0.85em;text-align: left;">`+ value.quantity + `</td>
				      <td data-label="Rate" class="optionRateTextHTMLView" style="padding: 10px;border-bottom: 1px solid #cccaca;font-size: 0.85em;text-align: left;">`+ value.rate + `</td>
				      <td data-label="Total" class="optionTotalTextHTMLView" style="padding: 10px;border-bottom: 1px solid #cccaca;font-size: 0.85em;text-align: left;">`+ value.total + `</td>
				   </tr>`);
				}
			});
		},
		error: function(error) {
			console.log(error);
		}
	});
}
function generateSelectedOpProInvoice() {
	const optionProductsIds = [];
	const optionProductProductNames = [];
	const optionProductDescs = [];
	const optionProductRates = [];
	const optionProductTotals = [];
	const optionProductQuantities = [];
	const estimateId = [];
	$('input[type=checkbox].generateProductCheck').each(function() {

		var opProCheck = (this.checked ? $(this).val() : "");
		optionProductsIds.push(opProCheck);
		var tableTrId = $(this).parent().parent().attr("id");
		if (this.checked) {
			var productName = $("#" + tableTrId).find("input[type=text]").eq(0).val();
			optionProductProductNames.push(productName);
		}
		if (this.checked) {
			var productDesc = $("#" + tableTrId).find("input[type=text]").eq(1).val();
			optionProductDescs.push(productDesc);
		}
		if (this.checked) {
			var productRate = $("#" + tableTrId).find("input[type=text]").eq(2).val();
			optionProductRates.push(productRate);
		}
		if (this.checked) {
			var productTotal = $("#" + tableTrId).find("input[type=text]").eq(3).val();
			optionProductTotals.push(productTotal);
		}
		if (this.checked) {
			var productQuantity = $("#" + tableTrId).find("input[type=number]").eq(0).val();
			optionProductQuantities.push(productQuantity);
		}
	});

	estimateId.push($("#viewEstimateId").val());

	var data = {
		optionProductsIds: optionProductsIds,
		optionProductProductNames: optionProductProductNames,
		optionProductDescs: optionProductDescs,
		optionProductRates: optionProductRates,
		optionProductTotals: optionProductTotals,
		optionProductQuantities: optionProductQuantities,
		estimateId: estimateId
	};

	$.ajax({
		url: '/invoice/generate-invoice', // Your Spring Boot controller endpoint
		type: 'POST',
		contentType: 'application/json',
		data: JSON.stringify(data), // Convert your array to JSON string
		success: function(response) {
			console.log(response);
			window.location.href = "/invoice/invoices";
		},
		error: function(error) {
			console.log(error);
		}
	});
}
