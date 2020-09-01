package com.mishaki.libsearchspinner.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

/**
 * Created by 杜壁奇<br/>
 * on 2020/08/31 20:31
 */
public class StringSearchSpinner extends SearchSpinner<String> {
    public StringSearchSpinner(Context context) {
        this(context, null, 0);
    }

    public StringSearchSpinner(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StringSearchSpinner(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFilterModel((searchContent, s) -> s.contains(searchContent));
        setContentModel(s -> s);
    }
}