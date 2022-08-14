package fr.epita.quiz.services.data.dao;

import fr.epita.quiz.datamodel.OpenQuestion;
import fr.epita.quiz.datamodel.Question;
import fr.epita.quiz.services.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OpenQuestionDBDAO {
    public void create(OpenQuestion question) throws SQLException, IOException {
        Connection connection = DBConnection.getConnection();
        String createTableQuery = "CREATE TABLE IF NOT EXISTS OPEN_QUESTION (id INTEGER PRIMARY KEY AUTOINCREMENT, question TEXT, answer TEXT, difficulty INTEGER, topics TEXT)";
        connection.prepareStatement(createTableQuery).execute();
        String insertQuery = "INSERT INTO OPEN_QUESTION (question, answer, difficulty, topics) values (?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(insertQuery);
        ps.setString(1, question.getQuestion());
        ps.setString(2, question.getAnswer());
        ps.setInt(3, question.getDifficulty());
        String topics = String.join(";", question.getTopics());
        ps.setString(4, topics.toUpperCase());
        ps.execute();
        connection.close();
    }

    public List<OpenQuestion> readAll() throws SQLException, IOException {
        Connection connection = DBConnection.getConnection();
        String sqlQuery = "select question, answer, difficulty, topics from OPEN_QUESTION";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        List<OpenQuestion> questions = getQuestions(preparedStatement);
        connection.close();
        return questions;
    }

    public List<OpenQuestion> read(String[] topics, Integer limit) throws SQLException, IOException {
        Connection connection = DBConnection.getConnection();
        String sqlQuery = "select question, answer, difficulty, topics from OPEN_QUESTION";
        if (topics.length > 0) {
            sqlQuery += " where topics like '%" + topics[0].toUpperCase() + "%'";
            for (int i = 1; i < topics.length; i++) {
                sqlQuery += " or topics like '%" + topics[i].toUpperCase() + "%'";
            }
        }
        sqlQuery += " limit ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        if (limit != null) {
            preparedStatement.setInt(1, limit);
        } else {
            preparedStatement.setInt(1, 10);
        }
        List<OpenQuestion> questions = getQuestions(preparedStatement);
        connection.close();
        return questions;
    }

    private List<OpenQuestion> getQuestions(PreparedStatement preparedStatement) throws SQLException {
        ResultSet resultSet = preparedStatement.executeQuery();
        List<OpenQuestion> questions = new ArrayList<>();
        while (resultSet.next()){
            String question = resultSet.getString("question");
            String answer = resultSet.getString("answer");
            int difficulty = resultSet.getInt("difficulty");
            String topics = resultSet.getString("topics");
            String[] topicsArray = topics.toUpperCase().split(";");
            questions.add(new OpenQuestion(question, topicsArray, difficulty, answer));
        }
        return questions;
    }

    public void update(OpenQuestion question, Integer id) throws SQLException, IOException {
        Connection connection = DBConnection.getConnection();
        String updateQuery = "UPDATE OPEN_QUESTION SET question = ?, answer = ?, difficulty = ?, topics = ? WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(updateQuery);
        ps.setString(1, question.getQuestion());
        ps.setString(2, question.getAnswer());
        ps.setInt(3, question.getDifficulty());
        String topics = String.join(";", question.getTopics());
        ps.setString(4, topics.toUpperCase());
        ps.setInt(5, id);
        ps.execute();
        connection.close();
    }

    public void delete(Integer id) throws SQLException, IOException {
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM OPEN_QUESTION WHERE id = ?");
        preparedStatement.setInt(1, id);
        preparedStatement.execute();
        connection.close();
    }
}
