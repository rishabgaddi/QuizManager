package fr.epita.quiz.tests;

import fr.epita.quiz.datamodel.OpenQuestion;
import fr.epita.quiz.datamodel.QuestionType;
import fr.epita.quiz.datamodel.Topic;
import fr.epita.quiz.services.data.dao.OpenQuestionDBDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TestOpenQuestionDBDAO {
    public static void main(String[] args) throws SQLException, IOException {
        OpenQuestionDBDAO dao = new OpenQuestionDBDAO();
        List<Topic> topics = new ArrayList<>();
        topics.add(new Topic("java"));
        topics.add(new Topic("uml"));
        OpenQuestion q1 = new OpenQuestion("What is the answer to the question?", topics, 1, "42");

        topics.remove(0);
        OpenQuestion q2 = new OpenQuestion("What is the answer to this new question?", topics, 1, "43");

        Integer id1 = dao.create(q1);
        Integer id2 = dao.create(q2);
        List<OpenQuestion> questions = dao.readAll();
        for (OpenQuestion question : questions) {
            System.out.println(question);
        }
        System.out.println("\n");
        questions = dao.read(topics, 5);
        for (OpenQuestion question : questions) {
            System.out.println(question);
        }
        dao.delete(id1);
        dao.delete(id2);
    }
}
