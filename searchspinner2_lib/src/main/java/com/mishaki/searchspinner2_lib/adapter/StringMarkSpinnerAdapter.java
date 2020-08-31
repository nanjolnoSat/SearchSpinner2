package com.mishaki.searchspinner2_lib.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mishaki.searchspinner2_lib.utils.SearchSpinnerConstant;
import com.mishaki.searchspinner2_lib.view.BaseSpinnerAdapter;

/**
 * Created by 杜壁奇<br/>
 * on 2020/08/31 10:11
 */
public class StringMarkSpinnerAdapter extends BaseSpinnerAdapter<String> {
    private int backgroundColor = SearchSpinnerConstant.Color.WHITE;
    private int textColor = SearchSpinnerConstant.Color.BLACK;
    private float textSize = SearchSpinnerConstant.Dimens.DEFAULT_TEXT_SIZE;
    private int textGravity = Gravity.CENTER_VERTICAL;
    private int leftPadding = 4;
    private int topPadding = 0;
    private int rightPadding = 0;
    private int bottomPadding = 0;

    private int matchedColor = SearchSpinnerConstant.Color.RED;

    @NonNull
    @Override
    protected View getNormalView(int position, @Nullable View convertView, ViewGroup parent) {
        final View view;
        if (convertView == null) {
            view = newTextView(parent.getContext());
        } else {
            view = convertView;
        }
        TextView textView = (TextView) view;
        initTextView(textView);
        textView.setText(getItem(position));
        return view;
    }

    @NonNull
    @Override
    protected View getSearchView(int position, @Nullable View convertView, ViewGroup parent, @NonNull String searchText) {
        final View view;
        if (convertView == null) {
            view = newTextView(parent.getContext());
        } else {
            view = convertView;
        }
        TextView textView = (TextView) view;
        initTextView(textView);
        int index = getItem(position).indexOf(searchText);
        if (index != -1) {
            SpannableString ss = new SpannableString(getItem(position));
            ForegroundColorSpan color = new ForegroundColorSpan(matchedColor);
            ss.setSpan(color, index, index + searchText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            textView.setText(ss);
        } else {
            textView.setText(getItem(position));
        }
        return view;
    }

    private TextView newTextView(Context context) {
        return new TextView(context);
    }

    private void initTextView(TextView textView) {
        textView.setBackgroundColor(backgroundColor);
        textView.setTextColor(textColor);
        textView.setTextSize(textSize);
        textView.setGravity(textGravity);
        textView.setPadding(leftPadding, topPadding, rightPadding, bottomPadding);
    }

    public void setBackgroundColor(int normalBackgroundColor) {
        if (this.backgroundColor == normalBackgroundColor) {
            return;
        }
        this.backgroundColor = normalBackgroundColor;
        notifyDataSetChanged();
    }

    public void setTextColor(int normalTextColor) {
        if (this.textColor == normalTextColor) {
            return;
        }
        this.textColor = normalTextColor;
        notifyDataSetChanged();
    }

    public void setTextSize(float normalTextSize) {
        if (this.textSize == normalTextSize) {
            return;
        }
        this.textSize = normalTextSize;
        notifyDataSetChanged();
    }

    public void setTextGravity(int normalTextGravity) {
        if (this.textGravity == normalTextGravity) {
            return;
        }
        this.textGravity = normalTextGravity;
        notifyDataSetChanged();
    }

    public void setLeftPadding(int normalStartPadding) {
        if (this.leftPadding == normalStartPadding) {
            return;
        }
        this.leftPadding = normalStartPadding;
        notifyDataSetChanged();
    }

    public void setTopPadding(int normalTopPadding) {
        if (this.topPadding == normalTopPadding) {
            return;
        }
        this.topPadding = normalTopPadding;
        notifyDataSetChanged();
    }

    public void setRightPadding(int normalEndPadding) {
        if (this.rightPadding == normalEndPadding) {
            return;
        }
        this.rightPadding = normalEndPadding;
        notifyDataSetChanged();
    }

    public void seBottomPadding(int normalBottomPadding) {
        if (this.bottomPadding == normalBottomPadding) {
            return;
        }
        this.bottomPadding = normalBottomPadding;
        notifyDataSetChanged();
    }

    public void setMatchedColor(int matchedColor) {
        if (this.matchedColor == matchedColor) {
            return;
        }
        this.matchedColor = matchedColor;
        notifyDataSetChanged();
    }
}
