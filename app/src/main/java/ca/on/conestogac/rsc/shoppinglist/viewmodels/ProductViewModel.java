package ca.on.conestogac.rsc.shoppinglist.viewmodels;

import androidx.databinding.Bindable;

import ca.on.conestogac.rsc.shoppinglist.BR;
import ca.on.conestogac.rsc.shoppinglist.data.ApplicationDbRepository;
import ca.on.conestogac.rsc.shoppinglist.databinding.ObservableViewModel;
import ca.on.conestogac.rsc.shoppinglist.data.models.Product;

public class ProductViewModel extends ObservableViewModel {
    private final ApplicationDbRepository db;

    // model values
    private final String productId;
    private boolean checked;
    private String title;

    public ProductViewModel(Product model, ApplicationDbRepository db) {
        this.db = db;

        // set model values
        productId = model.getId();
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

        // notify UI
        notifyPropertyChanged(BR.checked);

        // update DB
        db.products().updateProductChecked(productId, checked);
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
