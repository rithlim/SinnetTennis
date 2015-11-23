package com.keeper.score.my.sinnet.MatchGame;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.keeper.score.common.Enum;
import com.keeper.score.common.IAlertDialog;
import com.keeper.score.common.IAnnouncements;
import com.keeper.score.common.IGameListener;
import com.keeper.score.common.IMatch;
import com.keeper.score.common.IServer;
import com.keeper.score.common.ISetScore;
import com.keeper.score.common.ITutorial;
import com.keeper.score.my.sinnet.Announcer.AnnouncerFragment;
import com.keeper.score.my.sinnet.R;
import com.keeper.score.my.sinnet.Tutorial.TutorialFragment;
import com.keeper.score.utils.FragmentUtils;
import com.keeper.score.utils.SinnetPreferences;

public class MatchActivity extends Activity implements IGameListener, IServer, ISetScore, IAlertDialog, IAnnouncements, ITutorial, IMatch, DialogInterface.OnDismissListener {
    private static final String TAG = MatchActivity.class.getSimpleName();
    private static String mCurrentGameWinner;

    HomeGameFragment homeGameFragment;
    AwayGameFragment awayGameFragment;
    SetScoreFragment setScoreFragment;
    AnnouncerFragment announcerFragment;
    TutorialFragment tutorialFragment;

    IGameListener mHomeGameCallback;
    IGameListener mAwayGameCallback;
    ISetScore mSetScoreCallback;
    IAnnouncements mAnnouncementCallback;
    private static AlertDialog.Builder myDialog;

