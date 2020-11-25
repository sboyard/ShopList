package ca.on.conestogac.rsc.shoppinglist.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import ca.on.conestogac.rsc.shoppinglist.data.ApplicationDbRepository;
import ca.on.conestogac.rsc.shoppinglist.data.source.ApplicationDatabase;
import ca.on.conestogac.rsc.shoppinglist.databinding.FragmentShoppingListBinding;
import ca.on.conestogac.rsc.shoppinglist.databinding.RecyclerViewDataAdapter;
import ca.on.conestogac.rsc.shoppinglist.interfaces.ShoppingListener;
import ca.on.conestogac.rsc.shoppinglist.interfaces.ShoppingNavigator;
import ca.on.conestogac.rsc.shoppinglist.viewmodels.ShoppingListViewModel;
import ca.on.conestogac.rsc.shoppinglist.viewmodels.ShoppingViewModel;

public class ShoppingListFragment extends Fragment implements ShoppingListener {

    private final ShoppingNavigator shoppingNavigator;
    private FragmentShoppingListBinding binding;
    private RecyclerViewDataAdapter adapter;
    private ShoppingViewModel viewModel;

    public ShoppingListFragment(ShoppingNavigator shoppingNavigator) {
        this.shoppingNavigator = shoppingNavigator;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // title
        requireActivity().setTitle(R.string.app_name);

        // repository
        ApplicationDbRepository repository = new ApplicationDbRepository(ApplicationDatabase.getInstance(getContext()));

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_shopping_list,
                container,
                false);

        // view model & binding
        viewModel = new ShoppingViewModel(repository);
        binding.setViewModel(viewModel);

        // recycler view adapter
        adapter = new RecyclerViewDataAdapter(viewModel.getData(), R.layout.shopping_list_row);
        binding.rvShoppingLists.setAdapter(adapter);

        // init RecyclerView
        RecyclerView recyclerView = binding.rvShoppingLists;
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
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

        // drag and drop
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

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
    public void onShoppingListInserted(int position) {
        adapter.notifyItemInserted(position);
    }

    @Override
    public void onShoppingListItemMoved(int fromPosition, int toPosition) {
        adapter.notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onShoppingListRemoved(int position) {
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void onShoppingListActivityChange(ShoppingListViewModel shoppingList) {
        shoppingNavigator.onStartProductList(shoppingList);
    }


}