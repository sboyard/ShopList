package ca.on.conestogac.rsc.shoppinglist.views;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import ca.on.conestogac.rsc.shoppinglist.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}