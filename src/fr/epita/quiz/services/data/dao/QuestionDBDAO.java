package fr.epita.quiz.services.data.dao;

import fr.epita.quiz.services.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class QuestionDBDAO {
    public Map<Integer, String> getQuestionsWithId() throws SQLException, IOException {
        Connection connection = DBConnection.getConnection();
        String sqlQuery = "SELECT id, question FROM QUESTIONS ORDER BY id ASC";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        ResultSet resultSet = preparedStatement.executeQuery();
        Map<Integer, String> questions = new HashMap<>();
        while (resultSet.next()) {
            Integer id = resultSet.getInt("id");
            String question = resultSet.getString("question");
            questions.put(id, question);
        }
        connection.close();
        return questions;
    }
}
