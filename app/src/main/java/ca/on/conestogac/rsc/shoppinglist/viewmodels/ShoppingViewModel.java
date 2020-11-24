package ca.on.conestogac.rsc.shoppinglist.viewmodels;

import androidx.databinding.Bindable;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import ca.on.conestogac.rsc.shoppinglist.BR;
import ca.on.conestogac.rsc.shoppinglist.databinding.ObservableViewModel;
import ca.on.conestogac.rsc.shoppinglist.interfaces.ShoppingListener;
import ca.on.conestogac.rsc.shoppinglist.models.ShoppingList;

public class ShoppingViewModel extends ObservableViewModel {
    private final List<ShoppingListViewModel> data;
    private ShoppingListener shoppingListener;
    private String textShoppingListTitle;

    public ShoppingViewModel() {
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
    }

    public void onViewDestroyed() {
        shoppingListener = null;
    }

    public void onAddShoppingListClicked() {
        if (textShoppingListTitle != null && !textShoppingListTitle.equals("")) {
            addShoppingList(new ShoppingList(textShoppingListTitle));
            setTextShoppingListTitle("");
        }
    }

    public void onShoppingListClicked(@NotNull ShoppingListViewModel shoppingList) {
        shoppingListener.onShoppingListActivityChange(shoppingList);
    }

    public void onShoppingListRemoved(@NotNull ShoppingListViewModel shoppingList) {
        int index = data.indexOf(shoppingList);
        data.remove(index);
        shoppingListener.onShoppingListRemoved(index);
    }

    private void addShoppingList(ShoppingList shoppingList) {
        shoppingListener.onShoppingListInserted(data.size());
        data.add(new ShoppingListViewModel(shoppingList, this));
    }

    private void populateData() {
        // TODO: Do an asynchronous operation to fetch users.
    }
}
