package ca.on.conestogac.rsc.shoppinglist.databinding;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

public class BindingAdapters {
    @BindingAdapter("app:src")
    public static void setSendState(ImageView imageView, int resource) {
        if (resource > 0) {
            imageView.setImageResource(resource);
        }
    }
}
