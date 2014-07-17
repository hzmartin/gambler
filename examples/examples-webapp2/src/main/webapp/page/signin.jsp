<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<link rel="shortcut icon" href="./assets/ico/favicon.png">

<title><tiles:getAsString name="title" /></title>

<!-- Bootstrap core CSS -->
<link href="./bootstrap3/css/bootstrap.css" rel="stylesheet">

<!-- Custom styles for this template -->
<link href="./css/signin.css" rel="stylesheet">

</head>

<body>

	<div class="container">

		<form class="form-signin">
			<h2 class="form-signin-heading">Please sign in</h2>
			<input id="userId" type="text" class="form-control" placeholder="user id"
				required autofocus> <input id="password" type="password"
				class="form-control" placeholder="password" required> <label
				class="checkbox"> <input type="checkbox" id="remme"
				checked="checked"> Remember me
			</label>
			<div id="tip"></div>
			<button id="login" class="btn btn-lg btn-success btn-block"
				type="submit">Sign in</button>
		</form>
	</div>


	<!-- Bootstrap core JavaScript
    ================================================== -->
	<script src="./assets/js/jquery.js"></script>
	<script src="./assets/js/jquery.md5.js"></script>
	<script src="./bootstrap3/js/bootstrap.min.js"></script>
	<script type="text/javascript">
		var nextUrl = "${nextUrl}";
		var login = function() {
			var _remme = $("#remme").attr("checked") ? true : false;
			var userId = $("#userId").val();
			var password = $.md5($("#password").val());
			$.ajax({
				cache : false,
				dataType : "json",
				type : "GET",
				url : "/account/login.do",
				data : {
					userId : userId,
					password : password,
					remme : _remme
				},
				success : function(data) {
					if (data.code == "OK") {
						window.location.href = nextUrl;
					} else {
						$("#tip").html(
								"<div class='alert alert-danger alert-dismissable'>"
										+ data.msg + "</div>");
					}
				}
			});
		};
		$("#login").click(function(event) {
			event.preventDefault()
			login();
		});

		$("#remme").click(function() {
			$(this).attr("checked", !$(this).attr("checked"));
		});
	</script>
	<!-- Placed at the end of the document so the pages load faster -->
</body>
</html>
