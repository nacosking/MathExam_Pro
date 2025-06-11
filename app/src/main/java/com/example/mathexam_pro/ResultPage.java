package com.example.mathexam_pro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ResultPage extends AppCompatActivity {

    private Button playAgainBtn, backHomeBtn, reviewBtn;
    private TextView correctResult, questionAnswered, skipUsed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result_page);

        // Initialize views
        playAgainBtn = findViewById(R.id.playAgainBtn);
        backHomeBtn = findViewById(R.id.backHomeBtn);
        correctResult = findViewById(R.id.correctResult);
        questionAnswered = findViewById(R.id.questionAnswered);
        skipUsed = findViewById(R.id.skipUsed);
        reviewBtn = findViewById(R.id.reviewBtn);

        // Retrieve data from intent
        Intent intent = getIntent();
        int total = intent.getIntExtra("total", 0);
        int answered = intent.getIntExtra("answered", 0);
        int skipped = intent.getIntExtra("skipped", 0);
        int correct = intent.getIntExtra("correct", 0);
        int score = intent.getIntExtra("score", 0);

        // Debug logs
        Log.d("ResultPageDebug", "Total Questions: " + total);
        Log.d("ResultPageDebug", "Answered: " + answered);
        Log.d("ResultPageDebug", "Skipped: " + skipped);
        Log.d("ResultPageDebug", "Correct: " + correct);
        Log.d("ResultPageDebug", "Score: " + score + "%");

        // Display results
        correctResult.setText("Correct Answers: " + correct + " / " + total + " (" + score + "%)");
        questionAnswered.setText("Questions Answered: " + answered);
        skipUsed.setText("Questions Skipped: " + skipped);

        // Button functionality
        playAgainBtn.setOnClickListener(v -> {
            Intent restartIntent = new Intent(ResultPage.this, QuestionPage.class);
            startActivity(restartIntent);
            finish();
        });

        backHomeBtn.setOnClickListener(v -> {
            Intent homeIntent = new Intent(ResultPage.this, MainPage.class); // Change if your home is different
            startActivity(homeIntent);
            finish();
        });

        reviewBtn.setOnClickListener(v -> {
            Intent intent1 = new Intent(ResultPage.this, QuestionPage.class);
            intent1.putExtra("isReviewMode", true);
            startActivity(intent1);
        });
    }
}
