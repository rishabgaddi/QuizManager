package fr.epita.quiz.services;

import fr.epita.quiz.datamodel.*;
import fr.epita.quiz.services.data.dao.MCQQuestionDBDAO;
import fr.epita.quiz.services.data.dao.OpenQuestionDBDAO;
import fr.epita.quiz.services.data.dao.TopicDBDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QuestionManager {
    private static OpenQuestion createOpenQuestion(Scanner sc) {
        System.out.println("Enter the question: ");
        String openQuestion = sc.nextLine();
        System.out.println("Enter the answer: ");
        String openAnswer = sc.nextLine();
        System.out.println("Enter the topic, if there is more than one topic, separate them by semicolon: ");
        String openTopics = sc.nextLine();
        System.out.println("Enter the difficulty: ");
        int openDifficulty = sc.nextInt();
        sc.nextLine();
        String[] openTopicArray = openTopics.split(";");
        List<Topic> openTopicList = new ArrayList<Topic>();
        for (String topic : openTopicArray) {
            openTopicList.add(new Topic(topic));
        }
        return new OpenQuestion(openQuestion, openTopicList, openDifficulty, openAnswer);
    }

    private static MCQQuestion createMCQQuestion(Scanner sc) {
        System.out.println("Enter the question");
        String mcqQuestion = sc.nextLine();
        System.out.println("Enter the topic, if there is more than one topic, separate them by semicolon: ");
        String mcqTopics = sc.nextLine();
        System.out.println("Enter the difficulty: ");
        int mcqDifficulty = sc.nextInt();
        sc.nextLine();
        String[] mcqTopicArray = mcqTopics.split(";");
        List<Topic> mcqTopicList = new ArrayList<Topic>();
        for (String topic : mcqTopicArray) {
            mcqTopicList.add(new Topic(topic));
        }
        List<MCQChoice> mcqChoices = new ArrayList<MCQChoice>();
        System.out.println("Enter the choices and if it is the correct answer");
        String userChoice = "";
        while (!"q".equals(userChoice)) {
            System.out.println("Enter the choice or q to quit: ");
            userChoice = sc.nextLine();
            if (!"q".equals(userChoice)) {
                System.out.println("Press y if it is the correct answer, else press any other key: ");
                String mcqAnswer = sc.nextLine();
                if ("y".equalsIgnoreCase(mcqAnswer)) {
                    mcqChoices.add(new MCQChoice(userChoice, true));
                } else {
                    mcqChoices.add(new MCQChoice(userChoice, false));
                }
            }
        }
        return new MCQQuestion(mcqQuestion, mcqTopicList, mcqDifficulty, mcqChoices);
    }

    public static void add(Scanner sc) throws SQLException, IOException {
        System.out.println("Select the type of question");
        for (QuestionType qt : QuestionType.values()) {
            System.out.println(qt.ordinal() + " " + qt);
        }
        int choice = sc.nextInt();
        sc.nextLine();
        switch (choice) {
            case 0:
                OpenQuestion oq = createOpenQuestion(sc);
                OpenQuestionDBDAO oqDAO = new OpenQuestionDBDAO();
                oqDAO.create(oq);
                System.out.println("Question created successfully\n" + oq);
                break;
            case 1:
                MCQQuestion mq = createMCQQuestion(sc);
                MCQQuestionDBDAO mqDAO = new MCQQuestionDBDAO();
                mqDAO.create(mq);
                System.out.println("Question created successfully\n" + mq);
                break;
            default:
                System.out.println("Invalid choice");
                break;
        }
    }

    public static void update(Scanner sc) throws SQLException, IOException {
        System.out.println("Select the type of question");
        for (QuestionType qt : QuestionType.values()) {
            System.out.println(qt.ordinal() + " " + qt);
        }
        int choice = sc.nextInt();
        sc.nextLine();
        switch (choice) {
            case 0:
                System.out.println("Enter the question id: ");
                int openQuestionId = sc.nextInt();
                sc.nextLine();
                OpenQuestion oq = createOpenQuestion(sc);
                OpenQuestionDBDAO oqDAO = new OpenQuestionDBDAO();
                oqDAO.update(oq, openQuestionId);
                System.out.println("Question updated successfully\n" + oq);
                break;
            case 1:
                System.out.println("Enter the question id: ");
                int mcqQuestionId = sc.nextInt();
                sc.nextLine();
                MCQQuestion mq = createMCQQuestion(sc);
                MCQQuestionDBDAO mqDAO = new MCQQuestionDBDAO();
                mqDAO.update(mq, mcqQuestionId);
                System.out.println("Question updated successfully\n" + mq);
                break;
            default:
                System.out.println("Invalid choice");
                break;
        }
    }

    public static void delete(Scanner sc) throws SQLException, IOException {
        System.out.println("Enter the question id: ");
        int questionId = sc.nextInt();
        sc.nextLine();
        OpenQuestionDBDAO oqDAO = new OpenQuestionDBDAO();
        oqDAO.delete(questionId);
        System.out.println("Question deleted successfully");
    }

    public static void getAllQuestionsByType(Scanner sc) throws SQLException, IOException {
        System.out.println("Select the type of question");
        for (QuestionType qt : QuestionType.values()) {
            System.out.println(qt.ordinal() + " " + qt);
        }
        int choice = sc.nextInt();
        sc.nextLine();
        switch (choice) {
            case 0:
                OpenQuestionDBDAO oqDAO = new OpenQuestionDBDAO();
                List<OpenQuestion> oqList = oqDAO.readAll();
                for (OpenQuestion oq : oqList) {
                    System.out.println(oq);
                }
                break;
            case 1:
                MCQQuestionDBDAO mqDAO = new MCQQuestionDBDAO();
                List<MCQQuestion> mqList = mqDAO.readAll();
                for (MCQQuestion mq : mqList) {
                    System.out.println(mq);
                }
                break;
            default:
                System.out.println("Invalid choice");
                break;
        }
    }

    public static void getAllQuestionsByTopics(Scanner sc) throws SQLException, IOException {
        System.out.println("Select the type of question");
        for (QuestionType qt : QuestionType.values()) {
            System.out.println(qt.ordinal() + " " + qt);
        }
        int choice = sc.nextInt();
        sc.nextLine();
        switch (choice) {
            case 0:
                List<Topic> topicList = getTopics(sc);
                OpenQuestionDBDAO oqDAO = new OpenQuestionDBDAO();
                List<OpenQuestion> oqList = oqDAO.read(topicList, null);
                for (OpenQuestion oq : oqList) {
                    System.out.println(oq);
                }
                break;
            case 1:
                topicList = getTopics(sc);
                MCQQuestionDBDAO mqDAO = new MCQQuestionDBDAO();
                List<MCQQuestion> mqList = mqDAO.read(topicList, null);
                for (MCQQuestion mq : mqList) {
                    System.out.println(mq);
                }
                break;
            default:
                System.out.println("Invalid choice");
                break;
        }
    }

    private static List<Topic> getTopics(Scanner sc) throws SQLException, IOException {
        System.out.println("Select from the following topics: ");
        TopicDBDAO topicDAO = new TopicDBDAO();
        List<Topic> topicList = topicDAO.readAll();
        for (int i = 0; i < topicList.size(); i++) {
            System.out.println(i + " " + topicList.get(i).getName());
        }
        System.out.println("Enter the topic id, if you want to select multiple topics, enter them separated by a semicolon: ");
        String topic = sc.nextLine();
        String[] topicArray = topic.split(";");
        List<Topic> topicList2 = new ArrayList<Topic>();
        for (String topic2 : topicArray) {
            topicList2.add(topicList.get(Integer.parseInt(topic2)));
        }
        return topicList2;
    }
}
