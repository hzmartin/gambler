<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<script type="text/javascript" src="../scripts/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="../scripts/json.js"></script>
<script type='text/javascript' src='../dwr/engine.js'> </script>
<script type='text/javascript' src='../dwr/util.js'> </script>
<script type='text/javascript' src='../dwr/interface/Demo.js'> </script>
<script>
$( document ).ready(function() {
    $('#button_create').click(function(event){
    	var name = $('#username').val();
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
	<s:form action="create" method="post" id="user-create">
		<s:textfield label="User Name" name="username" id="username" />
		<s:textfield label="Password" name="password" id="password" />
		<s:submit type="button" id="button_create" value="Create"></s:submit>
	</s:form>
	<div id="result"></div>
	<div id="json"></div>
</body>
</html>
