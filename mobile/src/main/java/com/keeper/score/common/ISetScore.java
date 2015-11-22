package com.keeper.score.common;

/**
 * Created by Rith on 11/6/2015.
 */
public interface ISetScore extends IPlayers{
    void resetSetScores();
    void setFinalSetTBSetScore(int homeTBScore, int awayTBScore);
    void updateSetScores(String tag);
    void enableTieBreakerMode(boolean mode);
    boolean isTieBreakerModeEnabled();
    Enum.CURRENT_SET getCurrentSet();
    void endGameSetMatch(String winner);
    void firstSetActive();
    void secondSetActive();
    void thirdSetActive();
    void recordMatchScore();
}
