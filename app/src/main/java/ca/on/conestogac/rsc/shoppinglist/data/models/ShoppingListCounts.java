package ca.on.conestogac.rsc.shoppinglist.data.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Entity
public class ShoppingListCounts extends ShoppingList {

    @ColumnInfo(name = "checkedCount")
    private final int checkedCount;

    @ColumnInfo(name = "totalCount")
    private final int totalCount;

    public ShoppingListCounts(@NotNull String Id, @NotNull String title, int sortIndex, int checkedCount, int totalCount) {
        super(Id, title, sortIndex);
        this.checkedCount = checkedCount;
        this.totalCount = totalCount;
    }

    @Ignore
    public ShoppingListCounts(String title, int sortIndex, int checkedCount, int totalCount) {
        this(UUID.randomUUID().toString(), title, sortIndex, checkedCount, totalCount);
    }

    public int getCheckedCount() {
        return checkedCount;
    }

    public int getTotalCount() {
        return totalCount;
    }
}
