package com.example.backgammon.player;

import com.example.backgammon.gameLogic.GameLogic;

/**
 * Created by Marija on 12.2.2018..
 */

public class HumanPlayer extends Player {
    public HumanPlayer(int checkers, String name) {
        super(checkers, name);
    }

    @Override
    public void play(GameLogic gameLogic) {
        gameLogic.setClickEnable();
    }

    @Override
    public void rollDices(GameLogic gameLogic) {
        gameLogic.setShakeEnable();

    }
}
