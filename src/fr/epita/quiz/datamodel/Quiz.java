package fr.epita.quiz.datamodel;

import java.util.List;

public class Quiz {
    private Question type;
    private List<Topic> topics;

    public Quiz(Question type, List<Topic> topics) {
        this.type = type;
        this.topics = topics;
    }

    public Question getType() {
        return type;
    }

    public void setType(Question type) {
        this.type = type;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    public String toString() {
        return "Quiz [type=" + type + ", topics=" + topics + "]";
    }
}