    private static boolean isDeuce = false;
    public static boolean isNewMatch = false;
    private static int alertIconId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_match, null);
        setContentView(view);
        addLaunchFragment();
        alertIconId = R.drawable.ic_blue_alert;
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
    public void onBackPressed() {
        boolean didFinishTutorial = SinnetPreferences.getBooleanPreferences(this, TutorialFragment.class.getSimpleName());
        Enum.GAME_SCORE homeGameScore = mHomeGameCallback.getGameScore();
        Enum.GAME_SCORE awayGameScore = mAwayGameCallback.getGameScore();
        boolean loveGame = (homeGameScore.equals(Enum.GAME_SCORE.LOVE) && awayGameScore.equals(Enum.GAME_SCORE.LOVE));

        if (didFinishTutorial && (!loveGame || !isNewMatch)) {
            showAlert("End Match?",
                    "All scores will be lost. Are you sure you want to end the match?",
                    "Yes",
                    "No",
                    null,
                    Enum.ALERT_TYPE.END_MATCH);
        } else {
            finish();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /***********
     * HELP METHODS
     **************/

    private void addLaunchFragment() {
        FragmentManager fm = getFragmentManager();
        homeGameFragment = HomeGameFragment.newInstance("", "");
        awayGameFragment = AwayGameFragment.newInstance("", "");
        setScoreFragment = SetScoreFragment.newInstance("", "");
        announcerFragment = AnnouncerFragment.newInstance("", "");

        FragmentUtils.launchFragment(fm, setScoreFragment, R.id.set_container, false, SetScoreFragment.class.getSimpleName());
        FragmentUtils.launchFragment(fm, homeGameFragment, R.id.home_container, false, HomeGameFragment.class.getSimpleName());
        FragmentUtils.launchFragment(fm, awayGameFragment, R.id.away_container, false, AwayGameFragment.class.getSimpleName());
        FragmentUtils.launchFragment(fm, announcerFragment, R.id.main_layout, false, AnnouncerFragment.class.getSimpleName());

        if (!SinnetPreferences.getBooleanPreferences(this, TutorialFragment.class.getSimpleName())) {
            tutorialFragment = TutorialFragment.newInstance("", "");
            FragmentUtils.launchFragment(fm, tutorialFragment, R.id.main_layout, true, TutorialFragment.class.getSimpleName());
        }

        mHomeGameCallback = homeGameFragment;
        mAwayGameCallback = awayGameFragment;
        mSetScoreCallback = setScoreFragment;
        mAnnouncementCallback = announcerFragment;
    }

    @Override
    public void resetSetScores() {
        setScoringSystem(Enum.SCORING_SYSTEM.FULL_SET_SCORING);
        mSetScoreCallback.resetSetScores();
        updateAlertId(R.drawable.ic_blue_alert);
        setAnnouncements(getString(R.string.announcement_set_one), "", false);
    }

    @Override
    public void setFinalSetTBSetScore(int homeTBScore, int awayTBScore) {
        mSetScoreCallback.setFinalSetTBSetScore(homeTBScore, awayTBScore);
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
    public Enum.GAME_SCORE getOpponentsGameScore(String iAm) {
        if (iAm.equals(HomeGameFragment.class.getSimpleName())) {
            return mAwayGameCallback.getOpponentsGameScore(iAm);
        } else {
            return mHomeGameCallback.getOpponentsGameScore(iAm);
        }
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
    public void checkNewMatch() {
        if(mHomeGameCallback.getGameScore().equals(Enum.GAME_SCORE.LOVE)
                && mAwayGameCallback.getGameScore().equals(Enum.GAME_SCORE.LOVE)
                && SetScoreFragment.isNewMatch) {
            isNewMatch = true;
        } else {
            isNewMatch = false;
        }
    }

    @Override
    public void updateAllGameScores(Enum.GAME_SCORE serverScore, String serverString, Enum.GAME_SCORE receiverScore, String receiverString) {
        mHomeGameCallback.updateAllGameScores(serverScore, serverString, receiverScore, receiverString);
        mAwayGameCallback.updateAllGameScores(serverScore, serverString, receiverScore, receiverString);
    }

    @Override
    public void resetGameScores() {
        //updateAlertId(R.drawable.alerticon);
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
                getString(R.string.confirm_win_title),
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
        if (mHomeGameCallback.getScoringSystem().equals(Enum.SCORING_SYSTEM.TEN_POINT_SCORING)) {
            int homeTBScore = mHomeGameCallback.getOpponentTBScore(HomeGameFragment.class.getSimpleName());
            int awayTBScore = mAwayGameCallback.getOpponentTBScore(AwayGameFragment.class.getSimpleName());
            setFinalSetTBSetScore(homeTBScore, awayTBScore);
        }
        setScoringSystem(Enum.SCORING_SYSTEM.FULL_SET_SCORING);
        showAlert(
                getString(R.string.game_set_match_title),
                winner + getString(R.string.game_set_match_msg),
                getString(R.string.game_set_match_pos_btn),
                getString(R.string.home_btn),
                null,
                Enum.ALERT_TYPE.GAME_SET_MATCH);
    }

    @Override
    public void firstSetActive() {
        mAnnouncementCallback.setAnnouncements(getString(R.string.announcement_set_one), "", false);
        resetGameScores();
        resetSetScores();
    }

    @Override
    public void secondSetActive() {
        mAnnouncementCallback.setAnnouncements(getString(R.string.announcement_set_two), "", false);
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

//    private void showToast(String toast) {
//        Toast.makeText(this, toast,
//                Toast.LENGTH_LONG).show();
//    }

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
                                mAnnouncementCallback.setAnnouncements(getString(R.string.announcement_set_three), "Full Set", true);
                                break;
                            case GAME_SET_MATCH:
                                recordMatchScore();
                                resetGameScores();
                                resetSetScores();
                                updateAllGameScores(Enum.GAME_SCORE.LOVE, getString(R.string.heart_ascii), Enum.GAME_SCORE.LOVE, getString(R.string.heart_ascii));
                                firstSetActive();
                                mAnnouncementCallback.setAnnouncements(getString(R.string.announcement_set_one), "", false);
                                break;
                            case END_MATCH:
                                finish();
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
                                mAnnouncementCallback.setAnnouncements(getString(R.string.announcement_set_three), "10-Point TB", true);
                                break;
                            case GAME_SET_MATCH:
                                recordMatchScore();
                                finish();
//                                resetGameScores();
//                                resetSetScores();
//                                updateAllGameScores(Enum.GAME_SCORE.LOVE, getString(R.string.heart_ascii), Enum.GAME_SCORE.LOVE, getString(R.string.heart_ascii));
//                                firstSetActive();
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
                        default:
                            setScoringSystem(Enum.SCORING_SYSTEM.FULL_SET_SCORING);
                            resetGameScores();
                            resetSetScores();
                            break;

                    }
                }
            });
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            myDialog.setOnDismissListener(this);
        }
        myDialog.show();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Log.d(TAG, "onDismiss");
    }

    @Override
    public void setAnnouncements(String label, String subLabel, boolean showSubLabel) {
        mAnnouncementCallback.setAnnouncements(label, subLabel, showSubLabel);
    }

    public static void updateAlertId(int alertId) {
        alertIconId = alertId;
    }

    @Override
    public void recordMatchScore() {
        mSetScoreCallback.recordMatchScore();
    }

//    private void takeScreenshot() {
//        Date now = new Date();
//        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
//
//        try {
//            // image naming and path  to include sd card  appending name you choose for file
//            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";
//
//            // create bitmap screen capture
//            View v1 = getWindow().getDecorView().getRootView();
//            v1.setDrawingCacheEnabled(true);
//            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
//            v1.setDrawingCacheEnabled(false);
//
//            File imageFile = new File(mPath);
//
//            FileOutputStream outputStream = new FileOutputStream(imageFile);
//            int quality = 100;
//            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
//            outputStream.flush();
//            outputStream.close();
//
//            openScreenshot(imageFile);
//        } catch (Throwable e) {
//            // Several error may come out with file handling or OOM
//            e.printStackTrace();
//        }
//    }
//
//    private void openScreenshot(File imageFile) {
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_VIEW);
//        Uri uri = Uri.fromFile(imageFile);
//        intent.setDataAndType(uri, "image/*");
//        ActivityInfo activityInfo = intent.resolveActivityInfo(getPackageManager(), intent.getFlags());
//        if (activityInfo.exported) {
//            startActivity(intent);
//        } else {
//
//        }
//    }

    @Override
    public void endTutorial() {
        FragmentManager fm = getFragmentManager();
        fm.popBackStack();
        SinnetPreferences.putPreferences(this, TutorialFragment.class.getSimpleName(), true);
        firstSetActive();
    }
}
