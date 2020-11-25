package ca.on.conestogac.rsc.shoppinglist.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import ca.on.conestogac.rsc.shoppinglist.R;
import ca.on.conestogac.rsc.shoppinglist.interfaces.ActivityListener;
import ca.on.conestogac.rsc.shoppinglist.interfaces.ShoppingNavigator;
import ca.on.conestogac.rsc.shoppinglist.viewmodels.ShoppingListViewModel;

public class MainActivity extends AppCompatActivity implements ShoppingNavigator {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_host);

        if (savedInstanceState == null) {
            onStartShoppingList();
        }
    }

    @Override
    public void onEnableActionBarUp(boolean enabled) {
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(enabled);
            ab.setDisplayShowHomeEnabled(enabled);
        }
    }

    @Override
    public void onStartShoppingList() {
        ShoppingListFragment fragment = new ShoppingListFragment(this);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_content, fragment)
                .commit();

        onEnableActionBarUp(false);
    }

    @Override
    public void onStartProductList(ShoppingListViewModel shoppingListViewModel) {
        ProductListFragment fragment = new ProductListFragment(this, shoppingListViewModel);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_content, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack("product")
                .commit();

        onEnableActionBarUp(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_content);
        if (!(fragment instanceof ActivityListener) || !((ActivityListener) fragment).onBackPressed()) {
            super.onBackPressed();
        }
    }
}
