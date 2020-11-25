package ca.on.conestogac.rsc.shoppinglist.data.source;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ca.on.conestogac.rsc.shoppinglist.data.models.ShoppingList;

public class ShoppingListRepository implements ShoppingListsDataSource {

    protected final ApplicationDatabase db;
    protected final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    public ShoppingListRepository(ApplicationDatabase db) {
        this.db = db;
    }

    public void saveShoppingList(@NotNull ShoppingList shoppingList) {
        Runnable runnable = () -> {
            db.shoppingListsDao().insertShoppingList(shoppingList);
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void getShoppingLists(@NotNull final LoadShoppingListsCallback callback) {
        Runnable runnable = () -> {
            final List<ShoppingList> shoppingLists = db.shoppingListsDao().getShoppingLists();

            mainThreadHandler.post(() -> {
                callback.onShoppingListsLoaded(shoppingLists);
            });
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void updateShoppingListSortIndex(@NotNull String shoppingListId, int sortIndex) {
        Runnable runnable = () -> {
            db.shoppingListsDao().updateShoppingListIndex(shoppingListId, sortIndex);
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Override
    public void deleteShoppingList(@NonNull String shoppingListId) {
        Runnable runnable = () -> {
            db.shoppingListsDao().deleteShoppingListById(shoppingListId);
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
