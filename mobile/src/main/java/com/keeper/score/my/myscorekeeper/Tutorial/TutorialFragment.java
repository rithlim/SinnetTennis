package com.keeper.score.my.myscorekeeper.Tutorial;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.keeper.score.common.Enum;
import com.keeper.score.common.IAnnouncements;
import com.keeper.score.common.IGameListener;
import com.keeper.score.common.ISetScore;
import com.keeper.score.common.ITutorial;
import com.keeper.score.my.myscorekeeper.MatchGame.AwayGameFragment;
import com.keeper.score.my.myscorekeeper.MatchGame.HomeGameFragment;
import com.keeper.score.my.myscorekeeper.R;

public class TutorialFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView instructionLabel;
    private View view;
    private View setBoardView;

    private IGameListener gameListener;

    private boolean endTutorialComplete = false;
    private boolean beginTutorialComplete = false;
    private boolean swipeUpTutorialComplete = false;
    private boolean swipeDownTutorialComplete = false;
    private boolean resetGameTutorialComplete = false;
    private boolean resetSetTutorialComplete = false;
    private boolean increaseHomeSideComplete = false;

    private Enum.GAME_SCORE homeSide = Enum.GAME_SCORE.FIFTEEN;
    private Enum.GAME_SCORE awaySide = Enum.GAME_SCORE.THIRTY;

    private static float yStart = 0;
    private static float yEnd = 0;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TutorialFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TutorialFragment newInstance(String param1, String param2) {
        TutorialFragment fragment = new TutorialFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public TutorialFragment() {
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
        view = inflater.inflate(R.layout.fragment_tutorial, container, false);
        setBoardView = view.findViewById(R.id.set_tutorial_container);
        instructionLabel = (TextView) view.findViewById(R.id.tutorial_label);
        animateTextView();
        setupGameSetScores();
        setupListeners();
        return view;
    }

    private void setupGameSetScores() {
        ISetScore setListener = (ISetScore) getActivity();
        setListener.updateSetScores(HomeGameFragment.class.getSimpleName());
        setListener.updateSetScores(HomeGameFragment.class.getSimpleName());

        //First Set
        for (int i = 0; i < 6; i++) {
            setListener.updateSetScores(AwayGameFragment.class.getSimpleName());
        }

        //Second Set
        setListener.secondSetActive();
        for (int i = 0; i < 4; i++) {
            setListener.updateSetScores(AwayGameFragment.class.getSimpleName());
        }
        for (int i = 0; i < 5; i++) {
            setListener.updateSetScores(HomeGameFragment.class.getSimpleName());
        }

        gameListener = (IGameListener) getActivity();
        gameListener.updateAllGameScores(homeSide, "15", awaySide, "30");
        ((IAnnouncements) getActivity()).setAnnouncements(getString(R.string.tutorial_announcer_label), getString(R.string.tutorial_announcer_sub_label), true);
    }

    private void setupListeners() {
        if (view != null) {

            setBoardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (resetGameTutorialComplete && !resetSetTutorialComplete) {
                        ((ISetScore) getActivity()).resetSetScores();
                        ((IAnnouncements) getActivity()).setAnnouncements(getString(R.string.tutorial_announcer_complete), getString(R.string.tutorial_announcer_complete_sub_header), true);
                        instructionLabel.setText(getString(R.string.tutorial_lets_play));
                        animateTextView();
                        //instructionLabel.setText(getString(R.string.tutorial_lets_play));
                        endTutorialComplete = true;
                        resetSetTutorialComplete = true;
                    }
                    return true;
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (swipeDownTutorialComplete && !resetGameTutorialComplete) {
                        gameListener.resetGameScores();
                        instructionLabel.setText(getString(R.string.tutorial_reset_set));
                        //instructionLabel.setText(getString(R.string.tutorial_lets_play));
                        resetGameTutorialComplete = true;
                    }
                    return true;
                }
            });

            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int side = view.getWidth() / 2;
                    switch (MotionEventCompat.getActionMasked(event)) {
                        case (MotionEvent.ACTION_DOWN):
                            yStart = event.getY();
                            break;
                        case (MotionEvent.ACTION_UP):
                            yEnd = event.getY();
                            instructionLabel.clearAnimation();
                            if (!beginTutorialComplete) {
                                beginTutorialComplete = true;
                                instructionLabel.setText(getString(R.string.tutorial_swipe_up));
                            } else if (!swipeUpTutorialComplete && isAcceptableGesture()) {
                                if (yStart > yEnd) {
                                    swipeUpTutorialComplete = true;
                                    instructionLabel.setText(getString(R.string.tutorial_swipe_down));
                                    if (event.getX() >= side) {
                                        increaseHomeSideComplete = true;
                                        gameListener.updateAllGameScores(Enum.GAME_SCORE.THIRTY, "30", Enum.GAME_SCORE.THIRTY, "30");
                                    } else {
                                        gameListener.updateAllGameScores(Enum.GAME_SCORE.FIFTEEN, "15", Enum.GAME_SCORE.FORTY, "40");
                                    }
                                }
                            } else if (!swipeDownTutorialComplete && isAcceptableGesture() && (yStart < yEnd)) {
                                swipeDownTutorialComplete = true;
                                instructionLabel.setText(getString(R.string.tutorial_reset_game));
                                if (event.getX() >= side && increaseHomeSideComplete) {
                                    gameListener.updateAllGameScores(Enum.GAME_SCORE.FIFTEEN, "15", Enum.GAME_SCORE.THIRTY, "30");
                                } else if (event.getX() >= side && !increaseHomeSideComplete) {
                                    gameListener.updateAllGameScores(Enum.GAME_SCORE.LOVE, getString(R.string.heart_ascii), Enum.GAME_SCORE.FORTY, "40");
                                } else if (event.getX() <= side && !increaseHomeSideComplete) {
                                    gameListener.updateAllGameScores(Enum.GAME_SCORE.FIFTEEN, "15", Enum.GAME_SCORE.THIRTY, "30");
                                } else {
                                    gameListener.updateAllGameScores(Enum.GAME_SCORE.THIRTY, "30", Enum.GAME_SCORE.FIFTEEN, "15");
                                }
                            } else if (resetGameTutorialComplete && !resetSetTutorialComplete) {
                                resetGameTutorialComplete = true;
                            } else if (endTutorialComplete) {
                                ((ITutorial) getActivity()).endTutorial();
                            }
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private boolean isAcceptableGesture() {
        if (Math.abs(yStart - yEnd) < 50) {
            return false;
        } else {
            return true;
        }
    }

    private void animateTextView() {
        instructionLabel.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.heart_beat_infinite));
    }
}
