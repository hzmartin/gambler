<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="../scripts/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="../scripts/json.js"></script>
<script type='text/javascript' src='../dwr/engine.js'>
</script>
<script type='text/javascript' src='../dwr/util.js'>
</script>
<script type='text/javascript' src='../dwr/interface/Demo.js'>
</script>
<script>
	$(document).ready(function() {
		$('#button_test').click(function(event) {
			var name = "Demo";
			Demo.hello(name, function(data) {
				$("#result").text(data);
				$("#json").text(data.message);
			});
			event.preventDefault();
		})
	});
</script>
</head>
<body>
	<button id="button_test" value="Test">Test</button>
	<div id="result"></div>
	<div id="json"></div>
</body>
</html>
