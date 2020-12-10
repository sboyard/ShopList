package ca.on.conestogac.rsc.shoppinglist.data.source;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ca.on.conestogac.rsc.shoppinglist.data.models.ShoppingList;
import ca.on.conestogac.rsc.shoppinglist.data.models.ShoppingListCounts;

public class ShoppingListRepository implements ShoppingListsDataSource {

    protected final ApplicationDatabase db;
    protected final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    public ShoppingListRepository(ApplicationDatabase db) {
        this.db = db;
    }

    @Override
    public void saveShoppingList(@NotNull ShoppingList shoppingList) {
        Runnable runnable = () -> {
            db.shoppingListsDao().insertShoppingList(shoppingList);
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Override
    public void getShoppingLists(@NotNull final LoadShoppingListsCallback callback) {
        Runnable runnable = () -> {
            final List<ShoppingListCounts> shoppingLists = db.shoppingListsDao().getShoppingLists();

            mainThreadHandler.post(() -> {
                if (shoppingLists.isEmpty()) { // || shoppingLists.size() == 1 && TextUtils.isEmpty(shoppingLists.get(0).getId())) {
                    callback.onDataNotAvailable();
                } else {
                    callback.onShoppingListsLoaded(shoppingLists);
                }
            });
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Override
    public void getShoppingList(@NotNull final String shoppingListId, @NotNull final LoadShoppingListCallback callback) {
        Runnable runnable = () -> {
            final ShoppingList shoppingList = db.shoppingListsDao().getShoppingListById(shoppingListId);

            mainThreadHandler.post(() -> {
                if (shoppingList == null) {
                    callback.onDataNotAvailable();
                } else {
                    callback.onShoppingListLoaded(shoppingList);
                }
            });
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Override
    public void getShoppingListWithCounts(@NotNull final String shoppingListId, @NotNull final LoadShoppingListCallback callback) {
        Runnable runnable = () -> {
            final ShoppingListCounts shoppingList = db.shoppingListsDao().getShoppingListCountsById(shoppingListId);

            mainThreadHandler.post(() -> {
                if (shoppingList == null) {
                    callback.onDataNotAvailable();
                } else {
                    callback.onShoppingListCountsLoaded(shoppingList);
                }
            });
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Override
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

    @Override
    public void updateShoppingListTitle(@NonNull final String shoppingListId, @NonNull final String title) {
        Runnable runnable = () -> {
            db.shoppingListsDao().updateShoppingListTitle(shoppingListId, title);
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
