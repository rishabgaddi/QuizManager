package fr.epita.quiz.launcher;

import fr.epita.quiz.datamodel.MCQQuestion;
import fr.epita.quiz.datamodel.QuestionType;
import fr.epita.quiz.datamodel.Quiz;
import fr.epita.quiz.datamodel.Topic;
import fr.epita.quiz.services.QuizManager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Launcher {
    public static void main(String[] args) throws SQLException, IOException {
        QuizManager quizManager = new QuizManager();
        List<Topic> topics = new ArrayList<>();
        topics.add(new Topic("c++"));
        Quiz quiz = new Quiz(QuestionType.MULTIPLE_CHOICE, topics);
        quizManager.takeQuiz(quiz, 4);
    }
}
