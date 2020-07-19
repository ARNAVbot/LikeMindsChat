package com.example.likemindschat;

import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;
import static com.example.likemindschat.App.getAppContext;

public class Prefs {

    private static final String USER_NAME = "user_name";
    private static final String AUTH_TOKEN = "auth_token";

    private static SharedPreferences SHARED_PREFERENCES;
    private static SharedPreferences.Editor EDITOR;

    private static Prefs instance;

    public Prefs() {

    }

    public static Prefs getInstance() {
        if (SHARED_PREFERENCES == null) {
            SHARED_PREFERENCES = getAppContext().getSharedPreferences("Prefs", MODE_PRIVATE);
        }
        if (EDITOR == null) {
            EDITOR = SHARED_PREFERENCES.edit();
            EDITOR.apply();
        }
        if (null == instance) {
            synchronized (Prefs.class) {
                if (null == instance) {
                    instance = new Prefs();
                }
            }
        }
        return instance;
    }

    public void clearPrefs() {
        EDITOR.putString(AUTH_TOKEN, null);
        EDITOR.putString(USER_NAME, null);
        EDITOR.apply();
    }

    public String getAuthToken() {
        return SHARED_PREFERENCES.getString(AUTH_TOKEN, null);
    }

    public void setAuthToken(String token) {
        updatePreference(AUTH_TOKEN, token);
    }

    public String getUserName() {
        return SHARED_PREFERENCES.getString(USER_NAME, "");
    }

    public void setUserName(String userName) {
        updatePreference(USER_NAME, userName);
    }

    private void updatePreference(String key, String value) {
        EDITOR.putString(key, value);
        EDITOR.apply();
    }
}
