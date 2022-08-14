package fr.epita.quiz.datamodel;

import java.util.List;

public class OpenQuestion extends Question {
    private String answer;

    public OpenQuestion(String question, List<Topic> topics, Integer difficulty, String answer) {
        super(question, topics, difficulty, QuestionType.OPEN);
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
