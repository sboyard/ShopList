package ca.on.conestogac.rsc.shoppinglist.interfaces;

import ca.on.conestogac.rsc.shoppinglist.viewmodels.ShoppingListViewModel;

public interface ShoppingListener {
    void onShoppingListInserted(int position);
    void onShoppingListRemoved(int position);
    void onShoppingListActivityChange(ShoppingListViewModel shoppingList);
}
