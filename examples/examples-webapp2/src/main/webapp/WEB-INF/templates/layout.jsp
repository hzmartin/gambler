<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<!DOCTYPE html>
<html lang="zh_CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><tiles:getAsString name="title" /></title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<link rel="shortcut icon" href="/bootstrap3/assets/ico/favicon.png">

<title>Starter Template for Bootstrap</title>

<!-- Bootstrap core CSS -->
<link href="/bootstrap3/dist/css/bootstrap.css" rel="stylesheet">

<!-- Custom styles for this template -->
<link href="<tiles:getAsString name="css" />" rel="stylesheet">

<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
      <script src="/bootstrap3/assets/js/html5shiv.js"></script>
      <script src="/bootstrap3/assets/js/respond.min.js"></script>
    <![endif]-->
</head>
<body>

	<div class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<tiles:insertAttribute name="header" />
		</div>
	</div>

	<div class="container">
		<tiles:insertAttribute name="content" />
	</div>
	<!-- /.container -->


	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script src="../../assets/js/jquery.js"></script>
	<script src="../../dist/js/bootstrap.min.js"></script>

	<tiles:insertAttribute name="footer" />
</body>
</html>