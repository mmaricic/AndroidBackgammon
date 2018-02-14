package com.example.backgammon;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        ((ImageView) findViewById(R.id.logo)).setImageBitmap(img);
    }

    public void newGame(View v) {
        Intent intent = new Intent(this, SelectGameActivity.class);
        startActivity(intent);
    }
}
