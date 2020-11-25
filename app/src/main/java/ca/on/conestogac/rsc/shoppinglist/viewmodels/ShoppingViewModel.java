package ca.on.conestogac.rsc.shoppinglist.viewmodels;

import androidx.databinding.Bindable;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ca.on.conestogac.rsc.shoppinglist.BR;
import ca.on.conestogac.rsc.shoppinglist.data.ApplicationDbRepository;
import ca.on.conestogac.rsc.shoppinglist.databinding.ObservableViewModel;
import ca.on.conestogac.rsc.shoppinglist.interfaces.ShoppingListener;
import ca.on.conestogac.rsc.shoppinglist.data.models.ShoppingList;

public class ShoppingViewModel extends ObservableViewModel {
    private final List<ShoppingListViewModel> data;
    private final ApplicationDbRepository db;

    // ui
    private ShoppingListener shoppingListener;
    private String textShoppingListTitle;

    public ShoppingViewModel(ApplicationDbRepository db) {
        this.db = db;
        this.data = new ArrayList<>();
    }

    @Bindable
    public String getTextShoppingListTitle() {
        return textShoppingListTitle;
    }

    @Bindable
    public void setTextShoppingListTitle(String newList) {
        this.textShoppingListTitle = newList;
        notifyPropertyChanged(BR.textShoppingListTitle);
    }

    @Bindable
    public List<? extends ViewModel> getData() {
        return data;
    }

    public void onViewCreated(ShoppingListener shoppingListener) {
        this.shoppingListener = shoppingListener;
        populateData();
    }

    public void onViewDestroyed() {
        shoppingListener = null;
    }

    public void onAddShoppingListClicked() {
        if (textShoppingListTitle != null && !textShoppingListTitle.equals("")) {
            ShoppingList shoppingList = new ShoppingList(textShoppingListTitle, data.size());

            // insert into DB
            db.shoppingLists().saveShoppingList(shoppingList);

            // update fields
            addShoppingList(shoppingList);
            setTextShoppingListTitle("");
        }
    }

    public void onCollectionsSwap(int fromPosition, int toPosition) {
        if (fromPosition >= 0 && toPosition < data.size()) {
            // swap indices in collection
            Collections.swap(data, fromPosition, toPosition);

            // notify UI
            shoppingListener.onShoppingListItemMoved(fromPosition, toPosition);

            // update DB on new sortIndex
            int start = Math.min(fromPosition, toPosition);
            int end = Math.max(fromPosition, toPosition);
            for (int i = start; i <= end; i++) {
                ShoppingListViewModel shoppingList = data.get(i);
                db.shoppingLists().updateShoppingListSortIndex(shoppingList.getShoppingListId(), i);
            }
        }
    }

    public void onShoppingListRemoved(@NotNull ShoppingListViewModel shoppingList) {
        // remove
        int index = data.indexOf(shoppingList);
        data.remove(index);

        // notify UI
        shoppingListener.onShoppingListRemoved(index);

        // delete from DB
        db.shoppingLists().deleteShoppingList(shoppingList.getShoppingListId());
    }

    public void onShoppingListClicked(@NotNull ShoppingListViewModel shoppingList) {
        // notify UI
        shoppingListener.onShoppingListActivityChange(shoppingList);
    }

    private void addShoppingList(ShoppingList shoppingList) {
        ShoppingListViewModel shoppingListViewModel = new ShoppingListViewModel(shoppingList, this, db);

        // get Products
        shoppingListViewModel.populateData();

        // add
        data.add(shoppingListViewModel);

        // notify UI
        shoppingListener.onShoppingListInserted(data.size());
    }

    private void populateData() {
        if (data.size() > 0) {
            data.clear();
        }
        // read from DB
        db.shoppingLists().getShoppingLists(shoppingLists -> {
            for (ShoppingList shoppingList: shoppingLists) {
                // add to data as viewModel
                addShoppingList(shoppingList);
            }
        });
    }
}
