package com.keeper.score.my.sinnet.MatchGame;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.keeper.score.common.Enum;
import com.keeper.score.common.IAlertDialog;
import com.keeper.score.common.IAnnouncements;
import com.keeper.score.common.IPlayers;
import com.keeper.score.common.IScore;
import com.keeper.score.common.ISetScore;
import com.keeper.score.models.MatchRecord;
import com.keeper.score.my.sinnet.R;
import com.keeper.score.utils.MatchRecordManager;

import java.util.Calendar;


public class SetScoreFragment extends Fragment implements IPlayers, ISetScore {
    private static final String TAG = SetScoreFragment.class.getSimpleName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static TextView tvHomePlayerName;
    private static TextView tvAwayPlayerName;

    private static TextView tvHomePlayerFirstSetScore;
    private static TextView tvHomePlayerSecondSetScore;
    private static TextView tvHomePlayerThirdSetScore;
    private static TextView tvAwayPlayerFirstSetScore;
    private static TextView tvAwayPlayerSecondSetScore;
    private static TextView tvAwayPlayerThirdSetScore;

    private static int mHomePlayerSetOneScore;
    private static int mHomePlayerSetTwoScore;
    private static int mHomePlayerSetThreeScore;
    private static int mAwayPlayerSetOneScore;
    private static int mAwayPlayerSetTwoScore;
    private static int mAwayPlayerSetThreeScore;
    private static int mSetWonByHomePlayer = 0;
    private static int mSetWonByAwayPlayer = 0;

    private static Enum.CURRENT_SET mCurrentSet;

    private static boolean mTieBreakerMode;
    public static boolean mIsNewMatch = true;

    private ISetScore setScoreCallback;

    private static Calendar beginMatchDate;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SetScoreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SetScoreFragment newInstance(String param1, String param2) {
        SetScoreFragment fragment = new SetScoreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SetScoreFragment() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_score, container, false);
        injectViews(view);
        setupViewListener(view);
        mCurrentSet = Enum.CURRENT_SET.FIRST_SET;
        resetSetScores();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setScoreCallback = (ISetScore) getActivity();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setScoreCallback = (ISetScore) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /***********
     * IPlayers
     **********/
    @Override
    public String getPlayersName(String tag) {
        if (tag.equalsIgnoreCase(HomeGameFragment.class.getSimpleName())) {
            return tvHomePlayerName.getText().toString();
        } else {
            return tvAwayPlayerName.getText().toString();
        }

        //return setScoreTableView.getPlayersName(tag);
    }

    @Override
    public void setPlayersName(String tag, String name) {
        if (tag.equalsIgnoreCase(HomeGameFragment.class.getSimpleName())) {
            tvHomePlayerName.setText(name);
        } else {
            tvAwayPlayerName.setText(name);
        }
    }

    private void injectViews(View view) {
        tvHomePlayerName = (TextView) view.findViewById(R.id.set_frame_home_player_name);
        tvAwayPlayerName = (TextView) view.findViewById(R.id.set_frame_away_player_name);

        tvHomePlayerFirstSetScore = (TextView) view.findViewById(R.id.set_frame_home_first_set_score);
        tvHomePlayerSecondSetScore = (TextView) view.findViewById(R.id.set_frame_home_second_set_score);
        tvHomePlayerThirdSetScore = (TextView) view.findViewById(R.id.set_frame_home_third_set_score);

        tvAwayPlayerFirstSetScore = (TextView) view.findViewById(R.id.set_frame_away_first_set_score);
        tvAwayPlayerSecondSetScore = (TextView) view.findViewById(R.id.set_frame_away_second_set_score);
        tvAwayPlayerThirdSetScore = (TextView) view.findViewById(R.id.set_frame_away_third_set_score);
    }

