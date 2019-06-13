package com.example.navin.assignment1_game_nba;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;

/**
 * The Class MainActivity provides the view for when the game app is launched for the first time.
 * This Class/ Activity is mainly for letting player to take a picture and enter name and school year
 * before entering into the actual game play instructions and view.
 */
public class MainActivity extends AppCompatActivity {

    EditText nameEditText, yearEditText, ageEditText;
    Button nextBtn;
    ImageButton cameraImgBtn;
    ImageView playerImgView;

    View.OnClickListener nextBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (nameEditText.getText().toString().isEmpty() || yearEditText.getText().toString().isEmpty()) {
                nameEditText.setError("required!");
                yearEditText.setError("required!");
            } else {
                String playerName = "";
                String year = "";
                int yearInInt = 0;

                playerName = nameEditText.getText().toString();
                year = yearEditText.getText().toString();
                yearInInt = Integer.parseInt(year);
                //Toast.makeText(getApplicationContext(),yearInInt + "",Toast.LENGTH_SHORT).show();

                //sending the data to another activity (InstructionsActivity) using intent.
                Intent intent = new Intent(MainActivity.this, InstructionsActivity.class);
                intent.putExtra("playerName", playerName);
                intent.putExtra("year", year);

                //compress the image into bytes to send it to another activity.
                Drawable drawable = playerImgView.getDrawable();    //getting ImageView's drawable if its not null.
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();    //getting the bitmap used by drawable to render image.
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();  //new bytes array
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream); //compressing the format, quality and the stream of bitmap.
                byte[] bytes = byteArrayOutputStream.toByteArray();
                intent.putExtra("picture", bytes);

                startActivity(intent);
            }
        }
    };

    /**
     * letting a user take a picture with the onclick of camera button by sending a player to a media store.
     */
    View.OnClickListener cameraBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Toast.makeText(getApplicationContext(),"CAMERA",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);    //takes to actual camera.
            startActivityForResult(intent, 0);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");  //getting image from intent from camera OnClick() method.
        playerImgView.setImageBitmap(bitmap);   //setting a Bitmap as the content of this ImageView.
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Toast.makeText(getApplicationContext(), "HOME Pressed", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.navigation_leaderboard:
                    Toast.makeText(getApplicationContext(), "LEADERBOARD Pressed", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LeaderboardActivity.class);
                    startActivity(intent);
                    //return true;
                case R.id.navigation_settings:
                    Toast.makeText(getApplicationContext(), "SETTINGS Pressed", Toast.LENGTH_SHORT).show();
                    return true;
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        nameEditText = findViewById(R.id.nameEditText);
        yearEditText = findViewById(R.id.yearEditText);
        nextBtn = findViewById(R.id.nextBtn);
        cameraImgBtn = findViewById(R.id.cameraImgBtn);
        playerImgView = findViewById(R.id.playerImageView);

        nextBtn.setOnClickListener(nextBtnOnClickListener);
        cameraImgBtn.setOnClickListener(cameraBtnListener);
    }
}