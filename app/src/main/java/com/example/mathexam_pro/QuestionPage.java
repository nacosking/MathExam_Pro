package com.example.mathexam_pro;

import android.content.Intent;
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

    private Button option1, option2, option3, option4, submitBtn, nextBtn, previousBtn, skipped, resetBtn, resultBtn;
    private TextView description, skipRemain;

    private Button[] choiceButtons;

    private List<QuestionState> questionStates;
    private int currentQuestionIndex = 0;
    private int remainingSkips = 2;
    private boolean isReviewMode = false;


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
        skipRemain = findViewById(R.id.skipRemain);
        resetBtn = findViewById(R.id.resetBtn);
        resultBtn = findViewById(R.id.resultBtn);


        choiceButtons = new Button[]{option1, option2, option3, option4};
        isReviewMode = getIntent().getBooleanExtra("isReviewMode", false);
        loadNewQuestions();

        for (int i = 0; i < choiceButtons.length; i++) {
            int finalI = i;
            choiceButtons[i].setOnClickListener(v -> {
                QuestionState qs = questionStates.get(currentQuestionIndex);
                qs.setSelectedChoiceIndex(finalI);

                for (int j = 0; j < choiceButtons.length; j++) {
                    choiceButtons[j].setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    choiceButtons[j].setEnabled(true);
                }
                choiceButtons[finalI].setBackgroundColor(getResources().getColor(R.color.teal_200));
            });
        }

        nextBtn.setOnClickListener(v -> {
            for (int i = currentQuestionIndex + 1; i < questionStates.size(); i++) {
                if (!questionStates.get(i).isSkipped()) {
                    currentQuestionIndex = i;
                    loadQuestion(currentQuestionIndex);
                    updatePreviousButtonVisibility();
                    break;
                }
            }
        });

        previousBtn.setOnClickListener(v -> {
            for (int i = currentQuestionIndex - 1; i >= 0; i--) {
                if (!questionStates.get(i).isSkipped()) {
                    currentQuestionIndex = i;
                    loadQuestion(currentQuestionIndex);
                    updatePreviousButtonVisibility();
                    break;
                }
            }
        });

        submitBtn.setOnClickListener(v -> checkAnswer());

        skipped.setOnClickListener(v -> {
            QuestionState qs = questionStates.get(currentQuestionIndex);

            if (qs.isSkipped()) {
                Toast.makeText(this, "This question was already skipped!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (remainingSkips <= 0) {
                Toast.makeText(this, "No skips remaining!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!qs.isSubmitted()) {
                qs.setSkipped(true);
                qs.setSubmitted(true);
                remainingSkips--;
                updateSkipText();
                Toast.makeText(this, "Question skipped!", Toast.LENGTH_SHORT).show();

                boolean moved = false;
                for (int i = currentQuestionIndex + 1; i < questionStates.size(); i++) {
                    if (!questionStates.get(i).isSkipped()) {
                        currentQuestionIndex = i;
                        moved = true;
                        break;
                    }
                }

                if (!moved) {
                    for (int i = currentQuestionIndex - 1; i >= 0; i--) {
                        if (!questionStates.get(i).isSkipped()) {
                            currentQuestionIndex = i;
                            moved = true;
                            break;
                        }
                    }
                }

                if (moved) {
                    loadQuestion(currentQuestionIndex);
                    updatePreviousButtonVisibility();
                    updateNextButtonVisibility(); // <- Add this
                } else if (allQuestionsHandled()) {
                    submitBtn.setVisibility(View.GONE);
                    resultBtn.setVisibility(View.VISIBLE);
                }
            }

            updateResultButtonVisibility();

        });

        resetBtn.setOnClickListener(v -> {
            remainingSkips = 2;
            updateSkipText();
            loadNewQuestions();
            resultBtn.setVisibility(View.GONE);    // <-- Hide result button on reset
            submitBtn.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Quiz has been reset!", Toast.LENGTH_SHORT).show();
        });

        resultBtn.setOnClickListener(v -> goToResultPage());


        updateSkipText();
        updateResultButtonVisibility();

        if (isReviewMode) {
            submitBtn.setVisibility(View.GONE);
            skipped.setVisibility(View.GONE);
            resetBtn.setVisibility(View.GONE);
            resultBtn.setVisibility(View.VISIBLE);
        }

    }


    private void loadNewQuestions() {
        questionStates = new ArrayList<>();
        for (Question q : QuestionBank.getRandomQuestions(5)) {
            questionStates.add(new QuestionState(q));
        }
        currentQuestionIndex = 0;
        loadQuestion(currentQuestionIndex);
        updatePreviousButtonVisibility();
        updateNextButtonVisibility(); // <- Add this
    }


    private void updateSkipText() {
        skipRemain.setText("Skips left: " + remainingSkips);
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
            choiceButtons[i].setEnabled(!isReviewMode); // Disable buttons in review
        }

        if (isReviewMode) {
            // Highlight correct and selected answers
            for (int i = 0; i < choiceButtons.length; i++) {
                if (i == correct) {
                    choiceButtons[i].setBackgroundColor(getResources().getColor(R.color.warm_green));
                } else if (i == selected) {
                    choiceButtons[i].setBackgroundColor(getResources().getColor(R.color.warm_red));
                }
            }

            // Show explanation
            description.append("\n\nExplanation: " + q.getExplanation());
        } else {
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

        updatePreviousButtonVisibility();
        updateNextButtonVisibility(); // <- add this
    }


    private void updatePreviousButtonVisibility() {
        previousBtn.setVisibility(currentQuestionIndex == 0 ? View.GONE : View.VISIBLE);
    }

    private void updateNextButtonVisibility() {
        for (int i = currentQuestionIndex + 1; i < questionStates.size(); i++) {
            if (!questionStates.get(i).isSkipped()) {
                nextBtn.setVisibility(View.VISIBLE);
                return;
            }
        }
        nextBtn.setVisibility(View.GONE);
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

        qs.setSubmitted(true);

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

        if (allQuestionsHandled()) {
            submitBtn.setVisibility(View.GONE);
            resultBtn.setVisibility(View.VISIBLE);
        }

        updateResultButtonVisibility();
    }

    private boolean allQuestionsHandled() {
        for (QuestionState qs : questionStates) {
            if (!qs.isSkipped() && !qs.isSubmitted()) {
                return false;
            }
        }
        return true;
    }

    private void updateResultButtonVisibility() {
        if (allQuestionsHandled()) {
            submitBtn.setVisibility(View.GONE);
            resultBtn.setVisibility(View.VISIBLE);
        } else {
            resultBtn.setVisibility(View.GONE);
            submitBtn.setVisibility(View.VISIBLE);
        }
    }


    private void goToResultPage() {
        Intent intent = new Intent(QuestionPage.this, ResultPage.class);

        if (isReviewMode) {
            // Pass the original result data from intent
            Intent fromIntent = getIntent();
            intent.putExtra("total", fromIntent.getIntExtra("total", 0));
            intent.putExtra("answered", fromIntent.getIntExtra("answered", 0));
            intent.putExtra("skipped", fromIntent.getIntExtra("skipped", 0));
            intent.putExtra("correct", fromIntent.getIntExtra("correct", 0));
            intent.putExtra("score", fromIntent.getIntExtra("score", 0));
        } else {
            // Calculate normally
            int totalQuestions = questionStates.size();
            int answered = 0;
            int skipped = 0;
            int correct = 0;

            for (QuestionState qs : questionStates) {
                if (qs.isSkipped()) {
                    skipped++;
                } else if (qs.isSubmitted()) {
                    answered++;
                    if (qs.getSelectedChoiceIndex() == qs.getQuestion().getCorrectAnswerIndex()) {
                        correct++;
                    }
                }
            }

            int scorePercentage = (int) (((double) correct / totalQuestions) * 100);

            intent.putExtra("total", totalQuestions);
            intent.putExtra("answered", answered);
            intent.putExtra("skipped", skipped);
            intent.putExtra("correct", correct);
            intent.putExtra("score", scorePercentage);
        }

        startActivity(intent);
        finish();
    }

}
