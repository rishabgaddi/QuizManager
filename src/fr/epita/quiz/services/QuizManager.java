package fr.epita.quiz.services;

import fr.epita.quiz.datamodel.MCQQuestion;
import fr.epita.quiz.datamodel.OpenQuestion;
import fr.epita.quiz.datamodel.Question;
import fr.epita.quiz.datamodel.Quiz;
import fr.epita.quiz.services.data.dao.MCQQuestionDBDAO;
import fr.epita.quiz.services.data.dao.OpenQuestionDBDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class QuizManager {

    public void takeQuiz(Quiz type, Integer totalQuestions) throws SQLException, IOException {
        Integer totalScore = 0;
        Question question = type.getType();
        if (question instanceof OpenQuestion) {
            OpenQuestionDBDAO dao = new OpenQuestionDBDAO();
            List<OpenQuestion> questions = dao.read(type.getTopics(), totalQuestions);
            for (OpenQuestion openQuestion : questions) {
                System.out.println(openQuestion.getQuestion());
                System.out.println("Your answer: ");
                String answer = System.console().readLine();
                if (openQuestion.getAnswer().equals(answer)) {
                    totalScore++;
                }
            }
        } else if (question instanceof MCQQuestion) {
            MCQQuestionDBDAO dao = new MCQQuestionDBDAO();
            List<MCQQuestion> questions = dao.read(type.getTopics(), totalQuestions);
            for (MCQQuestion mcqQuestion : questions) {
                System.out.println(mcqQuestion.getQuestion());
                for (int i = 1; i <= mcqQuestion.getChoices().size(); i++) {
                    System.out.println("  (" + i + ") " + mcqQuestion.getChoices().get(i - 1).getChoice());
                }
                System.out.println("Your answer: ");
                String answer = System.console().readLine();
                String[] answers = answer.split(";");
                boolean allCorrect = false;
                for (String ans : answers) {
                    if (Integer.parseInt(ans) <= mcqQuestion.getChoices().size()) {
                        if (mcqQuestion.getChoices().get(ans.charAt(0) - '1').isValid()) {
                            allCorrect = true;
                        } else {
                            allCorrect = false;
                            break;
                        }
                    } else {
                        allCorrect = false;
                        break;
                    }
                }
                if (allCorrect) {
                    totalScore++;
                }
            }
        }
        System.out.println("Your score is: " + totalScore);
    }

}
