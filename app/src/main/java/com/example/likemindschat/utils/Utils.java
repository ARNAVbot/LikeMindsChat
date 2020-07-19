package com.example.likemindschat.utils;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.likemindschat.Prefs;

public class Utils {

    public static boolean isEmpty(String field) {
        if (field == null || field.equalsIgnoreCase("")) {
            return true;
        }
        return false;
    }

    public static void hideKeyboard(Activity activity) {
//        InputMethodManager inputManager =
//                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputManager.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(),
//                0);
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static String getChatWindowId() {
        return String.valueOf(System.currentTimeMillis()).concat(Prefs.getInstance().getUserName().split("@")[0]);
    }

    public static String getCreatorName() {
        return Prefs.getInstance().getUserName().split("@")[0];
    }

}
