package fr.epita.quiz.datamodel;

import java.util.List;

public class Question {
    private String question;
    private List<Topic> topics;
    private Integer difficulty;
    private Enum<QuestionType> type;

    public Question(String question, List<Topic> topics, Integer difficulty, Enum<QuestionType> type) {
        this.question = question;
        this.topics = topics;
        this.difficulty = difficulty;
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public Enum<QuestionType> getType() {
        return type;
    }

    public void setType(Enum<QuestionType> type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Question{" +
                "question='" + question + '\'' +
                ", topics=" + topics +
                ", difficulty=" + difficulty +
                ", type=" + type +
                '}';
    }
}
