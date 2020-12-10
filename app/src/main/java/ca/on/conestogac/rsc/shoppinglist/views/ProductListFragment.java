package ca.on.conestogac.rsc.shoppinglist.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.fragment.app.Fragment;

import ca.on.conestogac.rsc.shoppinglist.App;
import ca.on.conestogac.rsc.shoppinglist.R;
import ca.on.conestogac.rsc.shoppinglist.databinding.FragmentProductListBinding;
import ca.on.conestogac.rsc.shoppinglist.interfaces.ProductListener;
import ca.on.conestogac.rsc.shoppinglist.viewmodels.ProductListsViewModel;

public class ProductListFragment extends Fragment implements ProductListener {
    private FragmentProductListBinding binding;
    private ProductListsViewModel viewModel = null;
    private String shoppingListId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_product_list,
                container,
                false);

        App app = (App)(requireActivity().getApplication());
        requireActivity().setTitle("");

        shoppingListId = requireArguments().getString("shoppingListId");

        // viewModel
        viewModel = new ProductListsViewModel(requireContext(), app.getRepository());
        binding.setViewModel(viewModel);

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.onViewCreated(shoppingListId,this);

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
        binding.rvProductsList.notifyItemInserted(position, true);
    }

    @Override
    public void onProductRangeInserted(int fromPosition, int toPosition) {
        binding.rvProductsList.notifyItemRangeInserted(fromPosition, toPosition, true);
    }

    @Override
    public void onProductItemMoved(int fromPosition, int toPosition) {
        binding.rvProductsList.notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onProductRemoved(int position) {
        binding.rvProductsList.notifyItemRemoved(position);
    }

    @Override
    public void onProductRangeRemoved(int fromPosition, int toPosition) {
        binding.rvProductsList.notifyItemRangeRemoved(fromPosition, toPosition);
    }
}
