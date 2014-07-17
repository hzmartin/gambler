<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="container" id="site_sub_navtabs">
	<ul class="nav nav-tabs">
		<li url="/scheduleview/listjobs.do"><a href="#">List Jobs</a></li>
		<li url="/scheduleview/listtriggers.do"><a href="#">List
				Triggers</a></li>
		<li url="/scheduleview/addjob.do"><a href="#">Create Job</a></li>
	</ul>
</div>
<div id="site_sub_navtabs_content" class="container">
	<h3 class="text-center">Currently Executing Jobs</h3>
	<div class="container">
		<table class="table table-hover table-bordered" id="executingjob_list">
			<thead>
				<tr>
					<td>name</td>
					<td>group</td>
					<td>job class</td>
					<td>description</td>
					<td>previous fire</td>
					<td>next fire</td>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
	</div>
</div>
<script>
	var listcurrentlyexecutingjobs = function() {
		$
				.ajax({
					cache : false,
					dataType : "json",
					type : "GET",
					url : "/scheduler/getCurrentlyExecutingJobs.do",
					success : function(resp) {
						if (resp.code == "OK") {
							$('#executingjob_list tbody').html('');
							$
									.each(
											resp.data,
											function(index, value) {
												$('#executingjob_list tbody')
														.append(
																'<tr><td>'
																		+ value.jobName
																		+ '</td><td>'
																		+ value.jobGroup
																		+ '</td><td>'
																		+ value.jobClass
																		+ '</td><td>'
																		+ value.jobDescription
																		+ '</td><td>'
																		+ value.previousFireTime
																		+ '</td><td>'
																		+ value.nextFireTime
																		+ '</td></tr>');
											});
						}
					}
				});
	}
	listcurrentlyexecutingjobs();

	$('#site_sub_navtabs .nav-tabs li').click(function() {
		$('#site_sub_navtabs .nav-tabs li').removeClass('active');
		$(this).addClass('active');
		var url = $(this).attr('url');
		$.ajax({
			cache : false,
			type : "GET",
			url : url,
			success : function(data) {
				$('#site_sub_navtabs_content').html(data);
			}
		});
	});
</script>