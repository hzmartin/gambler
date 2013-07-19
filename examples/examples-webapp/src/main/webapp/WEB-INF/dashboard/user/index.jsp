<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<body>
<s:form action="login" method="post">
    <s:textfield label="User Name" name="username"/>
    <s:textfield label="Password" name="password"/>
    <s:submit value="Login"/> 
</s:form>
</body>
</html>
