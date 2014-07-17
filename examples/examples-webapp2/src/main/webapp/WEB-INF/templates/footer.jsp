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
	$(".site_mainnav").click(function(event) {
		event.preventDefault();
		$('.site_mainnav').removeClass('active');
		$(this).addClass('active');
		var url = $(this).attr('url');
		$.ajax({
			cache : false,
			type : "GET",
			url : url,
			success : function(data) {
				$('#site_content').html(data);
			}
		});
	});
</script>