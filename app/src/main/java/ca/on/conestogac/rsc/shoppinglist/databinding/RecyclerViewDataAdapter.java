package ca.on.conestogac.rsc.shoppinglist.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RecyclerViewDataAdapter extends RecyclerView.Adapter<RecyclerViewDataAdapter.DataViewHolder> {
    private final int itemLayout;
    private List<? extends ViewModel> data;

    public RecyclerViewDataAdapter(List<? extends ViewModel> data, int itemLayout) {
        this.data = data;
        this.itemLayout = itemLayout;
    }

    @NotNull
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
    public void onViewAttachedToWindow(@NotNull DataViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.bind();
    }

    @Override
    public void onViewDetachedFromWindow(@NotNull DataViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.unbind();
    }

    @Override
    public int getItemCount() {
        return data.size();
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
