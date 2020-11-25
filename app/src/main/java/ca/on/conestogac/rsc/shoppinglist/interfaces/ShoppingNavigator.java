package ca.on.conestogac.rsc.shoppinglist.interfaces;

import ca.on.conestogac.rsc.shoppinglist.viewmodels.ShoppingListViewModel;

public interface ShoppingNavigator {
    void onEnableActionBarUp(boolean enabled);
    void onStartShoppingList();
    void onStartProductList(ShoppingListViewModel shoppingListViewModel);
}
