package pl.pjask.stocknews;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class MenuPreferences {
    private static final MenuPreferences instance = new MenuPreferences();

    private static final String PREFS_NAME = "stocknews";
    private static final String PREF_MENU_ITEMS = "menuItem";
    private static SharedPreferences settings;
    private Set<String> storedMenuItems;

    private MenuPreferences() {
    }

    public static MenuPreferences newInstance(Context context) {
        settings = context.getSharedPreferences(PREFS_NAME, 0);

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
