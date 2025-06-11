package com.example.mathexam_pro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuestionBank {
    public static List<Question> getRandomQuestions(int count) {
        List<Question> questions = new ArrayList<>();

        questions.add(new Question("What is the sum of the infinite series:\n1 − ½ + ¼ − ⅛ + ... ?", Arrays.asList("1", "0", "2", "Does not converge"), 0, "This is a geometric series with a = 1 and r = -1/2. Sum = a / (1 - r) = 1 / (1 + 0.5) = 2/3"));
        questions.add(new Question("Evaluate the limit:\nlim (x → 0) [sin(3x) / x]", Arrays.asList("0", "1", "3", "Undefined"), 2, "lim (sin(3x)/x) = 3 because lim (sin(kx)/x) = k as x → 0"));
        questions.add(new Question("How many integers between 1 and 1000 are divisible by neither 2 nor 5?", Arrays.asList("400", "500", "300", "600"), 0, "Use inclusion-exclusion principle to count values not divisible by 2 or 5"));
        questions.add(new Question("Find the determinant of the matrix:\n[[2, -1], [4, 3]]", Arrays.asList("10", "-10", "11", "5"), 0, "Determinant = (2)(3) - (-1)(4) = 6 + 4 = 10"));
        questions.add(new Question("If P(A) = 0.6, P(B) = 0.5, and P(A ∩ B) = 0.3, what is P(A ∪ B)?", Arrays.asList("0.8", "1.1", "0.9", "0.7"), 2, "P(A ∪ B) = P(A) + P(B) - P(A ∩ B) = 0.6 + 0.5 - 0.3 = 0.8"));
        questions.add(new Question("What is the smallest positive integer x such that:\n3x ≡ 1 (mod 7)", Arrays.asList("1", "2", "3", "5"), 3, "Try values: 3*5 = 15 ≡ 1 mod 7 → Answer: 5"));
        questions.add(new Question("Solve the equation:\nlog₂(x² − 1) = 3", Arrays.asList("x = 4", "x = ±3", "x = 3", "x = 2"), 1, "x² - 1 = 8 → x² = 9 → x = ±3"));

        Collections.shuffle(questions);

        // ✅ Safety check
        if (count > questions.size()) {
            count = questions.size(); // prevent crash
        }

        return questions.subList(0, count);
    }
}