    //Helper Methods
    private void setupViewListener(View view) {
        if (view != null) {
            //Server/Receiver Label- Click Listener
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //Log.d(TAG, "view.onLongClick()");
                    ((IAlertDialog) getActivity()).showAlert(
                            getString(R.string.reset_set_title),
                            getString(R.string.reset_set_msg),
                            getString(R.string.positive_btn_text),
                            getString(R.string.negative_btn_text),
                            null,
                            Enum.ALERT_TYPE.RESET_SET);
                    return true;
                }
            });

            tvHomePlayerFirstSetScore.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    @Override
    public void resetSetScores() {
        beginMatchDate = null;
        beginMatchDate = Calendar.getInstance();
        mIsNewMatch = true;
        mHomePlayerSetOneScore = 0;
        mHomePlayerSetTwoScore = 0;
        mHomePlayerSetThreeScore = 0;

        mAwayPlayerSetOneScore = 0;
        mAwayPlayerSetTwoScore = 0;
        mAwayPlayerSetThreeScore = 0;

        mCurrentSet = Enum.CURRENT_SET.FIRST_SET;
        mTieBreakerMode = false;
        mSetWonByAwayPlayer = 0;
        mSetWonByHomePlayer = 0;

        if (tvHomePlayerFirstSetScore != null) {
            setSetScore(tvHomePlayerFirstSetScore, 0);
            setSetScore(tvHomePlayerSecondSetScore, 0);
            setSetScore(tvHomePlayerThirdSetScore, 0);

            setSetScore(tvAwayPlayerFirstSetScore, 0);
            setSetScore(tvAwayPlayerSecondSetScore, 0);
            setSetScore(tvAwayPlayerThirdSetScore, 0);
        }
    }

    @Override
    public void setFinalSetTBSetScore(int homeTBScore, int awayTBScore) {
        setSetScore(tvHomePlayerThirdSetScore, homeTBScore);
        setSetScore(tvAwayPlayerThirdSetScore, awayTBScore);
    }

    private void setSetScore(TextView textView, int setScore) {
        textView.setText(String.valueOf(setScore));
    }

    @Override
    public void updateSetScores(String tag) {
        switch (mCurrentSet) {
            case FIRST_SET:
                mIsNewMatch = false;
                if (tag.equalsIgnoreCase(HomeGameFragment.class.getSimpleName())) {
                    mHomePlayerSetOneScore += 1;
                    setSetScore(tvHomePlayerFirstSetScore, mHomePlayerSetOneScore);
                } else {
                    mAwayPlayerSetOneScore += 1;
                    setSetScore(tvAwayPlayerFirstSetScore, mAwayPlayerSetOneScore);
                }
                break;
            case SECOND_SET:
                if (tag.equalsIgnoreCase(HomeGameFragment.class.getSimpleName())) {
                    mHomePlayerSetTwoScore += 1;
                    setSetScore(tvHomePlayerSecondSetScore, mHomePlayerSetTwoScore);
                } else {
                    mAwayPlayerSetTwoScore += 1;
                    setSetScore(tvAwayPlayerSecondSetScore, mAwayPlayerSetTwoScore);
                }
                break;
            case THIRD_SET:
                if (tag.equalsIgnoreCase(HomeGameFragment.class.getSimpleName())) {
                    mHomePlayerSetThreeScore += 1;
                    setSetScore(tvHomePlayerThirdSetScore, mHomePlayerSetThreeScore);
                } else {
                    mAwayPlayerSetThreeScore += 1;
                    setSetScore(tvAwayPlayerThirdSetScore, mAwayPlayerSetThreeScore);
                }
                break;
            case RESET:
                resetSetScores();
        }

        updateCurrentSet();
    }

    @Override
    public void enableTieBreakerMode(boolean mode) {
        mTieBreakerMode = mode;
        if (mode) {
            String currentSet = "";
            if (getCurrentSet().equals(Enum.CURRENT_SET.FIRST_SET))
                currentSet = getString(R.string.announcement_set_one);
            else if (getCurrentSet().equals(Enum.CURRENT_SET.SECOND_SET))
                currentSet = getString(R.string.announcement_set_two);
            else if (getCurrentSet().equals(Enum.CURRENT_SET.THIRD_SET))
                currentSet = getString(R.string.announcement_set_three);
            ((IAnnouncements) getActivity()).setAnnouncements(currentSet, "7-Point TB", true);
            ((IScore) getActivity()).setScoringSystem(Enum.SCORING_SYSTEM.SEVEN_POINT_SCORING);
        }
    }

    @Override
    public boolean isTieBreakerModeEnabled() {
        return mTieBreakerMode;
    }

    @Override
    public Enum.CURRENT_SET getCurrentSet() {
        return mCurrentSet;
    }

    @Override
    public void endGameSetMatch(String winner) {
        MatchActivity.updateAlertId(R.drawable.ic_trophy);
        setScoreCallback.endGameSetMatch(winner);
    }

    @Override
    public void firstSetActive() {
        setScoreCallback.firstSetActive();
        ((IScore) getActivity()).setScoringSystem(Enum.SCORING_SYSTEM.FULL_SET_SCORING);
    }

    @Override
    public void secondSetActive() {
        recordFirstSetWinner();
        setScoreCallback.secondSetActive();
        ((IScore) getActivity()).setScoringSystem(Enum.SCORING_SYSTEM.FULL_SET_SCORING);
    }

    @Override
    public void thirdSetActive() {
        recordSecondSetWinner();
        setScoreCallback.thirdSetActive();
        if (isSplitSet()) {
            ((IAlertDialog) getActivity()).showAlert(
                    getString(R.string.third_set_scoring_title),
                    getString(R.string.third_set_scoring_msg),
                    getString(R.string.third_set_scoring_game_pos),
                    getString(R.string.third_set_scoring_10_point_neg),
                    null,
                    Enum.ALERT_TYPE.SCORING_SYSTEM);
        } else {
            MatchActivity.updateAlertId(R.drawable.ic_trophy);
            endGameSetMatch(getMatchWinner());
        }
    }

    @Override
    public void recordMatchScore() {
        MatchRecord matchRecord = new MatchRecord();
        matchRecord.setHomePlayerName(getPlayersName(HomeGameFragment.class.getSimpleName()));
        matchRecord.setHomePlayerFirstSetScore(tvHomePlayerFirstSetScore.getText().toString());
        matchRecord.setHomePlayerSecondSetScore(tvHomePlayerSecondSetScore.getText().toString());
        matchRecord.setHomePlayerThirdSetScore(tvHomePlayerThirdSetScore.getText().toString());

        matchRecord.setAwayPlayerName(getPlayersName(AwayGameFragment.class.getSimpleName()));
        matchRecord.setAwayPlayerFirstSetScore(tvAwayPlayerFirstSetScore.getText().toString());
        matchRecord.setAwayPlayerSecondSetScore(tvAwayPlayerSecondSetScore.getText().toString());
        matchRecord.setAwayPlayerThirdSetScore(tvAwayPlayerThirdSetScore.getText().toString());

        Calendar endMatchDate = Calendar.getInstance();
        //Date
        StringBuilder matchDate = new StringBuilder();
        matchDate.append("Date: ")
                .append(endMatchDate.get(Calendar.YEAR)).append("/")
                .append(endMatchDate.get(Calendar.MONTH) + 1).append("/")
                .append(endMatchDate.get(Calendar.DATE));
        matchRecord.setMatchDate(matchDate.toString());

        //Begin Time
        if (beginMatchDate != null) {
            StringBuilder matchBeginTime = new StringBuilder();
            matchBeginTime.append("Start: ")
                    .append(convert24To12(beginMatchDate.get(Calendar.HOUR_OF_DAY))).append(":")
                    .append(getDisplayMinute(beginMatchDate.get(Calendar.MINUTE)));
            if (isMatchInAM(beginMatchDate.get(Calendar.HOUR_OF_DAY))) {
                matchBeginTime.append(" AM");
            } else {
                matchBeginTime.append(" PM");
            }
            matchRecord.setMatchBeginTime(matchBeginTime.toString());
        }

        //End Time
        StringBuilder matchEndTime = new StringBuilder();
        matchEndTime.append("End: ")
                .append(convert24To12(endMatchDate.get(Calendar.HOUR_OF_DAY))).append(":")
                .append(getDisplayMinute(endMatchDate.get(Calendar.MINUTE)));
        if (isMatchInAM(endMatchDate.get(Calendar.HOUR_OF_DAY))) {
            matchEndTime.append(" AM");
        } else {
            matchEndTime.append(" PM");
        }
        matchRecord.setMatchEndTime(matchEndTime.toString());

        MatchRecordManager.recordMatchScore(getActivity(), matchRecord);
    }

    private String getMonth(int month) {
        switch (month) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
        }
        return "undefined";
    }

    private String getDisplayMinute(int minute) {
        return (minute < 10) ? "0" + minute : String.valueOf(minute);
    }

    private int convert24To12(int hour) {
        return ((hour % 12) == 0) ? 12 : (hour % 12);
    }

    private boolean isMatchInAM(int hour) {
        return ((hour < 12) ? true : false);
    }

    private boolean isSplitSet() {
        return (mSetWonByHomePlayer == mSetWonByAwayPlayer);
    }

    private String getMatchWinner() {
        if (mSetWonByAwayPlayer > mSetWonByHomePlayer)
            return tvAwayPlayerName.getText().toString();
        else
            return tvHomePlayerName.getText().toString();
    }

    private void recordFirstSetWinner() {
        //First Set
        if (mHomePlayerSetOneScore > mAwayPlayerSetOneScore)
            mSetWonByHomePlayer++;
        else if (mHomePlayerSetOneScore < mAwayPlayerSetOneScore)
            mSetWonByAwayPlayer++;
    }

    private void recordSecondSetWinner() {
        //Second Set
        if (mHomePlayerSetTwoScore > mAwayPlayerSetTwoScore)
            mSetWonByHomePlayer++;
        else if (mHomePlayerSetTwoScore < mAwayPlayerSetTwoScore)
            mSetWonByAwayPlayer++;
    }

    private void recordThirdSetWinner() {
        if (mHomePlayerSetThreeScore > mAwayPlayerSetThreeScore)
            mSetWonByHomePlayer++;
        else if (mHomePlayerSetThreeScore < mAwayPlayerSetThreeScore)
            mSetWonByAwayPlayer++;
    }

    private void updateCurrentSet() {
        Log.d(TAG, "CurrentSet: " + mCurrentSet.name());
        switch (mCurrentSet) {
            case FIRST_SET:
                if (shouldStartNextSet(mHomePlayerSetOneScore, mAwayPlayerSetOneScore)) {
                    mCurrentSet = Enum.CURRENT_SET.SECOND_SET;
                    Log.d(TAG, "Update CurrentSet: " + mCurrentSet.name());
                    secondSetActive();
                }
                break;
            case SECOND_SET:
                if (shouldStartNextSet(mHomePlayerSetTwoScore, mAwayPlayerSetTwoScore)) {
                    mCurrentSet = Enum.CURRENT_SET.THIRD_SET;
                    Log.d(TAG, "Update CurrentSet: " + mCurrentSet.name());
                    thirdSetActive();
                }
                break;
            case THIRD_SET:
                if (shouldStartNextSet(mHomePlayerSetThreeScore, mAwayPlayerSetThreeScore)) {
                    recordThirdSetWinner();
                    mCurrentSet = Enum.CURRENT_SET.RESET;
                    Log.d(TAG, "Update CurrentSet: " + mCurrentSet.name());
                    endGameSetMatch(getMatchWinner());
                }
                break;
            case RESET:
                resetSetScores();
                break;
        }
    }

    private boolean shouldStartNextSet(int first, int second) {
        Enum.SCORING_SYSTEM scoringSystem = ((IScore) getActivity()).getScoringSystem();
        if (scoringSystem.equals(Enum.SCORING_SYSTEM.TEN_POINT_SCORING) && getCurrentSet().equals(Enum.CURRENT_SET.THIRD_SET)) {
            return true;
        }

        int diff = Math.abs(first - second);
        if (first >= 6 && second >= 6) {
            if (diff >= 1) {
                Log.d(TAG, "NextSet!");
                enableTieBreakerMode(false);
                return true;
            } else if (diff == 0) {
                Log.d(TAG, "TieBreaker Mode!");
                enableTieBreakerMode(true);
                return false;
            }
        } else if (first >= 6 || second >= 6) {
            if (diff >= 2) {
                Log.d(TAG, "NextSet!");
                enableTieBreakerMode(false);
                return true;
            }
        }
        enableTieBreakerMode(false);
        return false;
    }
}
