<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="container">
	<div class="page-header">
		<h3>List Triggers</h3>
	</div>
	<div class="container">
		<table class="table table-hover table-bordered" id="trigger_list">
			<thead>
				<tr>
					<td>group/job</td>
					<td>name</td>
					<td>group</td>
					<td>type</td>
					<td>state</td>
					<td>description</td>
					<td>next fire</td>
					<td>previous fire</td>
					<td>start</td>
					<td>end</td>
					<td>misfireinstruction</td>
					<td>actions</td>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
	</div>
</div>
<script>
	var listtriggers = function() {
		$
				.ajax({
					cache : false,
					dataType : "json",
					type : "GET",
					url : "<%= request.getContextPath() %>/scheduler/getTriggerList.do",
					success : function(resp) {
						if (resp.code == "OK") {
							$('#trigger_list tbody').html('');
							$
									.each(
											resp.data,
											function(index, value) {
												$('#trigger_list tbody')
														.append(
																'<tr jobname="' + value.jobName + '" jobgroup="' + value.jobGroup + '"><td>'
																		+ value.jobGroup + '/' + value.jobName
																		+ '</td><td>'
																		+ value.name
																		+ '</td><td>'
																		+ value.group
																		+ '</td><td>'
																		+ value.type
																		+ '</td><td>'
																		+ value.stateLabel
																		+ '</td><td>'
																		+ value.description
																		+ '</td><td>'
																		+ value.nextFireTime
																		+ '</td><td>'
																		+ value.previousFireTime
																		+ '</td><td>'
																		+ value.startTime
																		+ '</td><td>'
																		+ value.endTime
																		+ '</td><td>'
																		+ value.misfireInstruction
																		+ '</td><td><button type="button" class="btn btn-primary trigger_view">View</button></tr>');
											});
							bind_action_handler();
                                                        $('#trigger_list').dataTable({"paging":true, "order":[[6,"asc"]],"stateSave":true});
						}
					}
				});
	}

	var bind_action_handler = function() {
		$('#trigger_list tbody tr .trigger_view').click(function(event) {
			event.preventDefault();
			var triggerrow = $(this).parent().parent();
			var name = triggerrow.attr('jobname');
			var group = triggerrow.attr('jobgroup');
			$.ajax({
				cache : false,
				type : "GET",
				url : "<%= request.getContextPath() %>/scheduleview/viewjob.do",
				data : {
					jobName : name,
					jobGroup : group
				},
				success : function(resp) {
					$('#site_sub_navtabs_content').html(resp);
				}
			});
		});

	}
	listtriggers();
</script>