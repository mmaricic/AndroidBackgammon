package com.example.backgammon;

import android.content.Context;
import android.database.Cursor;

import com.example.backgammon.db.DbModel;

/**
 * Created by Marija on 17.2.2018..
 */

public class StatisticController {
    DbModel dbModel;

    public StatisticController(Context context){
        dbModel = new DbModel(context);
    }

    public Cursor selectTwoPlayerStatistic(String player1, String player2) {
        return dbModel.selectTwoPlayerStatistic(player1, player2);
    }

    public long getNumberOfWins(String player1, String player2, String winner) {
        return dbModel.getNumberOfWins(player1, player2, winner);
    }

    public void deleteData(String player1, String player2) {
        dbModel.deleteData(player1, player2);
    }

    public Cursor selectAll() {
        return dbModel.selectAll();
    }

    public void deleteAll() {
        dbModel.deleteAll();
    }
}
