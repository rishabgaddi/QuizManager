package fr.epita.quiz.datamodel;

public class OpenQuestion extends Question {
    private String answer;

    public OpenQuestion(String question, String[] topics, Integer difficulty, String answer) {
        super(question, topics, difficulty);
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
