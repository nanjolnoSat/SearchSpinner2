package com.mishaki.libsearchspinner.controller;

import android.graphics.drawable.Drawable;
import android.widget.EditText;

/**
 * Created by 杜壁奇<br/>
 * on 2020/09/02 09:16
 */
public class SearchSpinnerSearchController {
    private final EditText popupSearchView;

    public SearchSpinnerSearchController(EditText popupSearchView) {
        this.popupSearchView = popupSearchView;
    }

    public void setSearchViewTextSize(float sp) {
        popupSearchView.setTextSize(sp);
    }

    public void setSearchViewTextSize(int unit, float size) {
        popupSearchView.setTextSize(unit, size);
    }

    public void setSearchViewTextColor(int color) {
        popupSearchView.setTextColor(color);
    }

    public void setSearchViewHint(String text) {
        popupSearchView.setHint(text);
    }

    public void setSearchViewHintColor(int color) {
        popupSearchView.setHintTextColor(color);
    }

    public void setSearchViewBackground(int resId) {
        popupSearchView.setBackgroundResource(resId);
    }

    public void setSearchViewBackground(Drawable drawable) {
        popupSearchView.setBackground(drawable);
    }

    public void setSearchViewBackgroundColor(int color) {
        popupSearchView.setBackgroundColor(color);
    }
}
