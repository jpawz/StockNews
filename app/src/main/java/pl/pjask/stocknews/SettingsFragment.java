package pl.pjask.stocknews;

import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

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
        Preference button = (Preference) getPreferenceManager().findPreference("update_symbols_button");
        if (button != null) {
            button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    MenuPreferences.newInstance(getContext()).
                            updateSymbolList();
                    return true;
                }
            });
        }
    }
}
