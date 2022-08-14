package fr.epita.quiz.services.data.dao;

import fr.epita.quiz.datamodel.MCQChoice;
import fr.epita.quiz.datamodel.MCQQuestion;
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
        String createTableQuery = "CREATE TABLE IF NOT EXISTS MCQ_QUESTION (id INTEGER PRIMARY KEY AUTOINCREMENT, question TEXT, difficulty INTEGER, topics TEXT, choices TEXT, answers TEXT)";
        connection.prepareStatement(createTableQuery).execute();
        String insertQuery = "INSERT INTO MCQ_QUESTION (question, difficulty, topics, choices, answers) values (?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(insertQuery);
        ps.setString(1, question.getQuestion());
        ps.setInt(2, question.getDifficulty());
        String topics = String.join(";", question.getTopics());
        ps.setString(3, topics.toUpperCase());
        List<MCQChoice> choices = question.getChoices();
        String choicesString = "";
        String answersString = "";
        for (MCQChoice choice : choices) {
            choicesString += choice.getChoice() + ";";
            if (choice.isValid()) {
                answersString += choice.getChoice() + ";";
            }
        }
        ps.setString(4, choicesString);
        ps.setString(5, answersString);
        ps.execute();
        connection.close();
    }

    public List<MCQQuestion> readAll() throws SQLException, IOException {
        Connection connection = DBConnection.getConnection();
        String sqlQuery = "select question, difficulty, topics, choices, answers from MCQ_QUESTION";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        List<MCQQuestion> questions = getQuestions(preparedStatement);
        connection.close();
        return questions;
    }

    public List<MCQQuestion> read(String[] topics, Integer limit) throws SQLException, IOException {
        Connection connection = DBConnection.getConnection();
        String sqlQuery = "select question, difficulty, topics, choices, answers from MCQ_QUESTION";
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
        List<MCQQuestion> questions = getQuestions(preparedStatement);
        connection.close();
        return questions;
    }

    private List<MCQQuestion> getQuestions(PreparedStatement preparedStatement) throws SQLException, IOException {
        ResultSet resultSet = preparedStatement.executeQuery();
        List<MCQQuestion> questions = new ArrayList<>();
        while (resultSet.next()){
            String question = resultSet.getString("question");
            int difficulty = resultSet.getInt("difficulty");
            String topics = resultSet.getString("topics");
            String choices = resultSet.getString("choices");
            String answers = resultSet.getString("answers");
            String [] topicsArray = topics.toUpperCase().split(";");
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
            questions.add(new MCQQuestion(question, topicsArray, difficulty, choicesList));
        }
        return questions;
    }

    public void update(MCQQuestion question, Integer id) throws SQLException, IOException {
        Connection connection = DBConnection.getConnection();
        String updateQuery = "UPDATE MCQ_QUESTION SET question = ?, difficulty = ?, topics = ?, choices = ?, answers = ? WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(updateQuery);
        ps.setString(1, question.getQuestion());
        ps.setInt(2, question.getDifficulty());
        String topics = String.join(";", question.getTopics());
        ps.setString(3, topics.toUpperCase());
        List<MCQChoice> choices = question.getChoices();
        String choicesString = "";
        String answersString = "";
        for (MCQChoice choice : choices) {
            choicesString += choice.getChoice() + ";";
            if (choice.isValid()) {
                answersString += choice.getChoice() + ";";
            }
        }
        ps.setString(4, choicesString);
        ps.setString(5, answersString);
        ps.setInt(6, id);
        ps.execute();
        connection.close();
    }

    public void delete(Integer id) throws SQLException, IOException {
        Connection connection = DBConnection.getConnection();
        String deleteQuery = "DELETE FROM MCQ_QUESTION WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(deleteQuery);
        ps.setInt(1, id);
        ps.execute();
        connection.close();
    }
}

