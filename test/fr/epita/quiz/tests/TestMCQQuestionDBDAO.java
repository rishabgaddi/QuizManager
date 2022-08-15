package fr.epita.quiz.tests;

import fr.epita.quiz.datamodel.MCQChoice;
import fr.epita.quiz.datamodel.MCQQuestion;
import fr.epita.quiz.datamodel.Topic;
import fr.epita.quiz.services.data.dao.MCQQuestionDBDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TestMCQQuestionDBDAO {
    public static void main(String[] args) throws SQLException, IOException {
        MCQQuestionDBDAO dao = new MCQQuestionDBDAO();
        List<Topic> topics = new ArrayList<>();
        topics.add(new Topic("java"));
        topics.add(new Topic("uml"));
        List<MCQChoice> choices = new ArrayList<>();
        choices.add(new MCQChoice("42", true));
        choices.add(new MCQChoice("43", true));
        MCQQuestion q1 = new MCQQuestion("What is the answer to the question?", topics, 1, choices);

        topics.remove(0);
        MCQQuestion q2 = new MCQQuestion("What is the answer to this new question?", topics, 1, choices);

        Integer id1 = dao.create(q1);
        Integer id2 = dao.create(q2);
        List<MCQQuestion> questions = dao.readAll();
        for (MCQQuestion question : questions) {
            System.out.println(question);
        }
        System.out.println("\n");
        questions = dao.read(topics, 3);
        for (MCQQuestion question : questions) {
            System.out.println(question);
        }
        dao.delete(id1);
        dao.delete(id2);
    }
}
