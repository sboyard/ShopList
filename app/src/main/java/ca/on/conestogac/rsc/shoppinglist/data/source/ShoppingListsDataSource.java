package ca.on.conestogac.rsc.shoppinglist.data.source;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ca.on.conestogac.rsc.shoppinglist.data.models.ShoppingList;

public interface ShoppingListsDataSource {
    interface LoadShoppingListsCallback {
        void onShoppingListsLoaded(List<ShoppingList> shoppingLists);
    }

    // create
    void saveShoppingList(@NotNull ShoppingList shoppingList);

    // read
    void getShoppingLists(@NotNull final LoadShoppingListsCallback callback);

    // update
    void updateShoppingListSortIndex(@NonNull String shoppingListId, int sortIndex);

    // delete
    void deleteShoppingList(@NonNull String shoppingListId);
}
