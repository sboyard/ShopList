package ca.on.conestogac.rsc.shoppinglist.data.source;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ca.on.conestogac.rsc.shoppinglist.data.models.ShoppingList;

@Dao
public interface ShoppingListsDao {

    /**
     * Insert a shoppingList in the database. If the shoppingList already exists, replace it.
     *
     * @param shoppingList the shoppingList to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertShoppingList(ShoppingList shoppingList);

    /**
     * Select all shoppingLists from the shoppingList table.
     *
     * @return all shoppingLists.
     */
    @Query("SELECT * FROM shoppinglist ORDER BY sortIndex")
    List<ShoppingList> getShoppingLists();

    /**
     * Update a shoppingList.
     *
     * @param shoppingListId   id of the shoppingList
     * @param sortIndex        index to be updated
     */
    @Query("UPDATE shoppinglist SET sortIndex = :sortIndex WHERE shoppinglistid = :shoppingListId")
    void updateShoppingListIndex(String shoppingListId, int sortIndex);

    /**
     * Delete a shoppingList by id.
     *
     * @return the number of shoppingLists deleted. This should always be 1.
     */
    @Query("DELETE FROM shoppinglist WHERE shoppinglistid = :shoppingListId")
    int deleteShoppingListById(String shoppingListId);
}
