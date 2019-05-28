<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="Martin">
<title><tiles:getAsString name="title" />&nbsp;<c:if test="${environment ne ''}">[<c:out value="${environment}" />]</c:if></title>
<link rel="shortcut icon" href="/assets/ico/favicon.png">

<!-- Bootstrap core CSS -->
<link href="<%= request.getContextPath() %>/bootstrap3/css/bootstrap.min.css" rel="stylesheet">
<link href="<%= request.getContextPath() %>/assets/css/docs.css" rel="stylesheet">
<link href="<%= request.getContextPath() %>/assets/css/jquery.dataTables.css" rel="stylesheet">
<link href="<%= request.getContextPath() %>/assets/css/jquery.dataTables_themeroller.css" rel="stylesheet">
<!-- Custom styles for this template -->
<link href="<%= request.getContextPath() %>/css/base.css" rel="stylesheet">
<link href="<%= request.getContextPath() %>/css/sticky-footer-navbar.css" rel="stylesheet">
<link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/jquery.fileupload.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/assets/js/datepicker/css/datepicker.css">


<!--  Java Scripts  -->
<script src="<%= request.getContextPath() %>/assets/js/jquery.js"></script>
<script src="<%= request.getContextPath() %>/assets/js/jquery.md5.js"></script>
<script src="<%= request.getContextPath() %>/assets/js/jquery.dataTables.min.js"></script>
<script src="<%= request.getContextPath() %>/bootstrap3/js/bootstrap.min.js"></script>
<script src="<%= request.getContextPath() %>/assets/js/datepicker/js/bootstrap-datepicker.js"></script>
<script src="<%= request.getContextPath() %>/assets/js/vendor/jquery.ui.widget.js"></script>
<!-- The Iframe Transport is required for browsers without support for XHR file uploads -->
<script src="<%= request.getContextPath() %>/assets/js/jquery.iframe-transport.js"></script>
<!-- The basic File Upload plugin -->
<script src="<%= request.getContextPath() %>/assets/js/jquery.fileupload.js"></script>

<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
      <script src="<%= request.getContextPath() %>/assets/js/html5shiv.js"></script>
      <script src="<%= request.getContextPath() %>/assets/js/respond.min.js"></script>
    <![endif]-->
</head>
<body>
	
	<div id="wrap">
		<tiles:insertAttribute name="header" />
		<div class="container" id="site_content">
			<tiles:insertAttribute name="content" />
		</div>
	</div>
	<tiles:insertAttribute name="footer" />
</body>
</html>