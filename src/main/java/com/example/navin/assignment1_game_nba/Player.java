package com.example.navin.assignment1_game_nba;

import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

/**
 * The class Player is for handling and managing the player data and game stats.
 */
public class Player {
    private String name;
    private int year, age, score, streak;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();      //firebase initialisation


    public Player(String name, int year) {
        this.name = name;
        this.year = year;
    }

    public Player() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getStreak() {
        return streak;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }

    /**
     * the method for adding players information and game statistics to the firebase database upon a completion of a game.
     * @param name
     * @param year
     * @param score
     * @param streak
     */
    public void insertPlayerStatsToDatabase(String name, String year, String score, String streak)
    {
        Map<String, Object> playerData = new HashMap<String, Object>();
        playerData.put("name", name);
        playerData.put("year", year);
        playerData.put("score", score);
        playerData.put("streak", streak);

        firebaseFirestore.collection("players").add(playerData).
                addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("check", "Document Added with ID: " + documentReference.getId());
//                Toast.makeText(getApplicationContext(),"SUCCEEDED!", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("check2", "Error adding document!", e);
//                Toast.makeText(getApplicationContext(),"FAILED!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
