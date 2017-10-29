package pl.pjask.stocknews;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import pl.pjask.stocknews.settings.SettingsFragment;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fragment fragment = new SettingsFragment();
        getSupportFragmentManager().beginTransaction()
                .add(fragment, "tag")
                .commit();
    }
}
