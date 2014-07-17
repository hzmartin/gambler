<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="container">
	<div class="page-header">
		<h3>System User</h3>
	</div>
	<div class="containter">
		<form class="form-inline" role="form">
			<div class="form-group">
				<label class="sr-only" for="sys_userid">User Id</label> <input
					type="text" class="form-control" id="sys_userid"
					placeholder="User Id">
			</div>
			<div class="form-group">
				<label class="sr-only" for="sys_nick">Nick</label> <input
					type="text" class="form-control" id="sys_nick" placeholder="Nick">
			</div>
			<button id="sys_user_query" type="button" class="btn btn-info">Query</button>
			<button id="sys_user_update" type="button" class="btn btn-primary">Create/Update</button>
			<button id="sys_user_del" type="button" class="btn btn-danger">Delete</button>
		</form>
	</div>
	<div class="container hidden" id="perm-content">
		<hr />
		<h1>
			User: <span id="sys_user_info"></span>
		</h1>
		<p>&nbsp;</p>
		<table class="table table-hover" id="perm-list">
			<thead>
				<tr>
					<td>&nbsp;</td>
					<td>Permission</td>
					<td>Remark</td>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
		<button id="perm-update" type="button" class="btn btn-warning">Grant/Revoke
			Permission</button>
	</div>
</div>
<script>
	$('#sys_user_query')
			.click(
					function(event) {
						event.preventDefault();
						$
								.ajax({
									cache : false,
									dataType : "json",
									type : "GET",
									url : "/sysmgmt/getUserPermission.do",
									data : {
										userId : $('#sys_userid').val()
									},
									success : function(data) {
										if (data.code == "OK") {
											$('#perm-list tbody').html('');
											$('#perm-content').removeClass(
													'hidden');
											var user = data.data[0];
											var perms = data.data[1];
											var detail = user.nick ? user.userId
													+ ", " + user.nick
													: user.userId;
											$('#sys_user_info').text(detail);
											$("#perm-list").attr('userId',
													user.userId);
											$
													.each(
															perms,
															function(index,
																	value) {
																var remark = value.remark ? value.remark
																		: "";
																var roleHave = value.roleHave;
																var userHave = value.userHave;
																var col1cell = '<input type="checkbox" value="' + value.pid + '" class="nothave">'
																if (roleHave) {
																	col1cell = '<input type="checkbox" value="' + value.pid + '" disabled="true" checked="true" class="rolehave">'
																}
																if (userHave) {
																	col1cell = '<input type="checkbox" value="' + value.pid + '" checked="true" class="userhave">'
																}
																$(
																		'#perm-list tbody')
																		.append(
																				'<tr><td>'
																						+ col1cell
																						+ '</td><td>'
																						+ value.name
																						+ '</td><td>'
																						+ remark
																						+ '</td></tr>');
															});
											$(
													"#perm-list tbody td input:checkbox")
													.click(
															function() {
																$(this)
																		.attr(
																				"checked",
																				!$(
																						this)
																						.attr(
																								"checked"));
															});
										} else {
											alert(data.msg);
										}
									}
								});

					});

	$('#perm-update').click(function(e) {
		e.preventDefault();
		var userId = $("#perm-list").attr('userId');
		var config = {};
		$.each($('input:checkbox.userhave'), function(index, value) {
			config["" + value.value] = value.checked;
		});
		$.each($('input:checkbox.nothave'), function(index, value) {
			config["" + value.value] = value.checked;
		});
		$.ajax({
			cache : false,
			dataType : "json",
			type : "POST",
			url : "/sysmgmt/updateUserPermission.do",
			data : {
				userId : userId,
				config : JSON.stringify(config)
			},
			success : function(data) {
				alert(data.msg);
			}
		});
	});

	$('#sys_user_del').click(function(event) {
		event.preventDefault();
		$('#perm-content').addClass('hidden');
		$.ajax({
			cache : false,
			dataType : "json",
			type : "POST",
			url : "/sysmgmt/delUser.do",
			data : {
				userId : $('#sys_userid').val()
			},
			success : function(data) {
				alert(data.msg);
			}
		});
	});

	$('#sys_user_update').click(function(event) {
		event.preventDefault();
		$('#perm-content').addClass('hidden');
		$.ajax({
			cache : false,
			dataType : "json",
			type : "POST",
			url : "/sysmgmt/createOrUpdateUser.do",
			data : {
				userId : $('#sys_userid').val(),
				nick : $('#sys_nick').val()
			},
			success : function(data) {
				alert(data.msg);
			}
		});
	});
</script>
