package com.example.backgammon.player;


import com.example.backgammon.gameLogic.CalculationState;

import java.io.Serializable;

/**
 * Created by Marija on 12.2.2018..
 */

public abstract class Player implements Serializable{
    protected int checkers;
    protected int state = CalculationState.RegularState;
    protected String name;

    protected Player(int checkers, String name) {
        this.checkers = checkers;
        this.name = name;
    }

    public int getCheckers() {
        return checkers;
    }

    public void setCheckers(int checkers) {
        this.checkers = checkers;
    }

   public abstract void play();

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
