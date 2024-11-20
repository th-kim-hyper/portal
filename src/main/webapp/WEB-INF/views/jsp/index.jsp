<%@page import="java.util.Date"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<h1>하이퍼정보 /WEB-INF/views/jsp/index.jsp</h1>
<div>현재시각 : <%= new Date() %></div>
<table>
	<tr>
		<td style="border:solid 1px black">${props.name}</td>
		<td style="border:solid 1px black">${props.version}</td>
		<td style="border:solid 1px black">${props.storageMode}</td>
		<td style="border:solid 1px black">${props.datasource}</td>
		<td style="border:solid 1px black">${props.ext.enable}</td>	
	</tr>
</table>
</body>
</html>