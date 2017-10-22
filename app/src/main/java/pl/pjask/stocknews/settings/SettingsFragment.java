package pl.pjask.stocknews.settings;

import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import pl.pjask.stocknews.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prepareUpdateSymbolsButton();

    }

    private void prepareUpdateSymbolsButton() {
        Preference button = getPreferenceManager().findPreference("update_symbols_button");
        if (button != null) {
            button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Hints.getInstance(getContext())
                            .updateSymbolList();
                    return true;
                }
            });
        }
    }
}
