package ca.on.conestogac.rsc.shoppinglist.viewmodels;

import android.content.Context;
import android.text.TextUtils;

import androidx.databinding.Bindable;
import androidx.databinding.Observable;
import androidx.databinding.ObservableField;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import ca.on.conestogac.rsc.shoppinglist.BR;
import ca.on.conestogac.rsc.shoppinglist.R;
import ca.on.conestogac.rsc.shoppinglist.data.ApplicationDbRepository;
import ca.on.conestogac.rsc.shoppinglist.data.models.Product;
import ca.on.conestogac.rsc.shoppinglist.data.models.ShoppingList;
import ca.on.conestogac.rsc.shoppinglist.data.models.ShoppingListCounts;
import ca.on.conestogac.rsc.shoppinglist.data.source.ShoppingListsDataSource.LoadShoppingListCallback;
import ca.on.conestogac.rsc.shoppinglist.data.source.ProductDataSource.LoadProductsCallback;
import ca.on.conestogac.rsc.shoppinglist.databinding.ObservableViewModel;
import ca.on.conestogac.rsc.shoppinglist.interfaces.ItemListener;
import ca.on.conestogac.rsc.shoppinglist.interfaces.ProductListener;

public class ProductListsViewModel extends ObservableViewModel
        implements LoadProductsCallback, LoadShoppingListCallback, ItemListener<ProductItemViewModel> {
    private final ObservableField<ShoppingList> modelObservable = new ObservableField<>();
    private final List<ProductItemViewModel> products = new ArrayList<>();
    private final Context context;
    private final ApplicationDbRepository db;
    private ProductListener productListener;

    private String shoppingListId;
    private String textTitleNewProduct;
    private boolean empty;
    public final ObservableField<String> shoppingListTitle = new ObservableField<>();

    public ProductListsViewModel(Context context, ApplicationDbRepository db) {
        this.context = context;
        this.db = db;

        modelObservable.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                ShoppingList shoppingList = modelObservable.get();
                if (shoppingList != null) {
                    shoppingListId = shoppingList.getId();
                    shoppingListTitle.set(shoppingList.getTitle());
                }
            }
        });
    }

    public void onViewCreated(String shoppingListId, ProductListener productListener) {
        this.productListener = productListener;

        // get shoppingList & products from DB
        db.shoppingLists().getShoppingList(shoppingListId, this);
        db.products().getProductsByShoppingListId(shoppingListId, this);
    }

    public void onViewDestroyed() {
        productListener = null;
    }

    @Bindable
    public List<ProductItemViewModel> getProducts() {
        return products;
    }

    @Bindable
    public String getTextTitleNewProduct() {
        return textTitleNewProduct;
    }

    @Bindable
    public void setTextTitleNewProduct(String title) {
        textTitleNewProduct = title;
        notifyPropertyChanged(BR.textTitleNewProduct);
    }

    private void updateIsEmpty() {
        empty = products.size() == 0;
        notifyPropertyChanged(BR.empty);
    }

    @Bindable
    public boolean isEmpty() {
        return empty;
    }

    @Bindable
    public int getNoProductsIcon() {
        // TODO: change this drawable resource after filter modes are added
        return R.drawable.ic_clipboard_pencil;
    }

    @Bindable
    public int getNoProductsLabel() {
        // TODO: change this strings resource after filter modes are added
        return R.string.no_products_message;
    }

    public void onFabClick() {
        textTitleNewProduct = "New";
        Product product = new Product(shoppingListId, textTitleNewProduct, products.size());

        // insert into DB
        db.products().saveProduct(product);

        // update fields
        addProduct(createProductItem(product));
        setTextTitleNewProduct("");
    }

    public void onAddProductClicked() {
        if (!TextUtils.isEmpty(textTitleNewProduct)) {
            Product product = new Product(shoppingListId, textTitleNewProduct, products.size());

            // insert int DB
            db.products().saveProduct(product);

            // update fields
            addProduct(createProductItem(product));
            setTextTitleNewProduct("");
        } else {
            productListener.onSnackBarDisplay(context.getString(R.string.name_required));
        }
    }

    private void notifyProductsUpdated() {
        notifyPropertyChanged(BR.empty);
        notifyPropertyChanged(BR.noProductsLabel);
        notifyPropertyChanged(BR.noProductsIcon);
        updateIsEmpty();
    }

    private ProductItemViewModel createProductItem(Product product) {
        ProductItemViewModel productItem = new ProductItemViewModel(context, db);
        productItem.bindModel(product);
        productItem.start(this);

        return productItem;
    }

    private void addProduct(ProductItemViewModel productItem) {
        // add
        products.add(productItem);

        // notify UI
        productListener.onProductInserted(products.size());
        notifyProductsUpdated();
    }

    private void addProductRange(List<Product> products) {
        // add
        int fromPosition = this.products.size();
        for (Product product: products) {
            this.products.add(createProductItem(product));
        }

        // notify UI
        this.productListener.onProductRangeInserted(fromPosition, this.products.size());
        notifyProductsUpdated();
    }

    @Override
    public void onShoppingListLoaded(ShoppingList shoppingList) {
        modelObservable.set(shoppingList);
    }

    @Override
    public void onShoppingListCountsLoaded(ShoppingListCounts shoppingList) {

    }

    @Override
    public void onProductsLoaded(List<Product> products) {
        int toPosition = this.products.size();
        if (toPosition > 0) {
            // remove
            this.products.clear();

            // notify UI
            productListener.onProductRangeRemoved(0, toPosition);
        }
        // add
        addProductRange(products);
    }

    @Override
    public void onDataNotAvailable() {
        if (modelObservable.get() != null) {
            productListener.onProductRangeRemoved(0, products.size());
            products.clear();
        }
        modelObservable.set(null);
        notifyProductsUpdated();
    }

    @Override
    public void onItemRemove(@NotNull ProductItemViewModel product) {
        // remove
        int index = products.indexOf(product);
        products.remove(index);

        // notify UI
        productListener.onProductRemoved(index);
        productListener.onSnackBarDisplay(context.getString(R.string.product_deleted));
        notifyProductsUpdated();

        // delete from DB
        db.products().deleteProduct(product.getProductId());
    }

    @Override
    public void onItemClick(@NotNull ProductItemViewModel item) {

    }

    @Override
    public void onStartEditingItem(ProductItemViewModel item) {

    }

    @Override
    public void onStopEditingItem() {

    }
}
