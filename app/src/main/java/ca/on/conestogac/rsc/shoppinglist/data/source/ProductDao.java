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
     * Select all products from the product table with shoppingListId.
     *
     * @return all products.
     */
    @Query("SELECT * FROM product WHERE shoppinglistid = :shoppingListId ORDER BY sortIndex")
    List<Product> getProductsByShoppingListId(String shoppingListId);

    /**
     * Select count of products by shoppingListId.
     *
     * @return count of products.
     */
    @Query("SELECT COUNT(shoppinglistid) FROM product WHERE shoppinglistid = :shoppingListId ORDER BY sortIndex")
    int getCountByShoppingListId(String shoppingListId);

    /**
     * Update the checked status of a product
     *
     * @param productId    id of the product
     * @param checked      checked to be updated
     */

    @Query("UPDATE product SET checked = :checked WHERE productId = :productId")
    void updateChecked(String productId, boolean checked);

    /**
     * Update the title of a product
     *
     * @param productId    id of the product
     * @param title      title to be updated
     */
    @Query("UPDATE product SET title = :title WHERE productId = :productId")
    void updateTitle(String productId, String title);

    /**
     * Delete a product by id.
     *
     * @return the number of products deleted. This should always be 1.
     */
    @Query("DELETE FROM product WHERE productId = :productid")
    int deleteProductById(String productid);

    @Query("SELECT COUNT(*) FROM product")
     int getProductCount();

}
