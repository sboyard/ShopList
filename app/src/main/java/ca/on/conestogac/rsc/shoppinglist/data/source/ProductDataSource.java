package ca.on.conestogac.rsc.shoppinglist.data.source;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ca.on.conestogac.rsc.shoppinglist.data.models.Product;

public interface ProductDataSource {
    interface LoadProductsCallback {
        void onProductsLoaded(List<Product> products);
    }

    // create
    void saveProduct(@NotNull Product product);

    // read
    void getProductsByShoppingListId(@NonNull String shoppingListId, @NotNull final LoadProductsCallback callback);

    // update
    void updateProductChecked(@NotNull String productId, boolean checked);
}
