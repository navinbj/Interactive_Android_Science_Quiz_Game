package com.example.navin.assignment1_game_nba;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * the class GameActivity will act as a view and a controller for the game.
 * the activities will include updating the view with next question if answered correctly, updating the view when the game is over and
 * when the game is fully complete by answering all questions. This Class/ Activity also uses Firebase database for storing players info
 * and their game stats upon a completion of a game and when the game is over.
 */
public class GameActivity extends AppCompatActivity {

    private QuestionsAndAnswersLibrary questionsAndAnswersLibrary = new QuestionsAndAnswersLibrary();

    TextView questionNoTV, questionsTV;
    Button answer1Btn, answer2Btn, answer3Btn;
    Button viewLBBtn;
    EditText scoreEditText, streakEditText, playerEditText;
    private String correctAnswer;
    private String player;
    private String year;
    private int noOfLifelines;
    private int score = 0;
    private int streak = 0;
    private int noOfQuestion;
    private int questionPos = 1;
    private int questionNo;
    TextView gameOverTV;
    ImageView playerImageView;
    ImageView congratsImgView, starImgView, gameOverImgView;
    ImageView lifeline1, lifeline2, lifeline3;
    MediaPlayer backgroundMusicPlayer, yesMPlayer, noMPlayer, congratsMPlayer, gameOverMPlayer, clapSoundPlayer;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();      //firebase initialisation
    Player newPlayer;

    View.OnClickListener viewLBListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //directing player to a leaderboard activity view.
            Intent intent = new Intent(GameActivity.this, LeaderboardActivity.class);
            startActivity(intent);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        questionNoTV = findViewById(R.id.questionNoTV);
        questionsTV = findViewById(R.id.questionsTV);
        scoreEditText = findViewById(R.id.scoreEditText);
        streakEditText = findViewById(R.id.streakEditText);
        playerEditText = findViewById(R.id.playerEditText);
        answer1Btn = findViewById(R.id.answer1Btn);
        answer2Btn = findViewById(R.id.answer2Btn);
        answer3Btn = findViewById(R.id.answer3Btn);
        viewLBBtn = findViewById(R.id.viewLeaderBoardBtn);
        viewLBBtn.setOnClickListener(viewLBListener);
        congratsImgView = findViewById(R.id.congratsImgView);
        starImgView = findViewById(R.id.starImgView);
        gameOverTV = findViewById(R.id.gameOverTV);
        gameOverImgView = findViewById(R.id.gameOverImgView);
        lifeline1 = findViewById(R.id.lifeline1ImgView);
        lifeline2 = findViewById(R.id.lifeline2ImgView);
        lifeline3 = findViewById(R.id.lifeline3ImgView);
        playerImageView = findViewById(R.id.playerImageView);

        newPlayer = new Player();

        yesMPlayer = MediaPlayer.create(this, R.raw.yes);
        noMPlayer = MediaPlayer.create(this, R.raw.no);
        congratsMPlayer = MediaPlayer.create(this, R.raw.congrats_sound);
        gameOverMPlayer = MediaPlayer.create(this, R.raw.game_over);
        backgroundMusicPlayer = MediaPlayer.create(this, R.raw.background_music);
        clapSoundPlayer = MediaPlayer.create(this, R.raw.clapping);
        backgroundMusicPlayer.start();

        //retrieving data from another activity using bundle and intent.
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            String name = extras.getString("name");
            playerEditText.setText(name);
            player = name;

            year = extras.getString("year");
//            Toast.makeText(getApplicationContext(),year,Toast.LENGTH_SHORT).show();

