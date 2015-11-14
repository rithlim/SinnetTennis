package com.keeper.score.my.myscorekeeper;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.keeper.score.common.Enum;
import com.keeper.score.common.IAlertDialog;
import com.keeper.score.common.IAnnouncements;
import com.keeper.score.common.IPlayers;
import com.keeper.score.common.IScore;
import com.keeper.score.common.ISetScore;
import com.keeper.score.views.SetScoreTableView;


public class SetScoreFragment extends Fragment implements IPlayers, ISetScore {
    private static final String TAG = SetScoreFragment.class.getSimpleName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SetScoreTableView setScoreTableView;

    private static int mHomePlayerSetOneScore;
    private static int mHomePlayerSetTwoScore;
    private static int mHomePlayerSetThreeTBScore;
    private static int mAwayPlayerSetOneScore;
    private static int mAwayPlayerSetTwoScore;
    private static int mAwayPlayerSetThreeTBScore;
    private static int mSetWonByHomePlayer = 0;
    private static int mSetWonByAwayPlayer = 0;

    private static Enum.CURRENT_SET mCurrentSet;

    private static boolean mTieBreakerMode;

    private ISetScore setScoreCallback;

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
        setScoreTableView = (SetScoreTableView) view.findViewById(R.id.scoreTable);
        setupViewListener(view);
        mCurrentSet = Enum.CURRENT_SET.FIRST_SET;
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
        return setScoreTableView.getPlayersName(tag);
    }

    @Override
    public void setPlayersName(String tag, String name) {
        setScoreTableView.setPlayersName(tag, name);
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
        }
    }

    @Override
    public void resetSetScores() {
        mHomePlayerSetOneScore = 0;
        mHomePlayerSetTwoScore = 0;
        mHomePlayerSetThreeTBScore = 0;

        mAwayPlayerSetOneScore = 0;
        mAwayPlayerSetTwoScore = 0;
        mAwayPlayerSetThreeTBScore = 0;

        mCurrentSet = Enum.CURRENT_SET.FIRST_SET;
        mTieBreakerMode = false;
        mSetWonByAwayPlayer = 0;
        mSetWonByHomePlayer = 0;

//        if (setScoreCallback != null) {
//            setScoreCallback.resetSetScores();
//        }
        if (setScoreTableView != null)
            setScoreTableView.resetSetScores();
    }

    @Override
    public void updateSetScores(String tag) {
        switch (mCurrentSet) {
            case FIRST_SET:
                if (tag.equalsIgnoreCase(HomeGameFragment.class.getSimpleName()))
                    setScoreTableView.setHomeFirstSetScore(++mHomePlayerSetOneScore);
                else
                    setScoreTableView.setAwayFirstSetScore(++mAwayPlayerSetOneScore);
                break;
            case SECOND_SET:
                if (tag.equalsIgnoreCase(HomeGameFragment.class.getSimpleName()))
                    setScoreTableView.setHomeSecondSetScore(++mHomePlayerSetTwoScore);
                else
                    setScoreTableView.setAwaySecondSetScore(++mAwayPlayerSetTwoScore);
                break;
            case THIRD_SET:
                if (tag.equalsIgnoreCase(HomeGameFragment.class.getSimpleName()))
                    setScoreTableView.setHomeThirdSetScore(++mHomePlayerSetThreeTBScore);
                else
                    setScoreTableView.setAwayThirdSetScore(++mAwayPlayerSetThreeTBScore);
                break;
            case RESET:
                resetSetScores();
        }

        updateCurrentSet();
    }

    @Override
    public void enableTieBreakerMode(boolean mode) {
        mTieBreakerMode = mode;
        if(mode) {
            String currentSet="";
            if(getCurrentSet().equals(Enum.CURRENT_SET.FIRST_SET))
                currentSet = getString(R.string.announcement_set_one);
            else if (getCurrentSet().equals(Enum.CURRENT_SET.SECOND_SET))
                currentSet = getString(R.string.announcement_set_two);
            else if (getCurrentSet().equals(Enum.CURRENT_SET.THIRD_SET))
                currentSet = getString(R.string.announcement_set_three);
            ((IAnnouncements)getActivity()).setAnnouncements(currentSet + "-TB");
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
        MainActivity.updateAlertId(R.drawable.trophy72);
        setScoreCallback.endGameSetMatch(winner);
    }

    @Override
    public void firstSetActive() {
        setScoreCallback.firstSetActive();
    }

    @Override
    public void secondSetActive() {
        recordFirstSetWinner();
        setScoreCallback.secondSetActive();
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
            MainActivity.updateAlertId(R.drawable.trophy72);
            ((IAlertDialog) getActivity()).showAlert(
                    getString(R.string.game_set_match_title),
                    getMatchWinner() + getString(R.string.game_set_match_msg),
                    getString(R.string.game_set_match_pos_btn),
                    getString(R.string.confirm_win_neg_btn),
                    getString(R.string.quit_btn),
                    Enum.ALERT_TYPE.GAME_SET_MATCH);
        }
    }

    private boolean isSplitSet() {
        return (mSetWonByHomePlayer == mSetWonByAwayPlayer);
    }

    private String getMatchWinner() {
        if (mSetWonByAwayPlayer > mSetWonByHomePlayer)
            return setScoreTableView.getAwayPlayerName();
        else
            return setScoreTableView.getHomePlayerName();
    }

    private void recordFirstSetWinner() {
        //First Set
        if (setScoreTableView.getHomeFirstSetScore() > setScoreTableView.getAwayFirstSetScore())
            mSetWonByHomePlayer++;
        else if (setScoreTableView.getHomeFirstSetScore() < setScoreTableView.getAwayFirstSetScore())
            mSetWonByAwayPlayer++;
    }

    private void recordSecondSetWinner() {
        //Second Set
        if (setScoreTableView.getHomeSecondSetScore() > setScoreTableView.getAwaySecondSetScore())
            mSetWonByHomePlayer++;
        else if (setScoreTableView.getHomeSecondSetScore() < setScoreTableView.getAwaySecondSetScore())
            mSetWonByAwayPlayer++;
    }

    private void recordThirdSetWinner() {
        if (setScoreTableView.getHomeThirdSetScore() > setScoreTableView.getHomeThirdSetScore())
            mSetWonByHomePlayer++;
        else if (setScoreTableView.getHomeThirdSetScore() < setScoreTableView.getAwayThirdSetScore())
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
                if (shouldStartNextSet(mHomePlayerSetThreeTBScore, mAwayPlayerSetThreeTBScore)) {
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
