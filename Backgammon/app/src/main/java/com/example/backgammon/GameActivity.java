package com.example.backgammon;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.backgammon.gameLogic.GameLogic;


public class GameActivity extends AppCompatActivity implements GameLogic.GameInterface, View.OnTouchListener {
    private GameLogic gameLogic;
    private boolean shakeEnabled = false;
    private boolean clickEnabled = false;
    private TextView messageBox;
    private TextView[] players = new TextView[2];
    private int player = 1;
    private BoardView table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        String player1 = intent.getStringExtra("PLAYER1");
        String player2 = intent.getStringExtra("PLAYER2");
        int compNum = intent.getIntExtra("COMP_NUM", -1);
        players[0] = ((TextView) findViewById(R.id.player1Name));
        players[1] = ((TextView) findViewById(R.id.player2Name));
        players[1].setTextColor(Color.parseColor("#a18d78"));
        table = ((BoardView) findViewById(R.id.board));
        table.setOnTouchListener(this);
        ImageData imageData = table.getImageData();
        messageBox = ((TextView) findViewById(R.id.notification));
        setPlayersData(player1, player2);
        gameLogic = new GameLogic(compNum, player1, player2, this, imageData);
        imageData.setController(gameLogic);
    }

    private void setPlayersData(String player1, String player2) {
        ((TextView) findViewById(R.id.player1Name)).setText(player1);
        ((TextView) findViewById(R.id.player2Name)).setText(player2);

        final ImageView image1 = ((ImageView) findViewById(R.id.player1Checker));
        image1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                image1.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                setImage(image1, Color.WHITE);
            }
        });
        final ImageView image2 = ((ImageView) findViewById(R.id.player2Checker));
        image2.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                image2.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                setImage(image2, Color.DKGRAY);
            }
        });
    }

    private void setImage(ImageView image, int color) {
        int w = image.getWidth();
        Bitmap tempBitmap = Bitmap.createBitmap(w, w, Bitmap.Config.ARGB_8888);
        Canvas tempCanvas = new Canvas(tempBitmap);
        Checker tempChecker = new Checker(color, new PointF(w / 2, w / 2));
        tempCanvas.drawCircle(w / 2, w / 2, w / 2 - 3, tempChecker.getColor());
        tempCanvas.drawCircle(w / 2, w / 2, w / 2 - 3, tempChecker.getBorderColor());
        image.setImageBitmap(tempBitmap);
    }

    @Override
    public void setShakeEnable(boolean shakeEnable) {
        this.shakeEnabled = shakeEnable;
    }

    @Override
    public void setClickEnable(boolean clickEnable) {
        this.clickEnabled = clickEnable;
    }

    @Override
    public void refresh(String message, boolean renderImage) {
        messageBox.setText(message);
        if (renderImage)
            table.invalidate();
    }

    @Override
    public void changeActivePlayer() {
        /*obelezi nekako na tabli ko je trenutni igrac*/
        players[player].setTextColor(Color.WHITE);
        player = (player + 1) % 2;
        players[player].setTextColor(Color.parseColor("#a18d78"));
    }

    @Override
    public void enableDices(boolean enable) {
        ((Button) findViewById(R.id.roll)).setEnabled(enable);
    }

    @Override
    public void setDices(int diceOne, int diceTwo) {
        ((Button) findViewById(R.id.roll)).setText(diceOne + " " + diceTwo);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!clickEnabled)
            return false;
        PointF position = new PointF(event.getX(), event.getY());
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                gameLogic.fingerDown(position);
                return true;
            case MotionEvent.ACTION_MOVE:
                gameLogic.fingerMove(position);
                return true;
            case MotionEvent.ACTION_UP:
                gameLogic.fingerUp(position);
                return true;

        }
        return false;
    }

    public void roll(View v) {
        gameLogic.dicesThrown();
    }
}