            byte[] bytes = extras.getByteArray("image");    //storing the bytes of an image in a local array variable
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);      //decoding an immutable bitmap
            ImageView image = (ImageView) findViewById(R.id.playerImageView);
            image.setImageBitmap(bitmap);   //setting a Bitmap as the content of this ImageView.
        }

        updateQuestion();   //update the quesiton on the launch of an activity.
        questionNoTV.setText("Question " + questionPos + " of 10");


        //the code for controlling the activities based on the onclick of answer1 button.
        answer1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (answer1Btn.getText().toString().equalsIgnoreCase(correctAnswer))
                {
                    rightAnswerButtonOnClick();
                }
                else
                {
                    wrongAnswerButtonOnClick();
                }
            }
        });

        answer2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (answer2Btn.getText().toString().equalsIgnoreCase(correctAnswer)) {
                    rightAnswerButtonOnClick();
                }
                else
                {
                    wrongAnswerButtonOnClick();
                }
            }
        });

        answer3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (answer3Btn.getText().toString().equalsIgnoreCase(correctAnswer))
                {
                    rightAnswerButtonOnClick();
                }
                else
                {
                    wrongAnswerButtonOnClick();
                }
            }
        });
    }

    /**
     * the method for updating the view for the next question.
     */
    private void updateQuestion()
    {
        if(noOfQuestion == 10)      //last question.
        {
            //Toast.makeText(getApplicationContext(), "Questions Limit Reached!!", Toast.LENGTH_SHORT).show();

            questionsTV.setVisibility(View.GONE);
            questionNoTV.setVisibility(View.GONE);
            answer1Btn.setVisibility(View.GONE);
            answer2Btn.setVisibility(View.GONE);
            answer3Btn.setVisibility(View.GONE);
            viewLBBtn.setVisibility(View.VISIBLE);
            congratsImgView.setVisibility(View.VISIBLE);
            starImgView.setVisibility(View.VISIBLE);

            if(backgroundMusicPlayer.isPlaying()) {
                backgroundMusicPlayer.stop();
            }
            congratsMPlayer.start();
            clapSoundPlayer.start();
        }
        else
        {
            noOfQuestion++;     //increment the number of question.
            resetButtonsPosition(answer1Btn, answer2Btn, answer3Btn, 0);

            //update the view with next question and answers.
            questionsTV.setText(questionsAndAnswersLibrary.getQuestion(questionNo));
            answer1Btn.setText(questionsAndAnswersLibrary.getAnswerChoice1(questionNo));
            answer2Btn.setText(questionsAndAnswersLibrary.getAnswerChoice2(questionNo));
            answer3Btn.setText(questionsAndAnswersLibrary.getAnswerChoice3(questionNo));

            correctAnswer = questionsAndAnswersLibrary.getCorrectAnswer(questionNo);
            questionNo++;

            moveButtons(answer1Btn);
            moveButtons(answer2Btn);
            moveButtons(answer3Btn);
        }
    }

    /**
     * the  method for animating the buttons by passing the duration (speed) of animation and the translation of y axis.
     * @param button
     */
    private void moveButtons(Button button)
    {
        button.animate().setDuration(20000);    //the speed of animation
        button.animate().translationY(1200);    //the value of y axis up to where the button moves
        checkButtonTranslate(button);
    }

    /**
     * the method for getting the current y axis translation of the button.
     * if the button has gone past the bottom of the screen, update the view (new question)
     * @param button
     */
    private void checkButtonTranslate(Button button)
    {
        int[] loc = new int[2];
        button.getLocationOnScreen(loc);
        int y = loc[1];

        if (y > 1200) {
            //Toast.makeText(getApplicationContext(),"EXCEDDED! " + y, Toast.LENGTH_SHORT).show();
            updateQuestion();
        }
    }

    /**
     * the method for resetting the buttons' positions by specifying the value for y axis translation.
     * @param button1
     * @param button2
     * @param button3
     * @param translateY
     */
    private void resetButtonsPosition(Button button1, Button button2, Button button3, int translateY) {
        button1.setTranslationY(translateY);
        button2.setTranslationY(translateY);
        button3.setTranslationY(translateY);
    }

    /**
     * the method for starting a vibration.
     */
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(300);  //vibrates for 300 milliseconds.
    }

    /**
     * the method for controlling and updating the views and activities if the button clicked has got the correct answer.
     */
    private void rightAnswerButtonOnClick()
    {
        //if the button clicked has the correct answer, then do the following;

        yesMPlayer.start();
        updateQuestion();

        Toast.makeText(getApplicationContext(), "CORRECT!", Toast.LENGTH_SHORT).show();

        score += 100;
        scoreEditText.setText(score + "");

        streak += 1;
        streakEditText.setText(streak + "");

        questionPos += 1;
        questionNoTV.setText("Question " + noOfQuestion + " of 10");

        if (score == 1000) {
            newPlayer.insertPlayerStatsToDatabase(player, year, score + "", streak + "");
            //Toast.makeText(getApplicationContext(), "OVERRRRRR\t" + score, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * the method for controlling the updating the views and activities if the button clicked has got the wrong answer.
     */
    private void wrongAnswerButtonOnClick()
    {
        //if the button clicked has the wrong answer, then do the following;
        noOfLifelines += 1;

        vibrate();
        noMPlayer.start();
        streak = 0;
        streakEditText.setText(streak + "");

        if(noOfLifelines == 1) {
            Toast.makeText(getApplicationContext(), "WRONG!!", Toast.LENGTH_SHORT).show();
            lifeline1.setVisibility(View.INVISIBLE);
        }

        else if(noOfLifelines == 2) {
            Toast.makeText(getApplicationContext(), "WRONG!!", Toast.LENGTH_SHORT).show();
            lifeline2.setVisibility(View.INVISIBLE);
        }

        else if(noOfLifelines == 3)
        {
            newPlayer.insertPlayerStatsToDatabase(player, year, score + "", streak + "");

            Toast.makeText(getApplicationContext(), "WRONG and GAME OVER!!", Toast.LENGTH_SHORT).show();

            lifeline3.setVisibility(View.INVISIBLE);

            if(backgroundMusicPlayer.isPlaying()) {
                backgroundMusicPlayer.stop();
            }
            gameOverMPlayer.start();

            questionsTV.setVisibility(View.GONE);
            questionNoTV.setVisibility(View.GONE);
            answer1Btn.setVisibility(View.GONE);
            answer2Btn.setVisibility(View.GONE);
            answer3Btn.setVisibility(View.GONE);

            starImgView.setVisibility(View.INVISIBLE);
            gameOverTV.setVisibility(View.VISIBLE);
            gameOverImgView.setVisibility(View.VISIBLE);
            viewLBBtn.setVisibility(View.VISIBLE);
        }
    }
}