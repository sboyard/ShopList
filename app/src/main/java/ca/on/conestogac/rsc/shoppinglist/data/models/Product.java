package ca.on.conestogac.rsc.shoppinglist.data.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

@Entity(tableName = "product",
        foreignKeys = @ForeignKey(entity = ShoppingList.class,
            parentColumns = "shoppinglistid",
            childColumns = "shoppinglistid",
            onDelete = ForeignKey.CASCADE))
public class Product {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "productId")
    private final String Id;

    @NonNull
    @ColumnInfo(name = "shoppinglistid")
    private final String shoppingListId;

    @ColumnInfo(name = "checked")
    private boolean checked;

    @NonNull
    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "sortindex")
    private final int sortIndex;

    public Product(@NotNull String Id, @NonNull String shoppingListId, @NotNull String title, boolean checked, int sortIndex) {
        this.Id = Id;
        this.shoppingListId = shoppingListId;
        this.title = title;
        this.checked = checked;
        this.sortIndex = sortIndex;
    }

    @Ignore
    public Product(@NotNull String Id, @NonNull String shoppingListId, @NotNull String title, int sortIndex) {
        this(Id, shoppingListId, title, false, sortIndex);
    }

    @Ignore
    public Product(@NonNull String shoppingListId, @NotNull String title, int sortIndex) {
        this(UUID.randomUUID().toString(), shoppingListId, title, false, sortIndex);
    }

    @NonNull
    public String getId() {
        return Id;
    }

    @NonNull
    public String getShoppingListId() {
        return shoppingListId;
    }

    public boolean isChecked() {
        return checked;
    }

    @NotNull
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
        Product product = (Product) o;
        return Objects.equals(Id, product.Id) &&
                Objects.equals(title, product.title) &&
                Objects.equals(checked, product.checked);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, title, checked);
    }

    @Override
    public String toString() {
        return "Product with title " + title;
    }
}
