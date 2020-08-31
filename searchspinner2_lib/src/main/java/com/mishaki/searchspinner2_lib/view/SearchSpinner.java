package com.mishaki.searchspinner2_lib.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mishaki.searchspinner2_lib.R;
import com.mishaki.searchspinner2_lib.model.SpinnerContentModel;
import com.mishaki.searchspinner2_lib.model.SpinnerFilterModel;
import com.mishaki.searchspinner2_lib.utils.SearchSpinnerConstant;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * Created by 杜壁奇
 * on 2020/08/30 21:41
 */
public class SearchSpinner<T> extends LinearLayout implements View.OnClickListener {
    //子类不能设置的，只能由该类初始化
    @NonNull
    private TextView textView;
    @NonNull
    private ImageView imageView;

    //可以交给子类初始化的，提供set方法
    private int popupRootViewLayoutId = View.NO_ID;
    private int popupSearchId = View.NO_ID;
    private int popupDataId = View.NO_ID;
    private int popupTipId = View.NO_ID;
    @NonNull
    private PopupWindow popupWindow;
    @NonNull
    private EditText popupSearchView;
    @NonNull
    private ListView popupDataView;
    @NonNull
    private TextView popupTipView;

    //内部使用的
    private final int screenHeight;
    private final int statusBarHeight;
    private List<T> list = new ArrayList<>();
    private boolean isSearch = false;
    private final List<T> searchList = new ArrayList<>();

    //外部设置上下弹出动画
    private int topPopupAnim = View.NO_ID;
    private int bottomPopupAnim = View.NO_ID;

    private SpinnerFilterModel<T> filterModel;
    private SpinnerContentModel<T> contentModel;

    private BaseSpinnerAdapter<T> adapter = null;

    private float adapterItemHeight = SearchSpinnerConstant.Dimens.FLOAT_DEFAULT;
    private float searchViewHeight = SearchSpinnerConstant.Dimens.FLOAT_DEFAULT;
    private float emptyTipViewHeight = SearchSpinnerConstant.Dimens.FLOAT_DEFAULT;

    //内部使用
    private float actualAdapterItemHeight = 0f;
    private float actualSearchViewHeight = 0f;
    private float actualEmptyTipViewHeight = 0f;

    private float elevationSize = SearchSpinnerConstant.Dimens.DEFAULT_ELEVATION_SIZE;

    private int selectIndex = SearchSpinnerConstant.Dimens.INT_DEFAULT;
    private int searchSelectIndex = SearchSpinnerConstant.Dimens.INT_DEFAULT;
    private String searchContent = SearchSpinnerConstant.Other.STRING_EMPTY_VALUE;

    private OnSelectedListener<T> onSelectedListener = null;

    public SearchSpinner(Context context) {
        this(context, null, 0);
    }

