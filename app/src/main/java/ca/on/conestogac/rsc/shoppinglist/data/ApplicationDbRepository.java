package ca.on.conestogac.rsc.shoppinglist.data;

import ca.on.conestogac.rsc.shoppinglist.data.source.ApplicationDatabase;
import ca.on.conestogac.rsc.shoppinglist.data.source.ProductRepository;
import ca.on.conestogac.rsc.shoppinglist.data.source.ShoppingListRepository;

public final class ApplicationDbRepository {
    private ShoppingListRepository shoppingListRepository;
    private ProductRepository productRepository;

    public ApplicationDbRepository(ApplicationDatabase db) {
        shoppingListRepository = new ShoppingListRepository(db);
        productRepository = new ProductRepository(db);
    }

    public ShoppingListRepository shoppingLists() {
        return shoppingListRepository;
    }

    public ProductRepository products() {
        return productRepository;
    }
}
