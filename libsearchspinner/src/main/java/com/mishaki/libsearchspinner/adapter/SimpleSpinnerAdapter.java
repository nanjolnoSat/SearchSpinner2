package com.mishaki.libsearchspinner.adapter;

import android.view.ViewGroup;

import com.mishaki.libsearchspinner.view.BaseSpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by 杜壁奇<br/>
 * on 2020/09/01 10:21
 */
public abstract class SimpleSpinnerAdapter<T, VH extends RecyclerView.ViewHolder> extends BaseSpinnerAdapter<T, VH> {
    @NonNull
    @Override
    protected final VH onCreateSearchViewHolder(@NonNull ViewGroup parent, int viewType, @NonNull String searchContent) {
        return onCreateNormalViewHolder(parent, viewType);
    }

    @Override
    protected final void onBindSearchViewHolder(@NonNull VH holder, int position, @NonNull String searchContent) {
        onBindNormalViewHolder(holder, position);
    }
}
