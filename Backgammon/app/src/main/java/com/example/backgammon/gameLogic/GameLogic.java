package com.example.backgammon.gameLogic;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.widget.Toast;

import com.example.backgammon.ImageData;
import com.example.backgammon.models.GameData;
import com.example.backgammon.player.Player;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Marija on 12.2.2018..
 */

public class GameLogic {

    public void setStartingRow(int startingRow) {
        gameData.setStartingRow(startingRow);
    }

    public interface GameInterface {
        void setShakeEnable(boolean shakeEnable);

        void setClickEnable(boolean clickEnable);

        void refresh(String message, boolean renderImage);

        void changeActivePlayer();

        void enableDices(boolean enable);

        void setDices(int diceOne, int diceTwo);
    }

    private GameData gameData;
    private GameInterface gameInterface;
    private ImageData imageData;

    public GameLogic(int compNum, String player1, String player2, GameInterface gameInterface, ImageData imageData) {
        this.gameInterface = gameInterface;
        this.imageData = imageData;
        gameData = new GameData(compNum, player1, player2);
        throwDices();
    }

    private void setTable() {
        int[] table = gameData.getTable();
        table[0] = 2;
        table[5] = -5;
        table[7] = -3;
        table[11] = 5;
        table[12] = -5;
        table[16] = 3;
        table[18] = 5;
        table[23] = -2;

        imageData.setCheckers(0, 2, Color.DKGRAY);
        imageData.setCheckers(5, 5, Color.WHITE);
        imageData.setCheckers(7, 3, Color.WHITE);
        imageData.setCheckers(11, 5, Color.DKGRAY);
        imageData.setCheckers(12, 5, Color.WHITE);
        imageData.setCheckers(16, 3, Color.DKGRAY);
        imageData.setCheckers(18, 5, Color.DKGRAY);
        imageData.setCheckers(23, 2, Color.WHITE);
    }

    public void sizeSet() {
        setTable();
    }

    private void determineOrder(int diceValue) {
        /*jedan igraca baca kocke
        * zatm sledeci baca kocke
        * odredi ko je currentPlayer
        * i pozovi throwDices*/
        if (gameData.getDices()[0] == 0) {
            gameData.setDiceOne(diceValue);
            gameInterface.changeActivePlayer();
            gameData.changeCurrentPlayer();
            throwDices();
        } else {
            gameData.setDiceTwo(diceValue);
            int[] dices = gameData.getDices();
            if (dices[0] > dices[1]) {
                gameData.setCurrentPlayer(0);
                gameInterface.changeActivePlayer();
            } else
                gameData.setCurrentPlayer(1);
            gameData.gameState = GameData.ThrowDices;
            throwDices();
        }
    }

    private void throwDices() {
        /*trazi od igraca da baci kocke
        * (za sad radi random)*/
        gameInterface.refresh("Please throw the dices", true);
        gameData.getPlayers()[gameData.getCurrentPlayer()].rollDices(this);
    }

    public void dicesThrown() {
        if (gameData.gameState == GameData.SelectPlayer)
            determineOrder((int) (Math.random() * 5) + 1);
        else {
             /*poziva se kada korisnik baci kocke
            * odavde se postavi modelu sta treba i
            * prelazi na calculateMoves*/
            int diceOne = (int) (Math.random() * 5) + 1;
            int diceTwo = (int) (Math.random() * 5) + 1;
            gameInterface.setDices(diceOne, diceTwo);
            gameData.setDices(diceOne, diceTwo);
            gameInterface.enableDices(false);
            gameData.gameState = GameData.CalculateMoves;
            calculateMoves();
        }
    }

    private void calculateMoves() {
        /*trazi od trenutnog stanja da ti
        * izracuna poteze, pa renderuj sliku opet
        * i predji na sledece stanje play
        * ako nema poteza, predji na out of moves*/
        int calcState = gameData.getPlayers()[gameData.getCurrentPlayer()].getState();
        gameData.getCalculationState(calcState).calculateMoves(gameData);
        if (gameData.getPossibleMoves().isEmpty()) {
            gameData.gameState = GameData.outOfMoves;
            outOfMoves();
        } else {
            if (gameData.getPossibleMoves().size() == 1 && gameData.getPossibleMoves().get(-1) != null)
                imageData.highlightFromBlot(-1, gameData.getPlayers()[gameData.getCurrentPlayer()].getCheckers());
            else
                imageData.highlightCheckers(gameData.getPossibleMoves().keySet());
            gameInterface.refresh("Make your move. You can move highlighted checkers.", true);
            if (gameData.gameState == GameData.CalculateMoves)
                gameData.gameState = GameData.playingFirstTime;
            else
                gameData.gameState = GameData.playingSecongTime;
            play();
        }
    }

    private void play() {
        /*kazi igracu da igra*/
        gameData.getPlayers()[gameData.getCurrentPlayer()].play(this);
    }

