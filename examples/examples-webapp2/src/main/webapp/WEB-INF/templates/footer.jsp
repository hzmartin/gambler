<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div id="footer">
	<div class="container">
		<p class="text-muted">
			<span class="en-code" style="font-family: arial; font-size: 14px;">©</span>
			Gambler
		</p>
	</div>
</div>

<script type="text/javascript">
	var userId = "${sgambler.userId}";
	$("#username").html(userId + " &nbsp;<b class='caret'></b>");
	$("#logout").click(function(event) {
		event.preventDefault();
		$.getJSON("/account/logout.do", function(data) {
			if (data.code == "OK") {
				window.location.href = "signin.do";
			} else {
				alert(data.msg);
			}
		});
	});
</script>