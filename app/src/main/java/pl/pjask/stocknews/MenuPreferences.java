package pl.pjask.stocknews;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class MenuPreferences {
    private static final MenuPreferences instance = new MenuPreferences();

    public static final String PREF_MENU_ITEMS = "menuItem";
    private static SharedPreferences settings;
    private Set<String> storedMenuItems;

    private MenuPreferences() {
    }

    public static MenuPreferences newInstance(Context context) {
        settings = PreferenceManager.getDefaultSharedPreferences(context);

        return instance;
    }

    public Set<String> getMenuItems() {
        storedMenuItems =
                new TreeSet<>(settings.getStringSet(PREF_MENU_ITEMS, Collections.<String>emptySet()));
        return storedMenuItems;
    }

    public void addMenuItem(String itemName) {
        storedMenuItems.add(itemName);

        SharedPreferences.Editor editor = settings.edit();
        editor.putStringSet(PREF_MENU_ITEMS, storedMenuItems)
                .apply();
    }
}
