package com.keeper.score.my.myscorekeeper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.keeper.score.common.Enum;
import com.keeper.score.common.IAlertDialog;
import com.keeper.score.common.IAnnouncements;
import com.keeper.score.common.IGameListener;
import com.keeper.score.common.IServer;
import com.keeper.score.common.ISetScore;
import com.keeper.score.utils.FragmentUtils;

public class MainActivity extends Activity implements IGameListener, IServer, ISetScore, IAlertDialog, IAnnouncements, DialogInterface.OnDismissListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static String mCurrentGameWinner;

    HomeGameFragment homeGameFragment;
    AwayGameFragment awayGameFragment;
    SetScoreFragment setScoreFragment;
    AnnouncerFragment announcerFragment;

    IGameListener mHomeGameCallback;
    IGameListener mAwayGameCallback;
    ISetScore mSetScoreCallback;
    IAnnouncements mAnnouncementCallback;
    private static AlertDialog.Builder myDialog;

    private static boolean isDeuce = false;
    private static int mHomePlayerSetOneScore;
    private static int mHomePlayerSetTwoScore;
    private static int mHomePlayerSetThreeTBScore;
    private static int mAwayPlayerSetOneScore;
    private static int mAwayPlayerSetTwoScore;
    private static int mAwayPlayerSetThreeTBScore;
    private static int alertIconId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_main, null);
        setContentView(view);
        addFragment();
        resetSetScores();
        alertIconId = R.drawable.alerticon;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    /***********
     * HELP METHODS
     **************/

    private void addFragment() {
        homeGameFragment = HomeGameFragment.newInstance("", "");
        awayGameFragment = AwayGameFragment.newInstance("", "");
        setScoreFragment = SetScoreFragment.newInstance("", "");
        announcerFragment = AnnouncerFragment.newInstance("", "");

        FragmentManager fm = getFragmentManager();

//        FragmentUtils.launchFragment(fm, mainFragment, R.id.background_container, true, MainFragment.class.getSimpleName());
//
        FragmentUtils.launchFragment(fm, homeGameFragment, R.id.home_container, false, HomeGameFragment.class.getSimpleName());
        FragmentUtils.launchFragment(fm, awayGameFragment, R.id.away_container, false, AwayGameFragment.class.getSimpleName());
        FragmentUtils.launchFragment(fm, setScoreFragment, R.id.set_container, false, SetScoreFragment.class.getSimpleName());
        FragmentUtils.launchFragment(fm, announcerFragment, R.id.main_layout, false, AnnouncerFragment.class.getSimpleName());


        mHomeGameCallback = homeGameFragment;
        mAwayGameCallback = awayGameFragment;
        mSetScoreCallback = setScoreFragment;
        mAnnouncementCallback = announcerFragment;
    }

    @Override
    public void resetSetScores() {
        mHomePlayerSetOneScore = 0;
        mHomePlayerSetTwoScore = 0;
        mHomePlayerSetThreeTBScore = 0;

        mAwayPlayerSetOneScore = 0;
        mAwayPlayerSetTwoScore = 0;
        mAwayPlayerSetThreeTBScore = 0;

        setScoringSystem(Enum.SCORING_SYSTEM.FULL_SET_SCORING);
        mSetScoreCallback.resetSetScores();
        updateAlertId(R.drawable.alerticon);
        setAnnouncements(getString(R.string.announcement_set_one));
    }

    @Override
    public Enum.GAME_SCORE getGameScore() {
        return Enum.GAME_SCORE.LOVE;
    }

    @Override
    public void setDeuceMode(boolean mode) {
        isDeuce = mode;
        mHomeGameCallback.setDeuceMode(mode);
        mAwayGameCallback.setDeuceMode(mode);
    }

    @Override
    public void updateDeuceModelView(boolean mode) {
        mHomeGameCallback.updateDeuceModelView(mode);
        mAwayGameCallback.updateDeuceModelView(mode);
    }

    @Override
    public boolean isDeuceMode() {
        isDeuce = (mHomeGameCallback.getGameScore() == Enum.GAME_SCORE.FORTY
                && mAwayGameCallback.getGameScore() == Enum.GAME_SCORE.FORTY) ? true : false;
        return isDeuce;
    }

    @Override
    public int getOpponentTBScore(String tag) {
        if (tag.equalsIgnoreCase(HomeGameFragment.class.getSimpleName()))
            return mAwayGameCallback.getOpponentTBScore(tag);
        else
            return mHomeGameCallback.getOpponentTBScore(tag);
    }

    @Override
    public void setServer(String serverTag) {
        mHomeGameCallback.setServer(serverTag);
        mAwayGameCallback.setServer(serverTag);
    }

    @Override
    public void toggleServer() {
        if (mHomeGameCallback.isServer())
            setServer(AwayGameFragment.class.getSimpleName());
        else if (mAwayGameCallback.isServer())
            setServer(HomeGameFragment.class.getSimpleName());
    }

    @Override
    public boolean isServer() {
        return false;
    }

    @Override
    public void updateAllGameScores(Enum.GAME_SCORE serverScore, String serverString, Enum.GAME_SCORE receiverScore, String receiverString) {
        mHomeGameCallback.updateAllGameScores(serverScore, serverString, receiverScore, receiverString);
        mAwayGameCallback.updateAllGameScores(serverScore, serverString, receiverScore, receiverString);
    }

    @Override
    public void resetGameScores() {
        updateAlertId(R.drawable.alerticon);
        mHomeGameCallback.resetGameScores();
        mAwayGameCallback.resetGameScores();
    }

    @Override
    public void setScoringSystem(Enum.SCORING_SYSTEM scoringType) {
        homeGameFragment.setScoringSystem(scoringType);
        awayGameFragment.setScoringSystem(scoringType);
    }

    @Override
    public Enum.SCORING_SYSTEM getScoringSystem() {
        return homeGameFragment.getScoringSystem();
    }

    @Override
    public void submitGameResults(String tag, Enum.GAME_SCORE playerScore) {
        // Just need to know which player won the game.
        // SetScoreFragment will keep track of overall set scores.
        mCurrentGameWinner = tag;
        String winner = getPlayersName(tag);

        showAlert(
                winner + getString(R.string.confirm_win_title),
                winner + getString(R.string.confirm_win_msg),
                getString(R.string.positive_btn_text),
                getString(R.string.confirm_win_neg_btn),
                getString(R.string.confirm_win_neutral_btn),
                Enum.ALERT_TYPE.CONFIRM_GAME_SCORE
        );
    }

    @Override
    public void updateSetScores(String tag) {
        mSetScoreCallback.updateSetScores(tag);
        toggleServer();
    }

    @Override
    public void enableTieBreakerMode(boolean mode) {

    }

    @Override
    public boolean isTieBreakerModeEnabled() {
        return mSetScoreCallback.isTieBreakerModeEnabled();
    }

    @Override
    public Enum.CURRENT_SET getCurrentSet() {
        return mSetScoreCallback.getCurrentSet();
    }

    @Override
    public void endGameSetMatch(String winner) {
        setScoringSystem(Enum.SCORING_SYSTEM.FULL_SET_SCORING);
        showAlert(
                getString(R.string.game_set_match_title),
                winner + getString(R.string.game_set_match_msg),
                getString(R.string.game_set_match_pos_btn),
                getString(R.string.confirm_win_neg_btn),
                getString(R.string.quit_btn),
                Enum.ALERT_TYPE.GAME_SET_MATCH);
    }

    @Override
    public void firstSetActive() {
        showToast(getString(R.string.toast_starting_1st_set));
        mAnnouncementCallback.setAnnouncements(getString(R.string.announcement_set_one));
    }

    @Override
    public void secondSetActive() {
        showToast(getString(R.string.toast_starting_2nd_set));
        mAnnouncementCallback.setAnnouncements(getString(R.string.announcement_set_two));
    }

    @Override
    public void thirdSetActive() {
    }

    @Override
    public String getPlayersName(String tag) {
        if (tag.equalsIgnoreCase(HomeGameFragment.class.getSimpleName()))
            return mHomeGameCallback.getPlayersName(tag);
        else
            return mAwayGameCallback.getPlayersName(tag);
    }

    /*********
     * IPlayers
     *******/
    @Override
    public void setPlayersName(String tag, String name) {
        mHomeGameCallback.setPlayersName(tag, name);
        mAwayGameCallback.setPlayersName(tag, name);
        mSetScoreCallback.setPlayersName(tag, name);
    }

    /***********
     * Toast Helper
     */

    private void showToast(String toast) {
        Toast.makeText(this, toast,
                Toast.LENGTH_LONG).show();
    }

    /*********
     * IAlertDialog
     **********/
    @Override
    public void showAlert(String title, String msg, String posBtnText, String negBtnTxt, String neutralBtnText, final Enum.ALERT_TYPE alertType) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        //alertDialogFragment.show(ft, "dialog");

        myDialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setCancelable(false)
                .setMessage(msg)
                .setPositiveButton(posBtnText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (alertType) {
                            case CONFIRM_GAME_SCORE:
                                updateSetScores(mCurrentGameWinner);
                            case RESET_GAME:
                                mHomeGameCallback.resetGameScores();
                                mAwayGameCallback.resetGameScores();
                                updateAllGameScores(Enum.GAME_SCORE.LOVE, getString(R.string.heart_ascii), Enum.GAME_SCORE.LOVE, getString(R.string.heart_ascii));
                                break;
                            case RESET_SET:
                                resetSetScores();
                                break;
                            case SCORING_SYSTEM:
                                setScoringSystem(Enum.SCORING_SYSTEM.FULL_SET_SCORING);
                                showToast(getString(R.string.toast_starting_3rd_set));
                                mAnnouncementCallback.setAnnouncements(getString(R.string.announcement_set_three));
                                break;
                            case GAME_SET_MATCH:
                                resetGameScores();
                                resetSetScores();
                                updateAllGameScores(Enum.GAME_SCORE.LOVE, getString(R.string.heart_ascii), Enum.GAME_SCORE.LOVE, getString(R.string.heart_ascii));
                                firstSetActive();
                                showToast(getString(R.string.toast_starting_1st_set));
                                mAnnouncementCallback.setAnnouncements(getString(R.string.announcement_set_one));
                                break;
                        }
                    }
                })
                .setNegativeButton(negBtnTxt, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (alertType) {
                            case RESET_GAME:
                                Log.d(TAG, "RESET_GAME");
                                break;
                            case RESET_SET:
                                Log.d(TAG, "RESET_SET");
                                break;
                            case CONFIRM_GAME_SCORE:
                                dialog.dismiss();
                                break;
                            case SCORING_SYSTEM:
                                Log.d(TAG, "SCORING_SYSTEM");
                                setScoringSystem(Enum.SCORING_SYSTEM.TEN_POINT_SCORING);
                                showToast(getString(R.string.toast_starting_3rd_set));
                                mAnnouncementCallback.setAnnouncements(getString(R.string.announcement_set_three));
                                break;
                            case GAME_SET_MATCH:
                                break;

                        }
                    }
                })
                .setIcon(alertIconId);
        if (neutralBtnText != null && !neutralBtnText.isEmpty()) {
            myDialog.setNeutralButton(neutralBtnText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (alertType) {
                        case RESET_GAME:
                            break;
                        case RESET_SET:
                            break;
                        case CONFIRM_GAME_SCORE:
                            Log.d(TAG, "CONFIRM_GAME_SCORE");
                            resetGameScores();
                            dialog.dismiss();
                            break;
                        case SCORING_SYSTEM:
                            break;
                        case GAME_SET_MATCH:
                            resetGameScores();
                            resetSetScores();
                            finish();
                            break;
                        default:
                            setScoringSystem(Enum.SCORING_SYSTEM.FULL_SET_SCORING);
                            resetGameScores();
                            resetSetScores();
                            break;

                    }
                }
            });
        }
        myDialog.setOnDismissListener(this);
        myDialog.show();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Log.d(TAG, "onDismiss");
    }

    @Override
    public void setAnnouncements(String text) {
        mAnnouncementCallback.setAnnouncements(text);
    }

    public static void updateAlertId(int alertId) {
        alertIconId = alertId;
    }
}
