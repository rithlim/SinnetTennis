package com.keeper.score.my.myscorekeeper;


import android.app.Fragment;
import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.keeper.score.common.Enum;
import com.keeper.score.common.IAlertDialog;
import com.keeper.score.common.IGameListener;
import com.keeper.score.common.IScore;
import com.keeper.score.common.IScoreKeeper;
import com.keeper.score.common.IServer;
import com.keeper.score.common.ISetScore;
import com.keeper.score.common.SinnetPreferences;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseGameFragment extends Fragment implements IScoreKeeper, IGameListener, IServer {
    private final String TAG = this.getClass().getSimpleName();

    private Enum.GAME_SCORE mGameScore = Enum.GAME_SCORE.LOVE;
    private View mView;
    private TextView mTvGameScore;
    private TextView mTvPlayerName;
    private TextView mTvServerReceiver;
    private EditText mEtPlayer;
    private ImageView mServerIcon;

    private InputMethodManager inputMethodManager;

    private int m7PointTieBreakerLimit = 7;
    private int m10PointTieBreakerLimit = 10;
    private int mTBScoreLimit = m7PointTieBreakerLimit;
    private int mTieBreakerScore;
    private static float yStart = 0;
    private static float yEnd = 0;

    IGameListener mGameListener;
    IServer mServerListener;
    IScoreKeeper mScoreKeeper;
    ISetScore mSetScoreListener;
    private boolean mIsServer;

    private static Enum.SCORING_SYSTEM mScoringSystem;

    @Override
    public void onResume() {
        super.onResume();
        loadPreferences();
    }

    protected void setView(View view, int tvID, int tvPlayer, int etPlayer, int tvServerReceiver, boolean isServer, int serverRId) {
        mView = view;
        mTvGameScore = (TextView) view.findViewById(tvID);
        Enum.GAME_LOVE = getString(R.string.heart_ascii);
        mTvGameScore.setText(Enum.GAME_LOVE);
        mTvPlayerName = (TextView) view.findViewById(tvPlayer);
        mEtPlayer = (EditText) view.findViewById(etPlayer);
        mTvServerReceiver = (TextView) view.findViewById(tvServerReceiver);
        mGameListener = (IGameListener) getActivity();
        mServerListener = (IServer) getActivity();
        mSetScoreListener = (ISetScore) getActivity();
        mIsServer = isServer;
        mServerIcon = (ImageView) view.findViewById(serverRId);

        init();
        setupViewListeners(view);
    }

    /*****************
     * Init
     **************/
    private void init() {
        if (mIsServer) {
            mServerIcon.setVisibility(View.VISIBLE);
        } else {
            mServerIcon.setVisibility(View.INVISIBLE);
        }

        inputMethodManager = (InputMethodManager) getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        resetGameScores();
        mScoringSystem = Enum.SCORING_SYSTEM.FULL_SET_SCORING;
    }

    @Override
    public void setKeeper(IScoreKeeper scoreKeeper) {
        mScoreKeeper = scoreKeeper;
    }

    /*****************
     * Listeners
     **************/
    private void setupViewListeners(final View view) {
        //Players Name TextView
        mTvPlayerName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = MotionEventCompat.getActionMasked(event);
                switch (action) {
                    case (MotionEvent.ACTION_DOWN):
                        //Log.d(TAG, "mTvPlayerName.ACTION_DOWN");
                        inputMethodManager.toggleSoftInputFromWindow(view.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                        mTvPlayerName.setVisibility(View.GONE);
                        break;
                    case (MotionEvent.ACTION_UP):
                        //Log.d(TAG, "mTvPlayerName.ACTION_UP)");
                        mEtPlayer.setVisibility(View.VISIBLE);
                        break;
                }
                mEtPlayer.requestFocus();
                mEtPlayer.setEnabled(true);
                return true;
            }
        });

        mEtPlayer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    setPlayersName((TextView) v);
                    mTvPlayerName.requestFocus();
                    mEtPlayer.setVisibility(View.GONE);
                    mTvPlayerName.setVisibility(View.VISIBLE);
                }
            }
        });

        mEtPlayer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        //Player EditText
        mEtPlayer.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                setPlayersName(v);
                mTvPlayerName.requestFocus();
                mEtPlayer.setVisibility(View.GONE);
                mTvPlayerName.setVisibility(View.VISIBLE);
                inputMethodManager.hideSoftInputFromWindow(mEtPlayer.getWindowToken(), 0);
                return false;
            }
        });

        //View Listener for increasing/decreasing score points
        if (view != null) {
            //Server/Receiver Label- Click Listener
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.d(TAG, "view.onLongClick()");
                    ((IAlertDialog) getActivity()).showAlert(
                            getString(R.string.reset_game_title),
                            getString(R.string.reset_game_msg),
                            getString(R.string.positive_btn_text),
                            getString(R.string.negative_btn_text),
                            null,
                            Enum.ALERT_TYPE.RESET_GAME);
                    return true;
                }
            });

            mTvServerReceiver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mServerListener.setServer(TAG);
                }
            });

            //View OnTouch Listener
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.d(TAG, "view.onTouch()");
                    inputMethodManager.hideSoftInputFromWindow(mEtPlayer.getWindowToken(), 0);
                    int action = MotionEventCompat.getActionMasked(event);
                    switch (action) {
                        case (MotionEvent.ACTION_DOWN):
                            //Log.d(TAG, "ACTION_DOWN");
                            yStart = event.getY();
                            return false;
                        case (MotionEvent.ACTION_MOVE):
                            //Log.d(TAG, "ACTION_MOVE");
                            return false;
                        case (MotionEvent.ACTION_UP):
                            //Log.d(TAG, "ACTION_UP");
                            yEnd = event.getY();
                            if (isAcceptableGesture()) {
                                if (yStart > yEnd) {
                                    increaseScore();
                                } else {
                                    decreaseScore();
                                }
                            }
                            return false;
                        case (MotionEvent.ACTION_CANCEL):
                            //Log.d(TAG, "ACTION_CANCEL");
                            return false;
                        case (MotionEvent.ACTION_OUTSIDE):
                            //Log.d(TAG, "ACTION_OUTSIDE");
                            return false;
                        default:
                            resetCoordinates();
                            return false;
                    }
                }
            });
        }
    }

    public void setPlayersName(final TextView v) {
        String playersName = v.getText().toString().trim();
        playersName = specialTransform(playersName);
        if (!playersName.isEmpty() && !playersName.equalsIgnoreCase("")) {
            mGameListener.setPlayersName(TAG, playersName);
            mTvPlayerName.setText(playersName);
        } else {
            mTvPlayerName.setText(mTvPlayerName.getText());
        }
    }

    private String specialTransform(String playerName) {
        if (playerName.equalsIgnoreCase(getString(R.string.player_rith_name))) {
            return "The Man!";
        } else if (playerName.equalsIgnoreCase(getString(R.string.player_kim_name))) {
            return "Lovely Kim!";
        } else if (playerName.equalsIgnoreCase(getString(R.string.player_jason_name))) {
            return "Fuck Face!";
        } else return playerName;
    }

    @Override
    public void increaseScore() {
        if (mSetScoreListener.isTieBreakerModeEnabled() || !getScoringSystem().equals(Enum.SCORING_SYSTEM.FULL_SET_SCORING)) {
            int oldTBScore = mTieBreakerScore;
            mTieBreakerScore += 1;
            updateTextView(String.valueOf(mTieBreakerScore));
            mTBScoreLimit = ((IScore) getActivity()).getScoringSystem().equals(Enum.SCORING_SYSTEM.TEN_POINT_SCORING)
                    ? m10PointTieBreakerLimit
                    : m7PointTieBreakerLimit;
            boolean isMyLimit = mTieBreakerScore >= mTBScoreLimit;
            boolean isOpponentLimit = mGameListener.getOpponentTBScore(TAG) >= mTBScoreLimit;
            boolean isMoreThan2 = moreThan2PointsDifferent();
            if ((isMyLimit || isOpponentLimit) && isMoreThan2) {
                if (mTieBreakerScore > mGameListener.getOpponentTBScore(TAG))
                    submitGameResults(TAG, mGameScore);
                else
                    opponentWins();
            }
        } else {
            switch (getGameScore()) {
                case LOVE:
                    updateGameScore(Enum.GAME_SCORE.FIFTEEN);
                    updateTextView(Enum.GAME_FIFTEEN);
                    break;
                case FIFTEEN:
                    updateGameScore(Enum.GAME_SCORE.THIRTY);
                    updateTextView(Enum.GAME_THIRTY);
                    break;
                case THIRTY:
                    updateGameScore(Enum.GAME_SCORE.FORTY);
                    updateTextView(Enum.GAME_FORTY);
                    if (mGameListener.isDeuceMode()) {
                        mGameListener.setDeuceMode(true);
                        mGameListener.updateDeuceModelView(true);
                    }
                    break;
                case FORTY:
                    if (mGameListener.isDeuceMode()) {
                        updateGameScore(Enum.GAME_SCORE.DEUCE);
                        updateTextView(Enum.GAME_DEUCE);
                    } else {
                        submitGameResults(TAG, mGameScore);
                    }
                    break;
                case DEUCE:
                    if (mIsServer)
                        mGameListener.updateAllGameScores(
                                Enum.GAME_SCORE.ADD_IN, Enum.GAME_ADD_IN,
                                Enum.GAME_SCORE.ADD_OUT, Enum.GAME_ADD_OUT);
                    else
                        mGameListener.updateAllGameScores(
                                Enum.GAME_SCORE.ADD_OUT, Enum.GAME_ADD_OUT,
                                Enum.GAME_SCORE.ADD_IN, Enum.GAME_ADD_IN);
                    break;
                case ADD_OUT:
                    if (mIsServer)
                        mGameListener.updateAllGameScores(
                                Enum.GAME_SCORE.DEUCE, Enum.GAME_DEUCE,
                                Enum.GAME_SCORE.DEUCE, Enum.GAME_DEUCE);
                    else
                        mGameListener.updateAllGameScores(
                                Enum.GAME_SCORE.DEUCE, Enum.GAME_DEUCE,
                                Enum.GAME_SCORE.DEUCE, Enum.GAME_DEUCE);
                    break;
                case ADD_IN:
                    submitGameResults(TAG, mGameScore);
                    break;
            }
        }
    }

    @Override
    public void decreaseScore() {
        if (mSetScoreListener.isTieBreakerModeEnabled() || !getScoringSystem().equals(Enum.SCORING_SYSTEM.FULL_SET_SCORING)) {
            mTieBreakerScore -= 1;
            if (mTieBreakerScore < 0) mTieBreakerScore = 0;
            updateTextView(String.valueOf(mTieBreakerScore));
            mTBScoreLimit = (mSetScoreListener.getCurrentSet().equals(Enum.CURRENT_SET.THIRD_SET))
                    ? m10PointTieBreakerLimit
                    : m7PointTieBreakerLimit;
            if ((mTieBreakerScore >= mTBScoreLimit || mGameListener.getOpponentTBScore(TAG) >= mTBScoreLimit) && moreThan2PointsDifferent()) {
                opponentWins();
            }
        } else {
            switch (getGameScore()) {
                case FIFTEEN:
                    updateGameScore(Enum.GAME_SCORE.LOVE);
                    updateTextView(Enum.GAME_LOVE);
                    break;
                case THIRTY:
                    updateGameScore(Enum.GAME_SCORE.FIFTEEN);
                    updateTextView(Enum.GAME_FIFTEEN);
                    break;
                case FORTY:
                    updateGameScore(Enum.GAME_SCORE.THIRTY);
                    updateTextView(Enum.GAME_THIRTY);
                    mGameListener.isDeuceMode();
                    break;
                case DEUCE:
                    if (mIsServer)
                        mGameListener.updateAllGameScores(
                                Enum.GAME_SCORE.ADD_OUT, Enum.GAME_ADD_OUT,
                                Enum.GAME_SCORE.ADD_IN, Enum.GAME_ADD_IN);
                    else //Receiver
                        mGameListener.updateAllGameScores(
                                Enum.GAME_SCORE.ADD_IN, Enum.GAME_ADD_IN,
                                Enum.GAME_SCORE.ADD_OUT, Enum.GAME_ADD_OUT);
                    break;
                case ADD_OUT:
                    opponentWins();
                    break;
                case ADD_IN:
                    mGameListener.updateAllGameScores(
                            Enum.GAME_SCORE.DEUCE, Enum.GAME_DEUCE,
                            Enum.GAME_SCORE.DEUCE, Enum.GAME_DEUCE);
                    break;
            }
        }
    }

    @Override
    public void resetGameScores() {
        updateGameScore(Enum.GAME_SCORE.LOVE);
        updateTextView(Enum.GAME_LOVE);
        mTieBreakerScore = 0;
        mTvGameScore.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.game_score_label_normal_size));
    }

    @Override
    public void setScoringSystem(Enum.SCORING_SYSTEM scoringSystem) {
        mScoringSystem = scoringSystem;
    }

    @Override
    public Enum.SCORING_SYSTEM getScoringSystem() {
        return mScoringSystem;
    }


    @Override
    public void updateAllGameScores(Enum.GAME_SCORE serverScore, String serverString, Enum.GAME_SCORE receiverScore, String receiverString) {
        if (mIsServer) {
            //Log.d(TAG, "Server String: " + serverString);
            updateGameScore(serverScore);
            updateTextView(serverString);
        } else {
            //Log.d(TAG, "Receiver String: " + receiverString);
            updateGameScore(receiverScore);
            updateTextView(receiverString);
        }
    }

    @Override
    public Enum.GAME_SCORE getGameScore() {
        return mGameScore;
    }

    @Override
    public boolean isDeuceMode() {
        if (mGameListener != null) {
            return mGameListener.isDeuceMode();
        }
        return false;
    }

    @Override
    public int getOpponentTBScore(String tag) {
        return mTieBreakerScore;
    }

    @Override
    public void setDeuceMode(boolean mode) {
        if (isDeuceMode()) {
            //Log.d(TAG, "DeuceMode ON!");
            mTvGameScore.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.game_score_label_deuce_size));
        } else {
            //Log.d(TAG, "DeuceMode OFF!");
            mTvGameScore.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.game_score_label_normal_size));
        }
    }

    @Override
    public void updateDeuceModelView(boolean mode) {
        updateGameScore(Enum.GAME_SCORE.DEUCE);
        updateTextView(Enum.GAME_DEUCE);
    }

    private void updateTextView(String score) {
        mTvGameScore.refreshDrawableState();
        mTvGameScore.setText(score);
        animateTextView(score);
    }

    private void animateTextView(String score) {
        mTvGameScore.clearAnimation();
        if (score.equalsIgnoreCase(getString(R.string.heart_ascii))) {
            Log.d(TAG, "updateText: " + score);
            //mTvGameScore.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.heart_beat));
        } else {
            mTvGameScore.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
        }
    }

    @Override
    public void setServer(String serverTag) {
        mIsServer = (serverTag.equalsIgnoreCase(TAG)) ? true : false;
        //Log.d(TAG, "isServer: " + mIsServer);
        boolean isServer = mIsServer;

        if (mIsServer) {
            mServerIcon.setVisibility(View.VISIBLE);
            mTvServerReceiver.setText(getString(R.string.server_label));
            mTvServerReceiver.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_in_left));
        } else {
            mServerIcon.setVisibility(View.INVISIBLE);
            mTvServerReceiver.setText(getString(R.string.receiver_label));
            mTvServerReceiver.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
        }
    }

    @Override
    public void toggleServer() {
    }

    @Override
    public boolean isServer() {
        return mIsServer;
    }

    @Override
    public void submitGameResults(String tag, Enum.GAME_SCORE playerScore) {
        mGameListener.submitGameResults(tag, playerScore);
    }

    @Override
    public String getPlayersName(String tag) {
        return mTvPlayerName.getText().toString();
    }

    @Override
    public void setPlayersName(String tag, String name) {
        if (tag.equalsIgnoreCase(TAG)) {
            mTvPlayerName.setText(name);

            SinnetPreferences.putPreferences(this.getActivity(), tag, name);
        }
    }

    /**************
     * Helper Methods
     ****************/

    private void opponentWins() {
        if (TAG.equalsIgnoreCase(HomeGameFragment.class.getSimpleName()))
            submitGameResults(AwayGameFragment.class.getSimpleName(), mGameScore);
        else if (TAG.equalsIgnoreCase(AwayGameFragment.class.getSimpleName()))
            submitGameResults(HomeGameFragment.class.getSimpleName(), mGameScore);
    }

    private boolean moreThan2PointsDifferent() {
        int opponentTBScore = mGameListener.getOpponentTBScore(TAG);
        int diff = Math.abs(opponentTBScore - mTieBreakerScore);
        return (diff >= 2);
    }

    private void updateGameScore(Enum.GAME_SCORE score) {
        mGameScore = score;
    }

    private boolean isAcceptableGesture() {
        if (Math.abs(yStart - yEnd) < 50) {
            return false;
        } else {
            return true;
        }
    }

    private void resetCoordinates() {
        yEnd = 0;
        yStart = 0;
    }

    public void loadPreferences() {
        String cachedPlayersName = SinnetPreferences.getPreferences(getActivity(), TAG);
        if (cachedPlayersName != null && !cachedPlayersName.isEmpty()) {
            mTvPlayerName.setText(cachedPlayersName);
            mSetScoreListener.setPlayersName(TAG, cachedPlayersName);
        }
    }
}
