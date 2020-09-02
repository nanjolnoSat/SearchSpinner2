package com.mishaki.libsearchspinner.controller;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by 杜壁奇<br/>
 * on 2020/09/02 09:19
 */
public class SearchSpinnerArrowController {
    private final ImageView imageView;

    public SearchSpinnerArrowController(ImageView imageView) {
        ControllerUtil.checkView(imageView,"ArrowView");
        this.imageView = imageView;
    }

    public void showArrow() {
        imageView.setVisibility(View.VISIBLE);
    }

    public void hideArrow() {
        imageView.setVisibility(View.GONE);
    }

    public void setArrowColor(int color) {
        imageView.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
    }

    public void setArrowImage(Drawable drawable) {
        imageView.setImageDrawable(drawable);
    }

    public void setArrowImage(int resId) {
        imageView.setImageResource(resId);
    }

    public void setArrowWidth(float width) {
        ViewGroup.LayoutParams param = imageView.getLayoutParams();
        param.width = (int) width;
        imageView.setLayoutParams(param);
    }

    public void setArrowHeight(float height) {
        ViewGroup.LayoutParams param = imageView.getLayoutParams();
        param.height = (int) height;
        imageView.setLayoutParams(param);
    }

    public void setArrowSize(float width, float height) {
        ViewGroup.LayoutParams param = imageView.getLayoutParams();
        param.width = (int) width;
        param.height = (int) height;
        imageView.setLayoutParams(param);
    }
}
