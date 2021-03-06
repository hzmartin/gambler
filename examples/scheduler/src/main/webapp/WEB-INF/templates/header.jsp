<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="navbar navbar-default navbar-static-top" role="navigation">
	<div class="container">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target=".navbar-collapse">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="<%= request.getContextPath() %>/index.do">Gambler <c:if test="${environment ne ''}"><span class="badge"><c:out value="${environment}" /></span></c:if></a>
		</div>
		<div class="navbar-collapse collapse">
			<ul class="nav navbar-nav">
				<c:forEach var="i" items="${mainnav}">
					<li class="site_mainnav" url="${i.url}"><a href="#">${i.name}</a></li>
				</c:forEach>
			</ul>
			<ul class="nav navbar-nav navbar-right">
				<li id="fat-menu" class="dropdown"><a href="#"
					class="dropdown-toggle" data-toggle="dropdown" id="username"> </a>
					<ul class="dropdown-menu">
						<c:if test="${sgambler.issuper == 1}">
							<li><a href="#" id="switchuser">Switch User</a></li>
						</c:if>
						<li><a href="#" id="updatepassword">Update Password</a></li>
						<li><a href="#" id="logout">Logout</a></li>
					</ul></li>
			</ul>
		</div>
		<!--/.nav-collapse -->

	</div>
</div>