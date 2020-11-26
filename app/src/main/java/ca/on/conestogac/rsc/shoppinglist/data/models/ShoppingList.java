package ca.on.conestogac.rsc.shoppinglist.data.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

@Entity(tableName = "shoppinglist")
public class ShoppingList {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "shoppinglistid")
    private final String Id;

    @NonNull
    @ColumnInfo(name = "title")
    private final String title;

    @ColumnInfo(name = "sortindex")
    private final int sortIndex;

    public ShoppingList(@NotNull String Id, @NotNull String title, int sortIndex) {
        this.Id = Id;
        this.title = title;
        this.sortIndex = sortIndex;
    }

    @Ignore
    public ShoppingList(String title, int sortIndex) {
        this(UUID.randomUUID().toString(), title, sortIndex);
    }

    @NonNull
    public String getId() {
        return Id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public int getSortIndex() {
        return sortIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingList shoppingList = (ShoppingList) o;
        return Objects.equals(Id, shoppingList.Id) &&
                Objects.equals(title, shoppingList.title) &&
                Objects.equals(sortIndex, shoppingList.sortIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, title, sortIndex);
    }

    @Override
    public String toString() {
        return "ShoppingList with title " + title;
    }
}
