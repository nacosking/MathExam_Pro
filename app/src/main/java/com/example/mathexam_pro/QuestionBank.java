package com.example.mathexam_pro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionBank {
    public static List<Question> getQuestions() {
        List<Question> questions = new ArrayList<>();

        questions.add(new Question(
                R.string.question_capital_india,
                Arrays.asList("Bengaluru", "Mumbai", "Jaipur", "New Delhi"),
                3,
                "New Delhi is the capital of India."
        ));

        questions.add(new Question(
                R.string.question_capital_japan,
                Arrays.asList("Kobe", "Kyoto", "Osaka", "Tokyo"),
                3,
                "Tokyo is the capital of Japan."
        ));

        questions.add(new Question(
                R.string.question_capital_malaysia,
                Arrays.asList("Selangor", "Negeri Sembilan", "Johor Bahru", "Kuala Lumpur"),
                3,
                "Kuala Lumpur is the capital of Malaysia."
        ));

        // Add more questions here...

        return questions;
    }
}


