$(document).ready(function() {

	$('#btn-add-tab').click(function() {
		generateOption();
	});


	$('#myTab_custom').on('click', '.estimate-close', function() {
		var $li = $(this).parents('li');
		var isLastLi = $li.is(':last-child'); // Check if the current li is the last one
		var isFirstLi = $li.is(':first-child'); // Check if the current li is the first one
		if (!isFirstLi || $('#myTab_custom li').length > 1) {
			$li.remove();
			var liLength = this.id.substring(15, this.id.length);
			$("#tab" + liLength).remove();
			$("#tab_label" + (liLength - 1)).addClass("active");
			$("#tab" + (liLength - 1)).addClass(" show active");
			if (isLastLi) {
				// Add close button to the new last li if there is more than one li remaining
				if ($('#myTab_custom > .nav-item').length > 1) {
					$('#myTab_custom li:last-child ul li').append('<a  class="dropdown-item estimate-close" type="button"  id="deleteOptionBtn' + (liLength - 1) + '"><span>Delete</span></a>');
				}
			}			
		}
	});
	
	$('#myTab_custom').on('click', '.job-close', function() {
		var $li = $(this).parents('li');
		var isLastLi = $li.is(':last-child'); // Check if the current li is the last one
		var isFirstLi = $li.is(':first-child'); // Check if the current li is the first one
		if (!isFirstLi || $('#myTab_custom li').length > 1) {
			$li.remove();
			var liLength = this.id.substring(15, this.id.length);
			$("#tab" + liLength).remove();
			$("#tab_label" + (liLength - 1)).addClass("active");
			$("#tab" + (liLength - 1)).addClass(" show active");
			if (isLastLi) {
				// Add close button to the new last li if there is more than one li remaining
				if ($('#myTab_custom > .nav-item').length > 1) {
					$('#myTab_custom li:last-child ul li').append('<a  class="dropdown-item job-close" type="button"  id="deleteOptionBtn' + (liLength - 1) + '"><span>Delete</span></a>');
				}
			}			
		}
	});

});

