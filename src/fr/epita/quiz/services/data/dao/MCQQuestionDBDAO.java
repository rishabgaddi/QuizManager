package fr.epita.quiz.services.data.dao;

import fr.epita.quiz.datamodel.MCQChoice;
import fr.epita.quiz.datamodel.MCQQuestion;
import fr.epita.quiz.datamodel.QuestionType;
import fr.epita.quiz.datamodel.Topic;
import fr.epita.quiz.services.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MCQQuestionDBDAO {
    public void create(MCQQuestion question) throws SQLException, IOException {
        Connection connection = DBConnection.getConnection();
        String createTableQuery = "CREATE TABLE IF NOT EXISTS QUESTIONS (id INTEGER PRIMARY KEY AUTOINCREMENT, type VARCHAR(255), question TEXT, answer TEXT, difficulty INTEGER, choices TEXT)";
        connection.prepareStatement(createTableQuery).execute();
        String insertQuery = "INSERT INTO QUESTIONS (type, question, answer, difficulty, choices) values (?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);
        ps.setString(1, question.getType().toString());
        ps.setString(2, question.getQuestion());
        List<MCQChoice> choices = question.getChoices();
        String choicesString = "";
        String answersString = "";
        for (MCQChoice choice : choices) {
            choicesString += choice.getChoice() + ";";
            if (choice.isValid()) {
                answersString += choice.getChoice() + ";";
            }
        }
        ps.setString(3, answersString);
        ps.setInt(4, question.getDifficulty());
        ps.setString(5, choicesString);
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

    public List<MCQQuestion> readAll() throws SQLException, IOException {
        Connection connection = DBConnection.getConnection();
        String sqlQuery = "SELECT id, question, answer, difficulty, choices FROM QUESTIONS WHERE type = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        preparedStatement.setString(1, QuestionType.MULTIPLE_CHOICE.toString());
        List<MCQQuestion> questions = getQuestions(preparedStatement);
        connection.close();
        return questions;
    }

    public List<MCQQuestion> read(List<Topic> topics, Integer limit) throws SQLException, IOException {
        Connection connection = DBConnection.getConnection();
        String sqlQuery = "SELECT DISTINCT q.id, q.question, q.answer, q.difficulty, q.choices FROM QUESTIONS q JOIN TOPICS t ON q.id = t.question_id WHERE q.type = ?";
        sqlQuery += " AND t.name IN (SELECT name FROM TOPICS WHERE name IN (?)) ORDER BY RANDOM() LIMIT ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        preparedStatement.setString(1, QuestionType.MULTIPLE_CHOICE.toString());
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
        List<MCQQuestion> questions = getQuestions(preparedStatement);
        connection.close();
        return questions;
    }

    private List<MCQQuestion> getQuestions(PreparedStatement preparedStatement) throws SQLException, IOException {
        ResultSet resultSet = preparedStatement.executeQuery();
        List<MCQQuestion> questions = new ArrayList<>();
        TopicDBDAO topicDBDAO = new TopicDBDAO();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String question = resultSet.getString("question");
            int difficulty = resultSet.getInt("difficulty");
            String choices = resultSet.getString("choices");
            String answers = resultSet.getString("answers");
            List<MCQChoice> choicesList = new ArrayList<>();
            List<String> choicesArray = new ArrayList<>();
            choicesArray.addAll(Arrays.asList(choices.split(";")));
            List<String> answersArray = new ArrayList<>();
            answersArray.addAll(Arrays.asList(answers.split(";")));
            for (int i = 0; i < choicesArray.size(); i++) {
                boolean isValid = answersArray.contains(choicesArray.get(i));
                MCQChoice choice = new MCQChoice(choicesArray.get(i), isValid);
                choicesList.add(choice);
            }
            List<Topic> topics = topicDBDAO.read(id);
            questions.add(new MCQQuestion(question, topics, difficulty, choicesList));
        }
        return questions;
    }

    public void update(MCQQuestion question, Integer id) throws SQLException, IOException {
        Connection connection = DBConnection.getConnection();
        String updateQuery = "UPDATE QUESTIONS SET type = ?, question = ?, answer = ?, difficulty = ?, choices = ? WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(updateQuery);
        ps.setString(1, question.getType().toString());
        ps.setString(2, question.getQuestion());
        List<MCQChoice> choices = question.getChoices();
        String choicesString = "";
        String answersString = "";
        for (MCQChoice choice : choices) {
            choicesString += choice.getChoice() + ";";
            if (choice.isValid()) {
                answersString += choice.getChoice() + ";";
            }
        }
        ps.setString(3, answersString);
        ps.setInt(4, question.getDifficulty());
        ps.setString(5, choicesString);
        ps.setInt(6, id);
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
        PreparedStatement ps = connection.prepareStatement(deleteQuery);
        ps.setInt(1, id);
        ps.execute();
        TopicDBDAO topicDBDAO = new TopicDBDAO();
        topicDBDAO.deleteWithQuestion(id);
        connection.close();
    }
}

