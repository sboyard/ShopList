package ca.on.conestogac.rsc.shoppinglist.data.source;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ca.on.conestogac.rsc.shoppinglist.data.models.Product;

public class ProductRepository implements ProductDataSource {

    protected final ApplicationDatabase db;
    protected final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    public ProductRepository(ApplicationDatabase db) {
        this.db = db;
    }

    @Override
    public void saveProduct(@NotNull Product product) {
        Runnable runnable = () -> {
            db.productsDao().insertProduct(product);
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Override
    public void getProductsByShoppingListId(@NotNull String shoppingListId, @NotNull final LoadProductsCallback callback) {
        Runnable runnable = () -> {
            final List<Product> products = db.productsDao().getProductsByShoppingListId(shoppingListId);

            mainThreadHandler.post(() -> {
                callback.onProductsLoaded(products);
            });
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Override
    public void updateProductChecked(@NotNull String productId, boolean checked) {
        Runnable runnable = () -> {
            db.productsDao().updateChecked(productId, checked);
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Override
    public void updateProductTitle(@NotNull String productId, @NotNull String title) {
        Runnable runnable = () -> {
            db.productsDao().updateTitle(productId, title);
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Override
    public void deleteProduct(@NonNull String productId) {
        Runnable runnable = () -> {
            db.productsDao().deleteProductById(productId);
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
