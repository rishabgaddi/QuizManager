package fr.epita.quiz.services.data.dao;

import fr.epita.quiz.datamodel.Student;
import fr.epita.quiz.services.DBConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDBDAO {
    public void create(Student student) throws SQLException, IOException {
        Connection connection = DBConnection.getConnection();
        String createTableQuery = "CREATE TABLE IF NOT EXISTS STUDENTS( id varchar(30), studentName varchar(255))";
        connection.prepareStatement(createTableQuery).execute();
        String insertQuery = "INSERT INTO STUDENTS(id, studentName) values (?, ?)";
        PreparedStatement ps = connection.prepareStatement(insertQuery);
        ps.setString(1, student.getId());
        ps.setString(2, student.getName());
        ps.execute();
        connection.close();
    }

    public List<Student> readAll() throws SQLException, IOException {
        Connection connection = DBConnection.getConnection();

        String sqlQuery = "select id, studentName from STUDENTS";

        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        ResultSet resultSet = preparedStatement.executeQuery();

        List<Student> students = new ArrayList<>();
        while (resultSet.next()){
            String name = resultSet.getString("studentName");
            String id = resultSet.getString("id");
            students.add(new Student(id, name));
        }
        connection.close();

        return students;
    }

    public void delete(Student student) throws SQLException, IOException {
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM STUDENTS WHERE STUDENTS.id = ?");
        preparedStatement.setString(1, student.getId());
        preparedStatement.execute();
        connection.close();
    }
}
