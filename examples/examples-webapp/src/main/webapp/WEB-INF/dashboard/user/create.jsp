<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="../scripts/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="../scripts/json.js"></script>
<script>
	$(document).ready(function() {
		$('#button_test').click(function(event) {
			var name = $('#username').val();
			event.preventDefault();
		})
	});
</script>
</head>
<body>
<s:if test="hasActionErrors()">
		<div>
			<s:actionerror />
		</div>
	</s:if>
	<s:form action="create" method="post" id="user-create">
		<s:textfield label="User Name" name="user.userId" id="username" />
		<s:textfield label="Password" name="user.password" id="password" />
		<s:submit type="button" id="button_create" value="Create"></s:submit>
		<s:submit type="button" id="button_test" value="Test"></s:submit>
	</s:form>
	<div id="result"></div>
</body>
</html>
