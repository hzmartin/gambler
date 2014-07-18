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
		$.getJSON("/gambler/account/logout.do", function(data) {
			if (data.code == "OK") {
				window.location.href = "signin.do";
			} else {
				alert(data.msg);
			}
		});
	});
	$("#updatepassword").click(function(event) {
		event.preventDefault();
		var oldPass = prompt("Old Password:");
		if (!oldPass) {
			return;
		}
		var newpass = prompt("New Password");
		if (!newpass) {
			return;
		}
		$.getJSON("/gambler/sysmgmt/updatePassword.do", {
			userId : userId,
			oldPass : $.md5(oldPass),
			newPass : $.md5(newpass)
		}, function(data) {
			alert(data.msg);
		});
	});
	$('#switchuser').click(function(event) {
		event.preventDefault();
		var targetId = prompt("please enter the target user id");
		if (!targetId) {
			return;
		}
		$.getJSON("/gambler/account/switchUser.do", {
			userId : targetId
		}, function(data) {
			if (data.code == "OK") {
				window.location.href = "index.html";
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