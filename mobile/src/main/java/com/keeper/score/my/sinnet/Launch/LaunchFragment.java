package com.keeper.score.my.sinnet.Launch;


import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.keeper.score.common.*;
import com.keeper.score.common.Enum;
import com.keeper.score.my.sinnet.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class LaunchFragment extends Fragment {
    private static final String TAG = LaunchFragment.class.getSimpleName();
    private static TextView tvPlayButton;
    private static TextView tvRecordsButton;

    public LaunchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_launch, container, false);
        tvPlayButton = (TextView) view.findViewById(R.id.launch_play_button);
        tvRecordsButton = (TextView) view.findViewById(R.id.records_button);
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

        tvRecordsButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = MotionEventCompat.getActionMasked(event);
                switch (action) {
                    case (MotionEvent.ACTION_DOWN):
                        Log.d(TAG, "ACTION_DOWN");
                        tvRecordsButton.setTextSize(25f);
                        return false;
                    case (MotionEvent.ACTION_UP):
                        Log.d(TAG, "ACTION_UP");
                        tvRecordsButton.setTextSize(20f);
                        getRecords();
                        return false;
                }
                return false;
            }
        });
    }

    private void play() {
        ((ILaunch)getActivity()).performAction(Enum.LAUNCH_BUTTON.PLAY);
    }

    private void getRecords() {
        ((ILaunch)getActivity()).performAction(Enum.LAUNCH_BUTTON.MATCH_RECORD_LIST);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void delay(int durationInSeconds) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                play();
            }
        }, durationInSeconds * 1000);
    }

    private void rotateImage() {
//        RotateAnimation anim = new RotateAnimation(0f, 350f, 15f, 15f);
//        anim.setInterpolator(new LinearInterpolator());
//        anim.setRepeatCount(Animation.INFINITE);
//        anim.setDuration(1000);

// Start animating the image

// Later.. stop the animation
        //ivSinnetLogo.setAnimation(null);
    }
}
