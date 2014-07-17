<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="container">
	<div class="page-header">
		<h3>List Jobs</h3>
	</div>
	<div class="container">
		<table class="table table-hover table-bordered" id="job_list">
			<thead>
				<tr>
					<td>name</td>
					<td>group</td>
					<td>job class</td>
					<td>description</td>
					<td>actions</td>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
	</div>
</div>
<script>
	var listjobs = function() {
		$
				.ajax({
					cache : false,
					dataType : "json",
					type : "GET",
					url : "/scheduler/getJobList.do",
					success : function(resp) {
						if (resp.code == "OK") {
							$('#job_list tbody').html('');
							$
									.each(
											resp.data,
											function(index, value) {
												$('#job_list tbody')
														.append(
																'<tr name="' + value.name + '" group="' + value.group + '"><td>'
																		+ value.name
																		+ '</td><td>'
																		+ value.group
																		+ '</td><td>'
																		+ value.jobClass
																		+ '</td><td>'
																		+ value.description
																		+ '</td><td><button type="button" class="btn btn-default job_view">View</button>'
																		+ '&nbsp;<button type="button" class="btn btn-danger job_del">Delete</button></td></tr>');
											});
							bind_action_handler();
						}
					}
				});
	}

	var bind_action_handler = function() {
		$('#job_list tbody tr .job_view').click(function(event) {
			event.preventDefault();
			var jobrow = $(this).parent().parent();
			var name = jobrow.attr('name');
			var group = jobrow.attr('group');
			$.ajax({
				cache : false,
				type : "GET",
				url : "/scheduleview/viewjob.do",
				data : {
					jobName : name,
					jobGroup : group
				},
				success : function(resp) {
					$('#site_sub_navtabs_content').html(resp);
				}
			});
		});

		$('#job_list tbody tr .job_del').click(
				function(event) {
					event.preventDefault();
					var jobrow = $(this).parent().parent();
					var name = jobrow.attr('name');
					var group = jobrow.attr('group');
					var answer = confirm("are you sure to delete job(" + group
							+ ", " + name + ")?");
					if (!answer) {
						return;
					}
					$.ajax({
						cache : false,
						dataType : "json",
						type : "POST",
						url : "/scheduler/deleteJob.do",
						data : {
							jobName : name,
							jobGroup : group
						},
						success : function(resp) {
							if (resp.code == "OK") {
								jobrow.remove();
							}
						}
					});
				});
	}
	listjobs();
</script>