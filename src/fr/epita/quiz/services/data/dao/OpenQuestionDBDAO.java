package fr.epita.quiz.services.data.dao;

import fr.epita.quiz.datamodel.OpenQuestion;
import fr.epita.quiz.datamodel.Question;
import fr.epita.quiz.datamodel.QuestionType;
import fr.epita.quiz.datamodel.Topic;
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
        String createTableQuery = "CREATE TABLE IF NOT EXISTS QUESTIONS (id INTEGER PRIMARY KEY AUTOINCREMENT, type VARCHAR(255), question TEXT, answer TEXT, difficulty INTEGER, choices TEXT)";
        connection.prepareStatement(createTableQuery).execute();
        String insertQuery = "INSERT INTO QUESTIONS (type, question, answer, difficulty) values (?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);
        ps.setString(1, question.getType().toString());
        ps.setString(2, question.getQuestion());
        ps.setString(3, question.getAnswer());
        ps.setInt(4, question.getDifficulty());
        ps.execute();
        ResultSet rs = ps.getGeneratedKeys();
        int id = 0;
        if (rs.next()) {
            id = rs.getInt(1);
        }
        TopicDBDAO topicDBDAO = new TopicDBDAO();
        for (Topic topic : question.getTopics()) {
            topicDBDAO.create(topic, id);
        }
        connection.close();
    }

    public List<OpenQuestion> readAll() throws SQLException, IOException {
        Connection connection = DBConnection.getConnection();
        String sqlQuery = "SELECT id, question, answer, difficulty FROM QUESTIONS WHERE type = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        preparedStatement.setString(1, QuestionType.OPEN.toString());
        List<OpenQuestion> questions = getQuestions(preparedStatement);
        connection.close();
        return questions;
    }

    public List<OpenQuestion> read(List<Topic> topics, Integer limit) throws SQLException, IOException {
        Connection connection = DBConnection.getConnection();
        String sqlQuery = "SELECT DISTINCT q.id, q.question, q.answer, q.difficulty FROM QUESTIONS q JOIN TOPICS t ON q.id = t.question_id WHERE q.type = ?";
        sqlQuery += " AND t.name IN (SELECT name FROM TOPICS WHERE name IN (?)) ORDER BY RANDOM() LIMIT ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        preparedStatement.setString(1, QuestionType.OPEN.toString());
        String topicsString = "";
        for (Topic topic : topics) {
            topicsString += topic.getName() + ",";
        }
        topicsString = topicsString.substring(0, topicsString.length() - 1);
        preparedStatement.setString(2, topicsString);
        if (limit != null) {
            preparedStatement.setInt(3, limit);
        } else {
            preparedStatement.setInt(3, 10);
        }
        List<OpenQuestion> questions = getQuestions(preparedStatement);
        connection.close();
        return questions;
    }

    private List<OpenQuestion> getQuestions(PreparedStatement preparedStatement) throws SQLException, IOException {
        ResultSet resultSet = preparedStatement.executeQuery();
        List<OpenQuestion> questions = new ArrayList<>();
        TopicDBDAO topicDBDAO = new TopicDBDAO();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String question = resultSet.getString("question");
            String answer = resultSet.getString("answer");
            int difficulty = resultSet.getInt("difficulty");
            List<Topic> topics = topicDBDAO.read(id);
            questions.add(new OpenQuestion(question, topics, difficulty, answer));
        }
        return questions;
    }

    public void update(OpenQuestion question, Integer id) throws SQLException, IOException {
        Connection connection = DBConnection.getConnection();
        String updateQuery = "UPDATE QUESTIONS SET type = ?, question = ?, answer = ?, difficulty = ? WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(updateQuery);
        ps.setString(1, question.getType().toString());
        ps.setString(2, question.getQuestion());
        ps.setString(3, question.getAnswer());
        ps.setInt(4, question.getDifficulty());
        ps.setInt(5, id);
        ps.execute();
        TopicDBDAO topicDBDAO = new TopicDBDAO();
        topicDBDAO.deleteWithQuestion(id);
        for (Topic topic : question.getTopics()) {
            topicDBDAO.create(topic, id);
        }
        connection.close();
    }

    public void delete(Integer id) throws SQLException, IOException {
        Connection connection = DBConnection.getConnection();
        String deleteQuery = "DELETE FROM QUESTIONS WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
        preparedStatement.setInt(1, id);
        preparedStatement.execute();
        TopicDBDAO topicDBDAO = new TopicDBDAO();
        topicDBDAO.deleteWithQuestion(id);
        connection.close();
    }
}
