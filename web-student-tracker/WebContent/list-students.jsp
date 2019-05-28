<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*, com.luv2code.web.jdbc.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Student Tracker App</title>
<link type="text/css" rel="stylesheet" href="css/style.css">
</head>

<%
	List<Student> theStudents = (List<Student>) request.getAttribute("STUDENT_LIST");
%>
<body>
	<div id="wrapper">
		<div id="header">
			<h2>Foobar University</h2>
		</div>
	</div>
	<div id="container">
		<div id="content">
			<table>
				<tr>
					<th>First Name</th>
					<th>Last Name</th>
					<th>Email</th>
				</tr>

				<%
					for (Student tempStudent : theStudents) {
				%>

				<tr>
					<td><%=tempStudent.getFirstName()%></td>
					<td><%=tempStudent.getLastName()%></td>
					<td><%=tempStudent.getEmail()%></td>
				</tr>

				<%
					}
				%>
			</table>
		</div>
	</div>
</body>
</html>