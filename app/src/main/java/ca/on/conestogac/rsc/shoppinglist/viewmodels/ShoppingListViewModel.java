package ca.on.conestogac.rsc.shoppinglist.viewmodels;

import androidx.databinding.Bindable;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import ca.on.conestogac.rsc.shoppinglist.BR;
import ca.on.conestogac.rsc.shoppinglist.databinding.ObservableViewModel;
import ca.on.conestogac.rsc.shoppinglist.interfaces.ProductListener;
import ca.on.conestogac.rsc.shoppinglist.models.ShoppingList;
import ca.on.conestogac.rsc.shoppinglist.models.Product;

public class ShoppingListViewModel extends ObservableViewModel {
    private String title;
    private final ShoppingViewModel parent;
    private final List<ProductViewModel> data;

    private ProductListener productListener;

    private String textProductTitle;

    public ShoppingListViewModel(ShoppingList shoppingList, ShoppingViewModel parent) {
        // set model values
        this.title = shoppingList.getTitle();

        this.parent = parent;
        this.data = new ArrayList<>();
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
    public int getCount() {
        int completed = 0;
        for (ProductViewModel product: data) {
            if (product.isChecked()) {
                completed++;
            }
        }
        return completed;
    }

    @Bindable
    public int getSize() {
        return data.size();
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    @Bindable
    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
        // TODO : Update DB instance
    }

    @Bindable
    public List<? extends ViewModel> getData() {
        return data;
    }

    public void onViewCreated(ProductListener productListener) {
        this.productListener = productListener;
    }

    public void onViewDestroyed() {
        productListener = null;
    }

    public void onClicked() {
        parent.onShoppingListClicked(this);
    }

    public void onAddProductClicked() {
        if (textProductTitle != null && !textProductTitle.equals("")) {
            addProduct(new Product(textProductTitle));
            setTextProductTitle("");
        }
    }

    public void onRemoveClicked() {
        // TODO : remove DB instance
        // TODO : binding for a confirmation dialog would be great
        parent.onShoppingListRemoved(this);
    }

    private void addProduct(Product product) {
        productListener.onProductInserted(data.size());
        data.add(new ProductViewModel(product));
    }

    private void populateData() {
        // TODO: Do an asynchronous operation to fetch users.
    }
}
