package com.keeper.score.common;

/**
 * Created by Rith on 11/6/2015.
 */
public interface IAlertDialog {
    void showAlert(String title, String msg, String posBtnText, String negBtnTxt, String neutralBtnText, Enum.ALERT_TYPE alertType);
}
