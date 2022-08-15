package fr.epita.quiz.services.data.dao;

import fr.epita.quiz.datamodel.Topic;
import fr.epita.quiz.services.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TopicDBDAO {
    public void create(Topic topic, Integer questionID) throws SQLException, IOException {
        Connection connection = DBConnection.getConnection();
        String createTableQuery = "CREATE TABLE IF NOT EXISTS TOPICS( id SERIAL PRIMARY KEY, name VARCHAR(255), question_id INTEGER REFERENCES QUESTIONS(id))";
        connection.prepareStatement(createTableQuery).execute();
        String insertQuery = "INSERT INTO TOPICS(name, question_id) values (?, ?)";
        PreparedStatement ps = connection.prepareStatement(insertQuery);
        ps.setString(1, topic.getName().toUpperCase());
        ps.setInt(2, questionID);
        ps.execute();
        connection.close();
    }

    public List<Topic> readAll() throws SQLException, IOException {
        Connection connection = DBConnection.getConnection();
        String sqlQuery = "select DISTINCT name from TOPICS";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Topic> topics = new ArrayList<>();
        while (resultSet.next()) {
            String name = resultSet.getString("name");
            topics.add(new Topic(name));
        }
        connection.close();
        return topics;
    }

    public List<Topic> read(Integer questionID) throws SQLException, IOException {
        Connection connection = DBConnection.getConnection();
        String sqlQuery = "SELECT DISTINCT name FROM TOPICS WHERE question_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        preparedStatement.setInt(1, questionID);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Topic> topics = new ArrayList<>();
        while (resultSet.next()) {
            String name = resultSet.getString("name");
            topics.add(new Topic(name));
        }
        connection.close();
        return topics;
    }

    public void update(Topic topic, Integer id) throws SQLException, IOException {
        Connection connection = DBConnection.getConnection();
        String updateQuery = "UPDATE TOPICS SET name = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
        preparedStatement.setString(1, topic.getName().toUpperCase());
        preparedStatement.setInt(2, id);
        preparedStatement.execute();
        connection.close();
    }

    public void delete(Integer id) throws SQLException, IOException {
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM TOPICS WHERE id = ?");
        preparedStatement.setString(1, id.toString());
        preparedStatement.execute();
        connection.close();
    }

    public void deleteWithQuestion(Integer questionID) throws SQLException, IOException {
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM TOPICS WHERE question_id = ?");
        preparedStatement.setInt(1, questionID);
        preparedStatement.execute();
        connection.close();
    }
}
