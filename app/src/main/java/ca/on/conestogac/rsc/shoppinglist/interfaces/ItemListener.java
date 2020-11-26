package ca.on.conestogac.rsc.shoppinglist.interfaces;

import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

public interface ItemListener<T extends ViewModel> {
    void onItemRemove(@NotNull T item);
    void onItemClick(@NotNull T item);
}
