package ca.on.conestogac.rsc.shoppinglist.data.source;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ca.on.conestogac.rsc.shoppinglist.data.models.ShoppingList;
import ca.on.conestogac.rsc.shoppinglist.data.models.ShoppingListCounts;

public interface ShoppingListsDataSource {
    interface LoadShoppingListsCallback {
        void onShoppingListsLoaded(List<ShoppingListCounts> shoppingLists);
        void onDataNotAvailable();
    }

    interface LoadShoppingListCallback {
        void onShoppingListLoaded(ShoppingList shoppingList);
        void onShoppingListCountsLoaded(ShoppingListCounts shoppingList);
        void onDataNotAvailable();
    }

    // create
    void saveShoppingList(@NotNull ShoppingList shoppingList);

    // read
    void getShoppingLists(@NotNull final LoadShoppingListsCallback callback);
    void getShoppingList(@NotNull final String shoppingListId, @NotNull final LoadShoppingListCallback callback);
    void getShoppingListWithCounts(@NotNull final String shoppingListId, @NotNull final LoadShoppingListCallback callback);
    void updateShoppingListTitle(@NonNull final String shoppingListId, @NonNull final String title);

    // update
    void updateShoppingListSortIndex(@NonNull String shoppingListId, int sortIndex);

    // delete
    void deleteShoppingList(@NonNull String shoppingListId);
}
