<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="container">
	<div class="page-header">
		<h3>
			Job Detail <small></small>
		</h3>
	</div>
	<ul id="job_info">
		<li>name: <span id="viewjob_name"></span></li>
		<li>group: <span id="viewjob_group"></span></li>
		<li>job class: <span id="viewjob_jobClass"></span></li>
		<li>description: <span id="viewjob_description"></span></li>
	</ul>
	<hr />
	<h3>Variables</h3>
	<ul id="job_vars">
	</ul>
	<hr />
	<h3>Triggers</h3>
	<table class="table table-hover table-bordered" id="job_triggers">
		<thead>
			<tr>
				<td>name</td>
				<td>group</td>
				<td>next fire</td>
				<td>type</td>
				<td>state</td>
				<td>description</td>
				<td>actions</td>
			</tr>
		</thead>
		<tbody>
		</tbody>
	</table>
	<hr />
	<h3>Actions</h3>
	<p>
		<button type="button" class="btn btn-primary" id="jobdetail_refresh">Refresh</button>
		<button type="button" class="btn btn-warning" id="job_runoncenow">Run
			Once Now</button>
		<button type="button" class="btn btn-success" id="job_simple_schedule">Simple
			Schedule</button>
		<button type="button" class="btn btn-info" id="job_cron_schedule">Cron
			Schedule</button>
	</p>
	<hr />
	<div id="tip"></div>
</div>

<!-- Modal -->
<div class="modal fade" id="simple_schedule_modal" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">Simple Schedule</h4>
			</div>
			<div class="modal-body">
				name: <input type="text" class="form-control"
					placeholder="trigger name" id="simple_trigger_name"><br />
				group: <input type="text" class="form-control"
					placeholder="trigger group" id="simple_trigger_group"> <br />
				description:
				<textarea class="form-control" rows="3" placeholder="description"
					id="simple_trigger_description"></textarea>
				<br /> start time: <input type="text" class="form-control"
					placeholder="start time, format: yyyyMMddHHmmss etc.,"
					id="simple_trigger_starttime"><br /> end time: <input
					type="text" class="form-control"
					placeholder="end time, format: yyyyMMddHHmmss etc.,"
					id="simple_trigger_endtime"><br /> repeat count: <input
					type="text" class="form-control"
					placeholder="The number of times for the Trigger to repeat firing"
					id="simple_trigger_repeatcount"><br /> repeat interval(*required): <input
					type="text" class="form-control"
					placeholder="The number of milliseconds to pause between the repeat firing."
					id="simple_trigger_repeatinterval"> <br />
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal"
					id="simple_trigger_close">Close</button>
				<button type="button" class="btn btn-primary"
					id="simple_trigger_save">Save</button>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
</div>
<!-- /.modal -->

<!-- Modal -->
<div class="modal fade" id="cron_schedule_modal" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">Cron Schedule</h4>
			</div>
			<div class="modal-body">
				name: <input type="text" class="form-control"
					placeholder="trigger name" id="cron_trigger_name"><br />
				group: <input type="text" class="form-control"
					placeholder="trigger group" id="cron_trigger_group"> <br />
				description:
				<textarea class="form-control" rows="3" placeholder="description"
					id="cron_trigger_description"></textarea>
				<br /> cron expression(*required): <a href="<%= request.getContextPath() %>/cron.html" target="_blank">see
					doc</a><input type="text" class="form-control"
					placeholder="cron expression" id="cron_trigger_cronex"><br />
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal"
					id="cron_trigger_close">Close</button>
				<button type="button" class="btn btn-primary" id="cron_trigger_save">Save</button>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
