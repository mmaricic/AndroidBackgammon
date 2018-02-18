package com.example.backgammon.db;

import android.provider.BaseColumns;

/**
 * Created by Marija on 17.2.2018..
 */

public class DBContract {
    public static class Score implements BaseColumns {
        public static final String TABLE_NAME = "gameScore";
        public static final String KEY_PLAYER1 = "player1";
        public static final String KEY_PLAYER2 = "player2";
        public static final String KEY_TIME = "time";
        public static final String KEY_WINNER = "winner";
    }
}
