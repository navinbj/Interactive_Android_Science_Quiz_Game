package com.example.navin.assignment1_game_nba;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;

/**
 * The Instructions Activity will not perform anything logically but only focuses on the view for
 * displaying instructions on how to play a game.
 */
public class InstructionsActivity extends AppCompatActivity {

    private TextView mTextMessage;
    Button playGameBtn;
    String playerName = "";
    String yearField = "";
    ImageView testImgView;

    View.OnClickListener playGameBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            //Toast.makeText(getApplicationContext(),"INT: " + playerName,Toast.LENGTH_LONG).show();

            //sending data to another activity (GameActivity) using intent.
            Intent intent = new Intent(InstructionsActivity.this, GameActivity.class);
            intent.putExtra("name", playerName);
            intent.putExtra("year", yearField);

            Drawable drawable = testImgView.getDrawable();  //getting ImageView's drawable if its not null.
            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap(); //getting the bitmap used by drawable to render image.
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();  //new bytes array
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream); //compressing the format, quality and the stream of bitmap.
            byte[] bytes = byteArrayOutputStream.toByteArray();
            intent.putExtra("image", bytes);

            startActivity(intent);
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Toast.makeText(getApplicationContext(),"HOME Pressed",Toast.LENGTH_LONG).show();
                    return true;
                case R.id.navigation_leaderboard:
                    Toast.makeText(getApplicationContext(),"LEADERBOARD Pressed",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(InstructionsActivity.this, LeaderboardActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_settings:
                    Toast.makeText(getApplicationContext(),"SETTINGS Pressed",Toast.LENGTH_LONG).show();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        playGameBtn = findViewById(R.id.playGameBtn);
        playGameBtn.setOnClickListener(playGameBtnOnClickListener);
        testImgView = findViewById(R.id.testImgView);

        //retrieving data from previous activity using bundle and intent.
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String name = extras.getString("playerName");
            playerName = name;

            String year = extras.getString("year");
            yearField += year;
            //Toast.makeText(getApplicationContext(), year + "", Toast.LENGTH_LONG).show();

            byte[] bytes = extras.getByteArray("picture");      //storing the bytes of an image in a local array variable
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);  //decoding an immutable bitmap
            ImageView image = (ImageView) findViewById(R.id.testImgView);
            image.setImageBitmap(bitmap);   //setting a Bitmap as the content of this ImageView.
        }
    }
}