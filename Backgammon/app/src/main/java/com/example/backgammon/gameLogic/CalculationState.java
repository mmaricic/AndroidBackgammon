package com.example.backgammon.gameLogic;

import com.example.backgammon.models.GameData;

/**
 * Created by Marija on 12.2.2018..
 */

public interface CalculationState {
    public static final int RegularState = 0;
    public static final int BlotState = 1;
    public static final int BearingOffState = 2;

    public void caluclateMoves(GameData gameData);
}
