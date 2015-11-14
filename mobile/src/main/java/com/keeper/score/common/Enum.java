package com.keeper.score.common;

import android.app.Application;

import com.keeper.score.my.myscorekeeper.MainActivity;
import com.keeper.score.my.myscorekeeper.R;

/**
 * Created by Rith on 10/31/2015.
 */
public class Enum {

    public static String GAME_LOVE;
    public static final String GAME_FIFTEEN = "15";
    public static final String GAME_THIRTY = "30";
    public static final String GAME_FORTY = "40";
    public static final String GAME_DEUCE = "deuce";
    public static final String GAME_ADD_IN = "ad in";
    public static final String GAME_ADD_OUT = "ad out";
    public static final String GAME_WIN = "game";
    public static final String GAME_LOSE = "-";

    public enum GAME_SCORE {
        LOVE,
        FIFTEEN,
        THIRTY,
        FORTY,
        DEUCE,
        ADD_IN,
        ADD_OUT,
    }

    public enum ALERT_TYPE {
        RESET_GAME,
        RESET_SET,
        CONFIRM_GAME_SCORE,
        SCORING_SYSTEM,
        GAME_SET_MATCH
    }

    public enum CURRENT_SET {
        FIRST_SET,
        SECOND_SET,
        THIRD_SET,
        RESET
    }

    public enum SCORING_SYSTEM {
        FULL_SET_SCORING,
        SEVEN_POINT_SCORING,
        TEN_POINT_SCORING
    }
}
