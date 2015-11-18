package com.keeper.score.my.myscorekeeper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.keeper.score.common.*;
import com.keeper.score.common.Enum;
import com.keeper.score.utils.FragmentUtils;

public class LaunchActivity extends AppCompatActivity implements ILaunch{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        FragmentUtils.launchFragment(getFragmentManager(), new LaunchFragment(), R.id.launch_activity_container, true, LaunchFragment.class.getSimpleName());
    }

    @Override
    public void performAction(com.keeper.score.common.Enum.LAUNCH_BUTTON button) {
        switch(button) {
            case PLAY:
                Intent intent = new Intent(this, MatchActivity.class);
                startActivity(intent);
                break;
            case STATISTICS:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        showAlert("Quit App",
//                "Are you sure you want to quit?",
//                "Yes",
//                "No",
//                null,
//                Enum.ALERT_TYPE.QUIT_APP);
    }

    public void showAlert(String title, String msg, String posBtnText, String negBtnTxt, String neutralBtnText, final Enum.ALERT_TYPE alertType) {
          AlertDialog.Builder myDialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setCancelable(false)
                .setMessage(msg)
                .setPositiveButton(posBtnText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (alertType) {
                            case QUIT_APP:
                                finish();
                                break;
                        }
                    }
                })
                .setNegativeButton(negBtnTxt, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);
        if (neutralBtnText != null && !neutralBtnText.isEmpty()) {
            myDialog.setNeutralButton(neutralBtnText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
        }
        myDialog.show();
    }
}
