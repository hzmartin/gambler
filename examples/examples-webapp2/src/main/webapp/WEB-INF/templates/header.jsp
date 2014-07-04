<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="navbar navbar-default navbar-fixed-top" role="navigation">
	<div class="container">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target=".navbar-collapse">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="/index.do">Gambler<c:if test="${msmode ne ''}">
					<span class="badge"><c:out value="${msmode}" /></span>
				</c:if></a>
		</div>
		<div class="navbar-collapse collapse">
			<ul class="nav navbar-nav">
				<c:forEach var="i" items="${mainnav}">
					<li><a href="${i.url}">${i.name}</a></li>
				</c:forEach>
			</ul>
			<ul class="nav navbar-nav navbar-right">
				<li><div style="margin-top: 5px;"></div> <img id="uicon"
					src="/assets/ico/apple-touch-icon-57-precomposed.png"
					style="width: 40px; height: 40px; border-radius: 50%;" /></li>
				<li id="fat-menu" class="dropdown"><a href="#"
					class="dropdown-toggle" data-toggle="dropdown" id="username"> </a>
					<ul class="dropdown-menu">
						<li><a href="#" id="logout">Logout</a></li>
					</ul></li>
			</ul>
		</div>
		<!--/.nav-collapse -->

	</div>
</div>