package com.example.backgammon;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        ((ImageView) findViewById(R.id.logo)).setImageBitmap(img);

    }

    @Override
    protected void onStart() {
        super.onStart();
        String path = getFilesDir().getAbsolutePath() + "/savedGame.txt";
        File file = new File(path);

        if(!file.exists())
            ((Button)findViewById(R.id.continueGame)).setEnabled(false);
        else
            ((Button)findViewById(R.id.continueGame)).setEnabled(true);
    }

    public void newGame(View v) {
        Intent intent = new Intent(this, SelectGameActivity.class);
        startActivity(intent);
    }

    public void continueGame(View v){
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("savedGame", true);
        startActivity(intent);
    }

    public void openStatistic(View view) {
        Intent intent = new Intent(this, StatisticActivity.class);
        startActivity(intent);
    }
}
