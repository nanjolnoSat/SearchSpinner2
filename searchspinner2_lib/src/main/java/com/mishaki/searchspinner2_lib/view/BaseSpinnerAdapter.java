package com.mishaki.searchspinner2_lib.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import com.mishaki.searchspinner2_lib.utils.SearchSpinnerConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 杜壁奇<br/>
 * on 2020/08/30 22:14
 */
public abstract class BaseSpinnerAdapter<T> extends BaseAdapter {
    @NonNull
    private String searchText = SearchSpinnerConstant.StringValues.EMPTY_VALUE;
    private List<T> list = new ArrayList<>();

    private float itemHeight = 0f;
    private boolean isShowSelectColor = true;
    private int normalItemColor = SearchSpinnerConstant.Color.WHITE;
    private int selectedItemColor = SearchSpinnerConstant.Color.GRAY;
    private int selectedPosition = SearchSpinnerConstant.Dimens.INT_DEFAULT;


    @Override
    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View rootView;
        if (searchText.length() == 0) {
            rootView = getNormalView(position, convertView, parent);
        } else {
            rootView = getSearchView(position, convertView, parent, searchText);
        }
        if (isShowSelectColor) {
            if (selectedPosition == position) {
                rootView.setBackgroundColor(selectedItemColor);
            } else {
                rootView.setBackgroundColor(normalItemColor);
            }
        }
        final AbsListView.LayoutParams param;
        if (rootView.getLayoutParams() == null) {
            param = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) itemHeight, 0);
        } else {
            param = (AbsListView.LayoutParams) rootView.getLayoutParams();
            param.height = (int) itemHeight;
        }
        rootView.setLayoutParams(param);
        return rootView;
    }

    @NonNull
    protected abstract View getNormalView(int position, @Nullable View convertView, ViewGroup parent);

    @NonNull
    protected abstract View getSearchView(int position, @Nullable View convertView, ViewGroup parent, @NonNull String searchText);

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

    public void setShowSelectColor(boolean showSelectColor) {
        if (this.isShowSelectColor == showSelectColor) {
            return;
        }
        isShowSelectColor = showSelectColor;
        notifyDataSetChanged();
    }

    public void setNormalItemColor(int normalItemColor) {
        if (this.normalItemColor == normalItemColor) {
            return;
        }
        this.normalItemColor = normalItemColor;
        notifyDataSetChanged();
    }

    public void setSelectedItemColor(int selectedItemColor) {
        if (this.selectedItemColor == selectedItemColor) {
            return;
        }
        this.selectedItemColor = selectedItemColor;
        notifyDataSetChanged();
    }

    public void setSelectedPosition(int selectedPosition) {
        if (this.selectedPosition == selectedPosition) {
            return;
        }
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

    public void setItemInfo(@NonNull String searchText, @NonNull List<T> list) {
        if (this.searchText.equals(searchText) && this.list == list) {
            return;
        }
        this.searchText = searchText;
        this.list = list;
        notifyDataSetChanged();
    }
}
