package ca.on.conestogac.rsc.shoppinglist.interfaces;

import ca.on.conestogac.rsc.shoppinglist.models.ShoppingList;

public interface ShoppingListener {
    void onShoppingListActivityChange(ShoppingList shoppingList);
}
