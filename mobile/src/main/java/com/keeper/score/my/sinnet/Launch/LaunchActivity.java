package com.keeper.score.my.sinnet.Launch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.keeper.score.common.ILaunch;
import com.keeper.score.my.sinnet.MatchGame.MatchActivity;
import com.keeper.score.my.sinnet.Records.MatchRecordListActivity;
import com.keeper.score.my.sinnet.R;
import com.keeper.score.utils.FragmentUtils;
import com.keeper.score.utils.MatchRecordManager;

public class LaunchActivity extends Activity implements ILaunch {
    private static final String TAG = LaunchActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        FragmentUtils.launchFragment(getFragmentManager(), new LaunchFragment(), R.id.launch_activity_container, false, LaunchFragment.class.getSimpleName());
    }

    @Override
    public void performAction(com.keeper.score.common.Enum.LAUNCH_BUTTON button) {
        switch (button) {
            case PLAY:
                Intent intent = new Intent(this, MatchActivity.class);
                startActivity(intent);
                break;
            case MATCH_RECORD_LIST:
                if(MatchRecordManager.loadMatchRecordList(this)) {
                    Log.d(TAG, "MatchRecordList loaded.");
                    intent = new Intent(this, MatchRecordListActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "No Records Available", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
