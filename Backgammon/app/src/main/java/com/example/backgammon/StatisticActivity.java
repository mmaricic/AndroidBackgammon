package com.example.backgammon;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.backgammon.db.DBContract;

public class StatisticActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    StatisticController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        controller = new StatisticController(this);
        String[] from = {DBContract.Score.KEY_PLAYER1, DBContract.Score.KEY_PLAYER2, "player1wins","player2wins", DBContract.Score._ID};
        int[] to = {R.id.player1_stat, R.id.player2_stat, R.id.player1_wins, R.id.player2_wins};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.list_stat, null, from, to, 0);
        ListView lv = ((ListView) findViewById(R.id.list_stat));
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
        setCursor();
    }

    public void deleteData(View view) {
        controller.deleteAll();
        ListView lv = ((ListView) findViewById(R.id.list_stat));
        ((SimpleCursorAdapter)lv.getAdapter()).changeCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = ((SimpleCursorAdapter)parent.getAdapter()).getCursor();
        String player1 = cursor.getString(cursor.getColumnIndex(DBContract.Score.KEY_PLAYER1));
        String player2 = cursor.getString(cursor.getColumnIndex(DBContract.Score.KEY_PLAYER2));
        Intent intent = new Intent(this, TwoPlayersStatisticActivity.class);
        intent.putExtra("Player1", player1);
        intent.putExtra("Player2", player2);
        startActivityForResult(intent, 0);

    }

    private void setCursor(){
        Cursor cursor = controller.selectAll();
        ListView lv = ((ListView) findViewById(R.id.list_stat));
        ((SimpleCursorAdapter)lv.getAdapter()).changeCursor(cursor);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        setCursor();
    }
}
