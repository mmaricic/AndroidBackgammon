package com.example.backgammon.player;

import android.os.AsyncTask;

import com.example.backgammon.gameLogic.GameLogic;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Marija on 12.2.2018..
 */

public class CompPlayer extends Player {
    public CompPlayer(int checkers, String name) {
        super(checkers, name);
    }

    @Override
    public void play(GameLogic gameLogic) {

      new Movement(gameLogic).execute();
    }

    @Override
    public void rollDices(GameLogic gameLogic) {
        gameLogic.dicesThrown();
    }

    private class Movement extends AsyncTask< Void, int[], Integer>{
        private GameLogic gameLogic;

        public Movement(GameLogic gameLogic){
            this.gameLogic = gameLogic;
        }

        @Override
        protected Integer doInBackground( Void... voids) {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LinkedHashMap<Integer, ArrayList<Integer>> moves = gameLogic.getPossibleMoves();
            ArrayList<Integer> possibleMoves = null;
            int oldPosition = -1;
            while((possibleMoves = moves.get(oldPosition)) == null) {
                oldPosition = (int) (Math.random() * 24);
            }
            int newPosition = (int) (Math.random()*possibleMoves.size());
            if(oldPosition == -1)
                newPosition = 0;
            int res[] = new int[2];
            res[0] = oldPosition;
            res[1] = possibleMoves.get(newPosition);
            publishProgress(res);
            gameLogic.setStartingRow(oldPosition);
            return possibleMoves.get(newPosition);
        }


        @Override
        protected void onProgressUpdate(int[]... positions) {
            gameLogic.moveGUIChecker(positions[0][0], positions[0][1]);
        }

        @Override
        protected void onPostExecute(Integer newPosition) {
            gameLogic.endOfMove(newPosition);
        }
    }
}
