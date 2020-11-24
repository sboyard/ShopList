package ca.on.conestogac.rsc.shoppinglist.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ca.on.conestogac.rsc.shoppinglist.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
}
