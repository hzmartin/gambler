<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="container">
	<!-- Main jumbotron for a primary marketing message or call to action -->
	<div class="jumbotron">
		<div class="container">
			<h1>
				Schedule System<c:if test="${environment ne ''}"><span class="badge"><c:out value="${environment}" /></span></c:if>
			</h1>
			<p>
				Feedback: <a href="mailto:hzwangqihui@gmail.com">hzwangqihui@gmail.com</a> or <a target="_blank" href="http://weibo.com/fhwangjiahui">@PossibleHZ</a>
			</p>
		</div>
	</div>
	<div class="page-header">
		<h2>
			<span class="label label-primary">Notices</span>
		</h2>
		<hr />
		<ol>
			<li>Focus on jobs management</li>
		</ol>
	</div>
</div>
<!-- /container -->