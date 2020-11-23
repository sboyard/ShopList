package ca.on.conestogac.rsc.shoppinglist.databinding;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewBinding {
    @BindingAdapter({"adapter", "data"})
    public void bind(RecyclerView recyclerView, RecyclerViewDataAdapter adapter, List<? extends ViewModel> data) {
        recyclerView.setAdapter(adapter);
        adapter.updateData(data);
    }
}
