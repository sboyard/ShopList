package ca.on.conestogac.rsc.shoppinglist.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import ca.on.conestogac.rsc.shoppinglist.R;

public class MainActivity extends AppCompatActivity {

    private boolean darkThemeOn;
    private SharedPreferences sharedPref;
    private boolean creatingActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        creatingActivity = true;

        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        setDarkThemeOn(darkThemeOn = sharedPref.getBoolean("theme", false));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_host);

        if (savedInstanceState == null) {
            ShoppingListFragment fragment = new ShoppingListFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.main_content, fragment)
                    .commit();
        }
    }

    protected void onResume() {

        boolean darkTheme = sharedPref.getBoolean("theme", false);
        if (darkTheme != this.darkThemeOn) {
            this.darkThemeOn = darkTheme;
            recreate();
        }
        creatingActivity = false;
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shopping_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
            case  R.id.menu_settings:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                return true;
//            case R.id.menu_statistics:
//                Intent intent = new Intent(getApplicationContext(), StatsActivity.class);
//
//                startActivity(intent);
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setDarkThemeOn(boolean darkThemeOn) {
        if (darkThemeOn) {
            setTheme(R.style.DarkAppTheme);
        } else      {
            setTheme(R.style.AppTheme);
        }
    }

}
