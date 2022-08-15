package fr.epita.quiz.datamodel;

public class MCQChoice {
    private String choice;
    private boolean valid;

    public MCQChoice(String choice, boolean valid) {
        this.choice = choice;
        this.valid = valid;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Override
    public String toString() {
        return "MCQChoice{" +
                "choice='" + choice + '\'' +
                ", valid=" + valid +
                '}';
    }
}
