package ca.on.conestogac.rsc.shoppinglist.views;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ca.on.conestogac.rsc.shoppinglist.R;
import ca.on.conestogac.rsc.shoppinglist.databinding.ActivityShoppingListBinding;
import ca.on.conestogac.rsc.shoppinglist.models.ShoppingList;
import ca.on.conestogac.rsc.shoppinglist.viewmodels.ShoppingListViewModel;

public class ShoppingListActivity extends AppCompatActivity implements LifecycleOwner {

    private ShoppingListViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set content view & get binding
        ActivityShoppingListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_shopping_list);

        // view model
        //viewModel = new ViewModelProvider(this).get(ListItemViewModel.class);
        ShoppingList shoppingList = (ShoppingList) getIntent().getSerializableExtra("model");

        setTitle(shoppingList.getTitle());

        //viewModel = new ShoppingListViewModel(shoppingList);
        //binding.setViewModel(viewModel);

        // init RecyclerView
        RecyclerView recyclerView = binding.getRoot().findViewById(R.id.rv_products);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        // get New List editText and performClick on submit button when enter on keyboard is pressed
        EditText edit_txt = (EditText) findViewById(R.id.text_new_product);
        edit_txt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN)) {
                ImageButton submitButton = findViewById(R.id.imageButton_new_product);
                submitButton.performClick();
                return true;
            }
            return false;
        });
    }
}
