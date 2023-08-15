function showWait() {
	$("#loading").html('<i class="fa-solid fa-gear fa-spin text-warning fs-2"></i>&nbsp;&nbsp;&nbsp;Processing.  Please wait ...');
	$("#loading").
		dialog(
			{				
				modal: true,
				width: 300,
				height: 100,
				show: {
					effect: "slide",
					duration: 500
				},
				hide: {
					effect: "fade",
					duration: 500
				},
				buttons: []
			}
		);
	$(".ui-dialog-titlebar").hide();
}

function hideWait() {
	$("#loading").dialog("close");
}

function validateImageFile(id) {
	var fileInput = document.getElementById(id);

	var filePath = fileInput.value;

	// Allowing file type
	var allowedExtensions = /(\.jpg|\.jpeg|\.png)$/i;

	if (!allowedExtensions.exec(filePath)) {
		$("#loading").html('<i class="fa-solid fa-circle-xmark text-warning fs-1"></i>  Invalid Image File.  Must be JPG/JPEG/PNG format');
		$("#loading").
			dialog(
				{
					title: "Invalid Image",
					modal: true,
					width: 300,
					height: 200,
					show: {
						effect: "slide",
						duration: 500
					},
					hide: {
						effect: "fade",
						duration: 500
					},
					buttons: [
						{
							html: "<span class='fa fa-check'> Ok</span>",
							open: function() {
								$(this).addClass('btn btn-warning');
							},
							click: function() {
								$(this).dialog("close");
							}
						}
					]
				}
			);		
		fileInput.value = '';
		$('#' + id + '-preview').html('<i class="fa-solid fa-person' + (id == 'mother'? '-dress': '') + ' text-primary fs-3"></i>');		
		return false;
	}

	return true;
}

function handleThumbnail(id) {

	$('#' + id).change(function() {
		if (validateImageFile(id)) {
			const file = this.files[0];
			//console.log(file);
			if (file) {
				let reader = new FileReader();
				reader.onload = function(event) {
					//console.log(event.target.result);
					$('#' + id + '-preview').html('<img id="' + id + '-preview" class="img-thumbnail rounded border border-info" src="' + event.target.result + '" alt="Preview picture of ' + id + '" />');
				}
				reader.readAsDataURL(file);
			}
		}
	});
}

function sleep() {
	setTimeout(function() {
		//console.log(people);
	}, 5000);
}

function handleChange() {
	handleThumbnail('father');
	handleThumbnail('mother');

	// function will get executed 
	// on click of submit button
	$("#submitReplicateai").click(function(ev) {
		
		if (validateImageFile('father') && validateImageFile('mother')) {
		
			// disabled the submit button
			$("#submitReplicateai").prop("disabled", true);
			showWait();
	
			ev.preventDefault();
	
			var replicateaiForm = $("#replicateaiForm")[0];
	
			// Create an FormData object 
			var data = new FormData(replicateaiForm);
	
			var url = $("#replicateaiForm").attr('action');
			$.ajax({
				type: "POST",
				enctype: 'multipart/form-data',
				url: url,
				data: data,
				processData: false,
				contentType: false,
				cache: false,
				timeout: 600000,
				success: function(result) {
					console.log("SUCCESS : ", result);
	
					do {
						$("#loading").html('<i class="fa-solid fa-gear fa-spin text-warning fs-2"></i>&nbsp;&nbsp;&nbsp;' + result[0].toUpperCase() + result.substring(1) + '.  Please wait ...');
	
						sleep();
						result = $.ajax({ url: "/status", method: 'get', async: false }).responseText;
						console.log(result);
					}
					while (result == "starting" || result == "processing");
	
					$('#replicateaiOutput').html('<div class="col-auto"><img id="child-prediction" class="img-fluid img-thumbnail border border-info rounded" src="' + result + '" alt="Prediction picture of the ' + $("input[name='gender']:checked").val() + '" />');
				},
				error: function(e) {
					console.log("ERROR : ", e);
				},
				complete: function() {
					hideWait();
					$("#submitReplicateai").prop("disabled", false);
				}
			});
		}

		return false;
	});
}
