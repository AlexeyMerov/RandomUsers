package com.alexeymerov.randomusers.presentation.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
abstract class BaseRecyclerAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    private List<T> mItems = new ArrayList<>();

    protected List<T> getItems() {
        return mItems;
    }

    public void setItems(List<T> items) {
        mItems.clear();
        mItems.addAll(items);
        autoNotify(mItems, items);
    }

    @Override
    public int getItemCount() {
        if (mItems == null) return 0;
        return mItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) onBindViewHolder(holder, position);
        else proceedPayloads(holder, position, payloads);
    }

    protected abstract void proceedPayloads(@NonNull VH holder, int position, @NonNull List<Object> payloads);

    protected void autoNotify(List<T> oldList, List<T> newList) {
        DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return oldList.size();
            }

            @Override
            public int getNewListSize() {
                return newList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return compareItems(oldList.get(oldItemPosition), newList.get(newItemPosition));
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
            }

            @Nullable
            @Override
            public Object getChangePayload(int oldItemPosition, int newItemPosition) {
                return compareContent(oldList.get(oldItemPosition), newList.get(newItemPosition));
            }
        }).dispatchUpdatesTo(this);

    }

    public abstract boolean compareItems(T oldItem, T newItem);

    protected Object compareContent(T oldItem, T newItem) {
        return null;
    }
}
