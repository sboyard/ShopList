package ca.on.conestogac.rsc.shoppinglist.data.source;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ca.on.conestogac.rsc.shoppinglist.data.models.Product;

@Dao
public interface ProductDao {

    /**
     * Insert a product in the database. If the product already exists, replace it.
     *
     * @param product the product to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProduct(Product product);

    /**
     * Select all products from the product table.
     *
     * @return all products.
     */
    @Query("SELECT * FROM product WHERE shoppinglistid = :shoppingListId ORDER BY sortIndex")
    List<Product> getProductsByShoppingListId(String shoppingListId);

    /**
     * Update the checked status of a product
     *
     * @param productId    id of the product
     * @param checked      checked to be updated
     */
    @Query("UPDATE product SET checked = :checked WHERE productId = :productId")
    void updateChecked(String productId, boolean checked);
}
