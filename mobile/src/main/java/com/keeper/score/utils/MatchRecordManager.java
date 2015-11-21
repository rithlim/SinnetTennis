package com.keeper.score.utils;

import android.content.Context;
import android.util.Log;

import com.keeper.score.models.MatchRecord;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rith on 11/20/2015.
 */
public class MatchRecordManager {
    private static final String TAG = MatchRecordManager.class.getSimpleName();
    private static final String STORAGE_KEY = "storageKey";
    private static List<MatchRecord> matchRecordList;

    public static List<MatchRecord> getMatchRecordList() {
        return matchRecordList;
    }

    public static boolean loadMatchRecordList(Context context) {
        List<MatchRecord> list = null;
        try {
            list = ((List<MatchRecord>) readObject(context));
        } catch (IOException ioe) {
            Log.d(TAG, ioe.toString());
        } catch (ClassNotFoundException cnfe) {
            Log.d(TAG, cnfe.toString());
        }

        if (list != null) {
            matchRecordList = list;
            return true;
        } else {
            matchRecordList = new ArrayList<>();
            return false;
        }
    }

    public static void recordMatchScore(Context context, MatchRecord matchRecord) {
        loadMatchRecordList(context);
        matchRecordList.add(matchRecord);
        storeMatchRecords(context);
    }

    public static void storeMatchRecords(Context context) {
        try {
            storeMatchList(context, matchRecordList);
        } catch (IOException ioe) {
            Log.d(TAG, ioe.toString());
        }
    }

    private static void storeMatchList(Context context, Object object) throws IOException {
        FileOutputStream fos = context.openFileOutput(STORAGE_KEY, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(object);
        oos.close();
        fos.close();
    }

    private static Object readObject(Context context) throws IOException, ClassNotFoundException {
        FileInputStream fis = context.openFileInput(STORAGE_KEY);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object object = ois.readObject();
        return object;
    }
}
