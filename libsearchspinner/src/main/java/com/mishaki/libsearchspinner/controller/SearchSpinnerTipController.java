package com.mishaki.libsearchspinner.controller;

import android.graphics.drawable.Drawable;
import android.widget.TextView;

/**
 * Created by 杜壁奇<br/>
 * on 2020/09/02 09:11
 */
public class SearchSpinnerTipController {
    private final TextView popupTipView;

    public SearchSpinnerTipController(TextView popupTipView) {
        this.popupTipView = popupTipView;
    }

    public void setTipText(CharSequence text) {
        popupTipView.setText(text);
    }

    public void setTipText(int resId) {
        popupTipView.setText(resId);
    }

    public void setTipTextColor(int textColor) {
        popupTipView.setTextColor(textColor);
    }

    public void setTipTextSize(float sp) {
        popupTipView.setTextSize(sp);
    }

    public void setTipTextSize(int unit, float size) {
        popupTipView.setTextSize(unit, size);
    }

    public void setTipViewGravity(int gravity) {
        popupTipView.setGravity(gravity);
    }

    public void setTipViewPaddingLeft(float paddingLeft) {
        popupTipView.setPadding((int) paddingLeft, popupTipView.getPaddingTop(), popupTipView.getPaddingRight(), popupTipView.getPaddingBottom());
    }

    public void setTipViewPaddingTop(float paddingTop) {
        popupTipView.setPadding(popupTipView.getPaddingLeft(), (int) paddingTop, popupTipView.getPaddingRight(), popupTipView.getPaddingBottom());
    }

    public void setTipViewPaddingRight(float paddingRight) {
        popupTipView.setPadding(popupTipView.getPaddingLeft(), popupTipView.getPaddingTop(), (int) paddingRight, popupTipView.getPaddingBottom());
    }

    public void setTipViewPaddingBottom(float paddingBottom) {
        popupTipView.setPadding(popupTipView.getPaddingLeft(), popupTipView.getPaddingTop(), popupTipView.getPaddingRight(), (int) paddingBottom);
    }

    public void setTipViewPadding(float paddingLeft, float paddingTop, float paddingRight, float paddingBottom) {
        popupTipView.setPadding(((int) paddingLeft), ((int) paddingTop), ((int) paddingRight), ((int) paddingBottom));
    }

    public void setTipViewBackground(int resId) {
        popupTipView.setBackgroundResource(resId);
    }

    public void setTipViewBackgroundDrawable(Drawable drawable) {
        popupTipView.setBackground(drawable);
    }

    public void setTipViewBackgroundColor(int color) {
        popupTipView.setBackgroundColor(color);
    }
}
