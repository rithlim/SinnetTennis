package com.keeper.score.my.myscorekeeper;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.keeper.score.common.IAnnouncements;

public class AnnouncerFragment extends Fragment implements IAnnouncements {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView tvAnnouncementLabel;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AnnouncerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AnnouncerFragment newInstance(String param1, String param2) {
        AnnouncerFragment fragment = new AnnouncerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AnnouncerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_announcer, container, false);
        tvAnnouncementLabel = (TextView) view.findViewById(R.id.announcement_label);
        setListeners();
        return view;
    }

    void setListeners() {
        tvAnnouncementLabel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(getActivity(), tvAnnouncementLabel.getText().toString(), Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void setAnnouncements(String text) {
        if (tvAnnouncementLabel != null) {
            tvAnnouncementLabel.setText(text);
            bounceAnimation();
        }
    }

    private int getDisplayHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    private void bounceAnimation() {
        tvAnnouncementLabel.clearAnimation();
        //tvAnnouncementLabel.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.bounce));
        TranslateAnimation translation;
        translation = new TranslateAnimation(0f, 0F, getDisplayHeight() / 2, 50);
        translation.setStartOffset(500);
        translation.setDuration(2000);
        translation.setFillAfter(true);
        translation.setInterpolator(new BounceInterpolator());
        tvAnnouncementLabel.startAnimation(translation);
    }
}
