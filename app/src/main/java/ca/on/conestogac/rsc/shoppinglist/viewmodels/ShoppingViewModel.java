package ca.on.conestogac.rsc.shoppinglist.viewmodels;

import androidx.databinding.Bindable;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import ca.on.conestogac.rsc.shoppinglist.BR;
import ca.on.conestogac.rsc.shoppinglist.R;
import ca.on.conestogac.rsc.shoppinglist.databinding.ObservableViewModel;
import ca.on.conestogac.rsc.shoppinglist.databinding.RecyclerViewDataAdapter;
import ca.on.conestogac.rsc.shoppinglist.models.ListItem;

public class ShoppingViewModel extends ObservableViewModel {
    private final List<ListItemViewModel> data;
    private final RecyclerViewDataAdapter adapter;

    @Bindable
    private String textNewList;

    public ShoppingViewModel() {
        data = new ArrayList<>();
        adapter = new RecyclerViewDataAdapter(R.layout.item_row);

        populateData();
    }

    public String getTextNewList() {
        return textNewList;
    }

    public void setTextNewList(String newList) {
        this.textNewList = newList;
        notifyPropertyChanged(BR.textNewList);
    }

    public void onAddNewListClicked() {
        if (textNewList != null && !textNewList.equals("")) {
            data.add(new ListItemViewModel(new ListItem(textNewList)));
            notifyPropertyChanged(BR.data);

            setTextNewList("");
        }
    }

    @Bindable
    public List<? extends ViewModel> getData() {
        return this.data;
    }

    @Bindable
    public RecyclerViewDataAdapter getAdapter() {
        return this.adapter;
    }

    private void populateData() {
        // Do an asynchronous operation to fetch users.
        data.add(new ListItemViewModel(new ListItem("Canadian Tire")));
        data.add(new ListItemViewModel(new ListItem("WalMart")));
        data.add(new ListItemViewModel(new ListItem("Home Depot")));

        notifyPropertyChanged(BR.data);
    }
}
