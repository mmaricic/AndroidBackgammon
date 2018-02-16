package com.example.backgammon.models;

import com.example.backgammon.gameLogic.BearingOffStateCalculation;
import com.example.backgammon.gameLogic.BlotStateCalculation;
import com.example.backgammon.gameLogic.CalculationState;
import com.example.backgammon.gameLogic.RegularStateCalculation;
import com.example.backgammon.player.CompPlayer;
import com.example.backgammon.player.HumanPlayer;
import com.example.backgammon.player.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Marija on 12.2.2018..
 */

public class GameData implements Serializable {

    public static final int SelectPlayer = 0;
    public static final int ThrowDices = 1;
    public static final int CalculateMoves = 2;
    public static final int CalculateMovesSecond = 3;
    public static final int playingFirstTime = 4;
    public static final int playingSecongTime = 5;
    public static final int endOfFirstMove = 6;
    public static final int endOfSecondMove = 7;
    public static final int outOfMoves = 8;

    private CalculationState[] calculationStates = new CalculationState[3];
    private int[] table = new int[24];
    private Player[] players = new Player[2];
    private int[] blots = new int[2];
    public int gameState = SelectPlayer;
    private int startingRow;

    private int currentPlayer;
    private int[] dices = new int[2];

    private LinkedHashMap<Integer, ArrayList<Integer>> possibleMoves;


    public GameData(int compNum, String player1, String player2) {
        calculationStates[0] = new RegularStateCalculation();
        calculationStates[1] = new BlotStateCalculation();
        calculationStates[2] = new BearingOffStateCalculation();

        for (int i = 0; i < 24; i++)
            table[i] = 0;

        players[0] = compNum == 0? new CompPlayer(-1, player1) : new HumanPlayer(-1, player1);
        players[1] = compNum == 1 ? new CompPlayer(1, player2) : new HumanPlayer(1, player2);

    }

    public CalculationState[] getCalculationStates() {
        return calculationStates;
    }

    public void setCalculationStates(CalculationState[] calculationStates) {
        this.calculationStates = calculationStates;
    }

    public int[] getTable() {
        return table;
    }

    public void setTable(int[] table) {
        this.table = table;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public int[] getBlots() {
        return blots;
    }

    public void setBlots(int[] blots) {
        this.blots = blots;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int[] getDices() {
        return dices;
    }

    public void setDices(int diceOne, int diceTwo) {
        dices[0] = diceOne;
        dices[1] = diceTwo;
    }

    public LinkedHashMap<Integer, ArrayList<Integer>> getPossibleMoves() {
        return possibleMoves;
    }

    public void setPossibleMoves(LinkedHashMap<Integer, ArrayList<Integer>> possibleMoves) {
        this.possibleMoves = possibleMoves;
    }


    public void setDiceOne(int diceOne) {
        dices[0] = diceOne;
    }

    public void setDiceTwo(int diceTwo) {
        dices[1] = diceTwo;
    }

    public CalculationState getCalculationState(int calcState) {
        return calculationStates[calcState];
    }

    public void setStartingRow(int startingRow) {
        this.startingRow = startingRow;
    }

    public int getStartingRow() {
        return startingRow;
    }

    public void changeCurrentPlayer() {
        currentPlayer = (currentPlayer+1)%2;
    }

    public int countOutsideHomeBoard(int checkers) {
        int count = 0;
        if(checkers == 1){
            for(int i = 0; i < 18; i++)
                if(table[i] > 0)
                    count+=table[i];
        }else{
            for(int i = 6; i < 24; i++)
                if(table[i] < 0)
                    count+=table[i];
        }
        return count;
    }
}
