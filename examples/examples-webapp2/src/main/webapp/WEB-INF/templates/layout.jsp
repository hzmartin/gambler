<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><tiles:getAsString name="title" /></title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="Martin">
<title><tiles:getAsString name="title" /></title>
<link rel="shortcut icon" href="/assets/ico/favicon.png">

<!-- Bootstrap core CSS -->
<link href="/bootstrap3/css/bootstrap.min.css" rel="stylesheet">
<link href="/assets/css/docs.css" rel="stylesheet">
<!-- Custom styles for this template -->
<link href="/css/sticky-footer-navbar.css" rel="stylesheet">
<link rel="stylesheet" href="/assets/css/jquery.fileupload.css">
<link rel="stylesheet" href="/assets/js/datepicker/css/datepicker.css">


<!--  Java Scripts  -->
<script src="/assets/js/jquery.js"></script>
<script src="/assets/js/jquery.md5.js"></script>
<script src="/bootstrap3/js/bootstrap.min.js"></script>
<script src="/assets/js/datepicker/js/bootstrap-datepicker.js"></script>
<script src="/assets/js/vendor/jquery.ui.widget.js"></script>
<!-- The Iframe Transport is required for browsers without support for XHR file uploads -->
<script src="/assets/js/jquery.iframe-transport.js"></script>
<!-- The basic File Upload plugin -->
<script src="/assets/js/jquery.fileupload.js"></script>

<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
      <script src="/assets/js/html5shiv.js"></script>
      <script src="/assets/js/respond.min.js"></script>
    <![endif]-->
</head>
<body>

	<div id="wrap">
		<tiles:insertAttribute name="header" />

		<tiles:insertAttribute name="content" />
	</div>
	<tiles:insertAttribute name="footer" />
</body>
</html>