<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<link rel="shortcut icon" href="/bootstrap3/assets/ico/favicon.png">

<title>Example</title>

<!-- Bootstrap core CSS -->
<link href="/bootstrap3/dist/css/bootstrap.css" rel="stylesheet">

<!-- Custom styles for this template -->
<link href="/css/signin.css" rel="stylesheet">

<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
      <script src="/bootstrap3/assets/js/html5shiv.js"></script>
      <script src="/bootstrap3/assets/js/respond.min.js"></script>
    <![endif]-->
</head>

<body>

	<div class="container">

		<form class="form-signin" action="/login.do" method="post">
			<h2 class="form-signin-heading">Please sign in</h2>
			<input id="wqh-username" name="username" type="text" class="form-control"
				placeholder="Email address" autofocus> <input
				id="wqh-password" name="password" type="password" class="form-control"
				placeholder="Password"> <label class="checkbox"> <input
				type="checkbox" value="remember-me" id="wqh-remme"> Remember
				me
			</label>
			<button class="btn btn-lg btn-primary btn-block" type="submit"
				id="wqh-login">Sign in</button>
		</form>

	</div>
	<!-- /container -->


	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script src="/bootstrap3/assets/js/jquery.js"></script>
	<script src="/bootstrap3/dist/js/bootstrap.min.js"></script>
</body>
</html>
