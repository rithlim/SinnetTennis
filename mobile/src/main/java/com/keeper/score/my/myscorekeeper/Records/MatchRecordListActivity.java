package com.keeper.score.my.myscorekeeper.Records;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.keeper.score.my.myscorekeeper.R;
import com.keeper.score.utils.FragmentUtils;

public class MatchRecordListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_record_list);
        FragmentUtils.launchFragment(getFragmentManager(), new MatchRecordListFragment(), R.id.match_record_list_activity_container, true, MatchRecordListFragment.class.getSimpleName());
    }
}
