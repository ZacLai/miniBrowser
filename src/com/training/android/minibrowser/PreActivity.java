package com.training.android.minibrowser;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PreActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyFragment())
                .commit();
    }
}
