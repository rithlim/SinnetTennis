package com.keeper.score.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Rith on 11/18/2015.
 */
public class MatchRecord implements Serializable{

    private String matchDate;
    private String homePlayerName;
    private String awayPlayerName;
    private String homePlayerFirstSetScore;
    private String homePlayerSecondSetScore;
    private String homePlayerThirdSetScore;
    private String awayPlayerFirstSetScore;
    private String awayPlayerSecondSetScore;
    private String awayPlayerThirdSetScore;

    public String getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(String matchDate) {
        this.matchDate = matchDate;
    }

    public String getHomePlayerName() {
        return homePlayerName;
    }

    public void setHomePlayerName(String homePlayerName) {
        this.homePlayerName = homePlayerName;
    }

    public String getAwayPlayerName() {
        return awayPlayerName;
    }

    public void setAwayPlayerName(String awayPlayerName) {
        this.awayPlayerName = awayPlayerName;
    }

    public String getHomePlayerFirstSetScore() {
        return homePlayerFirstSetScore;
    }

    public void setHomePlayerFirstSetScore(String homePlayerFirstSetScore) {
        this.homePlayerFirstSetScore = homePlayerFirstSetScore;
    }

    public String getHomePlayerSecondSetScore() {
        return homePlayerSecondSetScore;
    }

    public void setHomePlayerSecondSetScore(String homePlayerSecondSetScore) {
        this.homePlayerSecondSetScore = homePlayerSecondSetScore;
    }

    public String getHomePlayerThirdSetScore() {
        return homePlayerThirdSetScore;
    }

    public void setHomePlayerThirdSetScore(String homePlayerThirdSetScore) {
        this.homePlayerThirdSetScore = homePlayerThirdSetScore;
    }

    public String getAwayPlayerFirstSetScore() {
        return awayPlayerFirstSetScore;
    }

    public void setAwayPlayerFirstSetScore(String awayPlayerFirstSetScore) {
        this.awayPlayerFirstSetScore = awayPlayerFirstSetScore;
    }

    public String getAwayPlayerSecondSetScore() {
        return awayPlayerSecondSetScore;
    }

    public void setAwayPlayerSecondSetScore(String awayPlayerSecondSetScore) {
        this.awayPlayerSecondSetScore = awayPlayerSecondSetScore;
    }

    public String getAwayPlayerThirdSetScore() {
        return awayPlayerThirdSetScore;
    }

    public void setAwayPlayerThirdSetScore(String awayPlayerThirdSetScore) {
        this.awayPlayerThirdSetScore = awayPlayerThirdSetScore;
    }
}
