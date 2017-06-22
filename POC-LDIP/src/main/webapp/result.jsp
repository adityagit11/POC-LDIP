<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>POC-LDIP</title>
</head>
<body>
<%
	String videoid = (String)request.getAttribute("videoid");
	out.print("<iframe width=\"300\" height=\"350\" src=\"https://www.youtube.com/embed/"+ videoid +"\" frameborder=\"0\" allowfullscreen></iframe>");
%>
</body>
</html>