package ca.on.conestogac.rsc.shoppinglist.interfaces;

public interface ProductListener {
    void onProductInserted(int position);
    void onProductRemoved(int position);
}
