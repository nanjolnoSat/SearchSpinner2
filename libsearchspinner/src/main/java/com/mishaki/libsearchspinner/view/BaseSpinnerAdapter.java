package com.mishaki.libsearchspinner.view;

import android.view.View;
import android.view.ViewGroup;

import com.mishaki.libsearchspinner.utils.SearchSpinnerConstant;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by 杜壁奇<br/>
 * on 2020/08/30 22:14<br/>
 * 放在view包是因为有些方法只想包权限，不想public
 */
public abstract class BaseSpinnerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    @NonNull
    private String searchText = SearchSpinnerConstant.StringValues.EMPTY_VALUE;
    private List<T> list = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    private float itemHeight = 0f;
    private boolean isShowSelectColor = true;
    private int normalItemColor = SearchSpinnerConstant.Color.WHITE;
    private int selectedItemColor = SearchSpinnerConstant.Color.GRAY;
    private int selectedPosition = SearchSpinnerConstant.Dimens.INT_DEFAULT;

    public final T getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public final VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final VH viewHolder;
        if (searchText.length() == 0) {
            viewHolder = onCreateNormalViewHolder(parent, viewType);
        } else {
            viewHolder = onCreateSearchViewHolder(parent, viewType, searchText);
        }
        View rootView = viewHolder.itemView;
        final RecyclerView.LayoutParams param;
        if (rootView.getLayoutParams() == null) {
            param = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ((int) itemHeight));
        } else {
            param = (RecyclerView.LayoutParams) rootView.getLayoutParams();
            param.height = (int) itemHeight;
        }
        rootView.setLayoutParams(param);
        return viewHolder;
    }

    @NonNull
    protected abstract VH onCreateNormalViewHolder(@NonNull ViewGroup parent, int viewType);

    @NonNull
    protected abstract VH onCreateSearchViewHolder(@NonNull ViewGroup parent, int viewType, @NonNull String searchContent);

    @Override
    public final void onBindViewHolder(@NonNull VH holder, int position) {
        View rootView = holder.itemView;
        if (isShowSelectColor) {
            if (selectedPosition == position) {
                rootView.setBackgroundColor(selectedItemColor);
            } else {
                rootView.setBackgroundColor(normalItemColor);
            }
        } else {
            rootView.setBackgroundColor(normalItemColor);
        }
        if (searchText.length() == 0) {
            onBindNormalViewHolder(holder, position);
        } else {
            onBindSearchViewHolder(holder, position, searchText);
        }
        rootView.setOnClickListener((view) -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position);
            }
        });
    }

    protected abstract void onBindNormalViewHolder(@NonNull VH holder, int position);

    protected abstract void onBindSearchViewHolder(@NonNull VH holder, int position, @NonNull String searchContent);

    @Override
    public int getItemCount() {
        return list.size();
    }

    void setItemHeight(float itemHeight) {
        if (this.itemHeight == itemHeight) {
            return;
        }
        this.itemHeight = itemHeight;
        notifyDataSetChanged();
    }

    void setList(List<T> list) {
        this.list = list;
    }

    void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public final void setShowSelectColor(boolean showSelectColor) {
        if (this.isShowSelectColor == showSelectColor) {
            return;
        }
        isShowSelectColor = showSelectColor;
        notifyDataSetChanged();
    }

    public final void setNormalItemColor(int normalItemColor) {
        if (this.normalItemColor == normalItemColor) {
            return;
        }
        this.normalItemColor = normalItemColor;
        notifyDataSetChanged();
    }

    public final void setSelectedItemColor(int selectedItemColor) {
        if (this.selectedItemColor == selectedItemColor) {
            return;
        }
        this.selectedItemColor = selectedItemColor;
        notifyDataSetChanged();
    }

    public final void setSelectedPosition(int selectedPosition) {
        if (this.selectedPosition == selectedPosition) {
            return;
        }
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

    public final void setItemInfo(@NonNull String searchText, @NonNull List<T> list) {
        if (this.searchText.equals(searchText) && this.list == list) {
            return;
        }
        this.searchText = searchText;
        this.list = list;
        notifyDataSetChanged();
    }

    interface OnItemClickListener {
        void onItemClick(int position);
    }
}
