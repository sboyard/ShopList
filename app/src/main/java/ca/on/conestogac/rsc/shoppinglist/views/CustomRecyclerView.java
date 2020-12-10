package ca.on.conestogac.rsc.shoppinglist.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ca.on.conestogac.rsc.shoppinglist.R;
import ca.on.conestogac.rsc.shoppinglist.databinding.RecyclerViewDataAdapter;

public class CustomRecyclerView extends LinearLayout {
    private int itemLayout;
    private List<? extends ViewModel> data;
    private RecyclerViewDataAdapter adapter;

    public final RecyclerView recyclerView;

    public CustomRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        View view = LayoutInflater.from(context)
                .inflate(R.layout.custom_recycler_view, this, true);

        // init RecyclerView
        recyclerView = view.findViewById(R.id.custom_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
    }

    public void setData(List<? extends ViewModel> data) {
        this.data = data;
        buildAdapter();
    }

    public void setItemLayout(int itemLayout) {
        this.itemLayout = itemLayout;
        buildAdapter();
    }

    public void notifyItemInserted(int position, boolean scrollToPosition) {
        if (adapter != null) {
            adapter.notifyItemInserted(position);
            if (scrollToPosition) {
                recyclerView.scrollToPosition(position);
            }
        }
    }

    public void notifyItemRangeInserted(int fromPosition, int toPosition, boolean scrollToFromPosition) {
        if (adapter != null) {
            adapter.notifyItemRangeInserted(fromPosition, toPosition);
            if (scrollToFromPosition) {
                recyclerView.scrollToPosition(fromPosition);
            }
        }
    }

    public void notifyItemMoved(int fromPosition, int toPosition) {
        if (adapter != null) {
            adapter.notifyItemMoved(fromPosition, toPosition);
        }
    }

    public void notifyItemRemoved(int position) {
        if (adapter != null) {
            adapter.notifyItemRemoved(position);
        }
    }

    public void notifyItemRangeRemoved(int fromPosition, int toPosition) {
        if (adapter != null) {
            adapter.notifyItemRangeRemoved(fromPosition, toPosition);
        }
    }

    private void buildAdapter() {
        adapter = new RecyclerViewDataAdapter(data, itemLayout);
        adapter.setHasStableIds(true);

        recyclerView.setAdapter(adapter);
    }
}
