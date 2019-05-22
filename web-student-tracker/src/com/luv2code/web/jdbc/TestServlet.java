package com.luv2code.web.jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// Define data source / connection pool for Resource Injection
	@Resource(name = "jdbc/web_student_tracker")
	private DataSource dataSource;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// set up a Printwriter
		PrintWriter out = response.getWriter();
		response.setContentType("text/plain");
		
		// Get a connnection to database
		Connection myConnection = null;
		Statement myStatement = null;
		ResultSet myResultSet = null;
		
		try {
			myConnection = dataSource.getConnection();
			
			// create a sql statement
			String sql = "select * from student";
			myStatement = myConnection.createStatement();
			
			// execute sql query
			myResultSet = myStatement.executeQuery(sql);
			
			// process the result set
			while (myResultSet.next()) {
				String email = (myResultSet).getString("email");
				out.println(email);
				
			}
			
		} catch (Exception e) {
			System.out.print(e);
		}

	}

}
