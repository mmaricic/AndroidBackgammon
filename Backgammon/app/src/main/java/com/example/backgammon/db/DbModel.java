package com.example.backgammon.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Marija on 17.2.2018..
 */

public class DbModel {
    private DbOpenHelper dbHelper;
    private DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    public DbModel(Context context) {
        dbHelper = new DbOpenHelper(context);
    }

    public void saveData(String name1, String name2, String winner) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put(DBContract.Score.KEY_PLAYER1, name1);
        values.put(DBContract.Score.KEY_PLAYER2, name2);
        values.put(DBContract.Score.KEY_WINNER, winner);
        values.put(DBContract.Score.KEY_TIME, formatter.format(new Date()));

        long rowId = db.insert(DBContract.Score.TABLE_NAME, null, values);
    }


    public Cursor selectTwoPlayerStatistic(String player1, String player2) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = "player1 = ? and player2 = ?";

        Cursor cursor = db.query(DBContract.Score.TABLE_NAME, null, selection, new String[]{player1, player2},
                null, null, "time DESC");
        return cursor;
    }

    public Cursor selectAll(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT _id, player1, SUM(CASE WHEN winner = player1 THEN 1 ELSE 0 END) as player1wins, " +
                "player2, SUM(CASE WHEN winner = player2 THEN 1 ELSE 0 END) as player2wins " +
                "from "+ DBContract.Score.TABLE_NAME+" group by player1, player2";

        return db.rawQuery(sql, null);
    }

    public long getNumberOfWins(String player1, String player2, String winner) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        long res = DatabaseUtils.queryNumEntries(db, DBContract.Score.TABLE_NAME);

        String [] args= {player1, player2, winner};
        String selection = "player1 = ? and player2 = ? and winner = ?";
        return DatabaseUtils.queryNumEntries(db, DBContract.Score.TABLE_NAME, selection, args);
    }

    public void deleteData(String player1, String player2) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(DBContract.Score.TABLE_NAME, "player1 = ? and player2 = ?", new String[]{player1, player2});
    }

    public void deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(DBContract.Score.TABLE_NAME, null, null);
    }
}
