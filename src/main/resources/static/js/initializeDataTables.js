// datatable with custo added buttons//
$(document).ready(function() {
	$('.custom-table').DataTable();
	$('button[data-bs-toggle="tab"]').on('shown.bs.tab', function(e) {
		$($.fn.dataTable.tables(true)).DataTable()
			.columns.adjust()
			.responsive.recalc();
	});
	$('#product-category-table_filter').append('<button class= "primary-btn" style="width:auto;"  data-bs-toggle="modal" data-bs-target="#addProductCategory"">+  Add Product Category </button>');
	$('#service-category-table_filter').append('<button class= "primary-btn" style="width:auto;"   data-bs-toggle="modal" data-bs-target="#addServiceCategory"">+  Add Service Category </button>');
	$('#workforce-table_filter').append('<a class= "primary-btn" href="/workforce/add">+ Add Staff</a>');
});


