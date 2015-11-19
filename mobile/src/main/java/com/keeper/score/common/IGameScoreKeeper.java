package com.keeper.score.common;

/**
 * Created by Rith on 10/28/2015.
 */
public interface IGameScoreKeeper extends IScore{
    void increaseScore();
    void decreaseScore();
    void setKeeper(IGameScoreKeeper scoreKeeper);
}
