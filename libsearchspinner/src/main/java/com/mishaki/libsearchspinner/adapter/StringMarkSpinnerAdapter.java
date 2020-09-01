package com.mishaki.libsearchspinner.adapter;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mishaki.libsearchspinner.utils.SearchSpinnerConstant;
import com.mishaki.libsearchspinner.view.BaseSpinnerAdapter;

import androidx.annotation.NonNull;

/**
 * Created by 杜壁奇<br/>
 * on 2020/08/31 10:11
 */
public class StringMarkSpinnerAdapter extends BaseSpinnerAdapter<String, SimpleViewHolder> {
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
    protected SimpleViewHolder onCreateNormalViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView textView = new TextView(parent.getContext());
        initTextView(textView);
        return new SimpleViewHolder(textView);
    }

    @NonNull
    @Override
    protected SimpleViewHolder onCreateSearchViewHolder(@NonNull ViewGroup parent, int viewType, @NonNull String searchContent) {
        return onCreateNormalViewHolder(parent, viewType);
    }

    @Override
    protected void onBindNormalViewHolder(@NonNull SimpleViewHolder holder, int position) {
        TextView textView = (TextView) holder.itemView;
        textView.setText(getItem(position));
    }

    @Override
    protected void onBindSearchViewHolder(@NonNull SimpleViewHolder holder, int position, @NonNull String searchContent) {
        TextView textView = (TextView) holder.itemView;
        int index = getItem(position).indexOf(searchContent);
        if (index != -1) {
            SpannableString ss = new SpannableString(getItem(position));
            ForegroundColorSpan color = new ForegroundColorSpan(matchedColor);
            ss.setSpan(color, index, index + searchContent.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            textView.setText(ss);
        } else {
            textView.setText(getItem(position));
        }
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
