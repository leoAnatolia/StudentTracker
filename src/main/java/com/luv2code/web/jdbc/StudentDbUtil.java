package com.luv2code.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class StudentDbUtil {

    DataSource dataSource;

    public StudentDbUtil(final DataSource theDataSource) {
        this.dataSource = theDataSource;
    }

    public List<Student> getStudents() throws Exception {
        final List<Student> students = new ArrayList<Student>();

        Connection myConnection = null;
        Statement myStatement = null;
        ResultSet myRS = null;

        try {
            // get a connection
            myConnection = this.dataSource.getConnection();

            // create sql statement
            final String sql = "select * from student order by last_name";
            myStatement = myConnection.createStatement();

            // execute query
            myRS = myStatement.executeQuery(sql);

            // process resultset

            while (myRS.next()) {

                // retrieve data from resultset row
                final int id = myRS.getInt("id");
                final String fName = myRS.getString("first_name");
                final String lName = myRS.getString("last_name");
                final String email = myRS.getString("email");

                // create student object
                final Student tempStudent = new Student(id, fName, lName, email);

                // add to student list

                students.add(tempStudent);

            }

        }

        finally {
            // close JDBC objects

            this.close(myConnection, myStatement, myRS);
        }

        return students;
    }

    private void close(final Connection myConnection, final Statement myStatement, final ResultSet myRS) {

        try {
            if (myRS != null) {
                myRS.close();
            }

            if (myStatement != null) {
                myStatement.close();
            }

            if (myConnection != null) {
                myConnection.close(); // puts connection back into db pool
            }

        } catch (final Exception e) {
            e.printStackTrace();
        }

    }

    public void addStudent(final Student theStudent) throws SQLException {

        Connection myConn = null;
        PreparedStatement myStmt = null;

        try {
            myConn = this.dataSource.getConnection();
            // create sql for insert
            final String sql = "insert into student" + "(first_name,last_name,email)" + "values(?,?,?)";

            myStmt = myConn.prepareStatement(sql);
            // set the parameter values for student

            myStmt.setString(1, theStudent.getFirstName());
            myStmt.setString(2, theStudent.getLastName());
            myStmt.setString(3, theStudent.getEmail());

            // execute sql insert

            myStmt.execute();
        } finally {
            // clean up JDBC objects

            this.close(myConn, myStmt, null);
        }
    }

    public Student getStudent(final String theStudentId) throws Exception {
        Student theStudent = null;

        Connection myConn = null;
        PreparedStatement myPS = null;
        ResultSet rs = null;

        try {

            final int studentId = Integer.parseInt(theStudentId);

            myConn = this.dataSource.getConnection();

            final String sql = "select * from student where id=?";

            myPS = myConn.prepareStatement(sql);

            myPS.setInt(1, studentId);

            rs = myPS.executeQuery();

            if (rs.next()) {
                final String firstName = rs.getString("first_name");
                final String lastName = rs.getString("last_name");
                final String email = rs.getString("email");

                theStudent = new Student(studentId, firstName, lastName, email);

            } else {
                throw new Exception("Couldn't find studentId:" + studentId);
            }

        } catch (final SQLException e) {
            // VASTODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            // close JDBC objects
            this.close(myConn, myPS, rs);
        }
        return theStudent;

    }

    public void updateStudent(final Student theStudent) throws Exception {

        Connection myConn = null;
        PreparedStatement myStmt = null;

        try {
            // get db connection
            myConn = this.dataSource.getConnection();

            // create SQL statement

            final String sql = "update student set first_name=?, last_name=?, email=? where id=?";

            // prepare statement
            myStmt = myConn.prepareStatement(sql);

            // set params
            myStmt.setString(1, theStudent.getFirstName());
            myStmt.setString(2, theStudent.getLastName());
            myStmt.setString(3, theStudent.getEmail());
            myStmt.setLong(4, theStudent.getId());

            // execute SQL statement
            myStmt.execute();

        } finally {
            this.close(myConn, myStmt, null);
        }
    }

    public void deleteStudent(final String theStudentId) throws Exception {
        // convert studentId to integer
        final int studentId = Integer.parseInt(theStudentId);

        Connection myConn = null;
        PreparedStatement myStmt = null;

        try {

            // get connection to database
            myConn = this.dataSource.getConnection();

            // create sql to delete the student
            final String deleteSQL = "delete from student where id=?";

            // prepare statement
            myStmt = myConn.prepareStatement(deleteSQL);

            // set parameters
            myStmt.setInt(1, studentId);

            // execute statement
            myStmt.execute();

        } finally {
            this.close(myConn, myStmt, null);
        }

    }
}
