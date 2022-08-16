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
    public Integer create(MCQQuestion question) throws SQLException, IOException {
        Connection connection = DBConnection.getConnection();
        String createTableQuery = "CREATE TABLE IF NOT EXISTS QUESTIONS (id SERIAL PRIMARY KEY, type VARCHAR(255), question TEXT, answer TEXT, difficulty INTEGER, choices TEXT)";
        connection.prepareStatement(createTableQuery).execute();
        String insertQuery = "INSERT INTO QUESTIONS (type, question, answer, difficulty, choices) values (?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);
        ps.setString(1, question.getType().toString());
        ps.setString(2, question.getQuestion());
        List<MCQChoice> choices = question.getChoices();
        StringBuilder choicesString = new StringBuilder();
        StringBuilder answersString = new StringBuilder();
        for (MCQChoice choice : choices) {
            choicesString.append(choice.getChoice()).append(";");
            if (choice.isValid()) {
                answersString.append(choice.getChoice()).append(";");
            }
        }
        ps.setString(3, answersString.toString());
        ps.setInt(4, question.getDifficulty());
        ps.setString(5, choicesString.toString());
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
        return id;
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
        StringBuilder sqlQuery = new StringBuilder("SELECT q.id, q.question, q.answer, q.difficulty, q.choices, MIN(RANDOM()) AS r" + " " +
                "FROM QUESTIONS q JOIN TOPICS t ON q.id = t.question_id WHERE q.type = ?");
        sqlQuery.append(" AND t.name IN (");
        if (topics.size() > 0) {
            sqlQuery.append("'").append(topics.get(0).getName().toUpperCase()).append("'");
            for (int i = 1; i < topics.size(); i++) {
                sqlQuery.append(", '").append(topics.get(i).getName().toUpperCase()).append("'");
            }
        }
        if (limit != null) {
            sqlQuery.append(") GROUP BY q.id ORDER BY r LIMIT ? ");
        } else {
            sqlQuery.append(") GROUP BY q.id ORDER BY r");
        }
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery.toString());
        preparedStatement.setString(1, QuestionType.MULTIPLE_CHOICE.toString());
        if (limit != null) {
            preparedStatement.setInt(2, limit);
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
            String answers = resultSet.getString("answer");
            List<MCQChoice> choicesList = new ArrayList<>();
            List<String> choicesArray = new ArrayList<>();
            choicesArray.addAll(Arrays.asList(choices.split(";")));
            List<String> answersArray = new ArrayList<>();
            answersArray.addAll(Arrays.asList(answers.split(";")));
            for (String s : choicesArray) {
                boolean isValid = answersArray.contains(s);
                MCQChoice choice = new MCQChoice(s, isValid);
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
        StringBuilder choicesString = new StringBuilder();
        StringBuilder answersString = new StringBuilder();
        for (MCQChoice choice : choices) {
            choicesString.append(choice.getChoice()).append(";");
            if (choice.isValid()) {
                answersString.append(choice.getChoice()).append(";");
            }
        }
        ps.setString(3, answersString.toString());
        ps.setInt(4, question.getDifficulty());
        ps.setString(5, choicesString.toString());
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
        TopicDBDAO topicDBDAO = new TopicDBDAO();
        topicDBDAO.deleteWithQuestion(id);
        Connection connection = DBConnection.getConnection();
        String deleteQuery = "DELETE FROM QUESTIONS WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(deleteQuery);
        ps.setInt(1, id);
        ps.execute();
        connection.close();
    }
}

