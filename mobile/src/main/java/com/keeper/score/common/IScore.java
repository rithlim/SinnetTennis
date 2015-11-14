package com.keeper.score.common;

/**
 * Created by Rith on 10/31/2015.
 */
public interface IScore extends IPlayers{
    void updateAllGameScores(Enum.GAME_SCORE serverScore, String serverString, Enum.GAME_SCORE receiverScore, String receiverString);
    void submitGameResults(String tag, Enum.GAME_SCORE playerScore);
    void resetGameScores();
    void setScoringSystem(Enum.SCORING_SYSTEM scoringSystem);
    Enum.SCORING_SYSTEM getScoringSystem();
}
