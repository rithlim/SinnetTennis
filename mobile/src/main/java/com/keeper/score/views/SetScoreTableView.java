package com.keeper.score.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.keeper.score.common.IPlayers;
import com.keeper.score.my.sinnet.MatchGame.HomeGameFragment;
import com.keeper.score.my.sinnet.R;

/**
 * Created by Rith on 11/1/2015.
 */
public class SetScoreTableView extends View implements IPlayers {
    private static final String TAG = SetScoreTableView.class.getSimpleName();
    private String mHomePlayerName;
    private String mAwayPlayerName;

    private int mHomeFirstSetScore;
    private int mHomeSecondSetScore;
    private int mHomeThirdSetScore;
    private int mAwayFirstSetScore;
    private int mAwaySecondSetScore;
    private int mAwayThirdSetScore;
    private int mHomeTextColor = 0;
    private int mAwayTextColor = 0;
    private int cWidth;
    private int cHeight;

    private float mBorderPadding = 0f;
    private float mTextSize = 0.0f;
    private int mSetScoreFontSize = 15;
    private float mSetLabelFontSize=0f;

    private boolean mShowText = false;

    private Rect homeFirstSetBox;
    private Rect homeSecondSetBox;
    private Rect homeThirdSetBox;
    private Rect awayFirstSetBox;
    private Rect awaySecondSetBox;
    private Rect awayThirdSetBox;

    private Paint mSetLabelPaint;
    private Paint mHomeSetScoreLabelPaint;
    private Paint mAwaySetScoreLabelPaint;
    private Paint mHomePlayerLabelPaint;
    private Paint mAwayPlayerLabelPaint;
    private Paint mBorderPaint;
    private Paint mHomeSetBoxPaint;
    private Paint mAwaySetBoxPaint;
    private Paint mShadowPaint;

    private static int startXPosBoxes;
    private static int startYPosBoxes;
    private static float boxWidthHeight = 0;
    private static int xSetBoxPadding = 220;
    private static int ySetBoxPadding = 20;
    private static int ySetBoxPlayerNamePadding = 50;
    private static int ySetBoxSetScoreLabelOffset = 62;

    public SetScoreTableView(Context context) {
        super(context);
        init();
    }

