package fr.epita.quiz.launcher;

import fr.epita.quiz.datamodel.*;
import fr.epita.quiz.services.Authentication;
import fr.epita.quiz.services.QuestionManager;
import fr.epita.quiz.services.QuizManager;
import fr.epita.quiz.services.data.dao.QuestionDBDAO;
import fr.epita.quiz.services.data.dao.StudentDBDAO;
import fr.epita.quiz.services.data.dao.TopicDBDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class Launcher {
    public static void main(String[] args) throws SQLException, IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Quiz Manager");
        System.out.println("-----------------------");
        System.out.println("Who are you?");
        System.out.println("1. Admin");
        System.out.println("2. Student");
        System.out.println("3. Exit");
        System.out.println("Please enter your choice: ");
        int option;
        try {
            option = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input");
            return;
        }
        String userType;
        switch (option) {
            case 1:
                userType = "admin";
                break;
            case 2:
                userType = "student";
                break;
            case 3:
                scanner.close();
                return;
            default:
                System.out.println("Invalid option");
                scanner.close();
                return;
        }
        System.out.println("Please enter your name: ");
        String userName = scanner.nextLine();
        System.out.println("Please enter your password: ");
        String password = scanner.nextLine();
        if (Authentication.authenticate(userType, userName, password)) {
            System.out.println("Login successful");
            System.out.println("----------------");
        } else {
            System.out.println("Login failed");
            scanner.close();
            return;
        }

        String userResponse = "";
        if ("admin".equals(userType)) {
            while (!"x".equals(userResponse)) {
                System.out.println("What operation would you like to do?");
                System.out.println("a. Add a question");
                System.out.println("b. Update a question");
                System.out.println("c. Delete a question");
                System.out.println("d. See all questions based on type");
                System.out.println("e. Search questions based on topics");
                System.out.println("f. See all questions with id");
                System.out.println("g. Create a student");
                System.out.println("h. See all the students");
                System.out.println("i. Delete a student");
                System.out.println("j. Take a quiz");
                System.out.println("k. Export a quiz");
                System.out.println("x. Exit");
                System.out.println("Please enter your choice: ");
                userResponse = scanner.nextLine();

                switch (userResponse) {
                    case "a":
                        QuestionManager.add(scanner);
                        break;
                    case "b":
                        QuestionManager.update(scanner);
                        break;
                    case "c":
                        QuestionManager.delete(scanner);
                        break;
                    case "d":
                        QuestionManager.getAllQuestionsByType(scanner);
                        break;
                    case "e":
                        QuestionManager.getAllQuestionsByTopics(scanner);
                        break;
                    case "f":
                        QuestionDBDAO dao = new QuestionDBDAO();
                        Map<Integer, String> questions = dao.getQuestionsWithId();
                        for (Map.Entry<Integer, String> entry : questions.entrySet()) {
                            System.out.println(entry.getKey() + " : " + entry.getValue());
                        }
                        break;
                    case "g":
                        Student student = getStudent(scanner);
                        StudentDBDAO studentDBDAO = new StudentDBDAO();
                        studentDBDAO.create(student);
                        System.out.println("Student created successfully\n" + student);
                        break;
                    case "h":
                        StudentDBDAO studentDBDAO1 = new StudentDBDAO();
                        List<Student> students = studentDBDAO1.readAll();
                        for (Student student1 : students) {
                            System.out.println(student1);
                        }
                        break;
                    case "i":
                        student = getStudent(scanner);
                        studentDBDAO = new StudentDBDAO();
                        studentDBDAO.delete(student);
                        System.out.println("Student deleted successfully\n" + student);
                        break;
                    case "j":
                        quizHandler(scanner, false);
                        break;
                    case "k":
                        quizHandler(scanner, true);
                        break;
                    case "x":
                        System.out.println("Exiting");
                        break;
                    default:
                        System.out.println("Invalid option, please try again");
                        break;
                }
            }
        } else {
            while (!"x".equals(userResponse)) {
                System.out.println("What operation would you like to do?");
                System.out.println("a. Take a quiz");
                System.out.println("b. Export a quiz");
                System.out.println("x. Exit");
                System.out.println("Please enter your choice: ");
                userResponse = scanner.nextLine();
                switch (userResponse) {
                    case "a":
                        quizHandler(scanner, false);
                        break;
                    case "b":
                        quizHandler(scanner, true);
                        break;
                    case "x":
                        System.out.println("Exiting");
                        break;
                    default:
                        System.out.println("Invalid option, please try again");
                        break;
                }
            }
        }
        scanner.close();
    }

    private static void quizHandler(Scanner scanner, boolean export) throws SQLException, IOException {
        System.out.println("Select the type of quiz you want to take: ");
        for (QuestionType qt : QuestionType.values()) {
            System.out.println(qt.ordinal() + " " + qt);
        }
        int quizType = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Select from the following topics: ");
        TopicDBDAO topicDAO = new TopicDBDAO();
        List<Topic> topicList = topicDAO.readAll();
        for (int i = 0; i < topicList.size(); i++) {
            System.out.println(i + " " + topicList.get(i).getName());
        }
        System.out.println("Enter the topic id, if you want to select multiple topics, enter them separated by a semicolon: ");
        String topic = scanner.nextLine();
        String[] topicArray = topic.split(";");
        List<Topic> topicList2 = new ArrayList<>();
        for (String topic2 : topicArray) {
            topicList2.add(topicList.get(Integer.parseInt(topic2)));
        }
        System.out.println("Enter the number of questions you want to take: ");
        int numberOfQuestions = scanner.nextInt();
        scanner.nextLine();
        switch (quizType) {
            case 0:
                Quiz quiz = new Quiz(QuestionType.OPEN, topicList2);
                if (export) {
                    QuizManager.exportQuiz(quiz, numberOfQuestions);
                } else {
                    QuizManager.takeQuiz(quiz, numberOfQuestions, scanner);
                }
                break;
            case 1:
                quiz = new Quiz(QuestionType.MULTIPLE_CHOICE, topicList2);
                if (export) {
                    QuizManager.exportQuiz(quiz, numberOfQuestions);
                } else {
                    QuizManager.takeQuiz(quiz, numberOfQuestions, scanner);
                }
                break;
            default:
                System.out.println("Invalid option");
                break;
        }
    }

    private static Student getStudent(Scanner scanner) {
        System.out.println("Please enter the student's name: ");
        String studentName = scanner.nextLine();
        System.out.println("Please enter the student's email: ");
        String studentEmail = scanner.nextLine();
        return new Student(studentEmail, studentName);
    }
}
