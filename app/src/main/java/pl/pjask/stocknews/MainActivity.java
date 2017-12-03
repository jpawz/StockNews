package pl.pjask.stocknews;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.SubMenu;

import org.apache.commons.lang3.time.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pl.pjask.stocknews.models.Stock;
import pl.pjask.stocknews.settings.ManageActivity;
import pl.pjask.stocknews.settings.SettingsActivity;
import pl.pjask.stocknews.utils.ArticlesProvider;
import pl.pjask.stocknews.utils.Hints;
import pl.pjask.stocknews.utils.MenuUtils;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "stocknews";
    private MenuUtils mMenuUtils;
    private DrawerLayout mDrawer;
    private ArrayList<Stock> activeStocks = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        setUpToolbar();

        mDrawer = findViewById(R.id.drawer_layout);

        updateSymbols();

        mMenuUtils = MenuUtils.getInstance(this);
        mMenuUtils.setMenuChangeListener(this::prepareNavigationDrawer);
        setActiveStockSymbolsForAllSymbols();

        prepareNavigationDrawer();

        prepareRootLayout();
    }

    private void updateSymbols() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        boolean autoupdate = preferences.getBoolean(getString(R.string.auto_update_key), false);

        if (!autoupdate) {
            return;
        }

        int interval = Integer.parseInt(preferences.getString(getString(R.string.update_interval_key), "0"));
        Date lastUpdate = new Date(preferences.getLong(getString(R.string.last_update_date), (new Date()).getTime()));
        Date nextUpdate = DateUtils.addDays(lastUpdate, interval);

        if (nextUpdate.after(new Date())) {
            Hints hints = Hints.getInstance(this);
            hints.updateSymbolList();
        }


    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        prepareNavigationDrawer();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
            case R.id.refresh:
                updateNews();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateNews() {
        for (Stock stock : activeStocks) {
            ArticlesProvider.getInstance(this).updateArticles(stock);
        }
    }

    private void prepareRootLayout() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.root_layout);

        if (fragment == null) {
            fragment = new ArticlesListFragment();
            fm.beginTransaction()
                    .add(R.id.root_layout, fragment)
                    .commit();
        }
    }


    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar_menu, menu);
        return true;
    }

    private void prepareNavigationDrawer() {
        NavigationView navigationView = findViewById(R.id.navigation_view);
        setupDrawerContent(navigationView);

        List<String> menuItems = mMenuUtils.getSymbolNames();

        MenuItem stockGroupItem = navigationView.getMenu().getItem(1);
        SubMenu subMenu = stockGroupItem.getSubMenu();
        subMenu.clear();

        for (String item : menuItems) {
            subMenu.add(item);
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                item -> {
                    selectDrawerItem(item);
                    return true;
                }
        );
    }

    private void selectDrawerItem(MenuItem item) {
        Fragment fragment = null;
        Class fragmentClass;
        Intent intent;
        switch (item.getItemId()) {
            case R.id.manage_news:
                intent = new Intent(MainActivity.this, ManageActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
                return;
            case R.id.settings:
                intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
                return;
            case R.id.all_stocks:
                fragmentClass = ArticlesListFragment.class;
                setActiveStockSymbolsForAllSymbols();
                break;
            default:
                fragmentClass = ArticlesListFragment.class;
                activeStocks.clear();
                activeStocks.add(mMenuUtils.getStock(item.getTitle().toString()));
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            Log.e(TAG, "exception at fragmentClass.newInstance()");
            e.printStackTrace();
        }

        Bundle fragmentArgs = new Bundle();
        fragmentArgs.putStringArrayList("symbols", getActiveStockSymbolNames());

        assert fragment != null;
        fragment.setArguments(fragmentArgs);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.root_layout, fragment)
                .commit();

        mDrawer.closeDrawers();
    }

    private void setActiveStockSymbolsForAllSymbols() {
        activeStocks = new ArrayList<>();
        activeStocks.addAll(mMenuUtils.getStocks());
    }

    private ArrayList<String> getActiveStockSymbolNames() {
        ArrayList<String> names = new ArrayList<>();
        for (Stock stock : activeStocks) {
            names.add(stock.getStockSymbol());
        }
        return names;
    }
}