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
	<h3 class="text-center">Job Management</h3>
</div>
<script>
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