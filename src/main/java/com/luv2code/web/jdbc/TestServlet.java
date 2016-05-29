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

    // Define data source/connection pool for Resource Injection
    @Resource(name = "jdbc/web_student_tracker")
    private DataSource        dataSource;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Step 1: Get the printwriter object
        PrintWriter out = response.getWriter();
        response.setContentType("text/plain");

        // Step 2: Get a connection to database
        Connection myConn = null;
        Statement myStmt = null;
        ResultSet myRs = null;

        try {

            myConn = this.dataSource.getConnection();
            // Step 3: Create a new SQL statement
            String sql = "SELECT * FROM STUDENT";
            myStmt = myConn.createStatement();

            // Step 4: Execute the SQL statements.
            myRs = myStmt.executeQuery(sql);

            // Step 5: Process the result set.
            while (myRs.next()) {
                out.println(myRs.getString("email"));
            }

        }

        catch (Exception exc) {

            exc.printStackTrace();

        }

    }

}
