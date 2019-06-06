package com.luv2code.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.eclipse.jdt.internal.compiler.flow.FinallyFlowContext;

public class StudentDBUtil {

	private DataSource dataSource;

	public StudentDBUtil(DataSource theDataSource) {

		dataSource = theDataSource;
	}

	public List<Student> getStudent() throws Exception {

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

			// process the result recordset
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

	public Student getStudent(String theStudentId) throws Exception {

		Student theStudent = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int studentId;

		try {

			// convert student id to an int˙
			studentId = Integer.parseInt(theStudentId);

			// get connection to database
			connection = dataSource.getConnection();

			// create sql statement to get selected student
			String sql = "select * from student where id=?";

			// create prepared statement
			preparedStatement = connection.prepareStatement(sql);

			// set params
			preparedStatement.setInt(1, studentId);

			// generate the recordset
			resultSet = preparedStatement.executeQuery();

			// retrieve data from result set row
			if (resultSet.next()) {
				String firstName = resultSet.getString("first_name");
				String lastName = resultSet.getString("last_name");
				String email = resultSet.getString("email");

				// use the studentId during construction of the new student object
				theStudent = new Student(studentId, firstName, lastName, email);

			} else {
				throw new Exception("Could not find that studentId: " + studentId);
			}
			return theStudent;
		} finally {
			// close jdbc objects
			close(connection, preparedStatement, resultSet);
		}

	}

	public void updateStudent(Student theStudent) throws Exception {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			// get db connection
			connection = dataSource.getConnection();
			// create SQL update statement
			String sql = "update student " + "set first_name=?, last_name=?, email=? " + "where id=?";
			// prepare statement
			preparedStatement = connection.prepareStatement(sql);
			// set params
			preparedStatement.setString(1, theStudent.getFirstName());
			preparedStatement.setString(2, theStudent.getLastName());
			preparedStatement.setString(3, theStudent.getEmail());
			preparedStatement.setInt(4, theStudent.getId());
			// execute sql statement
			preparedStatement.execute();
		} finally {
			// clean up jdbc objects
			close(connection, preparedStatement, null);
		}

	}

	public void deletStudent(String theStudentId) throws Exception{
		
		Connection connection = null;
		PreparedStatement pStatement = null;
		
		try {
			
			// convert student id to int
			int studentId = Integer.parseInt(theStudentId);
			
			// get connection to database
			connection = dataSource.getConnection();
			
			// create sql to delete the student
			String sql = "delete from student where id=?";
			
			// prepared statement to use the sql command
			pStatement = connection.prepareStatement(sql);
			
			// set the params
			pStatement.setInt(1, studentId);
			
			// execute the sql statement
			pStatement.execute();
			
		} finally {
			// clean up jdbc code
			close(connection, pStatement, null);
		}
		// 
		
	}

}
