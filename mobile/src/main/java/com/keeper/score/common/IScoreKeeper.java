package com.keeper.score.common;

/**
 * Created by Rith on 10/28/2015.
 */
public interface IScoreKeeper extends IScore{
    void increaseScore();
    void decreaseScore();
    void setKeeper(IScoreKeeper scoreKeeper);
}
