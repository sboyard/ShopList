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
import ca.on.conestogac.rsc.shoppinglist.models.Product;

public class ShoppingListViewModel extends ObservableViewModel {
    private final ShoppingList model;
    private final ShoppingViewModel parent;

    private List<ProductViewModel> data;
    private RecyclerViewDataAdapter adapter;

    private String textProductTitle;

    public ShoppingListViewModel(ShoppingList shoppingList, ShoppingViewModel parent) {
        this.model = shoppingList;
        this.parent = parent;
    }

    @Bindable
    public String getTextProductTitle() {
        return textProductTitle;
    }

    @Bindable
    public void setTextProductTitle(String newList) {
        this.textProductTitle = newList;
        notifyPropertyChanged(BR.textProductTitle);
    }

    @Bindable
    public ShoppingList getModel() {
        return model;
    }

    @Bindable
    public int getCount() {
        return 2;
    } // TODO: Add this functionality

    @Bindable
    public int getSize() {
        return 3;
    } // TODO: Add this functionality

    @Bindable
    public String getTitle() {
        return model.getTitle();
    }

    @Bindable
    public void setTitle(String title) {
        this.model.setTitle(title);
        notifyPropertyChanged(BR.title);
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
            adapter = new RecyclerViewDataAdapter(R.layout.product_row);
        }
        return adapter;
    }

    public void onClicked() {
        ShoppingListener listener = parent.getShoppingListener();
        if (listener != null) {
            listener.onShoppingListActivityChange(this.model);
        }
    }

    public void onAddProductClicked() {
        if (textProductTitle != null && !textProductTitle.equals("")) {
            addProduct(new Product(textProductTitle));
            setTextProductTitle("");
        }
    }

    private void addProduct(Product product) {
        data.add(new ProductViewModel(product));
    }

    private void populateData() {
        // TODO: Do an asynchronous operation to fetch users.
    }
}