    public SearchSpinner(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchSpinner(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        String defaultText = SearchSpinnerConstant.Other.STRING_EMPTY_VALUE;
        int textColor = SearchSpinnerConstant.Color.BLACK;
        float textSize = SearchSpinnerConstant.Dimens.FLOAT_DEFAULT;
        boolean showArrow = true;
        boolean changeArrowColor = false;
        int arrowColor = SearchSpinnerConstant.Color.GRAY;
        Drawable arrowImage = null;
        float arrowWidth = SearchSpinnerConstant.Dimens.FLOAT_DEFAULT;
        float arrowHeight = SearchSpinnerConstant.Dimens.FLOAT_DEFAULT;
        String tipText = SearchSpinnerConstant.Other.STRING_EMPTY_VALUE;
        int tipTextColor = SearchSpinnerConstant.Color.BLACK;
        float tipTextSize = SearchSpinnerConstant.Dimens.FLOAT_DEFAULT;
        float tipViewHeight = SearchSpinnerConstant.Dimens.FLOAT_DEFAULT;
        float searchTextSize = SearchSpinnerConstant.Dimens.FLOAT_DEFAULT;
        int searchTextColor = SearchSpinnerConstant.Color.BLACK;
        String searchHint = SearchSpinnerConstant.Other.STRING_EMPTY_VALUE;
        int searchHintColor = SearchSpinnerConstant.Color.TEXT_HINT;
        float searchViewHeight = SearchSpinnerConstant.Dimens.FLOAT_DEFAULT;
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SearchSpinner);

            adapterItemHeight = array.getDimension(R.styleable.SearchSpinner_ss_adapterItemHeight, SearchSpinnerConstant.Dimens.FLOAT_DEFAULT);
            elevationSize = array.getDimension(R.styleable.SearchSpinner_ss_elevationSize, SearchSpinnerConstant.Dimens.DEFAULT_ELEVATION_SIZE);
            defaultText = array.getString(R.styleable.SearchSpinner_ss_defaultText);
            textColor = array.getColor(R.styleable.SearchSpinner_ss_textColor, SearchSpinnerConstant.Color.BLACK);
            textSize = array.getDimension(R.styleable.SearchSpinner_ss_textSize, SearchSpinnerConstant.Dimens.FLOAT_DEFAULT);
            showArrow = array.getBoolean(R.styleable.SearchSpinner_ss_showArrow, true);
            changeArrowColor = array.getBoolean(R.styleable.SearchSpinner_ss_changeArrowColor, false);
            arrowColor = array.getColor(R.styleable.SearchSpinner_ss_arrowColor, SearchSpinnerConstant.Color.GRAY);
            arrowImage = array.getDrawable(R.styleable.SearchSpinner_ss_arrowImage);
            arrowWidth = array.getDimension(R.styleable.SearchSpinner_ss_arrowWidth, SearchSpinnerConstant.Dimens.FLOAT_DEFAULT);
            arrowHeight = array.getDimension(R.styleable.SearchSpinner_ss_arrowHeight, -SearchSpinnerConstant.Dimens.FLOAT_DEFAULT);
            tipText = array.getString(R.styleable.SearchSpinner_ss_tipText);
            tipTextColor = array.getColor(R.styleable.SearchSpinner_ss_tipTextColor, SearchSpinnerConstant.Color.BLACK);
            tipTextSize = array.getDimension(R.styleable.SearchSpinner_ss_tipTextSize, SearchSpinnerConstant.Dimens.FLOAT_DEFAULT);
            tipViewHeight = array.getDimension(R.styleable.SearchSpinner_ss_tipViewHeight, SearchSpinnerConstant.Dimens.FLOAT_DEFAULT);
            searchTextSize = array.getDimension(R.styleable.SearchSpinner_ss_searchTextSize, SearchSpinnerConstant.Dimens.FLOAT_DEFAULT);
            searchTextColor = array.getColor(R.styleable.SearchSpinner_ss_searchTextColor, SearchSpinnerConstant.Color.BLACK);
            searchHint = array.getString(R.styleable.SearchSpinner_ss_searchHint);
            searchHintColor = array.getColor(R.styleable.SearchSpinner_ss_searchHintColor, SearchSpinnerConstant.Color.TEXT_HINT);
            searchViewHeight = array.getDimension(R.styleable.SearchSpinner_ss_tipViewHeight, SearchSpinnerConstant.Dimens.FLOAT_DEFAULT);

            array.recycle();
        }

        super.setOrientation(LinearLayout.HORIZONTAL);

        initRootView(defaultText, textColor, textSize, showArrow, changeArrowColor, arrowColor, arrowImage, arrowWidth, arrowHeight);
        initResValue();
        initPopupWindow();
        initTipView(tipText, tipTextColor, tipTextSize, tipViewHeight);
        initSearchView(searchTextSize, searchTextColor, searchHint, searchHintColor, searchViewHeight);

        initPopupSearchViewTextWatcher();
        initPopupDataItemClick();

        statusBarHeight = getStatusBarHeight();
        screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        popupDataView.setAdapter(adapter);
        popupWindow.setElevation(elevationSize);
        super.setOnClickListener(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        popupWindow.setWidth(w);
        if (searchViewHeight == SearchSpinnerConstant.Dimens.FLOAT_DEFAULT) {
            setActualSearchViewHeight(h);
        } else {
            setActualSearchViewHeight(searchViewHeight);
        }
        if (adapterItemHeight == SearchSpinnerConstant.Dimens.FLOAT_DEFAULT) {
            setActualAdapterItemHeight(h);
        } else {
            setActualAdapterItemHeight(adapterItemHeight);
        }
        if (emptyTipViewHeight == SearchSpinnerConstant.Dimens.FLOAT_DEFAULT) {
            setActualEmptyTipViewHeight(h);
        } else {
            setActualEmptyTipViewHeight(emptyTipViewHeight);
        }
    }

