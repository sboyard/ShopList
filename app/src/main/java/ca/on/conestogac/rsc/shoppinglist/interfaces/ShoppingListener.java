package ca.on.conestogac.rsc.shoppinglist.interfaces;

import ca.on.conestogac.rsc.shoppinglist.viewmodels.ShoppingListItemViewModel;

public interface ShoppingListener {
    void onSnackBarDisplay(String message);
    void onShoppingListInserted(int position);
    void onShoppingListRangeInserted(int fromPosition, int toPosition);
    void onShoppingListItemMoved(int fromPosition, int toPosition);
    void onShoppingListRemoved(int position);
    void onShoppingListProductRangeRemoved(int fromPosition, int toPosition);
    void onShoppingListActivityChange(ShoppingListItemViewModel shoppingList);
}
