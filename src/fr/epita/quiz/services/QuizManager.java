package fr.epita.quiz.services;

import fr.epita.quiz.datamodel.*;
import fr.epita.quiz.services.data.dao.MCQQuestionDBDAO;
import fr.epita.quiz.services.data.dao.OpenQuestionDBDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class QuizManager {
    public void takeQuiz(Quiz quiz, Integer totalQuestions) throws SQLException, IOException {
        Scanner scanner = new Scanner(System.in);
        Integer totalScore = 0;
        Integer questionNumber = 0;
        if (QuestionType.OPEN.equals(quiz.getType())) {
            OpenQuestionDBDAO dao = new OpenQuestionDBDAO();
            List<OpenQuestion> questions = dao.read(quiz.getTopics(), totalQuestions);
            for (OpenQuestion openQuestion : questions) {
                questionNumber++;
                System.out.println(questionNumber + ". " + openQuestion.getQuestion());
                System.out.println("Your answer: ");
                String answer = scanner.nextLine();
                if (openQuestion.getAnswer().equals(answer)) {
                    totalScore++;
                }
            }
        } else if (QuestionType.MULTIPLE_CHOICE.equals(quiz.getType())) {
            MCQQuestionDBDAO dao = new MCQQuestionDBDAO();
            List<MCQQuestion> questions = dao.read(quiz.getTopics(), totalQuestions);
            for (MCQQuestion mcqQuestion : questions) {
                questionNumber++;
                System.out.println(questionNumber + ". " + mcqQuestion.getQuestion());
                for (int i = 1; i <= mcqQuestion.getChoices().size(); i++) {
                    System.out.println("  (" + i + ") " + mcqQuestion.getChoices().get(i - 1).getChoice());
                }
                System.out.println("Your answer: ");
                String answer = scanner.nextLine();
                String[] answers = answer.split(";");
                boolean allCorrect = false;
                List<MCQChoice> mcqChoices = mcqQuestion.getChoices();
                for (MCQChoice choice : mcqChoices) {
                    if (choice.isValid()) {
                        if (Arrays.asList(answers).contains(String.valueOf(mcqChoices.indexOf(choice) + 1))) {
                            allCorrect = true;
                        } else {
                            allCorrect = false;
                            break;
                        }
                    } else if (Arrays.asList(answers).contains(String.valueOf(mcqChoices.indexOf(choice) + 1))) {
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
        scanner.close();
    }
}
