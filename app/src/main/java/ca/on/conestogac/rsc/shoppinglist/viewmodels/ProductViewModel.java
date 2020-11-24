package ca.on.conestogac.rsc.shoppinglist.viewmodels;

import androidx.databinding.Bindable;

import ca.on.conestogac.rsc.shoppinglist.BR;
import ca.on.conestogac.rsc.shoppinglist.databinding.ObservableViewModel;
import ca.on.conestogac.rsc.shoppinglist.models.Product;

public class ProductViewModel extends ObservableViewModel {
    private boolean checked;
    private String title;

    public ProductViewModel(Product model) {
        // set model values
        checked = model.isChecked();
        title = model.getTitle();
    }

    @Bindable
    public boolean isChecked() {
        return checked;
    }

    @Bindable
    public void setChecked(boolean checked) {
        this.checked = checked;
        notifyPropertyChanged(BR.checked);
        // TODO: update DB instance
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    @Bindable
    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
        // TODO: update DB instance
    }

    public void onClicked() {

    }
}
