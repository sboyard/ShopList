package ca.on.conestogac.rsc.shoppinglist.views;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Observable;
import androidx.fragment.app.Fragment;
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

import ca.on.conestogac.rsc.shoppinglist.App;
import ca.on.conestogac.rsc.shoppinglist.R;
import ca.on.conestogac.rsc.shoppinglist.databinding.FragmentProductListBinding;
import ca.on.conestogac.rsc.shoppinglist.databinding.RecyclerViewDataAdapter;
import ca.on.conestogac.rsc.shoppinglist.interfaces.ProductListener;
import ca.on.conestogac.rsc.shoppinglist.viewmodels.ProductListsViewModel;

public class ProductListFragment extends Fragment implements ProductListener {
    private FragmentProductListBinding binding;
    private RecyclerViewDataAdapter adapter;
    private ProductListsViewModel viewModel = null;
    private String shoppingListId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        App app = (App)(requireActivity().getApplication());

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_product_list,
                container,
                false);

        shoppingListId = requireArguments().getString("shoppingListId");

        // viewModel
        viewModel = new ProductListsViewModel(requireContext(), app.getRepository());
        binding.setViewModel(viewModel);

        // recycler view adapter
        adapter = new RecyclerViewDataAdapter(viewModel.getProducts(), R.layout.product_row);
        binding.rvProductsList.setAdapter(adapter);

        // init RecyclerView
        RecyclerView recyclerView = binding.rvProductsList;
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        // get New List editText and performClick on submit button when enter on keyboard is pressed
        EditText edit_txt = (EditText) binding.textNewProduct;
        edit_txt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN)) {
                ImageButton submitButton = binding.imageButtonNewProduct;
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
    public void onResume() {
        super.onResume();
        viewModel.start(shoppingListId);
        viewModel.shoppingListTitle.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                requireActivity().setTitle(viewModel.shoppingListTitle.get());
            }
        });
    }

    @Override
    public void onDestroyView() {
        viewModel.onViewDestroyed();
        super.onDestroyView();
    }

    @Override
    public void onSnackBarDisplay(String message) {
        // TODO : display snackBar message
    }

    @Override
    public void onProductInserted(int position) {
        adapter.notifyItemInserted(position);
        binding.rvProductsList.scrollToPosition(position);
    }

    @Override
    public void onProductRangeInserted(int fromPosition, int toPosition) {
        adapter.notifyItemRangeInserted(fromPosition, toPosition);
        binding.rvProductsList.scrollToPosition(fromPosition);
    }

    @Override
    public void onProductItemMoved(int fromPosition, int toPosition) {
        adapter.notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onProductRemoved(int position) {
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void onProductRangeRemoved(int fromPosition, int toPosition) {
        adapter.notifyItemRangeRemoved(fromPosition, toPosition);
    }
}
