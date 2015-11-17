package com.keeper.score.utils;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Browser;
import android.util.Log;

/**
 * Created by Rith on 10/28/2015.
 */
public class FragmentUtils {

    public static void launchFragment (FragmentManager fm, Fragment frag, int containerRID, boolean addToBackStack, String tag) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if(addToBackStack){
            ft.add(containerRID, frag, tag);
            ft.addToBackStack(tag);
        } else {
            ft.replace(containerRID, frag, tag);
        }
        ft.commit();
    }

    public static Fragment getFragment(FragmentManager fm, int fragmentRID) {
        return fm.findFragmentById(fragmentRID);
    }

    public static Fragment getFragment(FragmentManager fm, String fragTag) {
        return fm.findFragmentByTag(fragTag);
    }

    public static Fragment getTopFragment(FragmentManager fm) {
        Fragment topFragment = null;
        if (fm.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry backEntry = fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1);
            String topFragmentTag = backEntry.getName();
            topFragment = fm.findFragmentByTag(topFragmentTag);
        }
        return topFragment;
    }

    public static void launchBrowser(Context context, String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

            //**** This will only work with the default browser. ***** //
            Bundle bundle = new Bundle();
            bundle.putString("Cookie", "");
            intent.putExtra(Browser.EXTRA_HEADERS, bundle);
            // ***** Default Browser ***** //

            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Intent target = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            Intent intent = Intent.createChooser(target, "Open with...");
            context.startActivity(intent);
        }
    }
}
