package fr.epita.quiz.datamodel;

public class Quiz {
    private Question type;
    private String[] topics;

    public Quiz(Question type, String[] topics) {
        this.type = type;
        this.topics = topics;
    }

    public Question getType() {
        return type;
    }

    public void setType(Question type) {
        this.type = type;
    }

    public String[] getTopics() {
        return topics;
    }

    public void setTopics(String[] topics) {
        this.topics = topics;
    }

    public String toString() {
        return "Quiz [type=" + type + ", topics=" + topics + "]";
    }
}
