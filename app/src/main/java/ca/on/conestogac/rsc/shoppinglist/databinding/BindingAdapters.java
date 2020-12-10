package ca.on.conestogac.rsc.shoppinglist.databinding;

import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

public class BindingAdapters {
    @BindingAdapter("src")
    public static void setImageResource(ImageView imageView, int resource) {
        if (resource > 0) {
            imageView.setImageResource(resource);
        }
    }

    @BindingAdapter(value={"android:animatedVisibility", "android:animatedVisibilityDuration"}, requireAll = false)
    public static void setAnimatedVisibility(View target, boolean isVisible, Integer delay) {
        if (delay == null) {
            TransitionManager.beginDelayedTransition((ViewGroup)target.getRootView());
        } else {
            AutoTransition autoTransition = new AutoTransition();
            autoTransition.setDuration(delay);
            TransitionManager.beginDelayedTransition((ViewGroup)target.getRootView(), autoTransition);
        }
        target.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("android:onActionDone")
    public static void setOnActionDone(EditText target, Runnable runnable) {
        target.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN)) {
                if (runnable != null) {
                    runnable.run();
                }
                return true;
            }
            return false;
        });
    }

    @BindingAdapter("android:requestFocus")
    public static void editTextRequestFocus(EditText target, boolean setFocus) {
        if (setFocus) {
            target.requestFocus();
            // place cursor at end of editText
            target.setSelection(target.getText().length());
        }
    }
}
