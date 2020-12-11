package ca.on.conestogac.rsc.shoppinglist.data.source;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ca.on.conestogac.rsc.shoppinglist.data.models.Product;
import ca.on.conestogac.rsc.shoppinglist.data.models.ShoppingList;

public interface ProductDataSource {
    interface LoadProductsCallback {
        void onProductsLoaded(List<Product> products);
        void onDataNotAvailable();
    }

    interface LoadProductCallback {
        void onProductCountReturned(int count);
    }

    // create
    void saveProduct(@NotNull Product product);

    // read
    void getProductsByShoppingListId(@NonNull String shoppingListId, @NotNull final LoadProductsCallback callback);

    // update
    void updateProductChecked(@NotNull String productId, boolean checked);
    void updateProductTitle(@NotNull String productId, @NotNull String title);
    void getProductsCount(@NotNull final ProductDataSource.LoadProductCallback callback);

    // delete
    void deleteProduct(@NonNull String productId);
}
