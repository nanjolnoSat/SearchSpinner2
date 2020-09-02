package com.mishaki.libsearchspinner.controller;

import android.graphics.drawable.Drawable;
import android.widget.TextView;

/**
 * Created by 杜壁奇<br/>
 * on 2020/09/02 09:08
 */
public class SearchSpinnerTextController {
    private final TextView textView;

    public SearchSpinnerTextController(TextView textView) {
        ControllerUtil.checkView(textView,"TextView");
        this.textView = textView;
    }

    public void setText(CharSequence text) {
        textView.setText(text);
    }

    public void setText(int resId) {
        textView.setText(resId);
    }

    public void setTextColor(int color) {
        textView.setTextColor(color);
    }

    public void setTextSize(float sp) {
        textView.setTextSize(sp);
    }

    public void setTextSize(int unit, float size) {
        textView.setTextSize(unit, size);
    }

    public void setTextBackground(int resId) {
        textView.setBackgroundResource(resId);
    }

    public void setTextBackgroundColor(int color) {
        textView.setBackgroundColor(color);
    }

    public void setTextGravity(int gravity) {
        textView.setGravity(gravity);
    }

    public void setTextPaddingLeft(float paddingLeft) {
        textView.setPadding((int) paddingLeft, textView.getPaddingTop(), textView.getPaddingRight(), textView.getPaddingBottom());
    }

    public void setTextPaddingTop(float paddingTop) {
        textView.setPadding(textView.getPaddingLeft(), (int) paddingTop, textView.getPaddingRight(), textView.getPaddingBottom());
    }

    public void setTextPaddingRight(float paddingRight) {
        textView.setPadding(textView.getPaddingLeft(), textView.getPaddingTop(), (int) paddingRight, textView.getPaddingBottom());
    }

    public void setTextPaddingBottom(float paddingBottom) {
        textView.setPadding(textView.getPaddingLeft(), textView.getPaddingTop(), textView.getPaddingRight(), (int) paddingBottom);
    }

    public void setTextPadding(float paddingLeft, float paddingTop, float paddingRight, float paddingBottom) {
        textView.setPadding((int) paddingLeft, (int) paddingTop, (int) paddingRight, (int) paddingBottom);
    }

    public void setTextBackground(Drawable drawable) {
        textView.setBackground(drawable);
    }
}
