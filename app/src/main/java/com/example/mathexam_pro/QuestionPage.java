package com.example.mathexam_pro;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class QuestionPage extends AppCompatActivity {

    private Button option1, option2, option3, option4, submitBtn, nextBtn, previousBtn, skipped;
    private TextView description;

    private Button[] choiceButtons;

    private List<QuestionState> questionStates;
    private int currentQuestionIndex = 0;
    private int lastShownPercentage = 0; // The score shown last time


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_question_page);

        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        submitBtn = findViewById(R.id.submitBtn);
        nextBtn = findViewById(R.id.nextBtn);
        previousBtn = findViewById(R.id.previousBtn);
        description = findViewById(R.id.description);
        skipped = findViewById(R.id.skipped);

        choiceButtons = new Button[]{option1, option2, option3, option4};

        // Load random 5 questions wrapped as QuestionStates
        List<Question> rawQuestions = QuestionBank.getRandomQuestions(5);
        questionStates = new ArrayList<>();
        for (Question q : rawQuestions) {
            questionStates.add(new QuestionState(q));
        }

        loadQuestion(currentQuestionIndex);
        updatePreviousButtonVisibility();

        // Setup choice buttons click to select option
        for (int i = 0; i < choiceButtons.length; i++) {
            int finalI = i;
            choiceButtons[i].setOnClickListener(v -> {
                // Save selected option index
                QuestionState qs = questionStates.get(currentQuestionIndex);
                qs.setSelectedChoiceIndex(finalI);

                // Highlight selected button and reset others
                for (int j = 0; j < choiceButtons.length; j++) {
                    choiceButtons[j].setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    choiceButtons[j].setEnabled(true);
                }
                choiceButtons[finalI].setBackgroundColor(getResources().getColor(R.color.teal_200));
            });
        }

        nextBtn.setOnClickListener(v -> {
            if (currentQuestionIndex < questionStates.size() - 1) {
                currentQuestionIndex++;
                loadQuestion(currentQuestionIndex);
                updatePreviousButtonVisibility();
            }
        });

        previousBtn.setOnClickListener(v -> {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--;
                loadQuestion(currentQuestionIndex);
                updatePreviousButtonVisibility();
            }
        });

        submitBtn.setOnClickListener(v -> {
            checkAnswer();
        });

        skipped.setOnClickListener(v -> {
            QuestionState qs = questionStates.get(currentQuestionIndex);

            if (qs.isSkipped()) {
                // Already skipped
                Toast.makeText(this, "This question was already skipped!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!qs.isSubmitted()) {
                qs.setSkipped(true);
                qs.setSubmitted(true); // Mark as submitted so it can't be answered

                Toast.makeText(this, "Question skipped!", Toast.LENGTH_SHORT).show();

                // Move to next question
                if (currentQuestionIndex < questionStates.size() - 1) {
                    currentQuestionIndex++;
                    loadQuestion(currentQuestionIndex);
                    updatePreviousButtonVisibility();
                } else {
                    Toast.makeText(this, "End of questions", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadQuestion(int index) {
        QuestionState qs = questionStates.get(index);
        Question q = qs.getQuestion();

        description.setText(q.getQuestionText());

        List<String> options = q.getOptions();
        int selected = qs.getSelectedChoiceIndex();
        int correct = q.getCorrectAnswerIndex();

        for (int i = 0; i < choiceButtons.length; i++) {
            choiceButtons[i].setText(options.get(i));
            choiceButtons[i].setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            choiceButtons[i].setEnabled(true);
        }

        if (qs.isSkipped()) {
            for (Button btn : choiceButtons) {
                btn.setEnabled(false);
                btn.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            }
            Toast.makeText(this, "This question was skipped.", Toast.LENGTH_SHORT).show();

        } else if (qs.isSubmitted()) {
            for (int i = 0; i < choiceButtons.length; i++) {
                choiceButtons[i].setEnabled(false);
                if (i == correct) {
                    choiceButtons[i].setBackgroundColor(getResources().getColor(R.color.warm_green));
                } else if (i == selected) {
                    choiceButtons[i].setBackgroundColor(getResources().getColor(R.color.warm_red));
                }
            }
        } else if (selected != -1) {
            choiceButtons[selected].setBackgroundColor(getResources().getColor(R.color.teal_200));
        }

    }



    private void updatePreviousButtonVisibility() {
        previousBtn.setVisibility(currentQuestionIndex == 0 ? View.GONE : View.VISIBLE);
    }

    private void checkAnswer() {
        QuestionState qs = questionStates.get(currentQuestionIndex);
        if (qs.isSubmitted()) {
            Toast.makeText(this, "Answer already submitted!", Toast.LENGTH_SHORT).show();
            return;
        }

        int selected = qs.getSelectedChoiceIndex();
        int correct = qs.getQuestion().getCorrectAnswerIndex();

        if (selected == -1) {
            Toast.makeText(this, "Please select an answer first", Toast.LENGTH_SHORT).show();
            return;
        }

        qs.setSubmitted(true); // Mark this question as submitted

        // Disable buttons and show correct/incorrect
        for (int i = 0; i < choiceButtons.length; i++) {
            choiceButtons[i].setEnabled(false);
            if (i == correct) {
                choiceButtons[i].setBackgroundColor(getResources().getColor(R.color.warm_green));
            } else if (i == selected) {
                choiceButtons[i].setBackgroundColor(getResources().getColor(R.color.warm_red));
            } else {
                choiceButtons[i].setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            }
        }
    }
}