    public void endOfMove(int newPos) {
        /*igrac zavrsio potez. proveri ako je prvi potez,
        * da li ima jos poteza. Ako je drugi potez, da li opet
        * baca kocke. Ako nista, predji na sledeceg i opet sve
        * */
        gameInterface.setClickEnable(false);
        int old = gameData.getStartingRow();
        gameData.setStartingRow(-2);
        if (newPos < -1) {
            imageData.highlightCheckers(gameData.getPossibleMoves().keySet());
            gameInterface.refresh("Make your move. You can move highlighted checkers.", true);
            play();
        } else {
            if (gameData.gameState == GameData.endOfSecondMove) {
                gameData.gameState = GameData.ThrowDices;
                gameData.changeCurrentPlayer();
                throwDices();
                return;
            }
            int move = Math.abs(newPos - old);
            if (old == -1 && gameData.getCurrentPlayer() == 0)
                move = 24 - newPos;
            int[] dices = gameData.getDices();
            boolean oneMove;
            if (oneMove = dices[0] == move)
                dices[0] = -1;
            else if (oneMove = dices[1] == move)
                dices[1] = -1;
            else
                dices[0] = dices[1] = -1;

            if (oneMove && (dices[0] > -1 || dices[1] > -1)) {
                gameData.gameState = GameData.CalculateMovesSecond;
                calculateMoves();
            } else {
                gameData.gameState = GameData.ThrowDices;
                gameData.changeCurrentPlayer();
                throwDices();
            }

        }
    }

    private void finishGame() {
    }

    private void outOfMoves() {
        Toast.makeText((Context) gameInterface, "No more moves.", Toast.LENGTH_LONG).show();
        gameData.gameState = GameData.ThrowDices;
        gameData.changeCurrentPlayer();
        throwDices();
    }

    public void setClickEnable() {
        gameInterface.setClickEnable(true);
    }

    public void setShakeEnable() {
        gameInterface.setShakeEnable(true);
        gameInterface.enableDices(true);
    }

    public void fingerDown(PointF position) {
        int id = imageData.highlightedClicked(position);
        gameData.setStartingRow(id);
        if (id > -2) {
            ArrayList<Integer> possibleFields = gameData.getPossibleMoves().get(id);
            imageData.highlightTriangles(possibleFields);
            imageData.resetColorChecker();
            gameInterface.refresh("Make your move. You can drop on highlighted triangles.", true);
        }
    }

    public void fingerMove(PointF position) {
        if (gameData.getStartingRow() > -2)
            imageData.moveChecker(position);
        gameInterface.refresh("Make your move. You can drop on highlighted triangles.", true);
    }

    public void fingerUp(PointF position) {
        Player currPlayer = gameData.getPlayers()[gameData.getCurrentPlayer()];
        if (gameData.getStartingRow() > -2) {
            int r = imageData.dropOnTriangle(position, gameData.getPossibleMoves().get(gameData.getStartingRow()));
            if (r >= 0) {
                moveChecker(gameData.getStartingRow(), r);
                imageData.putSelectedCheckerOnBoard(r);

            } else if(currPlayer.getState() == CalculationState.BearingOffState && imageData.droppedOnHomeBar(position)){
                imageData.removeSelectedChecker();
                r = currPlayer.getCheckers() == 1? 24: -1;

            }
            else{
                imageData.putSelectedCheckerOnBoard(gameData.getStartingRow());
                imageData.highlightCheckers(gameData.getPossibleMoves().keySet());
            }
            imageData.resetColorTriangles(gameData.getPossibleMoves().get(gameData.getStartingRow()));
            endOfMove(r);

        }
    }


    public LinkedHashMap<Integer, ArrayList<Integer>> getPossibleMoves() {
        return gameData.getPossibleMoves();
    }

    public void moveChecker(int oldPosition, int newPosition) {
        Player currentPlayer = gameData.getPlayers()[gameData.getCurrentPlayer()];
        int[] table = gameData.getTable();
        if (newPosition < 24 && newPosition > -1)
            table[newPosition] += currentPlayer.getCheckers();
        if (oldPosition > -1) {
            table[oldPosition] -= currentPlayer.getCheckers();
            if (gameData.countOutsideHomeBoard(currentPlayer.getCheckers()) == 0)
                currentPlayer.setState(CalculationState.BearingOffState);
        } else if (!imageData.hasMoreOnBlot(currentPlayer.getCheckers()))
            currentPlayer.setState(CalculationState.RegularState);
        if (table[newPosition] == 0) {
            table[newPosition] = currentPlayer.getCheckers();
            imageData.moveToBlot(newPosition);
            gameData.getPlayers()[(gameData.getCurrentPlayer() + 1) % 2].setState(CalculationState.BlotState);
        }
    }

    public void moveGUIChecker(int oldPosition, int newPosition) {
        int checkerColor = gameData.getCurrentPlayer() == 0 ? Color.WHITE : Color.DKGRAY;
        imageData.resetColorChecker();
        if (oldPosition == -1)
            imageData.removeFromBlot(checkerColor);
        else
            imageData.removeChecker(oldPosition);
        moveChecker(oldPosition, newPosition);
        if (newPosition < 24 && newPosition > -1)
            imageData.setCheckers(newPosition, 1, checkerColor);

    }

    public void continueGame(GameData gameData) {
        ///nastavi igru
    }
}
