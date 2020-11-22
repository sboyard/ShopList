package ca.on.conestogac.rsc.shoppinglist.viewmodels;

import androidx.databinding.Bindable;
import androidx.lifecycle.MutableLiveData;

import ca.on.conestogac.rsc.shoppinglist.BR;
import ca.on.conestogac.rsc.shoppinglist.R;
import ca.on.conestogac.rsc.shoppinglist.databinding.ObservableViewModel;
import ca.on.conestogac.rsc.shoppinglist.models.ListItem;

public class ListItemViewModel extends ObservableViewModel {
    private ListItem model;

    public ListItemViewModel(ListItem shoppingList) {
        this.model = shoppingList;
    }

    @Bindable
    public ListItem getModel() {
        return model;
    }

    @Bindable
    public int getCount() {
        return model.getCount();
    }

    @Bindable
    public int getSize() {
        return model.getSize();
    }

    @Bindable
    public String getTitle() {
        return model.getTitle();
    }

    @Bindable
    public void setTitle(String title) {
        this.model.setTitle(title);
        notifyPropertyChanged(BR.title);
    }
}
