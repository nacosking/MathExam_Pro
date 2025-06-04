package com.example.mathexam_pro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuestionBank {
    public static List<Question> getRandomQuestions(int count) {
        List<Question> questions = new ArrayList<>();

        questions.add(new Question("What is 2 + 2?", Arrays.asList("3", "4", "5", "6"), 1, "2 + 2 = 4"));
        questions.add(new Question("What is 9 × 3?", Arrays.asList("27", "18", "36", "21"), 0, "9 × 3 = 27"));
        questions.add(new Question("Square root of 81?", Arrays.asList("9", "8", "7", "6"), 0, "√81 = 9"));
        questions.add(new Question("10 / 2 = ?", Arrays.asList("4", "5", "6", "3"), 1, "10 ÷ 2 = 5"));
        questions.add(new Question("5² = ?", Arrays.asList("10", "15", "25", "20"), 2, "5² = 25"));
        // Add more questions as needed

        Collections.shuffle(questions); // Randomize
        return questions.subList(0, count); // Pick first `count` questions
    }
}



