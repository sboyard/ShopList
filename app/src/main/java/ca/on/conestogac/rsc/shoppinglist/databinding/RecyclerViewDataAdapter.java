package ca.on.conestogac.rsc.shoppinglist.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewDataAdapter extends RecyclerView.Adapter<RecyclerViewDataAdapter.DataViewHolder> {
    private final int itemLayout;
    private List<? extends ViewModel> data;

    public RecyclerViewDataAdapter(int itemLayout) {
        this.itemLayout = itemLayout;
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(itemLayout, new FrameLayout(parent.getContext()), false);
        return new DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, int position) {
        holder.setViewModel(data.get(position));
    }

    @Override
    public void onViewAttachedToWindow(DataViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.bind();
    }

    @Override
    public void onViewDetachedFromWindow(DataViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.unbind();
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateData(@Nullable List<? extends ViewModel> data) {
        if ((data == null || data.isEmpty()) && this.data != null) {
            this.data.clear();
        } else if (this.data != data) {
            this.data = data;
        }

        // notify the UI that the bound List has changed
        notifyDataSetChanged();
    }

    static class DataViewHolder extends RecyclerView.ViewHolder {
        ViewDataBinding binding;

        DataViewHolder(View itemView) {
            super(itemView);
            bind();
        }

        void bind() {
            if (binding == null) {
                binding = DataBindingUtil.bind(itemView);
            }
        }

        void unbind() {
            if (binding != null) {
                binding.unbind();
            }
        }

        void setViewModel(ViewModel viewModel) {
            if (binding != null) {
                binding.setVariable(BR.viewModel, viewModel);
                binding.executePendingBindings();
            }
        }
    }
}
