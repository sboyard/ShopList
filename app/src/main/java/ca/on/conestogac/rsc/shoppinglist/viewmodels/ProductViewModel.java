package ca.on.conestogac.rsc.shoppinglist.viewmodels;

import androidx.databinding.Bindable;

import ca.on.conestogac.rsc.shoppinglist.BR;
import ca.on.conestogac.rsc.shoppinglist.databinding.ObservableViewModel;
import ca.on.conestogac.rsc.shoppinglist.models.Product;

public class ProductViewModel extends ObservableViewModel {
    private final Product model;

    public ProductViewModel(Product model) {
        this.model = model;
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

    public void onClicked() {

    }
}
