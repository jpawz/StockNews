package pl.pjask.stocknews;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import pl.pjask.stocknews.Utils.BankierParser;

public class MenuPreferences {
    public static final String PREF_MENU_ITEMS = "menuItem";
    private static final String PREF_SYMBOLS = "symbols";
    private static final String TAG = "preferences";
    private static final MenuPreferences instance = new MenuPreferences();
    private static SharedPreferences settings;
    private Set<String> storedMenuItems;

    private MenuPreferences() {
    }

    public static MenuPreferences newInstance(Context context) {
        settings = PreferenceManager.getDefaultSharedPreferences(context);

        return instance;
    }

    /**
     * Returns Stock symbols used for creating menu.
     *
     * @return Stock symbols as a Set of Strings.
     */
    public Set<String> getMenuItems() {
        storedMenuItems =
                new TreeSet<>(settings.getStringSet(PREF_MENU_ITEMS, Collections.<String>emptySet()));
        return storedMenuItems;
    }

    /**
     * Adds stock symbol to menu.
     *
     * @param itemName name of stock symbol (last part of bankier url).
     */
    public void addMenuItem(String itemName) {
        storedMenuItems.add(itemName);

        SharedPreferences.Editor editor = settings.edit();
        editor.putStringSet(PREF_MENU_ITEMS, storedMenuItems)
                .apply();
    }

    /**
     * Get all menu items.
     *
     * @return set of Strings with symbol names.
     */
    public Set<String> getSymbols() {
        return new TreeSet<>(settings.getStringSet(PREF_SYMBOLS, Collections.<String>emptySet()));
    }

    /**
     * Replaces current menu with new symbols set.
     *
     * @param symbols set of Strings with symbol names.
     */
    public void setSymbols(Set<String> symbols) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putStringSet(PREF_MENU_ITEMS, symbols)
                .apply();
    }

    /**
     * Updates stored stock symbols with new ones (if website is accessible).
     */
    public void updateSymbolList() {
        (new UpdateSymbolsTask()).execute();
    }

    private class UpdateSymbolsTask extends AsyncTask<Void, Void, Boolean> {
        private Set<String> updatedSymbols;

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                updatedSymbols = (new BankierParser()).getSymbols();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                SharedPreferences.Editor editor = settings.edit();
                editor.putStringSet(PREF_SYMBOLS, updatedSymbols)
                        .apply();
            }
        }

    }
}