    public SetScoreTableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SetScoreTableView,
                0, 0);
        try {
            setHomePlayerName(a.getString(R.styleable.SetScoreTableView_home_player_name));
            setAwayPlayerName(a.getString(R.styleable.SetScoreTableView_away_player_name));
            setHomeFirstSetScore(a.getInteger(R.styleable.SetScoreTableView_first_setScore, 0));
            setHomeSecondSetScore(a.getInteger(R.styleable.SetScoreTableView_second_setScore, 0));
            setHomeThirdSetScore(a.getInteger(R.styleable.SetScoreTableView_third_setScore, 0));
            mSetLabelFontSize = a.getDimension(R.styleable.SetScoreTableView_set_label_size, 0);
            boxWidthHeight = a.getDimension(R.styleable.SetScoreTableView_set_box_hw, 0);
            //This does not invalidate nor request layout. Maybe image will not refresh if modified during runtime?
            mTextSize = a.getDimension(R.styleable.SetScoreTableView_labelSize, 0.0f);
            mHomeTextColor = a.getColor(R.styleable.SetScoreTableView_home_labelColor, 0xff000000);
            mAwayTextColor = a.getColor(R.styleable.SetScoreTableView_away_labelColor, 0xff000000);
        } finally {
            a.recycle();
        }

        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.cWidth = w;
        this.cHeight = h;
        super.onSizeChanged(w, h, oldw, oldh);

        Log.d(TAG, "cWidth: " + cWidth);
        Log.d(TAG, "cHeight: " + cHeight);
        Log.d(TAG, "oldWidth: " + oldw);
        Log.d(TAG, "oldHeight: " + oldh);

        int xOffset = 100;
        int yOffset = 10;
        startXPosBoxes = (cWidth / 3) + xOffset;
        startYPosBoxes = (cHeight / 4) - yOffset;
    }

    private void init() {
        // Force the background to software rendering because otherwise the Blur
        // filter won't work.
        Log.d(TAG, "init()");
        setLayerToSW(this);

        //Set Label
        mSetLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSetLabelPaint.setColor(0xffCC3300);
        mSetLabelPaint.setTextAlign(Paint.Align.CENTER);
        mSetLabelPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mSetLabelPaint.setTextSize(mSetLabelFontSize);

        //Set Score Label
        mHomeSetScoreLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHomeSetScoreLabelPaint.setColor(mHomeTextColor);
        mHomeSetScoreLabelPaint.setTextAlign(Paint.Align.CENTER);
        mHomeSetScoreLabelPaint.setStyle(Paint.Style.STROKE);
        mHomeSetScoreLabelPaint.setStrokeWidth(8f);

        mAwaySetScoreLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mAwaySetScoreLabelPaint.setColor(mAwayTextColor);
        mAwaySetScoreLabelPaint.setTextAlign(Paint.Align.CENTER);
        mAwaySetScoreLabelPaint.setStyle(Paint.Style.STROKE);
        mAwaySetScoreLabelPaint.setStrokeWidth(8f);

        // Set up the paint for the label text
        mHomePlayerLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHomePlayerLabelPaint.setColor(mHomeTextColor);
        mHomePlayerLabelPaint.setTextAlign(Paint.Align.RIGHT);
        mHomePlayerLabelPaint.setStrokeWidth(6f);

        //Away Text Paint
        mAwayPlayerLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mAwayPlayerLabelPaint.setColor(mAwayTextColor);
        mAwayPlayerLabelPaint.setTextAlign(Paint.Align.RIGHT);
        mAwayPlayerLabelPaint.setStrokeWidth(6f);

        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint.setColor(mHomeTextColor);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(10f);

        mHomeSetBoxPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHomeSetBoxPaint.setColor(mHomeTextColor);
        mHomeSetBoxPaint.setStyle(Paint.Style.STROKE);
        mHomeSetBoxPaint.setStrokeWidth(10f);

        mAwaySetBoxPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mAwaySetBoxPaint.setColor(mAwayTextColor);
        mAwaySetBoxPaint.setStyle(Paint.Style.STROKE);
        mAwaySetBoxPaint.setStrokeWidth(10f);

        //Home Set Boxes
        homeFirstSetBox = new Rect();
        homeSecondSetBox = new Rect();
        homeThirdSetBox = new Rect();

        //Away Set Boxes
        awayFirstSetBox = new Rect();
        awaySecondSetBox = new Rect();
        awayThirdSetBox = new Rect();

        //Font Sizes
        mHomePlayerLabelPaint.setTextSize(mTextSize);
        mAwayPlayerLabelPaint.setTextSize(mTextSize);
        mHomeSetScoreLabelPaint.setTextSize(mTextSize * 2);
        mAwaySetScoreLabelPaint.setTextSize(mTextSize * 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //Update Box Sizes
        updateSetBoxSize();

        Log.d(TAG, "onDraw()");
        super.onDraw(canvas);

        mHomePlayerLabelPaint.setTextAlign(Paint.Align.RIGHT);
        mAwayPlayerLabelPaint.setTextAlign(Paint.Align.RIGHT);

        //Home Players Name
        int adjustment = 17;
        int yHomePos = (int) ((canvas.getHeight() / 3) - ((mHomePlayerLabelPaint.descent() + mHomePlayerLabelPaint.ascent()) / 2)) + adjustment;
        int yAwayPos = (int) ((canvas.getHeight() / 3) + ((mHomePlayerLabelPaint.descent() + mHomePlayerLabelPaint.ascent()) / 2)) - adjustment;


        //Home Player Name
        int yPlayerLabelOffset = 65;
        int yPosHomePlayerLabel = startYPosBoxes + yPlayerLabelOffset;
        canvas.drawText(getHomePlayerName(), startXPosBoxes - ySetBoxPlayerNamePadding, startYPosBoxes + yPlayerLabelOffset, mHomePlayerLabelPaint);

        //Away Player Name
        int yPosAwayPlayerLabel = startYPosBoxes + (int)boxWidthHeight + ySetBoxPadding + yPlayerLabelOffset;
        canvas.drawText(getAwayPlayerName(), startXPosBoxes - ySetBoxPlayerNamePadding, yPosAwayPlayerLabel, mAwayPlayerLabelPaint);

        //Border
        canvas.drawRect(mBorderPadding, mBorderPadding, canvas.getWidth() - mBorderPadding, canvas.getHeight() - mBorderPadding, mBorderPaint);

        //Draw Set Boxes
        canvas.drawRect(homeFirstSetBox, mHomeSetBoxPaint);
        canvas.drawRect(homeSecondSetBox, mHomeSetBoxPaint);
        canvas.drawRect(homeThirdSetBox, mHomeSetBoxPaint);
        canvas.drawRect(awayFirstSetBox, mAwaySetBoxPaint);
        canvas.drawRect(awaySecondSetBox, mAwaySetBoxPaint);
        canvas.drawRect(awayThirdSetBox, mAwaySetBoxPaint);

        //Draw "Set" header label
        int paddingBetweenBoxSetLabel = startYPosBoxes - 31;
        canvas.drawText("Set1", homeFirstSetBox.centerX(), paddingBetweenBoxSetLabel, mSetLabelPaint);
        canvas.drawText("Set2", homeSecondSetBox.centerX(), paddingBetweenBoxSetLabel, mSetLabelPaint);
        canvas.drawText("Set3", homeThirdSetBox.centerX(), paddingBetweenBoxSetLabel, mSetLabelPaint);

        //Home Set Score Label
        //mHomePlayerLabelPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(String.valueOf(getHomeFirstSetScore()), homeFirstSetBox.centerX(), homeFirstSetBox.centerY() + ySetBoxSetScoreLabelOffset, mHomeSetScoreLabelPaint);
        canvas.drawText(String.valueOf(getHomeSecondSetScore()), homeSecondSetBox.centerX(), homeSecondSetBox.centerY() + ySetBoxSetScoreLabelOffset, mHomeSetScoreLabelPaint);
        canvas.drawText(String.valueOf(getHomeThirdSetScore()), homeThirdSetBox.centerX(), homeThirdSetBox.centerY() + ySetBoxSetScoreLabelOffset, mHomeSetScoreLabelPaint);

        //Away Set Score Label
        //mAwayPlayerLabelPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(String.valueOf(getAwayFirstSetScore()), awayFirstSetBox.centerX(), awayFirstSetBox.centerY() + ySetBoxSetScoreLabelOffset, mAwaySetScoreLabelPaint);
        canvas.drawText(String.valueOf(getAwaySecondSetScore()), awaySecondSetBox.centerX(), awaySecondSetBox.centerY() + ySetBoxSetScoreLabelOffset, mAwaySetScoreLabelPaint);
        canvas.drawText(String.valueOf(getAwayThirdSetScore()), awayThirdSetBox.centerX(), awayThirdSetBox.centerY() + ySetBoxSetScoreLabelOffset, mAwaySetScoreLabelPaint);
    }

    private void updateSetBoxSize() {
        //Home First Set Box
        int left = startXPosBoxes;
        int top = startYPosBoxes;
        int right = left + (int)boxWidthHeight;
        int bottom = top + (int)boxWidthHeight;
        homeFirstSetBox.set(left, top, right, bottom);

        //Home Second Set Box
        left += xSetBoxPadding;
        right += xSetBoxPadding;
        homeSecondSetBox.set(left, top, right, bottom);

        //Home Third Set Box
        left += xSetBoxPadding;
        right += xSetBoxPadding;
        homeThirdSetBox.set(left, top, right, bottom);

        //Away First Set Box
        left = startXPosBoxes;
        top = bottom + ySetBoxPadding;
        right = left + (int)boxWidthHeight;
        bottom = top + (int)boxWidthHeight;
        awayFirstSetBox.set(left, top, right, bottom);

        //Away Second Set Box
        left += xSetBoxPadding;
        right += xSetBoxPadding;
        awaySecondSetBox.set(left, top, right, bottom);

        //Away Third Set Box
        left += xSetBoxPadding;
        right += xSetBoxPadding;
        awayThirdSetBox.set(left, top, right, bottom);
    }

    ////////////////// SETTERS & GETTERS + HELPER METHODS ////////////////////

    public String getHomePlayerName() {
        return mHomePlayerName;
    }

    public void setHomePlayerName(String name) {
        this.mHomePlayerName = name;
        invalidateRequestLayout();
    }

    public String getAwayPlayerName() {
        return mAwayPlayerName;
    }

    public void setAwayPlayerName(String name) {
        this.mAwayPlayerName = name;
        invalidateRequestLayout();
    }

    public int getHomeFirstSetScore() {
        return mHomeFirstSetScore;
    }

    public void setHomeFirstSetScore(int mFirstSetScore) {
        this.mHomeFirstSetScore = mFirstSetScore;
        invalidateRequestLayout();
    }

    public int getHomeSecondSetScore() {
        return mHomeSecondSetScore;
    }

    public void setHomeSecondSetScore(int mSecondSetScore) {
        this.mHomeSecondSetScore = mSecondSetScore;
        invalidateRequestLayout();
    }

    public int getHomeThirdSetScore() {
        return mHomeThirdSetScore;
    }

    public void setHomeThirdSetScore(int mThirdSetScore) {
        this.mHomeThirdSetScore = mThirdSetScore;
        invalidateRequestLayout();
    }

    private void invalidateRequestLayout() {
        invalidate();
        requestLayout();
    }

    /////////////////// HELPER METHODS ///////////////////////

    public void setTextHeight(float textHeight) {
        mTextSize = textHeight;
        invalidate();
    }

    private void setLayerToSW(View v) {
        if (!v.isInEditMode() && Build.VERSION.SDK_INT >= 11) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    private void setLayerToHW(View v) {
        if (!v.isInEditMode() && Build.VERSION.SDK_INT >= 11) {
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
    }

    public int getAwayFirstSetScore() {
        return mAwayFirstSetScore;
    }

    public void setAwayFirstSetScore(int mAwayFirstSetScore) {
        this.mAwayFirstSetScore = mAwayFirstSetScore;
        invalidateRequestLayout();
    }

    public int getAwaySecondSetScore() {
        return mAwaySecondSetScore;
    }

    public void setAwaySecondSetScore(int mAwaySecondSetScore) {
        this.mAwaySecondSetScore = mAwaySecondSetScore;
        invalidateRequestLayout();
    }

    public int getAwayThirdSetScore() {
        return mAwayThirdSetScore;
    }

    public void setAwayThirdSetScore(int mAwayThirdSetScore) {
        this.mAwayThirdSetScore = mAwayThirdSetScore;
        invalidateRequestLayout();
    }

    public void resetSetScores() {
        setHomeFirstSetScore(0);
        setHomeSecondSetScore(0);
        setHomeThirdSetScore(0);
        setAwayFirstSetScore(0);
        setAwaySecondSetScore(0);
        setAwayThirdSetScore(0);
    }

    @Override
    public String getPlayersName(String tag) {
        if (tag.equalsIgnoreCase(HomeGameFragment.class.getSimpleName())) {
            return getHomePlayerName();
        } else {
            return getAwayPlayerName();
        }
    }

    @Override
    public void setPlayersName(String tag, String name) {
        if (tag.equalsIgnoreCase(HomeGameFragment.class.getSimpleName())) {
            setHomePlayerName(name);
        } else {
            setAwayPlayerName(name);
        }
    }
}

