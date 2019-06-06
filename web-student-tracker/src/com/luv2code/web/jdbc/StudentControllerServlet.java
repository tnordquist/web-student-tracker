package com.luv2code.web.jdbc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.sun.javadoc.ThrowsTag;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	StudentDBUtil studentDBUtil;

	@Resource(name = "jdbc/web_student_tracker")
	private DataSource dataSource;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.GenericServlet#init()
	 */
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();

		// create student db util ... and pass in the conn pool / datasource
		try {
			studentDBUtil = new StudentDBUtil(dataSource);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			// read the command parameter
			String theCommand = request.getParameter("command");

			// if the command is missing, default to listing students
			if (theCommand == null) {
				theCommand = "LIST";
			}

			switch (theCommand) {
			case "LIST":
				listStudents(request, response);
				break;
			case "LOAD":
				loadStudent(request, response);
				break;
			case "UPDATE":
				updateStudent(request, response);
				break;
			case "DELETE":
				deleteStudent(request,response);
			default:
				listStudents(request, response);
				break;
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
	
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException	{
	
	try {
		// read the command parameter
		String theCommand = request.getParameter("command");
		
		// route to the appropriate method
		switch (theCommand) {
		case "ADD":
			addStudent(request, response);
			break;

		default:
			listStudents(request, response);
		}
		
	} catch (Exception e) {
		throw new ServletException(e);
	}
	
}	

	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		// read student id from the form data
		String theStudentId = request.getParameter("studentId");	
		
		// delete student from the database
		studentDBUtil.deletStudent(theStudentId);
		
		// send the user back to the "list students" page
		listStudents(request, response);
	}

	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// read student info from form data
		int id = Integer.parseInt(request.getParameter("studentId"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");

		// create a new student object
		Student theStudent = new Student(id, firstName, lastName, email);

		// perform update on database
		studentDBUtil.updateStudent(theStudent);

		// send back to the "list students" page
		listStudents(request, response);
	}

	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// read student id from form data
		String theStudentId = request.getParameter("studentId");

		// get student from database (db util)
		Student theStudent = studentDBUtil.getStudent(theStudentId);

		// place student in the request attribute
		request.setAttribute("THE_STUDENT", theStudent);

		// send to jsp page: update-student-form.jsp
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/update-student-form.jsp");
		requestDispatcher.forward(request, response);

	}

	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// read student info from form data
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");

		// create a new student object
		Student theStudent = new Student(firstName, lastName, email);

		// add the student to the database
		studentDBUtil.addStudent(theStudent);

		// send back to main page, the student list
		// SEND AS REDIRECT to avoid multiple-browser reload issue
		// listStudents(request, response); this is code before correction was made
		response.sendRedirect(request.getContextPath() + "/StudentControllerServlet?command=List");

	}

	private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// get students from db util
		List<Student> students = studentDBUtil.getStudent();

		// add students to the request object
		request.setAttribute("STUDENT_LIST", students);

		// send to jsp page (view)
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
		dispatcher.forward(request, response);
	}

}
