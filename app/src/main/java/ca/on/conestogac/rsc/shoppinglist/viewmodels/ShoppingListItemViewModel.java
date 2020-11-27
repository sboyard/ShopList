package ca.on.conestogac.rsc.shoppinglist.viewmodels;

import android.content.Context;
import android.view.View;

import androidx.databinding.Bindable;
import androidx.databinding.Observable;
import androidx.databinding.ObservableField;

import java.lang.ref.WeakReference;

import ca.on.conestogac.rsc.shoppinglist.BR;
import ca.on.conestogac.rsc.shoppinglist.R;
import ca.on.conestogac.rsc.shoppinglist.data.ApplicationDbRepository;
import ca.on.conestogac.rsc.shoppinglist.data.models.ShoppingListCounts;
import ca.on.conestogac.rsc.shoppinglist.databinding.ObservableViewModel;
import ca.on.conestogac.rsc.shoppinglist.interfaces.ItemListener;

public class ShoppingListItemViewModel extends ObservableViewModel {
    private final ObservableField<ShoppingListCounts> modelObservable = new ObservableField<>();
    private final ApplicationDbRepository db;
    private final Context context;

    private WeakReference<ItemListener<ShoppingListItemViewModel>> itemListener;

    // model values
    private String shoppingListId;
    private String title;
    private int totalCount;
    private int checkedCount;

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
        // TODO : Update DB instance
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
        if (itemListener.get() != null) {
            itemListener.get().onItemClick(this);
        }
    }

    public void onRemoveClicked() {
        // TODO : binding for a confirmation dialog would be great / or undo snack bar
        if (itemListener.get() != null) {
            itemListener.get().onItemRemove(this);
        }
    }
}
