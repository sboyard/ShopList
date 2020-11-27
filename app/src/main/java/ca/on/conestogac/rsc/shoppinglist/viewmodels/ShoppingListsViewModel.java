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
    private ShoppingListener shoppingListener;
    private String textTitleNewShoppingList;

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

    @Bindable
    public boolean isEmpty() {
        return shoppingLists.size() == 0;
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
        populateData();
    }

    public void onViewDestroyed() {
        shoppingListener = null;
    }

    public void onAddShoppingListClicked() {
        if (!TextUtils.isEmpty(textTitleNewShoppingList)) {
            ShoppingListCounts shoppingList = new ShoppingListCounts(textTitleNewShoppingList, shoppingLists.size(), 0, 0);

            // insert into DB
            db.shoppingLists().saveShoppingList(shoppingList);

            // update fields
            addShoppingList(shoppingList);
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

    private void addShoppingList(ShoppingListCounts shoppingList) {
        ShoppingListItemViewModel itemViewModel = new ShoppingListItemViewModel(context, db);
        itemViewModel.bindModel(shoppingList);
        itemViewModel.start(this);

        // add
        this.shoppingLists.add(itemViewModel);

        // notify UI
        this.shoppingListener.onShoppingListInserted(shoppingLists.size());

        notifyPropertyChanged(BR.empty);
        notifyPropertyChanged(BR.noShoppingListsLabel);
    }

    private void addShoppingListRange(List<ShoppingListCounts> shoppingLists) {
        int fromPosition = this.shoppingLists.size();
        for (ShoppingListCounts shoppingList: shoppingLists) {
            addShoppingList(shoppingList);
        }
        this.shoppingListener.onShoppingListRangeInserted(fromPosition, this.shoppingLists.size());
    }

    private void populateData() {
        // read from DB
        db.shoppingLists().getShoppingLists(this);
    }

    @Override
    public void onShoppingListsLoaded(List<ShoppingListCounts> shoppingLists) {
        shoppingListener.onShoppingListProductRangeRemoved(0, this.shoppingLists.size());
        this.shoppingLists.clear();

        addShoppingListRange(shoppingLists);
    }

    @Override
    public void onDataNotAvailable() {
        shoppingListener.onShoppingListProductRangeRemoved(0, shoppingLists.size());
        this.shoppingLists.clear();

        notifyPropertyChanged(BR.empty);
        notifyPropertyChanged(BR.noShoppingListsLabel);
    }

    @Override
    public void onItemRemove(@NotNull ShoppingListItemViewModel shoppingList) {
        // remove
        int index = shoppingLists.indexOf(shoppingList);
        shoppingLists.remove(index);

        // notify UI
        shoppingListener.onShoppingListRemoved(index);
        shoppingListener.onSnackBarDisplay(context.getString(R.string.list_deleted));

        notifyPropertyChanged(BR.empty);
        notifyPropertyChanged(BR.noShoppingListsLabel);

        // delete from DB
        db.shoppingLists().deleteShoppingList(shoppingList.getShoppingListId());
    }

    @Override
    public void onItemClick(@NotNull ShoppingListItemViewModel shoppingList) {
        // notify UI
        shoppingListener.onShoppingListActivityChange(shoppingList);
    }
}
