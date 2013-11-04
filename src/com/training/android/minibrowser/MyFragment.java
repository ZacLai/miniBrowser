package com.training.android.minibrowser;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class MyFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        /*
         * this.findPreference("checkboxPref").setOnPreferenceChangeListener(
         * new OnPreferenceChangeListener() {
         * @SuppressLint("SetJavaScriptEnabled")
         * @Override public boolean onPreferenceChange(Preference preference,
         * Object newValue) { // TODO Auto-generated method stub Boolean
         * checkBoxVal = (Boolean) newValue; if (checkBoxVal.booleanValue() ==
         * true) { Log.d(TAG, "" + checkBoxVal.booleanValue());
         * MainActivity.gWebView.getSettings().setJavaScriptEnabled(true); }
         * else { Log.d(TAG, "" + checkBoxVal.booleanValue());
         * MainActivity.gWebView.getSettings().setJavaScriptEnabled(false); }
         * return true; } });
         */

    }

    public static Boolean getCheckboxPref(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("checkboxPref",
                true);
    }

}