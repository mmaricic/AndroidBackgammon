package com.example.backgammon.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.backgammon.db.DBContract.Score;

/**
 * Created by Marija on 17.2.2018..
 */

public class DbOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "backgammon.db";

    public static final int DATABASE_VERSION = 1;

    public static final String CREATE_TABLE;

    static{
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE ");
        query.append(Score.TABLE_NAME);
        query.append(" (");
        query.append(Score._ID);
        query.append(" INTEGER PRIMARY KEY, ");
        query.append(Score.KEY_PLAYER1);
        query.append(" TEXT, ");
        query.append(Score.KEY_PLAYER2);
        query.append(" TEXT, ");
        query.append(Score.KEY_TIME);
        query.append(" TEXT, ");
        query.append(Score.KEY_WINNER);
        query.append(" TEXT);");

        CREATE_TABLE = query.toString();
    }

    public DbOpenHelper(Context context) {
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
