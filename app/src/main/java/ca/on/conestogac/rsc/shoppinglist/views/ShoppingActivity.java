package ca.on.conestogac.rsc.shoppinglist.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import ca.on.conestogac.rsc.shoppinglist.R;
import ca.on.conestogac.rsc.shoppinglist.databinding.ActivityShoppingBinding;
import ca.on.conestogac.rsc.shoppinglist.interfaces.ShoppingListener;
import ca.on.conestogac.rsc.shoppinglist.models.ListItem;
import ca.on.conestogac.rsc.shoppinglist.viewmodels.ShoppingViewModel;

public class ShoppingActivity extends AppCompatActivity implements LifecycleOwner, ShoppingListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get binding from Layout and set ViewModel
        ActivityShoppingBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_shopping);
        binding.setViewModel(new ViewModelProvider(this).get(ShoppingViewModel.class));

        // init RecyclerView
        RecyclerView recyclerView = binding.getRoot().findViewById(R.id.rv_shopping_lists);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        // get New List editText and performClick on submit button when enter on keyboard is pressed
        EditText edit_txt = (EditText) findViewById(R.id.text_new_list);
        edit_txt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN)) {
                ImageButton submitButton = findViewById(R.id.imageButton_add_new_list);
                submitButton.performClick();
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onShoppingListClicked(ListItem shoppingList) {

    }
}
