package ca.on.conestogac.rsc.shoppinglist.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;

import ca.on.conestogac.rsc.shoppinglist.R;
import ca.on.conestogac.rsc.shoppinglist.databinding.FragmentShoppingListBinding;
import ca.on.conestogac.rsc.shoppinglist.databinding.RecyclerViewDataAdapter;
import ca.on.conestogac.rsc.shoppinglist.interfaces.ShoppingListener;
import ca.on.conestogac.rsc.shoppinglist.viewmodels.ShoppingListViewModel;
import ca.on.conestogac.rsc.shoppinglist.viewmodels.ShoppingViewModel;

public class ShoppingListFragment extends Fragment implements ShoppingListener {

    private FragmentShoppingListBinding binding;
    private RecyclerViewDataAdapter adapter;
    private ShoppingViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // title
        requireActivity().setTitle(R.string.app_name);

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_shopping_list,
                container,
                false);

        // view model & binding
        viewModel = new ViewModelProvider(this).get(ShoppingViewModel.class);
        binding.setViewModel(viewModel);

        // recycler view adapter
        adapter = new RecyclerViewDataAdapter(viewModel.getData(), R.layout.shopping_list_row);
        binding.rvShoppingLists.setAdapter(adapter);

        // init RecyclerView
        RecyclerView recyclerView = binding.rvShoppingLists;
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        // get New List editText and performClick on submit button when enter on keyboard is pressed
        EditText edit_txt = (EditText) binding.textNewShoppingList;
        edit_txt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN)) {
                ImageButton submitButton = binding.imageButtonNewShoppingList;
                submitButton.performClick();
                return true;
            }
            return false;
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.onViewCreated(this);
    }

    @Override
    public void onDestroyView() {
        viewModel.onViewDestroyed();
        super.onDestroyView();
    }

    @Override
    public void onShoppingListInserted(int position) {
        adapter.notifyItemInserted(position);
    }

    @Override
    public void onShoppingListRemoved(int position) {
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void onShoppingListActivityChange(ShoppingListViewModel shoppingList) {
        ProductListFragment fragment = new ProductListFragment(shoppingList);
        getParentFragmentManager().beginTransaction()
            .replace(R.id.main_content, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(null)
            .commit();
    }
}