package ca.on.conestogac.rsc.shoppinglist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import ca.on.conestogac.rsc.shoppinglist.data.ApplicationDbRepository;
import ca.on.conestogac.rsc.shoppinglist.data.models.ShoppingList;
import ca.on.conestogac.rsc.shoppinglist.data.models.ShoppingListCounts;
import ca.on.conestogac.rsc.shoppinglist.data.source.ShoppingListsDataSource;

public class StatsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean darkTheme = sharedPref.getBoolean("theme", false);
        setTheme(darkTheme ? R.style.DarkAppTheme : R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats_activity);

        // setup listener on preference change
        sharedPref.registerOnSharedPreferenceChangeListener(this);

        App app = (App)getApplication();
        ApplicationDbRepository db = app.getRepository();

        final TextView prodCount=(TextView)findViewById(R.id.prodCount);
        final TextView listCount=(TextView)findViewById(R.id.listCount);


        db.shoppingLists().getListCount(new ShoppingListsDataSource.LoadShoppingListCallback() {
            @Override
            public void onShoppingListCountReturned(int count) {
                listCount.setText(Integer.toString(count));
            }
            @Override
            public void onShoppingListLoaded(ShoppingList shoppingList) {}

            @Override
            public void onShoppingListCountsLoaded(ShoppingListCounts shoppingList) {

            }

            @Override
            public void onDataNotAvailable() {}
        });

        db.products().getProductsCount(count -> prodCount.setText(Integer.toString(count)));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("theme")) {
            recreate();
        }
    }

    public static class StatsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean ret = true;
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
            default :
                ret = super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}
