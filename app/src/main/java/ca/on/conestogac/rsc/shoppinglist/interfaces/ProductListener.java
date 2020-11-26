package ca.on.conestogac.rsc.shoppinglist.interfaces;

public interface ProductListener {
    void onSnackBarDisplay(String message);
    void onProductInserted(int position);
    void onProductRangeInserted(int fromPosition, int toPosition);
    void onProductItemMoved(int fromPosition, int toPosition);
    void onProductRemoved(int position);
    void onProductRangeRemoved(int fromPosition, int toPosition);
}
