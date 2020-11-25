package ca.on.conestogac.rsc.shoppinglist.viewmodels;

import androidx.databinding.Bindable;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import ca.on.conestogac.rsc.shoppinglist.BR;
import ca.on.conestogac.rsc.shoppinglist.data.ApplicationDbRepository;
import ca.on.conestogac.rsc.shoppinglist.databinding.ObservableViewModel;
import ca.on.conestogac.rsc.shoppinglist.interfaces.ProductListener;
import ca.on.conestogac.rsc.shoppinglist.data.models.ShoppingList;
import ca.on.conestogac.rsc.shoppinglist.data.models.Product;

public class ShoppingListViewModel extends ObservableViewModel {
    private final ShoppingViewModel parent;
    private final List<ProductViewModel> data;
    private final ApplicationDbRepository db;

    // ui
    private ProductListener productListener;
    private String textProductTitle;

    // model values
    private final String shoppingListId;
    private String title;

    public ShoppingListViewModel(ShoppingList shoppingList, ShoppingViewModel parent, ApplicationDbRepository db) {
        // set model values
        this.shoppingListId = shoppingList.getId();
        this.title = shoppingList.getTitle();

        this.parent = parent;
        this.db = db;
        this.data = new ArrayList<>();
    }

    public String getShoppingListId() {
        return shoppingListId;
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
        populateData();
    }

    public void onViewDestroyed() {
        productListener = null;
    }

    public void onClicked() {
        parent.onShoppingListClicked(this);
    }

    public void onAddProductClicked() {
        if (textProductTitle != null && !textProductTitle.equals("")) {
            Product product = new Product(shoppingListId, textProductTitle, data.size());

            // insert int DB
            db.products().saveProduct(product);

            // update fields
            addProduct(product);
            setTextProductTitle("");
        }
    }

    public void onRemoveClicked() {
        // TODO : binding for a confirmation dialog would be great / or undo snack bar
        parent.onShoppingListRemoved(this);
    }

    private void addProduct(Product product) {
        // add
        data.add(new ProductViewModel(product, db));

        // notify UI
        if (productListener != null) {
            productListener.onProductInserted(data.size());
        }

        notifyPropertyChanged(BR.size);
        notifyPropertyChanged(BR.count);
    }

    public void populateData() {
        if (data.size() > 0) {
            data.clear();
        }
        // read from DB
        db.products().getProductsByShoppingListId(shoppingListId, products -> {
            for (Product product: products) {
                // add to data as viewModel
                addProduct(product);
            }
        });
    }
}
