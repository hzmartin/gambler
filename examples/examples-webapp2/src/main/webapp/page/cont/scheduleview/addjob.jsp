<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="container">
	<div class="page-header">
		<h3>Add/Edit Job&nbsp;<small><em>durability</em> and <em>recover</em> will not be loaded</small></h3>
	</div>
	<form class="form-horizontal" role="form">
		<div class="form-group">
			<label for="jobName" class="col-sm-2 control-label">Job Name</label>
			<div class="col-sm-10">
				<input type="text" class="form-control" id="job_jobName"
					placeholder="Job Name">
			</div>
		</div>
		<div class="form-group">
			<label for="jobGroup" class="col-sm-2 control-label">Job
				Group</label>
			<div class="col-sm-10">
				<input type="text" class="form-control" id="job_jobGroup"
					placeholder="Job Group">
			</div>
		</div>
		<div class="form-group">
			<label for="jobClass" class="col-sm-2 control-label">Job
				Class</label>
			<div class="col-sm-10">
				<input type="text" class="form-control" id="job_jobClass"
					placeholder="Job Class">
			</div>
		</div>
		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-10">
				<div class="checkbox">
					<label> <input type="checkbox" id="job_durability">
						durability
					</label>
				</div>
			</div>
		</div>
		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-10">
				<div class="checkbox">
					<label> <input type="checkbox" id="job_recover">
						recover
					</label>
				</div>
			</div>
		</div>
		<div class="form-group">
			<label for="jobDescription" class="col-sm-2 control-label">Description</label>
			<div class="col-sm-10">
				<textarea class="form-control" rows="3" id="job_jobDescription"
					placeholder="Job Description"></textarea>
			</div>
		</div>
		<div class="form-group">
			<label for="jobVariables" class="col-sm-2 control-label">Variables</label>
			<div class="col-sm-10">
				<textarea class="form-control" rows="3" id="job_jobVariables"
					placeholder="Job Variables in JSON format"></textarea>
			</div>
		</div>
		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-10">
				<button type="submit" class="btn btn-info" id="job_load_btn">Load to Edit</button>
				<button type="submit" class="btn btn-primary" id="job_add_btn">Submit</button>
			</div>
		</div>
		<div class="form-group">
			<label for="tip" class="col-sm-2 control-label"></label><div id="tip"></div>
		</div>
	</form>
</div>
<script>
	$('#job_add_btn').click(function(event) {
		event.preventDefault();
		$('#tip').text('');
		$.ajax({
			cache : false,
			type : "POST",
			url : "<%=request.getContextPath()%>/scheduler/addJob.do",
			data : {
				jobName : $('#job_jobName').val(),
				jobGroup : $('#job_jobGroup').val(),
				jobClass : $('#job_jobClass').val(),
				durability : $("#job_durability").attr("checked") ? true : false,
				shouldRecover : $("#job_recover").attr("checked") ? true : false,
				description : $('#job_jobDescription').val(),
				jobDataMapJson : $('#job_jobVariables').val(),
			},
			success : function(resp) {
				$('#tip').text(resp.msg);
			}
		});
	});
	
	$("#job_load_btn").click(function(event) {
		event.preventDefault();
		$('#tip').text('');
		$.ajax({
			cache : false,
			type : "POST",
			url : "<%=request.getContextPath()%>/scheduler/getJob.do",
			data : {
				jobName : $('#job_jobName').val(),
				jobGroup : $('#job_jobGroup').val()
			},
			success : function(resp) {
				if (resp.code == 'OK') {
					var jobinfo = resp.data;
					if(jobinfo){
						$('#job_jobClass').val(jobinfo.jobClass);
						$('#job_jobDescription').text(jobinfo.description);
						$('#job_jobVariables').text(JSON.stringify(jobinfo.jobDataMap));	
					}else{
						$('#tip').text("job not found!");
					}
				} else {
					$('#tip').text(resp.msg);
				}
			}
		});
	});
	$("#job_durability").click(function() {
		$(this).attr("checked", !$(this).attr("checked"));
	});
	$("#job_recover").click(function() {
		$(this).attr("checked", !$(this).attr("checked"));
	});
</script>