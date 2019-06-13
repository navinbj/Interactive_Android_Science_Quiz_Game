package com.example.navin.assignment1_game_nba;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


/**
 * the class LeaderboardActivity has been created for displaying player's game statistics including
 * name, score and the best streak using a Firebase firestore.
 */
public class LeaderboardActivity extends AppCompatActivity {

    Button mainMenuBtn;
    TextView textView;
    LinearLayout linearLayout;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();      //firebase initialisation.

    View.OnClickListener mainMenuListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //directing user to a leaderboard view activity using intent.
            Intent intent = new Intent(LeaderboardActivity.this, MainActivity.class);
            startActivity(intent);  //starting an intent
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        mainMenuBtn = findViewById(R.id.mainMenuBtn);
        mainMenuBtn.setOnClickListener(mainMenuListener);

        linearLayout = findViewById(R.id.leaderboardLayout);
        textView = new TextView(this);
        textView.setTextSize(20);
        textView.setTextColor(Color.RED);
        textView.setPadding(20, 20, 20, 20);
        textView.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
        displayPlayersData();   //fetching data from firebase database.
        linearLayout.addView(textView);     //adding TextView as a child View to the layout for playing players' stats.
    }

    /**
     * the method for fetching data from firebase in the linear layout.
     * the method will display the top 8 scores.
     */
    void displayPlayersData()
    {
        //sorting the players stats by score and in the descending order.
        FirebaseFirestore.getInstance().collection("players").
                orderBy("score", Query.Direction.DESCENDING).
                get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    int i = 0;
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        i++;

                        //split the values contained in the Map Collection.
                        String[] parts = documentSnapshot.getData().values().toString().split(",");
                        textView.append(i + "\t\t\t\t" + parts[0] + "\t\t\t\t\t\t\t\t\t\t" + parts[2] + "\t\t\t\t\t\t\t\t\t\t" + parts[1]);;
                        textView.append("\n");
                        textView.append("\n");

                        if(i==8)
                            break;
                    }
                }
            }
        });
    }
}