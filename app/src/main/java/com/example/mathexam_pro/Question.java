package com.example.mathexam_pro;

import java.util.List;

public class Question {
    private int questionTextResId;
    private List<String> options;
    private int correctAnswerIndex;
    private String explanation;

    public Question(int questionTextResId, List<String> options, int correctAnswerIndex, String explanation) {
        this.questionTextResId = questionTextResId;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
        this.explanation = explanation;
    }

    public int getQuestionTextResId() {
        return questionTextResId;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public String getExplanation() {
        return explanation;
    }
}



