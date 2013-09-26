<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<s:if test="hasActionErrors()">
		<div>
			<s:actionerror />
		</div>
	</s:if>
<s:form action="login" method="post">
    <s:textfield label="User Name" name="user.userId"/>
    <s:textfield label="Password" name="user.password"/>
    <s:submit value="Login"/> 
</s:form>
</body>
</html>
