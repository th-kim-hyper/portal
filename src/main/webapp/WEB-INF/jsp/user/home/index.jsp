<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>하이퍼정보 /WEB-INF/views/jsp/user/home/index.jsp</h1>
<div>현재시각 : ${dt}</div>
props : ${applicationConfig.datasource}

<c:forEach items="${props}" var="item">
	<p>${item.key} : ${item.value}</p>
</c:forEach>
<div>nm : ${nm}</div>
<div>ver : ${ver}</div>
<div>smode : ${smode}</div>
<c:forEach items="${ps}" var="item">
	<p>${item}</p>
</c:forEach>
</body>
</html>