    @Override
    public void addView(View child) {
        throw new IllegalStateException("Can not addView");
    }

    @Override
    public void addView(View child, int index) {
        addView(child);
    }

    @Override
    public void addView(View child, int width, int height) {
        addView(child);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        addView(child);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        addView(child);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params) {
        addView(child);
        return false;
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        addView(child);
        return false;
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        throw new IllegalStateException("Can not setOnClickListener");
    }

    @Override
    public void setOrientation(int orientation) {
        throw new IllegalStateException("Can not setOrientation");
    }

    private int y = 0;
    private boolean isTop = false;

    @Override
    public void onClick(View v) {
        if (adapter == null) {
            return;
        }
        int[] point = new int[2];
        getLocationOnScreen(point);
        y = point[1];

        animateArrow(false);
        removeRule(popupDataView);
        removeRule(popupSearchView);
        removeRule(popupTipView);
        final PositionInfo positionInfo;
        if (isSearch) {
            positionInfo = getPositionInfo(searchList);
        } else {
            positionInfo = getPositionInfo(list);
        }
        popupWindow.setHeight((int) positionInfo.height);
        //在弹出的时候就记录,弹出的位置
        isTop = positionInfo.isTop;
        if (positionInfo.isTop) {
            if (topPopupAnim != View.NO_ID) {
                popupWindow.setAnimationStyle(topPopupAnim);
            }
            RelativeLayout.LayoutParams param1 = (RelativeLayout.LayoutParams) popupSearchView.getLayoutParams();
            param1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

            RelativeLayout.LayoutParams param2 = (RelativeLayout.LayoutParams) popupDataView.getLayoutParams();
            param2.addRule(RelativeLayout.ABOVE, popupSearchId);

            RelativeLayout.LayoutParams param3 = (RelativeLayout.LayoutParams) popupTipView.getLayoutParams();
            param3.addRule(RelativeLayout.ABOVE, popupSearchId);

            popupWindow.showAsDropDown(this, 0, 0, Gravity.BOTTOM | Gravity.START);
        } else {
            if (bottomPopupAnim != View.NO_ID) {
                popupWindow.setAnimationStyle(bottomPopupAnim);
            }
            RelativeLayout.LayoutParams param1 = (RelativeLayout.LayoutParams) popupDataView.getLayoutParams();
            param1.addRule(RelativeLayout.BELOW, popupSearchId);

            RelativeLayout.LayoutParams param2 = (RelativeLayout.LayoutParams) popupTipView.getLayoutParams();
            param2.addRule(RelativeLayout.BELOW, popupSearchId);

            popupWindow.showAsDropDown(this);
        }
    }

