package ca.on.conestogac.rsc.shoppinglist.data.source;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ca.on.conestogac.rsc.shoppinglist.data.models.Product;
import ca.on.conestogac.rsc.shoppinglist.data.models.ShoppingList;

@Database(entities = {ShoppingList.class, Product.class}, version = 1, exportSchema = false)
public abstract class ApplicationDatabase extends RoomDatabase {

    private static ApplicationDatabase INSTANCE;

    public abstract ShoppingListsDao shoppingListsDao();
    public abstract ProductDao productsDao();

    private static final Object sLock = new Object();

    public static ApplicationDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        ApplicationDatabase.class, "ShoppingList.db")
                        .build();
            }
            return INSTANCE;
        }
    }
}