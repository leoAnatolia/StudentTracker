# StudentTracker
This is a Java servlet and jsp based simple web application based on MVC design pattern.
All CRUD(create-read-update-delete) operations can be done.
As a database, MySQL is used and way of connection to database is JDBC. However, it can be replaced with 
an ORM implementation( e.g.Hibernate ).

First, you have to run the web_student_tracker_student.sql script on your MySQL database.

You can directly grab the StudentTracker.war file under /target directory  and deploy it to a servlet container(e.g. Tomcat).
Whenever the servlet container exposes the war files as a set of directories, you have to configure the datasource under 
META-INF/context.xml file to point the correct MySQL database with the necessary credentials for authetication.


