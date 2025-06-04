package com.example.mathexam_pro;

public class QuestionState {
    private Question question;
    private int selectedChoiceIndex = -1; // -1 means no answer yet
    private boolean isSubmitted = false;
    private boolean skipped = false;

    public QuestionState(Question question) {this.question = question;}

    public Question getQuestion() {return question;}

    public int getSelectedChoiceIndex() {return selectedChoiceIndex;}

    public void setSelectedChoiceIndex(int selectedChoiceIndex) {this.selectedChoiceIndex = selectedChoiceIndex;}

    public boolean isSubmitted() {return isSubmitted;}

    public void setSubmitted(boolean submitted) {isSubmitted = submitted;}

    public boolean isSkipped() {return skipped;}

    public void setSkipped(boolean skipped) {this.skipped = skipped;}
}
