package com.piggebank.settingsPackage;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.piggebank.android.R;

public class MyPreferenceFragment extends PreferenceFragment
{
    @Override
    public void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);


    }
}
