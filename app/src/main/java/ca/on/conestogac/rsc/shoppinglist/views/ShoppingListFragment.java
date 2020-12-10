package ca.on.conestogac.rsc.shoppinglist.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import ca.on.conestogac.rsc.shoppinglist.databinding.FragmentShoppingListBinding;
import ca.on.conestogac.rsc.shoppinglist.interfaces.ShoppingListener;
import ca.on.conestogac.rsc.shoppinglist.viewmodels.ShoppingListItemViewModel;
import ca.on.conestogac.rsc.shoppinglist.viewmodels.ShoppingListsViewModel;

public class ShoppingListFragment extends Fragment implements ShoppingListener {

    private FragmentShoppingListBinding binding;
    private ShoppingListsViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // title
        requireActivity().setTitle(R.string.app_name);
        App app = (App)requireActivity().getApplication();

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_shopping_list,
                container,
                false);

        // view model & binding
        viewModel = new ShoppingListsViewModel(requireContext(), app.getRepository());
        binding.setViewModel(viewModel);

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

        // drag and drop
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.rvShoppingLists.recyclerView);

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        return binding.getRoot();
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            viewModel.onCollectionsSwap(fromPosition, toPosition);
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };

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
    public void onSnackBarDisplay(String message) {
        // TODO : display snackBar message
    }

    @Override
    public void onShoppingListInserted(int position) {
        binding.rvShoppingLists.notifyItemInserted(position, true);
    }

    @Override
    public void onShoppingListRangeInserted(int fromPosition, int toPosition) {
        binding.rvShoppingLists.notifyItemRangeInserted(fromPosition, toPosition, true);
    }

    @Override
    public void onShoppingListItemMoved(int fromPosition, int toPosition) {
        binding.rvShoppingLists.notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onShoppingListRemoved(int position) {
        binding.rvShoppingLists.notifyItemRemoved(position);
    }

    @Override
    public void onShoppingListProductRangeRemoved(int fromPosition, int toPosition) {
        binding.rvShoppingLists.notifyItemRangeRemoved(fromPosition, toPosition);
    }

    @Override
    public void onShoppingListActivityChange(ShoppingListItemViewModel shoppingList) {
        ProductListFragment fragment = new ProductListFragment();

        Bundle args = new Bundle();
        args.putString("shoppingListId", shoppingList.getShoppingListId());
        fragment.setArguments(args);

        getParentFragmentManager().beginTransaction()
            .replace(R.id.main_content, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(null)
            .commit();
    }
}