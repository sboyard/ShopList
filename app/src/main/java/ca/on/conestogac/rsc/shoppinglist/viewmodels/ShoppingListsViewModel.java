package ca.on.conestogac.rsc.shoppinglist.viewmodels;

import android.content.Context;
import android.text.TextUtils;

import androidx.databinding.Bindable;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ca.on.conestogac.rsc.shoppinglist.BR;
import ca.on.conestogac.rsc.shoppinglist.R;
import ca.on.conestogac.rsc.shoppinglist.data.ApplicationDbRepository;
import ca.on.conestogac.rsc.shoppinglist.data.models.ShoppingListCounts;
import ca.on.conestogac.rsc.shoppinglist.data.source.ShoppingListsDataSource.LoadShoppingListsCallback;
import ca.on.conestogac.rsc.shoppinglist.databinding.ObservableViewModel;
import ca.on.conestogac.rsc.shoppinglist.interfaces.ItemListener;
import ca.on.conestogac.rsc.shoppinglist.interfaces.ShoppingListener;

public class ShoppingListsViewModel extends ObservableViewModel
        implements LoadShoppingListsCallback, ItemListener<ShoppingListItemViewModel> {
    private final List<ShoppingListItemViewModel> shoppingLists  = new ArrayList<>();
    private final Context context;
    private final ApplicationDbRepository db;

    // ui
    private ShoppingListItemViewModel editingShoppingList;
    private ShoppingListener shoppingListener;
    private String textTitleNewShoppingList;
    private boolean empty;

    public ShoppingListsViewModel(Context context, ApplicationDbRepository db) {
        this.context = context;
        this.db = db;
    }

    @Bindable
    public String getTextTitleNewShoppingList() {
        return textTitleNewShoppingList;
    }

    @Bindable
    public void setTextTitleNewShoppingList(String newList) {
        this.textTitleNewShoppingList = newList;
        notifyPropertyChanged(BR.textTitleNewShoppingList);
    }

    private void updateIsEmpty() {
        empty = shoppingLists.size() == 0;
        notifyPropertyChanged(BR.empty);
    }

    @Bindable
    public boolean isEmpty() {
        return empty;
    }

    @Bindable
    public int getNoShoppingListsLabel() {
        // TODO: change this strings resource after filter modes are added
        return R.string.no_shopping_lists_message;
    }

    @Bindable
    public List<? extends ViewModel> getShoppingLists() {
        return shoppingLists;
    }

    public void onViewCreated(ShoppingListener shoppingListener) {
        this.shoppingListener = shoppingListener;
        // read from DB
        db.shoppingLists().getShoppingLists(this);
    }

    public void onViewDestroyed() {
        shoppingListener = null;
    }

    public void onAddShoppingListFocusChange(boolean hasFocus) {
        if (hasFocus && editingShoppingList != null) {
            onStopEditingItem();
        }
    }

    public void onAddShoppingListClicked() {
        if (!TextUtils.isEmpty(textTitleNewShoppingList)) {
            int sortIndex = 0;
            if (shoppingLists.size() > 0) {
                sortIndex = shoppingLists.get(shoppingLists.size() - 1).getSortIndex();
            }

            ShoppingListCounts shoppingList = new ShoppingListCounts(textTitleNewShoppingList, sortIndex, 0, 0);

            // insert into DB
            db.shoppingLists().saveShoppingList(shoppingList);

            // update fields
            addShoppingList(createShoppingListItem(shoppingList));
            setTextTitleNewShoppingList("");
        } else {
            shoppingListener.onSnackBarDisplay(context.getString(R.string.title_required));
        }
    }

    public void onCollectionsSwap(int fromPosition, int toPosition) {
        if (fromPosition >= 0 && toPosition < shoppingLists.size()) {
            // swap indices in collection
            Collections.swap(shoppingLists, fromPosition, toPosition);

            // notify UI
            shoppingListener.onShoppingListItemMoved(fromPosition, toPosition);

            // update DB on new sortIndex
            int start = Math.min(fromPosition, toPosition);
            int end = Math.max(fromPosition, toPosition);
            for (int i = start; i <= end; i++) {
                ShoppingListItemViewModel shoppingList = shoppingLists.get(i);
                db.shoppingLists().updateShoppingListSortIndex(shoppingList.getShoppingListId(), i);
            }
        }
    }

    private void notifyShoppingListsUpdated() {
        notifyPropertyChanged(BR.empty);
        notifyPropertyChanged(BR.noShoppingListsLabel);
        updateIsEmpty();
    }

    private ShoppingListItemViewModel createShoppingListItem(ShoppingListCounts shoppingList) {
        ShoppingListItemViewModel shoppingListItem = new ShoppingListItemViewModel(context, db);
        shoppingListItem.bindModel(shoppingList);
        shoppingListItem.start(this);

        return shoppingListItem;
    }

    private void addShoppingList(ShoppingListItemViewModel shoppingListItem) {
        // add
        this.shoppingLists.add(shoppingListItem);

        // notify UI
        this.shoppingListener.onShoppingListInserted(shoppingLists.size());
        notifyShoppingListsUpdated();
    }

    private void addShoppingListRange(List<ShoppingListCounts> shoppingLists) {
        // add
        int fromPosition = this.shoppingLists.size();
        for (ShoppingListCounts shoppingList: shoppingLists) {
            this.shoppingLists.add(createShoppingListItem(shoppingList));
        }

        // notify UI
        this.shoppingListener.onShoppingListRangeInserted(fromPosition, this.shoppingLists.size());
        notifyShoppingListsUpdated();
    }

    @Override
    public void onShoppingListsLoaded(List<ShoppingListCounts> shoppingLists) {
        int toPosition = this.shoppingLists.size();
        if (toPosition > 0) {
            // remove
            this.shoppingLists.clear();

            // notify UI
            shoppingListener.onShoppingListProductRangeRemoved(0, toPosition);
        }
        // add
        addShoppingListRange(shoppingLists);
    }

    @Override
    public void onDataNotAvailable() {
        // remove
        int toPosition = shoppingLists.size();
        this.shoppingLists.clear();

        // notify UI
        shoppingListener.onShoppingListProductRangeRemoved(0, toPosition);
        notifyShoppingListsUpdated();
    }

    @Override
    public void onItemRemove(@NotNull ShoppingListItemViewModel shoppingList) {
        if (editingShoppingList != null && shoppingList != editingShoppingList) {
            onStopEditingItem();
        }

        // remove
        int index = shoppingLists.indexOf(shoppingList);
        shoppingLists.remove(index);

        // notify UI
        shoppingListener.onShoppingListRemoved(index);
        shoppingListener.onSnackBarDisplay(context.getString(R.string.list_deleted));
        notifyShoppingListsUpdated();

        // delete from DB
        db.shoppingLists().deleteShoppingList(shoppingList.getShoppingListId());
    }

    @Override
    public void onItemClick(@NotNull ShoppingListItemViewModel shoppingList) {
        if (editingShoppingList != null && shoppingList != editingShoppingList) {
            onStopEditingItem();
        }

        // notify UI
        shoppingListener.onShoppingListActivityChange(shoppingList);
    }

    @Override
    public void onStartEditingItem(ShoppingListItemViewModel shoppingList) {
        if (editingShoppingList != null && shoppingList != editingShoppingList) {
            onStopEditingItem();
        }
        editingShoppingList = shoppingList;

        // update UI - reset the Add Shopping list editText
        setTextTitleNewShoppingList("");
    }

    @Override
    public void onStopEditingItem() {
        ShoppingListItemViewModel prevEditing = editingShoppingList;
        editingShoppingList = null;

        if (prevEditing != null) {
            prevEditing.onUndoClicked();
        }
    }
}
