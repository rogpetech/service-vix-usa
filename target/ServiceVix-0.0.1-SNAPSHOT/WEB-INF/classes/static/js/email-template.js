
$(document).ready(function() {
	$("#templateType").change(function() {
		var templateType = $(this).val();
		if (templateType) {
			$('#emailTemplateBtn').prop('disabled', false);
			$('#emailTemplateBtn').css('opacity', "1.0");
			$('#emailTemplateBtn').css('cursor', "pointer");
			$.ajax({
				type: 'GET',
				url: '/email-template/' + templateType,
				success: function(response) {
					$("#templateSubject").val(response.templateSubject);
					$("#templateType").val(response.emailTemplateType);
					$("#templateId").val(response.id);
					if (response.templateText)
						tinymce.get('templateBody').setContent(response.templateText);
					else
						tinymce.get('templateBody').setContent('');
				}
			});
		} else {
			$('#emailTemplateBtn').prop('disabled', true);
			$('#emailTemplateBtn').css('opacity', "0.5");
			$('#emailTemplateBtn').css('cursor', "default");
			$("#templateSubject").val('');
			tinymce.get('templateBody').setContent('');
		}
	});

});