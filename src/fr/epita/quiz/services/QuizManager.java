package fr.epita.quiz.services;

import fr.epita.quiz.datamodel.*;
import fr.epita.quiz.services.data.dao.MCQQuestionDBDAO;
import fr.epita.quiz.services.data.dao.OpenQuestionDBDAO;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class QuizManager {
    private static File file = new File("./quiz.txt");

    public static void takeQuiz(Quiz quiz, Integer totalQuestions, Scanner scanner) throws SQLException, IOException {
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
                if (openQuestion.getAnswer().equalsIgnoreCase(answer)) {
                    System.out.println("Correct!");
                    totalScore++;
                } else {
                    System.out.println("Wrong!, the correct answer is: " + openQuestion.getAnswer());
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
                System.out.println("Enter choice of the answer (if multiple answers, separate them with a semicolon): ");
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
                    System.out.println("Correct!");
                    totalScore++;
                } else {
                    List<String> correctAnswers = new ArrayList<>();
                    for (MCQChoice choice : mcqChoices) {
                        if (choice.isValid()) {
                            correctAnswers.add(choice.getChoice());
                        }
                    }
                    System.out.println("Wrong! The correct answer is: " + correctAnswers);
                }
            }
        }
        System.out.println("Your score is: " + totalScore);
    }

    public static void exportQuiz(Quiz quiz, Integer totalQuestions) throws IOException, SQLException {
        FileOutputStream outputStream = new FileOutputStream(file);
        PrintWriter pw = new PrintWriter(outputStream);

        Integer questionNumber = 0;
        String line = "";
        if (QuestionType.OPEN.equals(quiz.getType())) {
            OpenQuestionDBDAO dao = new OpenQuestionDBDAO();
            List<OpenQuestion> questions = dao.read(quiz.getTopics(), totalQuestions);
            for (OpenQuestion openQuestion : questions) {
                questionNumber++;
                line = questionNumber + ". " + openQuestion.getQuestion();
                pw.println(line);
            }
        } else if (QuestionType.MULTIPLE_CHOICE.equals(quiz.getType())) {
            MCQQuestionDBDAO dao = new MCQQuestionDBDAO();
            List<MCQQuestion> questions = dao.read(quiz.getTopics(), totalQuestions);
            for (MCQQuestion mcqQuestion : questions) {
                questionNumber++;
                line = questionNumber + ". " + mcqQuestion.getQuestion();
                pw.println(line);
                for (int i = 1; i <= mcqQuestion.getChoices().size(); i++) {
                    line = "  (" + i + ") " + mcqQuestion.getChoices().get(i - 1).getChoice();
                    pw.println(line);
                }
            }
        }
        System.out.println("Quiz exported to " + file.getAbsolutePath());
        pw.flush();
        pw.close();
    }
}
