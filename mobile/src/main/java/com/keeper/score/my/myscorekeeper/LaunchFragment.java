package com.keeper.score.my.myscorekeeper;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.keeper.score.common.*;
import com.keeper.score.common.Enum;


/**
 * A simple {@link Fragment} subclass.
 */
public class LaunchFragment extends Fragment {
    private static final String TAG = LaunchFragment.class.getSimpleName();
    private static TextView tvPlayButton;
    private static TextView tvStatsButton;

    public LaunchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_launch, container, false);
        tvPlayButton = (TextView) view.findViewById(R.id.launch_play_button);
        tvStatsButton = (TextView) view.findViewById(R.id.statistics_button);
        setupListeners();
        return view;
    }

    private void setupListeners(){
        tvPlayButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = MotionEventCompat.getActionMasked(event);
                switch (action) {
                    case (MotionEvent.ACTION_DOWN):
                        tvPlayButton.setTextSize(25f);
                        return false;
                    case (MotionEvent.ACTION_UP):
                        tvPlayButton.setTextSize(20f);
                        play();
                        return false;
                }
                return false;
            }
        });

        tvStatsButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = MotionEventCompat.getActionMasked(event);
                switch (action) {
                    case (MotionEvent.ACTION_DOWN):
                        Log.d(TAG, "ACTION_DOWN");
                        tvStatsButton.setTextSize(25f);
                        return false;
                    case (MotionEvent.ACTION_UP):
                        Log.d(TAG, "ACTION_UP");
                        tvStatsButton.setTextSize(20f);
                        return false;
                }
                return false;
            }
        });
    }

    private void play() {
        ((ILaunch)getActivity()).performAction(Enum.LAUNCH_BUTTON.PLAY);
    }
}