    //private method start===========================
    private int dip(float value) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getContext().getResources().getDisplayMetrics()) + 0.5f);
    }

    private boolean stringIsNotEmpty(CharSequence value) {
        return value != null && value.length() != 0;
    }

    private boolean isEquals(Object obj1, Object obj2) {
        return obj1 == obj2 || obj1.equals(obj2);
    }

    private int getStatusBarHeight() {
        int resourceId = Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return Resources.getSystem().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    private void initPopupSearchViewTextWatcher() {
        popupSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                isSearch = stringIsNotEmpty(s);
                searchContent = s.toString();
                final int popupWindowHeight;
                if (isSearch) {
                    searchList.clear();
                    if (filterModel == null) {
                        throw new NullPointerException("SpinnerFilterModel is not be null");
                    }
                    searchList.addAll(ListUtil.filter(list, (it) -> filterModel.filterModel(searchContent, it)));
                    if (searchList.contains(list.get(selectIndex))) {
                        T content = list.get(selectIndex);
                        int count1 = -1;
                        for (int i = 0; i < list.size(); i++) {
                            if (isEquals(list.get(i), content)) {
                                count1++;
                            }
                            if (i == selectIndex) {
                                break;
                            }
                        }
                        int count2 = -1;
                        for (int i = 0; i < searchList.size(); i++) {
                            if (isEquals(searchList.get(i), content)) {
                                count2++;
                            }
                            if (count1 == count2) {
                                searchSelectIndex = i;
                                break;
                            }
                        }
                    } else {
                        searchSelectIndex = SearchSpinnerConstant.Other.NO_INDEX;
                        if (adapter != null) {
                            adapter.setSelectedPosition(SearchSpinnerConstant.Other.NO_INDEX);
                        }
                    }
                    if (searchList.isEmpty()) {
                        popupDataView.setVisibility(View.GONE);
                        popupTipView.setVisibility(View.VISIBLE);
                        popupWindowHeight = (int) (actualSearchViewHeight + dip(2f) + actualEmptyTipViewHeight);
                    } else {
                        popupTipView.setVisibility(View.GONE);
                        popupDataView.setVisibility(View.VISIBLE);
                        if (adapter != null) {
                            adapter.setItemInfo(searchContent, searchList);
                        }
                        popupWindowHeight = (int) getPopupSearchHeight(searchList);
                        if (searchSelectIndex != SearchSpinnerConstant.Other.NO_INDEX) {
                            popupDataView.setSelection(searchSelectIndex);
                            if (adapter != null) {
                                adapter.setSelectedPosition(searchSelectIndex);
                            }
                        }
                    }
                } else {
                    popupTipView.setVisibility(View.GONE);
                    popupDataView.setVisibility(View.VISIBLE);
                    if (adapter != null) {
                        adapter.setItemInfo(searchContent, list);
                    }
                    if (selectIndex != SearchSpinnerConstant.Other.NO_INDEX) {
                        popupDataView.setSelection(selectIndex);
                        if (adapter != null) {
                            adapter.setSelectedPosition(selectIndex);
                        }
                    }
                    popupWindowHeight = (int) getPopupSearchHeight(list);
                }
                if (isTop) {
                    popupWindow.update(getLeft(), y - popupWindowHeight, -1, popupWindowHeight);
                } else {
                    popupWindow.update(-1, popupWindowHeight);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
    }

    private void initPopupDataItemClick() {
        popupDataView.setOnItemClickListener((parent, view, position, id) -> {
            if (isSearch) {
                final String text;
                if (contentModel == null) {
                    text = searchList.get(position).toString();
                } else {
                    text = contentModel.getContent(searchList.get(position));
                }
                textView.setText(text);
                searchSelectIndex = position;
                if (adapter != null) {
                    adapter.setSelectedPosition(searchSelectIndex);
                }
                if (filterModel == null) {
                    throw new NullPointerException("SpinnerFilterModel is not be null");
                }
                int index = -1;
                for (int i = 0; i < list.size(); i++) {
                    if (filterModel.filterModel(searchContent, list.get(i))) {
                        index++;
                    }
                    if (index == searchSelectIndex) {
                        selectIndex = i;
                        break;
                    }
                }
                if (onSelectedListener != null) {
                    onSelectedListener.onSelected(this, selectIndex);
                }
            } else {
                searchSelectIndex = -1;
                final String text;
                if (contentModel == null) {
                    text = list.get(position).toString();
                } else {
                    text = contentModel.getContent(list.get(position));
                }
                textView.setText(text);
                selectIndex = position;
                if (adapter != null) {
                    adapter.setSelectedPosition(selectIndex);
                }
                if (onSelectedListener != null) {
                    onSelectedListener.onSelected(this, selectIndex);
                }
            }
            popupWindow.dismiss();
        });
    }

    private void removeRule(View view) {
        if (view.getParent() instanceof RelativeLayout) {
            RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) view.getLayoutParams();
            param.removeRule(RelativeLayout.BELOW);
            param.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            param.removeRule(RelativeLayout.ABOVE);
        }
    }
    //private method end===========================

    //protected method start===========================
    protected final void animateArrow(boolean isRelease) {
        if (isRelease) {
            imageView.animate().rotation(0f).start();
        } else {
            imageView.animate().rotation(180f).start();
        }
    }

    protected PositionInfo getPositionInfo(List<T> list) {
        float popupMaxHeight = getPopupMaxHeight(list);
        int bottomHeight = screenHeight - y - getHeight();
        //如果下面够显示,显示在下面
        if (bottomHeight > popupMaxHeight + elevationSize) {
            return new PositionInfo(false, popupMaxHeight);
        }
        //如果上面够显示,显示在上面
        int topHeight = y - statusBarHeight;
        if (topHeight > popupMaxHeight + elevationSize) {
            return new PositionInfo(true, popupMaxHeight);
        }
        //如果都不够
        boolean isTop = topHeight > bottomHeight;
        final float popupHeight;
        //如果上面大,上面的高度-阴影高度
        if (isTop) {
            popupHeight = topHeight - elevationSize;
        } else {
            //如果下面大,下面的高度-阴影高度
            popupHeight = bottomHeight - elevationSize;
        }
        return new PositionInfo(isTop, popupHeight);
    }
    //protected method end===========================

    //protected init start===========================
    protected void initRootView(String defaultText, int textColor, float textSize, boolean showArrow, boolean changeArrowColor, int arrowColor, Drawable arrowImage, float arrowWidth, float arrowHeight) {
        textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER_VERTICAL);
        //最大行数必须只能为1行
        textView.setMaxLines(1);
        if (textSize == SearchSpinnerConstant.Dimens.FLOAT_DEFAULT) {
            textView.setTextSize(SearchSpinnerConstant.Dimens.DEFAULT_TEXT_SIZE);
        } else {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }
        textView.setTextColor(textColor);
        //设置结果 ...
        textView.setEllipsize(TextUtils.TruncateAt.END);
        //默认的文本,没有设置则为空
        textView.setText(defaultText);
        LinearLayout.LayoutParams param1 = generateDefaultLayoutParams();
        param1.gravity = Gravity.CENTER_VERTICAL;
        //ImageView用剩下的都给TextView用
        param1.weight = 1f;
        textView.setLayoutParams(param1);
        super.addView(textView);

        imageView = new ImageView(getContext());
        if (arrowImage != null) {
            imageView.setImageDrawable(arrowImage);
        } else {
            imageView.setImageResource(R.drawable.search_down);
        }
        //如果不喜欢三角形的颜色,可以在layout文件通过attr修改
        if (changeArrowColor) {
            imageView.setColorFilter(new PorterDuffColorFilter(arrowColor, PorterDuff.Mode.SRC_IN));
        }
        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(0, 0);
        if (arrowWidth != SearchSpinnerConstant.Dimens.FLOAT_DEFAULT && arrowHeight != SearchSpinnerConstant.Dimens.FLOAT_DEFAULT) {
            param2.width = (int) arrowWidth;
            param2.height = (int) arrowHeight;
        } else {
            if (arrowWidth != SearchSpinnerConstant.Dimens.FLOAT_DEFAULT) {
                param2.width = (int) arrowWidth;
                param2.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            } else if (arrowHeight != SearchSpinnerConstant.Dimens.FLOAT_DEFAULT) {
                param2.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                param2.height = (int) arrowHeight;
            } else {
                param2.width = dip(SearchSpinnerConstant.Dimens.DEFAULT_ARROW_SIZE);
                param2.height = dip(SearchSpinnerConstant.Dimens.DEFAULT_ARROW_SIZE);
            }
        }
        param2.gravity = Gravity.CENTER_VERTICAL;
        param2.setMarginStart(dip(SearchSpinnerConstant.Dimens.ARROW_MARGIN_HORIZONTAL));
        param2.setMarginEnd(dip(SearchSpinnerConstant.Dimens.ARROW_MARGIN_HORIZONTAL));
        imageView.setLayoutParams(param2);
        imageView.setAdjustViewBounds(true);
        super.addView(imageView);
        if (!showArrow) {
            imageView.setVisibility(View.GONE);
        }
    }

    protected void initResValue() {
        setPopupRootViewLayoutId(R.layout.popup_search_spinner);
        setPopupSearchId(R.id.popup_search_spinner_et);
        setPopupDataId(R.id.popup_search_spinner_lv);
        setPopupTipId(R.id.popup_search_spinner_tv);
    }

    protected void initPopupWindow() {
        popupWindow = new PopupWindow(getContext());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        //设置白色背景
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.popup_search_spinner));
        try {
            //禁止输入法影响屏幕的高度,否则会导致PopupWindow显示的位置不准确
            popupWindow.setInputMethodMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            ((Activity) getContext()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        } catch (Exception ignore) {
        }
        View popupRootView = LayoutInflater.from(getContext()).inflate(popupRootViewLayoutId, null, false);
        popupSearchView = popupRootView.findViewById(popupSearchId);
        popupDataView = popupRootView.findViewById(popupDataId);
        popupTipView = popupRootView.findViewById(popupTipId);
        popupWindow.setContentView(popupRootView);
        popupWindow.setOnDismissListener(() -> {
            //当PopupWindow关闭的时候,让三角形旋转回来
            animateArrow(true);
        });
    }

    protected void initTipView(String tipText, int tipTextColor, float tipTextSize, float tipViewHeight) {
        popupTipView.setGravity(Gravity.CENTER);
        if (stringIsNotEmpty(tipText)) {
            popupTipView.setText(tipText);
        }
        popupTipView.setTextColor(tipTextColor);
        if (tipTextSize != SearchSpinnerConstant.Dimens.FLOAT_DEFAULT) {
            popupTipView.setTextSize(SearchSpinnerConstant.Dimens.DEFAULT_TEXT_SIZE);
        } else {
            popupTipView.setTextSize(TypedValue.COMPLEX_UNIT_PX, tipTextSize);
        }
        if (tipViewHeight != SearchSpinnerConstant.Dimens.FLOAT_DEFAULT) {
            popupTipView.setHeight((int) tipViewHeight);
        }
    }

    protected void initSearchView(float searchTextSize, int searchTextColor, String searchHint, int searchHintColor, float searchViewHeight) {
        popupSearchView.setGravity(Gravity.CENTER_VERTICAL);
        if (searchTextSize != SearchSpinnerConstant.Dimens.FLOAT_DEFAULT) {
            popupSearchView.setTextSize(SearchSpinnerConstant.Dimens.DEFAULT_TEXT_SIZE);
        } else {
            popupSearchView.setTextSize(TypedValue.COMPLEX_UNIT_PX, searchTextSize);
        }
        popupSearchView.setTextColor(searchTextColor);
        if (stringIsNotEmpty(searchHint)) {
            popupSearchView.setHint(searchHint);
        }
        popupSearchView.setHintTextColor(searchHintColor);
        if (searchViewHeight != SearchSpinnerConstant.Dimens.FLOAT_DEFAULT) {
            popupSearchView.setHeight((int) searchViewHeight);
        }
    }
    //protected init end===========================

    //protected get start===========================
    @NonNull
    protected TextView getTextView() {
        return textView;
    }

    @NonNull
    protected ImageView getImageView() {
        return imageView;
    }

    /**
     * 获取PopupWindow在search状态下的高度,不是使用searchList这个变量计算高度
     */
    protected float getPopupSearchHeight(List<T> list) {
        final float height;
        float maxHeight = getPopupMaxHeight(list);
        if (isTop) {
            if (maxHeight < y - statusBarHeight) {
                height = maxHeight;
            } else {
                height = y - statusBarHeight - elevationSize;
            }
        } else {
            if (screenHeight - y > maxHeight) {
                height = maxHeight;
            } else {
                height = screenHeight - y - getHeight() - elevationSize;
            }
        }
        return height;
    }

    protected float getPopupMaxHeight(List<T> list) {
        return actualSearchViewHeight + list.size() * actualAdapterItemHeight + dip(2f);
    }
    //protected get end===========================

    //protected set start===========================
    protected void setPopupRootViewLayoutId(int popupRootViewLayoutId) {
        this.popupRootViewLayoutId = popupRootViewLayoutId;
    }

    protected void setPopupSearchId(int popupSearchId) {
        this.popupSearchId = popupSearchId;
    }

    protected void setPopupDataId(int popupDataId) {
        this.popupDataId = popupDataId;
    }

    protected void setPopupTipId(int popupTipId) {
        this.popupTipId = popupTipId;
    }

    protected void setPopupWindow(@NonNull PopupWindow popupWindow) {
        this.popupWindow = popupWindow;
    }

    protected void setPopupSearchView(@NonNull EditText popupSearchView) {
        this.popupSearchView = popupSearchView;
    }

    protected void setPopupDataView(@NonNull ListView popupDataView) {
        this.popupDataView = popupDataView;
    }

    protected void setPopupTipView(@NonNull TextView popupTipView) {
        this.popupTipView = popupTipView;
    }
    //protected set end===========================

    //public set start===========================
    public void setTopPopupAnim(int topPopupAnim) {
        this.topPopupAnim = topPopupAnim;
    }

    public void setBottomPopupAnim(int bottomPopupAnim) {
        this.bottomPopupAnim = bottomPopupAnim;
    }

    public void setFilterModel(SpinnerFilterModel<T> filterModel) {
        this.filterModel = filterModel;
    }

    public void setContentModel(SpinnerContentModel<T> contentModel) {
        this.contentModel = contentModel;
    }

    public void setAdapter(BaseSpinnerAdapter<T> adapter) {
        if (adapter == null) {
            return;
        }
        this.adapter = adapter;
        adapter.setList(list);
        adapter.setSelectedPosition(selectIndex);
        adapter.setItemHeight(actualAdapterItemHeight);
        popupDataView.setAdapter(adapter);
    }

    public void setAdapterItemHeight(float adapterItemHeight) {
        this.adapterItemHeight = adapterItemHeight;
        if (adapterItemHeight > 0) {
            setActualAdapterItemHeight(adapterItemHeight);
        }
    }

    public void setSearchViewHeight(float searchViewHeight) {
        this.searchViewHeight = searchViewHeight;
        if (searchViewHeight > 0) {
            setActualSearchViewHeight(searchViewHeight);
        }
    }

    public void setEmptyTipViewHeight(float emptyTipViewHeight) {
        this.emptyTipViewHeight = emptyTipViewHeight;
        if (emptyTipViewHeight > 0) {
            setActualEmptyTipViewHeight(emptyTipViewHeight);
        }
    }

    public void setActualAdapterItemHeight(float actualAdapterItemHeight) {
        this.actualAdapterItemHeight = actualAdapterItemHeight;
        if (adapter != null) {
            adapter.setItemHeight(actualAdapterItemHeight);
        }
    }

    public void setActualSearchViewHeight(float actualSearchViewHeight) {
        this.actualSearchViewHeight = actualSearchViewHeight;
        popupSearchView.setHeight((int) actualSearchViewHeight);
    }

    public void setActualEmptyTipViewHeight(float actualEmptyTipViewHeight) {
        this.actualEmptyTipViewHeight = actualEmptyTipViewHeight;
        popupTipView.setHeight((int) actualEmptyTipViewHeight);
    }

    public void setAdapter(BaseSpinnerAdapter<T> adapter, List<T> list) {
        if (!list.isEmpty()) {
            selectIndex = 0;
            final String text;
            if (contentModel == null) {
                text = list.get(0).toString();
            } else {
                text = contentModel.getContent(list.get(0));
            }
            textView.setText(text);
        }
        this.list = list;
        setAdapter(adapter);
    }

    public void setOnSelectedListener(OnSelectedListener<T> onSelectedListener) {
        this.onSelectedListener = onSelectedListener;
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
        popupSearchView.setBackgroundDrawable(drawable);
    }

    public void setSearchViewBackgroundColor(int color) {
        popupSearchView.setBackgroundColor(color);
    }
    //public set end===========================

    public interface OnSelectedListener<T> {
        void onSelected(SearchSpinner<T> spinner, int position);
    }

    protected static class PositionInfo {
        final boolean isTop;
        final float height;

        public PositionInfo(boolean isTop, float height) {
            this.isTop = isTop;
            this.height = height;
        }
    }
}