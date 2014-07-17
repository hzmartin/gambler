<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="container">
	<h1>
		<span id="scheduler_name"></span>
	</h1>
	<hr />
	<p>
	<ul>
		<li>Instance Id: <span id="scheduler_instid"></span></li>
		<li>State: <span id="scheduler_state"></span></li>
		<li>Running Since: <span id="scheduler_running_since"></span></li>
		<li>Number Of Jobs Executed: <span
			id="scheduler_number_of_job_executed"></span></li>
		<li>Thread Pool Size: <span id="scheduler_thread_pool_size"></span></li>
		<li>Version: <span id="scheduler_version"></span></li>
	</ul>
	</p>
	<div class="container">
		<div class="row">
			<div class="col-md-1"></div>
			<div class="col-md-1">
				<span id="scheduler_start" class="glyphicon glyphicon-play"></span>
			</div>
			<div class="col-md-1">
				<span id="scheduler_pause" class="glyphicon glyphicon-pause"></span>
			</div>
			<div class="col-md-1">
				<span id="scheduler_stop" class="glyphicon glyphicon-stop"
					title="Stop immediately"></span>
			</div>
			<div class="col-md-1">
				<span id="scheduler_stop_wait" class="glyphicon glyphicon-off"
					title="Stop until all jobs completed"></span>
			</div>
		</div>
	</div>
	<h3></h3>
	<div class="container">
		<div class="row">
			<div class="col-md-1"></div>
			<div id="tip"></div>
		</div>
		<hr />
		<h3>
			<small>Summary:</small>
		</h3>
		<p id="scheduler_summary"></p>
	</div>
</div>

<script>
	var updateSchedulerInfo = function(schedulerInfo) {
		$('#scheduler_instid').text(schedulerInfo.schedulerInstanceId);
		$('#scheduler_thread_pool_size').text(schedulerInfo.threadPoolSize);
		$('#scheduler_running_since').text(schedulerInfo.runningSince);
		$('#scheduler_version').text(schedulerInfo.version);
		$('#scheduler_number_of_job_executed').text(
				schedulerInfo.numberOfJobsExecuted);
		$('#scheduler_name').text(schedulerInfo.schedulerName);
		$('#scheduler_summary').text(schedulerInfo.summary);
		$('#scheduler_state').text(schedulerInfo.state);
	}

	var doQuery = function() {
		$('#tip').text('');
		$.ajax({
			cache : false,
			dataType : "json",
			type : "GET",
			url : "/scheduler/getSchedulerInfo.do",
			success : function(resp) {
				if (resp.code == "OK") {
					updateSchedulerInfo(resp.data);
				} else {
					$('#tip').text(resp.msg);
				}
			}
		});
	}
	doQuery();

	$('#scheduler_start').click(function(event) {
		$('#tip').text('');
		event.preventDefault();
		$.ajax({
			cache : false,
			dataType : "json",
			type : "GET",
			url : "/scheduler/start.do",
			success : function(resp) {
				if (resp.code == "OK") {
					updateSchedulerInfo(resp.data);
				} else {
					$('#tip').text(resp.msg);
				}
			}
		});
	});

	$('#scheduler_pause').click(function(event) {
		$('#tip').text('');
		event.preventDefault();
		$.ajax({
			cache : false,
			dataType : "json",
			type : "GET",
			url : "/scheduler/standby.do",
			success : function(resp) {
				if (resp.code == "OK") {
					updateSchedulerInfo(resp.data);
				} else {
					$('#tip').text(resp.msg);
				}
			}
		});
	});

	$('#scheduler_stop').click(function(event) {
		$('#tip').text('');
		event.preventDefault();
		$.ajax({
			cache : false,
			dataType : "json",
			type : "GET",
			url : "/scheduler/shutdown.do",
			success : function(resp) {
				if (resp.code == "OK") {
					updateSchedulerInfo(resp.data);
				} else {
					$('#tip').text(resp.msg);
				}
			}
		});
	});

	$('#scheduler_stop_wait').click(function(event) {
		$('#tip').text('');
		event.preventDefault();
		$.ajax({
			cache : false,
			dataType : "json",
			type : "GET",
			url : "/scheduler/shutdown.do",
			data : {
				waitForJobsToComplete : true
			},
			success : function(resp) {
				if (resp.code == "OK") {
					updateSchedulerInfo(resp.data);
				} else {
					$('#tip').text(resp.msg);
				}
			}
		});
	});
</script>