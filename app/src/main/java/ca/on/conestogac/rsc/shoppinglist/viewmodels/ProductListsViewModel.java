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

    public void start(String shoppingListId) {
        this.shoppingListId = shoppingListId;
        db.shoppingLists().getShoppingList(shoppingListId, this);
        populateData();
    }

    public void onViewCreated(ProductListener productListener) {
        this.productListener = productListener;
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

    @Bindable
    public boolean isEmpty() {
        return products.size() == 0;
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

    public void onAddProductClicked() {
        if (!TextUtils.isEmpty(textTitleNewProduct)) {
            Product product = new Product(shoppingListId, textTitleNewProduct, products.size());

            // insert int DB
            db.products().saveProduct(product);

            // update fields
            addProduct(product);
            setTextTitleNewProduct("");
        } else {
            productListener.onSnackBarDisplay(context.getString(R.string.name_required));
        }
    }

    private void addProduct(Product product) {
        ProductItemViewModel itemViewModel = new ProductItemViewModel(context, db);
        itemViewModel.bindModel(product);
        itemViewModel.start(this);

        productListener.onProductInserted(products.size());
        products.add(itemViewModel);

        notifyPropertyChanged(BR.empty);
        notifyPropertyChanged(BR.noProductsLabel);
        notifyPropertyChanged(BR.noProductsIcon);
    }

    private void addProductRange(List<Product> products) {
        int fromPosition = this.products.size();
        for (Product product: products) {
            addProduct(product);
        }
        this.productListener.onProductRangeInserted(fromPosition, this.products.size());
    }

    private void populateData() {
        // get products from DB
        db.products().getProductsByShoppingListId(shoppingListId, this);
    }

    @Override
    public void onShoppingListLoaded(ShoppingList shoppingList) {
        modelObservable.set(shoppingList);
    }

    @Override
    public void onProductsLoaded(List<Product> products) {
        productListener.onProductRangeRemoved(0, this.products.size());
        this.products.clear();

        addProductRange(products);
    }

    @Override
    public void onDataNotAvailable() {
        if (modelObservable.get() != null) {
            productListener.onProductRangeRemoved(0, products.size());
            products.clear();
        }
        modelObservable.set(null);

        notifyPropertyChanged(BR.empty);
        notifyPropertyChanged(BR.noProductsLabel);
        notifyPropertyChanged(BR.noProductsIcon);
    }

    @Override
    public void onItemRemove(@NotNull ProductItemViewModel item) {
        //productListener.onSnackBarDisplay(context.getString(R.string.product_deleted));
    }

    @Override
    public void onItemClick(@NotNull ProductItemViewModel item) {

    }
}
