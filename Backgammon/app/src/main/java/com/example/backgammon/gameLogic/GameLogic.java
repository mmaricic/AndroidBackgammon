package com.example.backgammon.gameLogic;

import android.graphics.Color;
import android.graphics.PointF;

import com.example.backgammon.Checker;
import com.example.backgammon.models.GameData;
import com.example.backgammon.ImageData;

/**
 * Created by Marija on 12.2.2018..
 */

public class GameLogic {

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
    private Checker oldPosition;
    private ImageData imageData;

    public GameLogic(boolean multiplayer, String player1, String player2, GameInterface gameInterface, ImageData imageData) {
        this.gameInterface = gameInterface;
        this.imageData = imageData;
        gameData = new GameData(multiplayer, player1, player2);
        throwDices();
    }

    private void setTable() {
        int [] table = gameData.getTable();
        table[0] = -2;
        table[5] = 5;
        table[7] = 3;
        table[11] = -5;
        table[12] = 5;
        table[16] = -3;
        table[18] = -5;
        table[23] = 2;

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
            gameInterface.refresh("Please throw the dice", false);
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
        gameInterface.enableDices(true);
        gameInterface.refresh("Please throw the dices", false);
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
        int calcState = gameData.getCurrentPlayer().getState();
        gameData.getCalculationState(calcState).caluclateMoves(gameData);
        if (gameData.getPossibleMoves().isEmpty()) {
            gameData.gameState = GameData.outOfMoves;
            outOfMoves();
        } else {
            imageData.highlightCheckers(gameData.getPossibleMoves().keySet());
            gameInterface.refresh("Make your move. You can move highlighted checkers.", true);
            gameData.gameState = GameData.playing;
            play();
        }
    }

    private void play() {
        /*kazi igracu da igra*/
        gameData.getCurrentPlayer().play();
    }

    public void endOfMove() {
        /*igrac zavrsio potez. proveri ako je prvi potez,
        * da li ima jos poteza. Ako je drugi potez, da li opet
        * baca kocke. Ako nista, predji na sledeceg i opet sve
        * */
    }

    private void finishGame() {
    }

    private void outOfMoves() {
    }

    public void setClickEnable(boolean clickEnable) {
        gameInterface.setClickEnable(clickEnable);
    }

    public void fingerDown(PointF position) {
    }

    public void fingerMove(PointF position) {
    }

    public void fingerUp(PointF position) {
    }

    public void continueGame(GameData gameData) {
        ///nastavi igru
    }
}
