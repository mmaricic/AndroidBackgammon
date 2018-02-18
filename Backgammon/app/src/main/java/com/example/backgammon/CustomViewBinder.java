package com.example.backgammon;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.backgammon.db.DBContract;

/**
 * Created by Marija on 17.2.2018..
 */

public class CustomViewBinder implements SimpleCursorAdapter.ViewBinder {


    @Override
    public boolean setViewValue(View view, Cursor cursor,
                                int columnIndex) {
        String winner = cursor.getString(cursor.getColumnIndex(DBContract.Score.KEY_WINNER));
        if ((columnIndex == cursor.getColumnIndex(DBContract.Score.KEY_PLAYER1)||
                columnIndex == cursor.getColumnIndex(DBContract.Score.KEY_PLAYER2)) && winner.equals(cursor.getString(columnIndex))) {

            ((TextView)view).setTypeface(((TextView)view).getTypeface(), Typeface.BOLD_ITALIC);
            ((TextView)view).setTextColor(Color.WHITE);
            ((TextView)view).setText( cursor.getString(columnIndex));
            return true;
        }
        return false;
    }
}
