package com.example.pyrov.mywallpapers;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class WallpapersPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            Preference pageSize = findPreference(getString(R.string.settings_list_size_key));
            bindPreferenceSummaryToValue(pageSize);

            Preference listOrientation = findPreference(getString(R.string.settings_orientation_key));
            bindPreferenceSummaryToValue(listOrientation);

            Preference orderBy = findPreference(getString(R.string.settings_order_key));
            bindPreferenceSummaryToValue(orderBy);

            Preference safeSearch = findPreference(getString(R.string.settings_safe_search_key));
            bindPreferenceSummaryToValue(safeSearch);
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferencesString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferencesString);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            String stringValue = o.toString();
            preference.setSummary(stringValue);
            return true;
        }
    }
}
