package com.example.backgammon.gameLogic;

import com.example.backgammon.models.GameData;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Marija on 12.2.2018..
 */

public class RegularStateCalculation implements CalculationState {
    @Override
    public void caluclateMoves(GameData gameData) {
        HashMap<Integer, ArrayList<Integer>> result = new HashMap<>();
        int[] dices = gameData.getDices();
        int[] board = gameData.getTable();
        int currentPlayer = gameData.getCurrentPlayer().getCheckers();
        if(currentPlayer > 0){
            for(int i = 0; i < 24; i++)
                if(board[i] > 0){
                    ArrayList<Integer> temp = new ArrayList<>();
                    if(i + dices[0] < 24 && board[i + dices[0]] >= -1)
                        temp.add(i+dices[0]);
                    if(i + dices[1] < 24 && board[i + dices[1]] >= -1 )
                        temp.add(i+dices[1]);
                    if( i + dices[1] + dices[0] < 24 && board[i + dices[0]+ dices[1]] >= -1)
                        temp.add(i+dices[0]+dices[1]);
                    if(!temp.isEmpty())
                        result.put(i, temp);
                }
        } else {
            for(int i = 0; i < 24; i++)
                if(board[i] < 0){
                    ArrayList<Integer> temp = new ArrayList<>();
                    if(i - dices[0] > -1 && board[i - dices[0]] <= 1)
                        temp.add(i-dices[0]);
                    if(i - dices[1] > -1 && board[i - dices[1]] <= 1 )
                        temp.add(i-dices[1]);
                    if( i - dices[1] - dices[0] > -1 && board[i - dices[0]- dices[1]] <= 1)
                        temp.add(i-dices[0]-dices[1]);
                    if(!temp.isEmpty())
                        result.put(i, temp);
                }
        }
        gameData.setPossibleMoves(result);
    }
}
