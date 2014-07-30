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
<!-- Modal -->
<div class="modal fade" id="main-modal-updatepwd" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true"
	style="margin-left: -400px;">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">Update Password</h4>
			</div>
			<div class="modal-body">
				old password: <input type="password" class="form-control"
					placeholder="old password" id="main-oldpassword"><br />
				new password: <input type="password" class="form-control"
					placeholder="new password" id="main-newpassword"> <br />
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal"
					id="main-updatepwd-close">Close</button>
				<button type="button" class="btn btn-primary"
					id="main-updatepwd-save">Save</button>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
</div>
<!-- /.modal -->
<script type="text/javascript">
	var userId = "${sgambler.userId}";
	$("#username").html(userId + " &nbsp;<b class='caret'></b>");
	$("#logout").click(function(event) {
		event.preventDefault();
		$.getJSON("<%= request.getContextPath() %>/account/logout.do", function(data) {
			if (data.code == "OK") {
				window.location.href = "signin.do";
			} else {
				alert(data.msg);
			}
		});
	});
	$("#updatepassword").click(function(event) {
		event.preventDefault();
		$('#main-modal-updatepwd').modal('show');
	});
	$('#main-updatepwd-save').click(function(event) {
		event.preventDefault();
		$.ajax({
			cache : false,
			dataType : "json",
			type : "POST",
			url : "<%= request.getContextPath() %>/sysmgmt/updatePassword.do",
			data : {
				userId : userId,
				oldPass : $.md5($('#main-oldpassword').val()),
				newPass : $.md5($('#main-newpassword').val())
			},
			success : function(data) {
				if (data.code == "OK") {
					$('#main-modal-updatepwd').modal('hide');
				} else {
					alert(data.msg);
				}
			}
		});
	});
	$('#switchuser').click(function(event) {
		event.preventDefault();
		var targetId = prompt("please enter the target user id");
		if (!targetId) {
			return;
		}
		$.getJSON("<%= request.getContextPath() %>/account/switchUser.do", {
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