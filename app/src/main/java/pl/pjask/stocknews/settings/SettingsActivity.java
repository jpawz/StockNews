package pl.pjask.stocknews.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import pl.pjask.stocknews.R;
import pl.pjask.stocknews.utils.Hints;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            Preference updateButton = findPreference(getString(R.string.update_symbols_button));
            updateButton.setOnPreferenceClickListener(this::updateSymbols);

            Preference importPrefs = findPreference(getString(R.string.import_prefs_button));
            importPrefs.setOnPreferenceClickListener(this::importPrefs);

            Preference esportPrefs = findPreference(getString(R.string.export_prefs_button));
            esportPrefs.setOnPreferenceClickListener(this::esportPrefs);
        }

        private boolean esportPrefs(Preference preference) {
            Toast.makeText(getContext(), "not yet implemented", Toast.LENGTH_SHORT).show();
            return false;
        }

        private boolean importPrefs(Preference preference) {
            Toast.makeText(getContext(), "not yet implemented", Toast.LENGTH_SHORT).show();
            return false;
        }

        private boolean updateSymbols(Preference preference) {
            Hints.getInstance(getContext()).updateSymbolList();

            return false;
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
