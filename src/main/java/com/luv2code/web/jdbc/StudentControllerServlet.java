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

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private StudentDbUtil     studentDbUtil;

    @Resource(name = "jdbc/web_student_tracker")
    DataSource                dataSource;

    @Override
    public void init() throws ServletException {
        super.init();
        try {

            // create our student db util and pass datasource as parameter
            this.studentDbUtil = new StudentDbUtil(this.dataSource);
        } catch (final Exception e) {
            throw new ServletException(e);
        }

    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

        // read the command parameter

        String theCommand = request.getParameter("command");

        if (theCommand == null) {
            theCommand = "LIST";
        }
        try {
            // route the request to appropriate method
            switch (theCommand) {
                case "LIST":
                    // list the students ... in MVC fashion
                    this.listStudents(request, response);

                    break;
                case "ADD":

                    this.addStudent(request, response);
                    break;

                case "LOAD":

                    this.loadStudent(request, response);
                    break;

                case "UPDATE":

                    this.updateStudent(request, response);
                    break;

                case "DELETE":

                    this.deleteStudent(request, response);
                    break;
                default:
                    break;
            }

        } catch (final Exception e) {
            throw new ServletException(e);
        }
    }

    private void updateStudent(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        // read student info from form data

        final int id = Integer.parseInt(request.getParameter("studentId"));
        final String firstName = request.getParameter("firstName");
        final String lastName = request.getParameter("lastName");
        final String email = request.getParameter("email");

        // create a student object

        final Student theStudent = new Student(id, firstName, lastName, email);

        // update student info at the database

        this.studentDbUtil.updateStudent(theStudent);

        // list the students again

        this.listStudents(request, response);

    }

    private void loadStudent(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        // read student id from form data
        final String theStudentId = request.getParameter("studentId");

        // get student from database
        final Student theStudent = this.studentDbUtil.getStudent(theStudentId);

        // place student in request attribute
        request.setAttribute("THE_STUDENT", theStudent);

        // send to jsp page: update-student-form.jsp
        final RequestDispatcher dispatcher = request.getRequestDispatcher("/update-student-form.jsp");
        dispatcher.forward(request, response);
    }

    private void addStudent(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        // read student data from form
        final String firstName = request.getParameter("firstName");
        final String lastName = request.getParameter("lastName");
        final String email = request.getParameter("email");

        // create a student object with form data
        final Student theStudent = new Student(firstName, lastName, email);

        // add the student to the database
        this.studentDbUtil.addStudent(theStudent);

        // send back to main page (student list)
        this.listStudents(request, response);

    }

    private void listStudents(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        // get the students by db util
        final List<Student> studentList = this.studentDbUtil.getStudents();

        // set them as an attribute to request
        request.setAttribute("STUDENT_LIST", studentList);

        // send to JSP page(view)

        // Step 1: get request dispatcher
        final RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");

        // Step 2:forward the request to JSP
        dispatcher.forward(request, response);

    }

    private void deleteStudent(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        // read student id from form data (request)
        final String theStudentId = request.getParameter("studentId");
        // delete student from database
        this.studentDbUtil.deleteStudent(theStudentId);

        // send back to "list students" page
        this.listStudents(request, response);
    }

}
