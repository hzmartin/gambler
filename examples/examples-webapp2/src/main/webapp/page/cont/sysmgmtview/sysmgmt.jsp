<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="container" id="site_sub_navtabs">
	<ul class="nav nav-tabs">
		<li url="<%= request.getContextPath() %>/sysmgmtview/listusers.do"><a href="#">Users</a></li>
	</ul>
</div>
<div id="site_sub_navtabs_content" class="container">
	<h1 class="text-center">System Management</h1>
	<h3 class="text-center">click <small><i>Users</i></small> tab for more information</h3>
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