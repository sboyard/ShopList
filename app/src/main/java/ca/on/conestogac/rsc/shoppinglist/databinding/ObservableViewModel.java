package ca.on.conestogac.rsc.shoppinglist.databinding;

import androidx.databinding.Observable;
import androidx.databinding.PropertyChangeRegistry;
import androidx.lifecycle.ViewModel;

import ca.on.conestogac.rsc.shoppinglist.data.models.Product;

public abstract class ObservableViewModel extends ViewModel implements Observable {

    PropertyChangeRegistry callBacks = new PropertyChangeRegistry();

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        callBacks.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        callBacks.remove(callback);
    }

    public void notifyChange() {
        callBacks.notifyChange(this, 0);
    }

    public void notifyPropertyChanged(int propertyId) {
        callBacks.notifyChange(this, propertyId);
    }
}