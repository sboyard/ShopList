package ca.on.conestogac.rsc.shoppinglist.data.source;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ca.on.conestogac.rsc.shoppinglist.data.models.ShoppingList;
import ca.on.conestogac.rsc.shoppinglist.data.models.ShoppingListCounts;

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
    @Query("SELECT *, " +
            "(SELECT COUNT(shoppinglistid) FROM product p WHERE p.shoppinglistid=s.shoppinglistid AND checked=1) checkedCount, " +
            "(SELECT COUNT(shoppinglistid) FROM product p WHERE p.shoppinglistid=s.shoppinglistid) totalCount " +
            "FROM shoppinglist s ORDER BY sortIndex")
    List<ShoppingListCounts> getShoppingLists();

    /**
     * Select a shoppingList from the shoppingList table by id.
     *
     * @param shoppingListId the shoppingList id.
     * @return a shoppingList.
     */
    @Query("SELECT * FROM shoppinglist WHERE shoppinglistid = :shoppingListId")
    ShoppingList getShoppingListById(String shoppingListId);

    /**
     * Select a shoppingList from the shoppingList table by id with counts.
     *
     * @param shoppingListId the shoppingList id.
     * @return a shoppingList.
     */
    @Query("SELECT *, " +
            "(SELECT COUNT(shoppinglistid) FROM product p WHERE p.shoppinglistid=s.shoppinglistid AND checked=1) checkedCount, " +
            "(SELECT COUNT(shoppinglistid) FROM product p WHERE p.shoppinglistid=s.shoppinglistid) totalCount " +
            "FROM shoppinglist s WHERE shoppinglistid = :shoppingListId")
    ShoppingListCounts getShoppingListCountsById(String shoppingListId);

    /**
     * Update a shoppingList.
     *
     * @param shoppingListId   id of the shoppingList
     * @param sortIndex        index to be updated
     */
    @Query("UPDATE shoppinglist SET sortIndex = :sortIndex WHERE shoppinglistid = :shoppingListId")
    void updateShoppingListIndex(String shoppingListId, int sortIndex);

    /**
     * Update the title of a shoppingList
     *
     * @param shoppingListId    id of the shoppingList
     * @param title             title to be updated
     */
    @Query("UPDATE shoppinglist SET title = :title WHERE shoppingListId = :shoppingListId")
    void updateShoppingListTitle(String shoppingListId, String title);

    /**
     * Delete a shoppingList by id.
     *
     * @return the number of shoppingLists deleted. This should always be 1.
     */
    @Query("DELETE FROM shoppinglist WHERE shoppinglistid = :shoppingListId")
    int deleteShoppingListById(String shoppingListId);
}
