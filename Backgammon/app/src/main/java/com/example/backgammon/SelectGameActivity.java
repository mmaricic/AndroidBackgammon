package com.example.backgammon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class SelectGameActivity extends AppCompatActivity {
    int oldSelectedPlayer1;
    int oldSelectedPlayer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_game);
        oldSelectedPlayer1 = R.id.human1;
        oldSelectedPlayer2 = R.id.human2;
    }

    public void player1TypeChanged(View v) {
        int checked = ((RadioButton) v).getId();
        if (checked == oldSelectedPlayer1)
            return;
        if (checked == R.id.computer1) {
            ((EditText) findViewById(R.id.player1)).setText("Comp");
            ((EditText) findViewById(R.id.player1)).setEnabled(false);
            ((RadioButton) findViewById(R.id.computer2)).setClickable(false);

        } else {
            ((EditText) findViewById(R.id.player1)).setEnabled(true);
            ((EditText) findViewById(R.id.player1)).setText("");
            ((RadioButton) findViewById(R.id.computer2)).setClickable(true);
        }
        oldSelectedPlayer1 = checked;
    }

    public void player2TypeChanged(View v) {
        int checked = ((RadioButton) v).getId();
        if (checked == oldSelectedPlayer2)
            return;
        if (checked == R.id.computer2) {
            ((EditText) findViewById(R.id.player2)).setText("Comp");
            ((EditText) findViewById(R.id.player2)).setEnabled(false);
            ((RadioButton) findViewById(R.id.computer1)).setClickable(false);

        } else {
            ((EditText) findViewById(R.id.player2)).setEnabled(true);
            ((EditText) findViewById(R.id.player2)).setText("");
            ((RadioButton) findViewById(R.id.computer1)).setClickable(true);
        }
        oldSelectedPlayer2 = checked;
    }

    public void startGame(View view) {
        boolean multiplayer = true;
        String player1 = ((EditText) findViewById(R.id.player1)).getText().toString();
        String player2 = ((EditText) findViewById(R.id.player2)).getText().toString();
        if (player1 == null || player1.isEmpty() || player2 == null || player2.isEmpty())
            ((TextView) findViewById(R.id.error)).setText("Players names are required!");
        else {
            if (oldSelectedPlayer2 == R.id.computer2 || oldSelectedPlayer1 == R.id.computer1)
                multiplayer = false;
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("MULTIPLAYER", multiplayer);
            intent.putExtra("PLAYER1", player1);
            intent.putExtra("PLAYER2", player2);
            startActivity(intent);
        }
    }
}
