package com.example.backgammon;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import com.example.backgammon.GUIComponents.BoardView;
import com.example.backgammon.GUIComponents.Checker;
import com.example.backgammon.GUIComponents.ImageData;
import com.example.backgammon.gameLogic.GameLogic;


public class GameActivity extends AppCompatActivity implements GameLogic.GameInterface, View.OnTouchListener,
        SensorEventListener {
    private GameLogic gameLogic;
    private boolean shakeEnabled = false;
    private boolean clickEnabled = false;
    private TextView messageBox;
    private TextView[] players = new TextView[2];
    private int player = 1;
    private BoardView table;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private boolean oldValShake;
    private boolean oldValClick;
    private static final float SHAKE_THRESHOLD = 1.3f;
    private static final int SHAKE_SLOP_TIME_MS = 150;
    private long mShakeTimestamp;
    private boolean shaking;
    private boolean paused = false;
    private float accelLastGravity = 0.0f;
    private float accelCurrentGravity = 0.0f;
    private float accel = 0;
    private float alpha = 0.8f;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        players[0] = ((TextView) findViewById(R.id.player1Name));
        players[1] = ((TextView) findViewById(R.id.player2Name));
        players[1].setTextColor(Color.parseColor("#a18d78"));
        table = ((BoardView) findViewById(R.id.board));
        table.setOnTouchListener(this);
        messageBox = ((TextView) findViewById(R.id.notification));
        mediaPlayer = MediaPlayer.create(this, R.raw.dices);
        mediaPlayer.setLooping(true);

        Intent intent = getIntent();
        boolean continueGame = intent.getBooleanExtra("savedGame", false);
        if(!continueGame) {
            String player1 = intent.getStringExtra("PLAYER1");
            String player2 = intent.getStringExtra("PLAYER2");
            int compNum = intent.getIntExtra("COMP_NUM", -1);

            ImageData imageData = table.getImageData();
            setPlayersData(player1, player2);
            gameLogic = new GameLogic(compNum, player1, player2, this, imageData);
            imageData.setController(gameLogic);
        }else{
            ImageData imageData = table.getImageData();
            gameLogic = new GameLogic(this, imageData);
            imageData.setController(gameLogic);
        }

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gameLogic.startGame();
    }

    @Override
    public void setPlayersData(String player1, String player2) {
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

    public void setActivePlayer(int num){
        player = num;
    }
    @Override
    public void changeActivePlayer() {
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
    public void finishGame(String player1, String player2, String winner) {
        Intent intent = new Intent(this, TwoPlayersStatisticActivity.class);
        intent.putExtra("Player1", player1);
        intent.putExtra("Player2", player2);
        intent.putExtra("Winner", winner);
        startActivity(intent);
        finish();
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

    @Override
    public void onBackPressed() {
        gameLogic.saveGame();
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        gameLogic.saveGame();
        super.onStop();
    }

    @Override
    public void onResume() {
        if(paused) {
            shakeEnabled = oldValShake;
            clickEnabled = oldValClick;
            paused = false;
        }
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
       sensorManager.registerListener(this, accelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        sensorManager.unregisterListener(this);
        oldValShake = shakeEnabled;
        shakeEnabled = false;
        oldValClick = clickEnabled;
        clickEnabled = false;
        paused = true;
        super.onPause();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (shakeEnabled) {
            final long now = System.currentTimeMillis();

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            if(mShakeTimestamp == 0){
                mShakeTimestamp = now;
                return;
            }
            // gForce will be close to 1 when there is no movement.
            accelLastGravity = accelCurrentGravity;
            accelCurrentGravity = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = accelCurrentGravity - accelLastGravity;
            accel =  accel * alpha + (1-alpha)*delta;
            if(accel > 1.3)
                Log.d("backgammon",""+accel);
            if (accel > SHAKE_THRESHOLD) {
                // ignore shake events too close to each other (500ms)
                if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                    return;
                }

                if(!shaking)
                    shakeStarted();
            }
            else if(shaking)
                shakeStopped();

            mShakeTimestamp = now;
        }

    }

    private void shakeStopped() {
        Log.d("backgammon", "gotov");
        shaking = false;
        gameLogic.dicesThrown();
        mShakeTimestamp = 0;
        mediaPlayer.stop();
    }

    private void shakeStarted() {
        shaking = true;
        mediaPlayer.start();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
