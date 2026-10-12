package com.example.mindstep;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Gestor de Persistencia Local ligera usando SharedPreferences
 * Almacena el estado inicial, preferencias y banderas del usuario
 */
public class PreferencesManager {

    private static final String PREF_NAME = "MindStepPreferences";
    private static final String KEY_FIRST_RUN = "isFirstLogin";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_LAST_RISK = "lastRisk";
    private static final String KEY_LAST_SCORE = "lastScore";
    private static final String KEY_DARK_MODE = "darkMode";
    private static final String KEY_REMINDERS = "reminders";
    private static final String KEY_STRESS_ALERT = "stressAlert";

    private final SharedPreferences prefs;

    public PreferencesManager(Context context) {
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public boolean isFirstTime() {
        return prefs.getBoolean(KEY_FIRST_RUN, true);
    }

    public void setFirstTime(boolean isFirstTime) {
        prefs.edit().putBoolean(KEY_FIRST_RUN, isFirstTime).apply();
    }

    public String getUserName() {
        return prefs.getString(KEY_USER_NAME, "Axel");
    }

    public void setUserName(String name) {
        prefs.edit().putString(KEY_USER_NAME, name).apply();
    }

    public String getLastRisk() {
        return prefs.getString(KEY_LAST_RISK, "Bajo");
    }

    public void setLastRisk(String risk) {
        prefs.edit().putString(KEY_LAST_RISK, risk).apply();
    }

    public int getLastScore() {
        return prefs.getInt(KEY_LAST_SCORE, 85);
    }

    public void setLastScore(int score) {
        prefs.edit().putInt(KEY_LAST_SCORE, score).apply();
    }

    public boolean isDarkMode() {
        return prefs.getBoolean(KEY_DARK_MODE, false);
    }

    public void setDarkMode(boolean enabled) {
        prefs.edit().putBoolean(KEY_DARK_MODE, enabled).apply();
    }

    public boolean isRemindersEnabled() {
        return prefs.getBoolean(KEY_REMINDERS, true);
    }

    public void setRemindersEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_REMINDERS, enabled).apply();
    }

    public boolean isStressAlertEnabled() {
        return prefs.getBoolean(KEY_STRESS_ALERT, true);
    }

    public void setStressAlertEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_STRESS_ALERT, enabled).apply();
    }

    public void resetAll() {
        prefs.edit().clear().apply();
    }
}
