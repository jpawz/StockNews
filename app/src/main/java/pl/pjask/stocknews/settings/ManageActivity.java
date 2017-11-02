package pl.pjask.stocknews.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import pl.pjask.stocknews.R;


public class ManageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        Toolbar toolbar = findViewById(R.id.manage_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener((e) -> onBackPressed());

        Fragment fragment = new ManageStocksFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_manage, fragment)
                .commit();
    }
}
