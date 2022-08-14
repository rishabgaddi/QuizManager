package fr.epita.quiz.datamodel;

import java.util.ArrayList;
import java.util.List;

public class MCQQuestion extends Question {
    private List<MCQChoice> choices;

    public MCQQuestion(String question, List<Topic> topics, Integer difficulty, List<MCQChoice> choices) {
        super(question, topics, difficulty, QuestionType.MULTIPLE_CHOICE);
        this.choices = choices;
    }

    public List<MCQChoice> getChoices() {
        return choices;
    }

    public void setChoices(ArrayList<MCQChoice> choices) {
        this.choices = choices;
    }
}
