package ca.on.conestogac.rsc.shoppinglist.viewmodels;

import android.content.Context;
import android.text.TextUtils;

import androidx.databinding.Bindable;
import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.room.util.StringUtil;

import java.lang.ref.WeakReference;

import ca.on.conestogac.rsc.shoppinglist.BR;
import ca.on.conestogac.rsc.shoppinglist.data.ApplicationDbRepository;
import ca.on.conestogac.rsc.shoppinglist.databinding.ObservableViewModel;
import ca.on.conestogac.rsc.shoppinglist.data.models.Product;
import ca.on.conestogac.rsc.shoppinglist.interfaces.ItemListener;

public class ProductItemViewModel extends ObservableViewModel {
    private final Context context;
    private final ApplicationDbRepository db;

    private final ObservableField<Product> modelObservable = new ObservableField<>();
    private WeakReference<ItemListener<ProductItemViewModel>> itemListener;

    // model values
    private String productId;
    private boolean checked;
    private String title;
    private boolean editMode;

    public ProductItemViewModel(Context context, ApplicationDbRepository db) {
        this.context = context;
        this.db = db;

        modelObservable.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                Product product = modelObservable.get();
                if (product != null) {
                    productId = product.getId();
                    title = product.getTitle();
                    checked = product.isChecked();

                    notifyPropertyChanged(BR.title);
                    notifyPropertyChanged(BR.checked);
                }
            }
        });
    }

    public void start(ItemListener<ProductItemViewModel> itemListener) {
        this.itemListener = new WeakReference<>(itemListener);
    }

    public void bindModel(Product product) {
        modelObservable.set(product);
    }

    public String getProductId() {
        return productId;
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

        // updated DB
        db.products().updateProductChecked(productId, checked);
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    @Bindable
    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            // edit
            this.title = title;
            notifyPropertyChanged(BR.title);
            db.products().updateProductTitle(productId, title);
        } else {
            // delete
            itemListener.get().onItemRemove(this);
        }
    }

    @Bindable
    public boolean getEditMode() {
        return editMode;
    }

    private void setEditMode(boolean editMode) {
        this.editMode = editMode;
        notifyPropertyChanged(BR.editMode);
    }

    public void onFocusChange(boolean hasFocus) {
        setEditMode(hasFocus);
    }

    public void onRemoveClicked() {
        if (itemListener.get() != null) {
            itemListener.get().onItemRemove(this);
        }
    }

    public void onClicked() {
        //setEditMode(!editMode);
    }
}
