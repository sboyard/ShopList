package ca.on.conestogac.rsc.shoppinglist.viewmodels;

import androidx.databinding.Bindable;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import ca.on.conestogac.rsc.shoppinglist.BR;
import ca.on.conestogac.rsc.shoppinglist.R;
import ca.on.conestogac.rsc.shoppinglist.databinding.ObservableViewModel;
import ca.on.conestogac.rsc.shoppinglist.databinding.RecyclerViewDataAdapter;
import ca.on.conestogac.rsc.shoppinglist.interfaces.ShoppingListener;
import ca.on.conestogac.rsc.shoppinglist.models.ShoppingList;

public class ShoppingViewModel extends ObservableViewModel {
    private List<ShoppingListViewModel> data;
    private RecyclerViewDataAdapter adapter;
    private ShoppingListener shoppingListener;
    private String textShoppingListTitle;

    public void setShoppingListener(ShoppingListener shoppingListener) {
        this.shoppingListener = shoppingListener;
    }

    @Bindable
    public String getTextShoppingListTitle() {
        return textShoppingListTitle;
    }

    @Bindable
    public void setTextShoppingListTitle(String newList) {
        this.textShoppingListTitle = newList;
        notifyPropertyChanged(BR.textShoppingListTitle);
    }

    @Bindable
    public List<? extends ViewModel> getData() {
        if (data == null) {
            data = new ArrayList<>();
            populateData();
        }
        return data;
    }

    @Bindable
    public RecyclerViewDataAdapter getAdapter() {
        if (adapter == null) {
            adapter = new RecyclerViewDataAdapter(R.layout.shopping_list_row);
        }
        return adapter;
    }

    public ShoppingListener getShoppingListener() {
        return shoppingListener;
    }

    public void onAddShoppingListClicked() {
        if (textShoppingListTitle != null && !textShoppingListTitle.equals("")) {
            addShoppingList(new ShoppingList(textShoppingListTitle));
            setTextShoppingListTitle("");
        }
    }

    private void addShoppingList(ShoppingList shoppingList) {
        data.add(new ShoppingListViewModel(shoppingList, this));
        //adapter.notifyDataSetChanged();
    }

    private void populateData() {
        // TODO: Do an asynchronous operation to fetch users.
    }
}
