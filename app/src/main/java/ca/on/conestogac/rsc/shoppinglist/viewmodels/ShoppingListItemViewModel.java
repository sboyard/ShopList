package ca.on.conestogac.rsc.shoppinglist.viewmodels;

import android.content.Context;

import androidx.databinding.Bindable;
import androidx.databinding.Observable;
import androidx.databinding.ObservableField;

import java.lang.ref.WeakReference;

import ca.on.conestogac.rsc.shoppinglist.BR;
import ca.on.conestogac.rsc.shoppinglist.R;
import ca.on.conestogac.rsc.shoppinglist.data.ApplicationDbRepository;
import ca.on.conestogac.rsc.shoppinglist.data.models.ShoppingList;
import ca.on.conestogac.rsc.shoppinglist.data.models.ShoppingListCounts;
import ca.on.conestogac.rsc.shoppinglist.data.source.ShoppingListsDataSource;
import ca.on.conestogac.rsc.shoppinglist.databinding.ObservableViewModel;
import ca.on.conestogac.rsc.shoppinglist.interfaces.ItemListener;

public class ShoppingListItemViewModel extends ObservableViewModel
        implements ShoppingListsDataSource.LoadShoppingListCallback {
    private final ObservableField<ShoppingListCounts> modelObservable = new ObservableField<>();
    private final ApplicationDbRepository db;
    private final Context context;

    private WeakReference<ItemListener<ShoppingListItemViewModel>> itemListener;

    // model values
    private String shoppingListId;
    private String title;
    private String editingTitle;
    private int totalCount;
    private int checkedCount;

    private boolean editing;

    public ShoppingListItemViewModel(Context context, ApplicationDbRepository db) {
        this.context = context;
        this.db = db;

        modelObservable.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                ShoppingListCounts shoppingList = modelObservable.get();
                if (shoppingList != null) {
                    shoppingListId = shoppingList.getId();
                    title = shoppingList.getTitle();
                    editingTitle = title;
                    checkedCount = shoppingList.getCheckedCount();
                    totalCount = shoppingList.getTotalCount();
                    notifyPropertyChanged(BR.icon);
                    notifyPropertyChanged(BR.title);
                    notifyPropertyChanged(BR.description);
                }
            }
        });
    }

    public void start(ItemListener<ShoppingListItemViewModel> itemListener) {
        this.itemListener = new WeakReference<>(itemListener);
    }

    public void bindModel(ShoppingListCounts shoppingList) {
        this.modelObservable.set(shoppingList);
    }

    public String getShoppingListId() {
        return shoppingListId;
    }

    @Bindable
    public int getCheckedCount() {
        return checkedCount;
    }

    @Bindable
    public int getTotalCount() {
        return totalCount;
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    @Bindable
    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getEditingTitle() {
        return editingTitle;
    }

    @Bindable
    public void setEditingTitle(String title) {
        this.editingTitle = title;
        notifyPropertyChanged(BR.editingTitle);
        notifyPropertyChanged(BR.validEditingTitle);
    }

    @Bindable
    public boolean isEditing() {
        return this.editing;
    }

    @Bindable
    public void setEditing(boolean editing) {
        this.editing = editing;
        notifyPropertyChanged(BR.editing);
    }

    @Bindable
    public boolean isValidEditingTitle() {
        return title.length() > 0;
    }

    @Bindable
    public int getIcon() {
        if (totalCount > 0 && checkedCount == totalCount) {
            return R.drawable.ic_check;
        }
        return R.drawable.ic_shopping_cart;
    }

    @Bindable
    public int getDescription() {
        if (totalCount == 0) {
            return R.string.empty;
        } else if (checkedCount == totalCount) {
            return R.string.done;
        }
        return 0;
    }

    public void onClicked() {
        if (!isEditing() && itemListener.get() != null) {
            itemListener.get().onItemClick(this);
        }
    }

    public void onEditClicked() {
        setEditing(true);

        if (itemListener.get() != null) {
            itemListener.get().onStartEditingItem(this);
        }
    }

    public void onRemoveClicked() {
        // TODO : binding for a confirmation dialog would be great / or undo snack bar
        if (itemListener.get() != null) {
            itemListener.get().onItemRemove(this);
        }
    }

    public void onSaveClicked() {
        if (title.length() > 0) {
            setEditing(false);
            setTitle(editingTitle);

            db.shoppingLists().updateShoppingListTitle(shoppingListId, title);
            db.shoppingLists().getShoppingList(shoppingListId, this);

            if (itemListener.get() != null) {
                itemListener.get().onStopEditingItem();
            }
        } else {
            onUndoClicked();
        }
    }

    public void onUndoClicked() {
        if (editing) {
            setEditing(false);
            setEditingTitle(title);

            if (itemListener.get() != null) {
                itemListener.get().onStopEditingItem();
            }
        }
    }

    public int getSortIndex() {
        if (modelObservable.get() != null) {
            return modelObservable.get().getSortIndex();
        }
        return 0;
    }

    @Override
    public void onShoppingListCountReturned(int count) {

    }

    @Override
    public void onShoppingListLoaded(ShoppingList shoppingList) {

    }

    @Override
    public void onShoppingListCountsLoaded(ShoppingListCounts shoppingList) {
        bindModel(shoppingList);
    }

    @Override
    public void onDataNotAvailable() {

    }
}
