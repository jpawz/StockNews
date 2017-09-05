package pl.pjask.stocknews;

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

public class MainActivity extends AppCompatActivity {
    private final String TAG = "stocknews";

    private DrawerLayout mDrawer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        prepareNavigationDrawer();

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

    private void prepareNavigationDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        setupDrawerContent(navigationView);

        MenuPreferences menuPreferences = MenuPreferences.newInstance(this);
        Set<String> menuItems = menuPreferences.getMenuItems();

        MenuItem stockGroupItem = navigationView.getMenu().getItem(0);
        SubMenu subMenu = stockGroupItem.getSubMenu();

        if (menuItems != null) {
            Log.i("stocknews", menuItems.toString());
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
                Log.i(TAG, "manage news");
                fragmentClass = AddStockFragment.class;
                break;
            default:
                Log.i(TAG, "default");
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
                .addToBackStack(null)
                .commit();

        mDrawer.closeDrawers();
    }
}