package fr.epita.quiz.tests;

import fr.epita.quiz.services.data.dao.QuestionDBDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public class TestQuestionDBDAO {
    public static void main(String[] args) throws SQLException, IOException {
        QuestionDBDAO dao = new QuestionDBDAO();
        Map<Integer, String> questions = dao.getQuestionsWithId();
        for (Map.Entry<Integer, String> entry : questions.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}
