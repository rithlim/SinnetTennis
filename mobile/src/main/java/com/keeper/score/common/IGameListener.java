package com.keeper.score.common;

/**
 * Created by Rith on 10/29/2015.
 */
public interface IGameListener extends IScore, IServer{
    Enum.GAME_SCORE getGameScore();
    void tieBreakerToggle();
    boolean isLoveGame();
    void setDeuceMode(boolean mode);
    void updateDeuceModelView(boolean mode);
    boolean isDeuceMode();
    Enum.GAME_SCORE getOpponentsGameScore(String iAm);
    int getOpponentTBScore(String tag);
}
