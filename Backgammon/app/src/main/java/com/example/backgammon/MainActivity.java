package com.example.backgammon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.backgammon.db.DbModel;

import java.io.File;

import static com.example.backgammon.UtilParameter.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        ((ImageView) findViewById(R.id.logo)).setImageBitmap(img);

        SharedPreferences settings = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
        float detectShake = settings.getFloat(DEFAULT_DETECT_SHAKE, -100);
        if(detectShake == -100){
            SharedPreferences.Editor editor = settings.edit();
            editor.putFloat(DEFAULT_DETECT_SHAKE, 7f);
            editor.putFloat(DEFAULT_MIN_SHAKE, 0.8f);
            editor.apply();
        }

       // setStatistic();
    }

    private void setStatistic() {
        DbModel dbModel = new DbModel(this);
        dbModel.saveData("Comp", "Marija", "Comp");
        dbModel.saveData("Comp", "Marija", "Marija");
        dbModel.saveData("Comp", "Marija", "Comp");
        dbModel.saveData("Comp", "Marija", "Comp");
        dbModel.saveData("Marija", "Sofija", "Sofija");
        dbModel.saveData("Comp", "Sofija", "Comp");
        dbModel.saveData("Marija", "Sofija", "Sofija");
        dbModel.saveData("Comp", "Ivana", "Ivana");
        dbModel.saveData("Ivana", "Marija", "Comp");
        dbModel.saveData("Ivana", "Petar", "Petar");
        dbModel.saveData("Marija","Petar", "Marija");

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

    public void openParameters(View view) {
        Intent intent = new Intent(this, ParametersActivity.class);
        startActivity(intent);
    }
}
