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
    }
}