</div>
<!-- /.modal -->
<script>
	var jobName = "${jobName}";
	var jobGroup = "${jobGroup}";
	var jobDataMapJson = "";
	var getJobInfo = function() {
		$('#tip').text("");
		$
				.getJSON(
						"<%= request.getContextPath() %>/scheduler/getJob.do",
						{
							jobName : jobName,
							jobGroup : jobGroup,
							withTrigger : true
						},
						function(resp) {
							if (resp.code == "OK") {
								var data = resp.data;
								$('#viewjob_name').text(data.name);
								$('#viewjob_group').text(data.group);
								$('#viewjob_jobClass').text(data.jobClass);
								$('#viewjob_description')
										.text(data.description);
								if (data.jobDataMap) {
									$('#job_vars').html("");
									jobDataMapJson = JSON
											.stringify(data.jobDataMap);
									$.each(data.jobDataMap,
											function(key, value) {
												$('#job_vars').append(
														'<li>' + key + ": "
																+ value
																+ '</li>');
											});
								}
								if (data.triggers) {
									$('#job_triggers tbody').html("");
									$
											.each(
													data.triggers,
													function(index, value) {
														$('#job_triggers tbody')
																.append(
																		'<tr name="' + value.name + '" group="' + value.group + '"><td>'
																				+ value.name
																				+ '</td><td>'
																				+ value.group
																				+ '</td><td>'
																				+ value.nextFireTime
																				+ '</td><td>'
																				+ value.type
																				+ '</td><td>'
																				+ value.stateLabel
																				+ '</td><td>'
																				+ value.description
																				+ '</td><td><button type="button" class="btn btn-primary trigger_unschedule">Unschedule</button>'
																				+ '&nbsp;<button type="button" class="btn btn-warning trigger_pause">Pause</button>'
																				+ '&nbsp;<button type="button" class="btn btn-info trigger_resume">Resume</button></tr>');

													});
									bind_action_handler();
								}
							} else {
								$('#tip').text(resp.msg);
							}
						});
	}
	getJobInfo();

	$('#jobdetail_refresh').click(function(event) {
		event.preventDefault();
		getJobInfo();
	});

	var bind_action_handler = function() {
		$('#job_triggers tbody tr .trigger_unschedule').click(function(event) {
			event.preventDefault();
			var triggerrow = $(this).parent().parent();
			var name = triggerrow.attr('name');
			var group = triggerrow.attr('group');
			$.ajax({
				cache : false,
				dataType : "json",
				type : "POST",
				url : "<%= request.getContextPath() %>/scheduler/unscheduleJob.do",
				data : {
					triggerName : name,
					triggerGroup : group
				},
				success : function(resp) {
					triggerrow.remove();
				}
			});
		});

		$('#job_triggers tbody tr .trigger_pause').click(function(event) {
			event.preventDefault();
			var triggerrow = $(this).parent().parent();
			var name = triggerrow.attr('name');
			var group = triggerrow.attr('group');
			$.ajax({
				cache : false,
				dataType : "json",
				type : "POST",
				url : "<%= request.getContextPath() %>/scheduler/pauseTrigger.do",
				data : {
					triggerName : name,
					triggerGroup : group
				},
				success : function(resp) {
					$('#tip').text(resp.msg);
				}
			});
		});

		$('#job_triggers tbody tr .trigger_resume').click(function(event) {
			event.preventDefault();
			var triggerrow = $(this).parent().parent();
			var name = triggerrow.attr('name');
			var group = triggerrow.attr('group');
			$.ajax({
				cache : false,
				dataType : "json",
				type : "POST",
				url : "<%= request.getContextPath() %>/scheduler/resumeTrigger.do",
				data : {
					triggerName : name,
					triggerGroup : group
				},
				success : function(resp) {
					$('#tip').text(resp.msg);
				}
			});
		});
	}

	$('#job_runoncenow').click(function(event) {
		event.preventDefault();
		$.ajax({
			cache : false,
			dataType : "json",
			type : "POST",
			url : "<%= request.getContextPath() %>/scheduler/runOnceNow.do",
			data : {
				jobName : jobName,
				jobGroup : jobGroup,
				jobDataMapJson : jobDataMapJson
			},
			success : function(resp) {
				$('#tip').text(resp.msg);
			}
		});
	});

	$('#job_simple_schedule').click(function(event) {
		event.preventDefault();
		$('#simple_schedule_modal').modal('show');
	});

	$('#job_cron_schedule').click(function(event) {
		event.preventDefault();
		$('#cron_schedule_modal').modal('show');
	});
	$('#simple_trigger_save').click(function(event) {
		event.preventDefault();
		$.ajax({
			cache : false,
			dataType : "json",
			type : "POST",
			url : "<%= request.getContextPath() %>/scheduler/scheduleJob.do",
			data : {
				jobName : jobName,
				jobGroup : jobGroup,
				triggerName : $('#simple_trigger_name').val(),
				triggerGroup : $('#simple_trigger_group').val(),
				startTime : $('#simple_trigger_starttime').val(),
				endTime : $('#simple_trigger_endtime').val(),
				description : $('#simple_trigger_description').val(),
				repeatCount : $('#simple_trigger_repeatcount').val(),
				repeatInterval : $('#simple_trigger_repeatinterval').val()
			},
			success : function(data) {
				if (data.code == "OK") {
					$('#simple_schedule_modal').modal('hide');
				} else {
					alert(data.msg);
				}
			}
		});
	});

	$('#cron_trigger_save').click(function(event) {
		event.preventDefault();
		$.ajax({
			cache : false,
			dataType : "json",
			type : "POST",
			url : "<%= request.getContextPath() %>/scheduler/scheduleCronJob.do",
			data : {
				jobName : jobName,
				jobGroup : jobGroup,
				triggerName : $('#cron_trigger_name').val(),
				triggerGroup : $('#cron_trigger_group').val(),
				description : $('#cron_trigger_description').val(),
				cronEx : $('#cron_trigger_cronex').val(),
			},
			success : function(data) {
				if (data.code == "OK") {
					$('#cron_schedule_modal').modal('hide');
				} else {
					alert(data.msg);
				}
			}
		});
	});
</script>