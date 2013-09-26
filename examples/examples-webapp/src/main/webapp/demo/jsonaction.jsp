<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="../scripts/jquery-1.10.2.min.js"></script>
<script>
	$(document).ready(function() {
		$('#button_test').click(function(event) {
			var name = $('#username').val();
			$.ajax({
				dataType : "json",
				url : 'json.action',
				data : {
					'username' : name
				},
				success : function(data) {
					if (data.errorMessage) {
						$('#result').text(data.errorMessage);
					}
				}
			});
			event.preventDefault();
		})
	});
</script>
</head>
<body>
	<input type="text" id="username" />
	<button value="Test" id="button_test" type="submit">Test</button>
	<p />
	<div id="result"></div>
</body>
</html>
