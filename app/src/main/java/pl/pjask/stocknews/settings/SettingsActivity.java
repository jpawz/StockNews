package pl.pjask.stocknews.settings;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.util.Log;
import android.widget.Toast;

import com.obsez.android.lib.filechooser.ChooserDialog;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

import java.io.IOException;

import pl.pjask.stocknews.R;
import pl.pjask.stocknews.db.DBHelper;
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

            Preference exportPrefs = findPreference(getString(R.string.export_prefs_button));
            exportPrefs.setOnPreferenceClickListener(this::exportPrefs);
        }

        private boolean exportPrefs(Preference preference) {

            new ChooserDialog().with(getContext())
                    .withFilter(true, false)
                    .withStartFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath())
                    .withResources(R.string.title_choose_dir, R.string.dialog_ok, R.string.dialog_cancel)
                    .withChosenListener((path, pathFile) -> {
                        try {
                            DBHelper.getInstance(getContext()).exportDatabase(path);
                            Toast.makeText(getContext(), "Exported to " + path, Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Log.e("Settings", e.getMessage());
                        }
                    })
                    .build()
                    .show();
            return true;
        }

        private boolean importPrefs(Preference preference) {
            new ChooserDialog().with(getContext())
                    .withFilter(false, false, "db")
                    .withStartFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath())
                    .withChosenListener((path, pathFile) -> {
                        try {
                            DBHelper.getInstance(getContext()).importDatabase(path);
                            Toast.makeText(getContext(), "Imported", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Log.e("Settings", e.getMessage());
                        }
                    })
                    .build()
                    .show();
            return true;
        }


        private boolean updateSymbols(Preference preference) {
            Hints.getInstance(getContext()).updateSymbolList();

            return false;
        }


        @Override
        public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);

        }
    }
}
