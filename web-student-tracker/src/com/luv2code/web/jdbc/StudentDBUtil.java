package com.luv2code.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class StudentDBUtil {

	private DataSource dataSource;

	public StudentDBUtil(DataSource theDataSource) {

		dataSource = theDataSource;
	}

	public List<Student> getStudents() throws Exception {

		List<Student> students = new ArrayList<>();

		Connection myConnection = null;
		Statement myStatement = null;
		ResultSet myResultSet = null;

		try {

			// Get a connnection to database
			myConnection = dataSource.getConnection();

			// create a sql statement
			String sql = "select * from student order by last_name";
			myStatement = myConnection.createStatement();

			// execute sql query
			myResultSet = myStatement.executeQuery(sql);

			// process the result set
			while (myResultSet.next()) {

				// retrieve data from result set row
				int id = myResultSet.getInt("id");
				String firstName = myResultSet.getString("first_name");
				String lastName = myResultSet.getString("last_name");
				String email = myResultSet.getString("email");

				// create new student object
				Student tempStudent = new Student(id, firstName, lastName, email);

				// add it to the list of students
				students.add(tempStudent);
			}
			// close jdbc objects
			return students;
		} finally {
			close(myConnection, myStatement, myResultSet);
		}

	}

	private void close(Connection myConnection, Statement myStatement, ResultSet myResultSet) {

		try {
			if (myResultSet != null) {
				myResultSet.close();
			}
			if (myStatement != null) {
				myStatement.close();
			}
			if (myConnection != null) {
				myConnection.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void addStudent(Student theStudent) throws Exception {

		Connection myConnection = null;
		PreparedStatement myStatement = null;

		try {
			// get database connection
			myConnection = dataSource.getConnection();

			// create sql for insert
			String sql = "insert into student " + "(first_name, last_name, email)" + "values(?,?,?)";

			myStatement = myConnection.prepareStatement(sql);

			// set the param values for the student
			myStatement.setString(1, theStudent.getFirstName());
			myStatement.setString(2, theStudent.getLastName());
			myStatement.setString(3, theStudent.getEmail());

			// execute sql statement
			myStatement.execute();

		} finally {
			// take care of JDBC objects
			close(myConnection, myStatement, null);
		}

	}

}
