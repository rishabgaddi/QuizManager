package fr.epita.quiz.datamodel;

import java.util.List;

public class Quiz {
    private QuestionType type;
    private List<Topic> topics;

    public Quiz(QuestionType type, List<Topic> topics) {
        this.type = type;
        this.topics = topics;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "type=" + type +
                ", topics=" + topics +
                '}';
    }
}
