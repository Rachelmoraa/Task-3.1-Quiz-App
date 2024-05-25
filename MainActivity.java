package com.example.quizes_app;

// Imports for all classes
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

// MainActivity class
public class MainActivity extends AppCompatActivity {
    private EditText nameEditText;
    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameEditText = findViewById(R.id.name_edit_text);
        startButton = findViewById(R.id.start_button);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                intent.putExtra("NAME", name);
                startActivity(intent);
            }
        });
    }
}

// QuizActivity class
class QuizActivity extends AppCompatActivity {
    private TextView welcomeTextView;
    private ProgressBar progressBar;
    private TextView questionTextView;
    private Button answer1Button;
    private Button answer2Button;
    private Button answer3Button;
    private Button submitButton;
    private Button nextButton;

    private String[] questions = {"Question 1", "Question 2", "Question 3", "Question 4", "Question 5"};
    private String[][] answers = {
            {"Answer 1.1", "Answer 1.2", "Answer 1.3"},
            {"Answer 2.1", "Answer 2.2", "Answer 2.3"},
            {"Answer 3.1", "Answer 3.2", "Answer 3.3"},
            {"Answer 4.1", "Answer 4.2", "Answer 4.3"},
            {"Answer 5.1", "Answer 5.2", "Answer 5.3"}
    };
    private int[] correctAnswers = {0, 1, 2, 0, 1};

    private int currentQuestionIndex = 0;
    private int selectedAnswer = -1; // To keep track of the selected answer
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        welcomeTextView = findViewById(R.id.welcome_text_view);
        progressBar = findViewById(R.id.progress_bar);
        questionTextView = findViewById(R.id.question_text_view);
        answer1Button = findViewById(R.id.answer1_button);
        answer2Button = findViewById(R.id.answer2_button);
        answer3Button = findViewById(R.id.answer3_button);
        submitButton = findViewById(R.id.submit_button);
        nextButton = findViewById(R.id.next_button);

        String name = getIntent().getStringExtra("NAME");
        welcomeTextView.setText("Welcome " + name);

        answer1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAnswer(0);
            }
        });

        answer2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAnswer(1);
            }
        });

        answer3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAnswer(2);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextQuestion();
            }
        });

        updateQuestion();
    }

    private void selectAnswer(int answerIndex) {
        selectedAnswer = answerIndex;
        answer1Button.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        answer2Button.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        answer3Button.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));

        if (answerIndex == 0) answer1Button.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
        if (answerIndex == 1) answer2Button.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
        if (answerIndex == 2) answer3Button.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
    }

    private void checkAnswer() {
        if (selectedAnswer == -1) {
            Toast.makeText(QuizActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedAnswer == correctAnswers[currentQuestionIndex]) {
            score++;
            highlightCorrectAnswer(true);
        } else {
            highlightCorrectAnswer(false);
        }

        submitButton.setVisibility(View.GONE);
        nextButton.setVisibility(View.VISIBLE);
    }

    private void highlightCorrectAnswer(boolean isCorrect) {
        if (isCorrect) {
            if (selectedAnswer == 0) answer1Button.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
            if (selectedAnswer == 1) answer2Button.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
            if (selectedAnswer == 2) answer3Button.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        } else {
            if (selectedAnswer == 0) answer1Button.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            if (selectedAnswer == 1) answer2Button.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            if (selectedAnswer == 2) answer3Button.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        }
    }

    private void goToNextQuestion() {
        currentQuestionIndex++;
        if (currentQuestionIndex < questions.length) {
            updateQuestion();
            submitButton.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.GONE);
            answer1Button.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            answer2Button.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            answer3Button.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            selectedAnswer = -1;
        } else {
            showFinalScore();
        }
    }

    private void updateQuestion() {
        if (currentQuestionIndex < questions.length) {
            questionTextView.setText(questions[currentQuestionIndex]);
            answer1Button.setText(answers[currentQuestionIndex][0]);
            answer2Button.setText(answers[currentQuestionIndex][1]);
            answer3Button.setText(answers[currentQuestionIndex][2]);
            progressBar.setProgress((currentQuestionIndex + 1) * 100 / questions.length);
        }
    }

    private void showFinalScore() {
        Intent intent = new Intent(QuizActivity.this, FinalScoreActivity.class);
        intent.putExtra("SCORE", score);
        intent.putExtra("TOTAL", questions.length);
        intent.putExtra("NAME", getIntent().getStringExtra("NAME"));
        startActivity(intent);
        finish();
    }
}

// FinalScoreActivity class
class FinalScoreActivity extends AppCompatActivity {
    private TextView congratsTextView;
    private TextView scoreTextView;
    private Button takeNewQuizButton;
    private Button finishButton;
    private Button attemptArithmeticButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_score);

        congratsTextView = findViewById(R.id.congrats_text_view);
        scoreTextView = findViewById(R.id.score_text_view);
        takeNewQuizButton = findViewById(R.id.take_new_quiz_button);
        finishButton = findViewById(R.id.finish_button);
        attemptArithmeticButton = findViewById(R.id.attempt_arithmetic_button);

        String name = getIntent().getStringExtra("NAME");
        int score = getIntent().getIntExtra("SCORE", 0);
        int total = getIntent().getIntExtra("TOTAL", 5);

        congratsTextView.setText("Congratulations " + name);
        scoreTextView.setText("Your score is: " + score + "/" + total);

        takeNewQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinalScoreActivity.this, QuizActivity.class);
                intent.putExtra("NAME", name);
                startActivity(intent);
                finish();
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        attemptArithmeticButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinalScoreActivity.this, ArithmeticQuizActivity.class);
                startActivity(intent);
            }        });
        }
    }
    
    // ArithmeticQuizActivity class
    class ArithmeticQuizActivity extends AppCompatActivity {
        private TextView arithmeticQuestionTextView;
        private EditText arithmeticAnswerEditText;
        private Button arithmeticSubmitButton;
    
        private int correctAnswer;
    
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_arithmetic_quiz);
    
            arithmeticQuestionTextView = findViewById(R.id.arithmetic_question_text_view);
            arithmeticAnswerEditText = findViewById(R.id.arithmetic_answer_edit_text);
            arithmeticSubmitButton = findViewById(R.id.arithmetic_submit_button);
    
            generateNewQuestion();
    
            arithmeticSubmitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String answerText = arithmeticAnswerEditText.getText().toString();
                    if (!answerText.isEmpty()) {
                        int userAnswer = Integer.parseInt(answerText);
                        if (userAnswer == correctAnswer) {
                            Toast.makeText(ArithmeticQuizActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ArithmeticQuizActivity.this, "Incorrect. The correct answer is " + correctAnswer, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ArithmeticQuizActivity.this, "Please enter an answer", Toast.LENGTH_SHORT).show();
                    }
                    generateNewQuestion();
                }
            });
        }
    
        private void generateNewQuestion() {
            int num1 = (int) (Math.random() * 10);
            int num2 = (int) (Math.random() * 10);
            correctAnswer = num1 + num2;
    
            arithmeticQuestionTextView.setText("What is " + num1 + " + " + num2 + "?");
            arithmeticAnswerEditText.setText("");
        }
    }
    
       
