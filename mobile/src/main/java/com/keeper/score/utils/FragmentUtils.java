package com.keeper.score.utils;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

/**
 * Created by Rith on 10/28/2015.
 */
public class FragmentUtils {

    public static void launchFragment (FragmentManager fm, Fragment frag, int containerRID, boolean addToBackStack, String tag) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
        if(addToBackStack){
            ft.add(containerRID, frag, tag);
            ft.addToBackStack(tag);
        } else {
            ft.replace(containerRID, frag, tag);
        }
        ft.commit();
    }
}
