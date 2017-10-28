package pl.pjask.stocknews;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.SubMenu;

import java.util.Set;

import pl.pjask.stocknews.settings.SettingsFragment;
import pl.pjask.stocknews.utils.Hints;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "stocknews";
    private Menu menu;
    private DrawerLayout mDrawer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpToolbar();

        mDrawer = findViewById(R.id.drawer_layout);

        Hints hints = Hints.getInstance(this);
        hints.updateSymbolList();

        menu = Menu.getInstance(this);
        menu.setMenuChangeListener(this::prepareNavigationDrawer);

        prepareNavigationDrawer();

        prepareRootLayout();
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

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void prepareRootLayout() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.root_layout);

        if (fragment == null) {
            fragment = new NewsListFragment();
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

        Set<String> menuItems = menu.getSymbolNames();

        MenuItem stockGroupItem = navigationView.getMenu().getItem(0);
        SubMenu subMenu = stockGroupItem.getSubMenu();
        subMenu.clear();

        if (menuItems != null) {
            for (String item : menuItems) {
                subMenu.add(item);
            }
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        selectDrawerItem(item);
                        return true;
                    }
                }
        );
    }

    private void selectDrawerItem(MenuItem item) {
        Fragment fragment = null;
        Class fragmentClass;
        switch (item.getItemId()) {
            case R.id.manage_news:
                Intent intent = new Intent(MainActivity.this, ManageActivity.class);
                startActivity(intent);
                return;
            case R.id.settings:
                fragmentClass = SettingsFragment.class;
                break;
            default:
                fragmentClass = NewsListFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            Log.e(TAG, "exception at fragmentClass.newInstance()");
            e.printStackTrace();
        }

        Bundle fragmentArgs = new Bundle();
        fragmentArgs.putString("symbol", item.getTitle().toString());

        assert fragment != null;
        fragment.setArguments(fragmentArgs);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.root_layout, fragment)
//                .addToBackStack(null)
                .commit();

        mDrawer.closeDrawers();
    }
}