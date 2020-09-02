package com.mishaki.libsearchspinner.view;

import android.app.Activity;
import android.content.Context;
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
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mishaki.libsearchspinner.R;
import com.mishaki.libsearchspinner.controller.SearchSpinnerArrowController;
import com.mishaki.libsearchspinner.controller.SearchSpinnerSearchController;
import com.mishaki.libsearchspinner.controller.SearchSpinnerTextController;
import com.mishaki.libsearchspinner.controller.SearchSpinnerTipController;
import com.mishaki.libsearchspinner.model.SpinnerContentModel;
import com.mishaki.libsearchspinner.model.SpinnerFilterModel;
import com.mishaki.libsearchspinner.utils.SearchSpinnerConstant;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by 杜壁奇<br/>
 * on 2020/08/30 21:41
 */
public class SearchSpinner<T> extends LinearLayout implements View.OnClickListener {
    //子类不能设置的，只能由该类初始化
    @NonNull
    private TextView textView;
    @NonNull
    private SearchSpinnerTextController textController;
    @NonNull
    private ImageView imageView;
    @NonNull
    private SearchSpinnerArrowController arrowController;

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
    private SearchSpinnerSearchController searchController;
    @NonNull
    private RecyclerView popupDataView;
    @NonNull
    private TextView popupTipView;
    @NonNull
    private SearchSpinnerTipController tipController;

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

    //这里没办法限制泛型，要限制的话那还要限制ViewHolder的泛型
    //对于SearchSpinner来说，由它来限制ViewHolder的类型看起来怪怪的，这本来就不应该由它来做
    private BaseSpinnerAdapter adapter = null;
    private BaseSpinnerAdapter.OnItemClickListener onItemClickListener;

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
    private String searchContent = SearchSpinnerConstant.StringValues.EMPTY_VALUE;

    private OnSelectedListener onSelectedListener = null;

    //PositionInfo并不存在高并发使用场景，所以没必要使用资源池，只要有一个公共对象即可
    private final PositionInfo positionInfo = new PositionInfo();
    //独立成成员变量只是为了解决频繁new的问题
    private final ArrayList<View> otherChildList = new ArrayList<>();

    public SearchSpinner(Context context) {
        this(context, null, 0);
    }

    public SearchSpinner(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchSpinner(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        String defaultText = SearchSpinnerConstant.StringValues.EMPTY_VALUE;
        int textColor = SearchSpinnerConstant.Color.BLACK;
        float textSize = SearchSpinnerConstant.Dimens.FLOAT_DEFAULT;
        boolean showArrow = true;
        boolean changeArrowColor = false;
        int arrowColor = SearchSpinnerConstant.Color.GRAY;
        Drawable arrowImage = null;
        float arrowWidth = SearchSpinnerConstant.Dimens.FLOAT_DEFAULT;
        float arrowHeight = SearchSpinnerConstant.Dimens.FLOAT_DEFAULT;
        String tipText = SearchSpinnerConstant.StringValues.EMPTY_VALUE;
        int tipTextColor = SearchSpinnerConstant.Color.BLACK;
        float tipTextSize = SearchSpinnerConstant.Dimens.FLOAT_DEFAULT;
        float tipViewHeight = SearchSpinnerConstant.Dimens.FLOAT_DEFAULT;
        float searchTextSize = SearchSpinnerConstant.Dimens.FLOAT_DEFAULT;
        int searchTextColor = SearchSpinnerConstant.Color.BLACK;
        String searchHint = SearchSpinnerConstant.StringValues.EMPTY_VALUE;
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
            arrowHeight = array.getDimension(R.styleable.SearchSpinner_ss_arrowHeight, SearchSpinnerConstant.Dimens.FLOAT_DEFAULT);
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
        textController = new SearchSpinnerTextController(textView);
        arrowController = new SearchSpinnerArrowController(imageView);
        initResId();
        initPopupWindow();
        tipController = new SearchSpinnerTipController(popupTipView);
        searchController = new SearchSpinnerSearchController(popupSearchView);
        initTipView(tipText, tipTextColor, tipTextSize, tipViewHeight);
        initSearchView(searchTextSize, searchTextColor, searchHint, searchHintColor, searchViewHeight);

        initPopupSearchViewTextWatcher();
        popupDataView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        initPopupDataItemClick();

        statusBarHeight = Util.getStatusBarHeight();
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
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int childCount = getChildCount();
        //将多余的View移除
        if (childCount > 2) {
            if (childCount - 2 > 10) {
                otherChildList.ensureCapacity(childCount - 2);
            }
            for (int i = 2; i < childCount; i++) {
                View child = getChildAt(i);
                otherChildList.add(child);
            }
            for (View child : otherChildList) {
                removeView(child);
            }
            otherChildList.clear();
        }
    }

    @Override
    public final void setOrientation(int orientation) {
        super.setOrientation(LinearLayout.HORIZONTAL);
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
        popupWindow.setHeight((int) positionInfo.popupHeight);
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

            popupWindow.showAsDropDown(this, 0, 0, Gravity.START | Gravity.BOTTOM);
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
    private void removeRule(View view) {
        if (view.getParent() instanceof RelativeLayout) {
            RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) view.getLayoutParams();
            param.removeRule(RelativeLayout.BELOW);
            param.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            param.removeRule(RelativeLayout.ABOVE);
        }
    }

    private void initPopupSearchViewTextWatcher() {
        popupSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                isSearch = Util.stringIsNotEmpty(s);
                searchContent = s.toString();
                final int popupWindowHeight;
                if (isSearch) {
                    checkFilterModel();
                    searchList.clear();
                    ListUtil.filterTo(list, searchList, (it) -> filterModel.filterModel(searchContent, it));
                    if (searchList.contains(list.get(selectIndex))) {
                        T content = list.get(selectIndex);
                        int count1 = -1;
                        for (int i = 0; i < list.size(); i++) {
                            if (Util.isEquals(list.get(i), content)) {
                                count1++;
                            }
                            if (i == selectIndex) {
                                break;
                            }
                        }
                        int count2 = -1;
                        for (int i = 0; i < searchList.size(); i++) {
                            if (Util.isEquals(searchList.get(i), content)) {
                                count2++;
                            }
                            if (count1 != -1 && count1 == count2) {
                                searchSelectIndex = i;
                                break;
                            }
                        }
                    } else {
                        searchSelectIndex = SearchSpinnerConstant.Index.NO_INDEX;
                        if (adapter != null) {
                            adapter.setSelectedPosition(SearchSpinnerConstant.Index.NO_INDEX);
                        }
                    }
                    if (searchList.isEmpty()) {
                        popupDataView.setVisibility(View.GONE);
                        popupTipView.setVisibility(View.VISIBLE);
                        popupWindowHeight = (int) (actualSearchViewHeight + Util.dip(getContext(), 2f) + actualEmptyTipViewHeight);
                    } else {
                        popupTipView.setVisibility(View.GONE);
                        popupDataView.setVisibility(View.VISIBLE);
                        if (adapter != null) {
                            adapter.setItemInfo(searchContent, searchList);
                        }
                        popupWindowHeight = (int) getPopupSearchHeight(searchList);
                        if (searchSelectIndex != SearchSpinnerConstant.Index.NO_INDEX) {
                            popupDataView.scrollToPosition(searchSelectIndex);
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
                    if (selectIndex != SearchSpinnerConstant.Index.NO_INDEX) {
                        popupDataView.scrollToPosition(selectIndex);
                        if (adapter != null) {
                            adapter.setSelectedPosition(selectIndex);
                        }
                    }
                    popupWindowHeight = (int) getPopupSearchHeight(list);
                }
                if (isTop) {
                    popupWindow.update(getLeft(), y - popupWindowHeight, SearchSpinnerConstant.Dimens.POPUP_NO_CHANGE, popupWindowHeight);
                } else {
                    popupWindow.update(SearchSpinnerConstant.Dimens.POPUP_NO_CHANGE, popupWindowHeight);
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
        onItemClickListener = position -> {
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
                checkFilterModel();
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
            } else {
                searchSelectIndex = SearchSpinnerConstant.Index.NO_INDEX;
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
            }
            if (onSelectedListener != null) {
                onSelectedListener.onSelected(selectIndex);
            }
            popupWindow.dismiss();
        };
    }

    private void checkFilterModel() {
        if (filterModel == null) {
            throw new NullPointerException("SpinnerFilterModel is not be null");
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
            return positionInfo.setValue(false, popupMaxHeight);
        }
        //如果上面够显示,显示在上面
        int topHeight = y - statusBarHeight;
        if (topHeight > popupMaxHeight + elevationSize) {
            return positionInfo.setValue(true, popupMaxHeight);
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
        return positionInfo.setValue(isTop, popupHeight);
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
        LinearLayout.LayoutParams textViewParam = generateDefaultLayoutParams();
        textViewParam.gravity = Gravity.CENTER_VERTICAL;
        //ImageView用剩下的空间都给TextView用
        textViewParam.weight = 1f;
        textView.setLayoutParams(textViewParam);
        addView(textView);

        imageView = new ImageView(getContext());
        if (arrowImage != null) {
            imageView.setImageDrawable(arrowImage);
        } else {
            imageView.setImageResource(R.drawable.libss_search_down);
        }
        //如果不喜欢三角形的颜色,可以在layout文件通过attr修改
        if (changeArrowColor) {
            imageView.setColorFilter(new PorterDuffColorFilter(arrowColor, PorterDuff.Mode.SRC_IN));
        }
        LinearLayout.LayoutParams imageViewParam = generateDefaultLayoutParams();
        if (arrowWidth != SearchSpinnerConstant.Dimens.FLOAT_DEFAULT && arrowHeight != SearchSpinnerConstant.Dimens.FLOAT_DEFAULT) {
            imageViewParam.width = (int) arrowWidth;
            imageViewParam.height = (int) arrowHeight;
        } else {
            if (arrowWidth != SearchSpinnerConstant.Dimens.FLOAT_DEFAULT) {
                imageViewParam.width = (int) arrowWidth;
                imageViewParam.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            } else if (arrowHeight != SearchSpinnerConstant.Dimens.FLOAT_DEFAULT) {
                imageViewParam.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                imageViewParam.height = (int) arrowHeight;
            } else {
                imageViewParam.width = Util.dip(getContext(), SearchSpinnerConstant.Dimens.DEFAULT_ARROW_SIZE);
                imageViewParam.height = Util.dip(getContext(), SearchSpinnerConstant.Dimens.DEFAULT_ARROW_SIZE);
            }
        }
        imageViewParam.gravity = Gravity.CENTER_VERTICAL;
        imageViewParam.setMarginStart(Util.dip(getContext(), SearchSpinnerConstant.Dimens.ARROW_MARGIN_HORIZONTAL));
        imageViewParam.setMarginEnd(Util.dip(getContext(), SearchSpinnerConstant.Dimens.ARROW_MARGIN_HORIZONTAL));
        imageView.setLayoutParams(imageViewParam);
        imageView.setAdjustViewBounds(true);
        addView(imageView);
        if (!showArrow) {
            imageView.setVisibility(View.GONE);
        }
    }

    protected void initResId() {
        setPopupRootViewLayoutId(R.layout.libss_popup_search_spinner);
        setPopupSearchId(R.id.popup_search_spinner_et);
        setPopupDataId(R.id.popup_search_spinner_lv);
        setPopupTipId(R.id.popup_search_spinner_tv);
    }

    protected void initPopupWindow() {
        popupWindow = new PopupWindow(getContext());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        //设置白色背景
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.libss_popup_search_spinner));
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
        if (Util.stringIsNotEmpty(tipText)) {
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
        if (Util.stringIsNotEmpty(searchHint)) {
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
        return actualSearchViewHeight + list.size() * actualAdapterItemHeight + Util.dip(getContext(), 2f);
    }
    //protected get end===========================

    //protected set start===========================
    protected final void setPopupRootViewLayoutId(int popupRootViewLayoutId) {
        this.popupRootViewLayoutId = popupRootViewLayoutId;
    }

    protected final void setPopupSearchId(int popupSearchId) {
        this.popupSearchId = popupSearchId;
    }

    protected final void setPopupDataId(int popupDataId) {
        this.popupDataId = popupDataId;
    }

    protected final void setPopupTipId(int popupTipId) {
        this.popupTipId = popupTipId;
    }

    protected final void setPopupWindow(@NonNull PopupWindow popupWindow) {
        this.popupWindow = popupWindow;
    }

    protected final void setPopupSearchView(@NonNull EditText popupSearchView) {
        this.popupSearchView = popupSearchView;
    }

    protected final void setPopupDataView(@NonNull RecyclerView popupDataView) {
        this.popupDataView = popupDataView;
    }

    protected final void setPopupTipView(@NonNull TextView popupTipView) {
        this.popupTipView = popupTipView;
    }
    //protected set end===========================

    //public method start===========================
    public final void setTopPopupAnim(int topPopupAnim) {
        this.topPopupAnim = topPopupAnim;
    }

    public final void setBottomPopupAnim(int bottomPopupAnim) {
        this.bottomPopupAnim = bottomPopupAnim;
    }

    public final void setFilterModel(SpinnerFilterModel<T> filterModel) {
        this.filterModel = filterModel;
    }

    public final void setContentModel(SpinnerContentModel<T> contentModel) {
        this.contentModel = contentModel;
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

    public final <VH extends RecyclerView.ViewHolder> void setAdapter(BaseSpinnerAdapter<T, VH> adapter) {
        if (adapter == null) {
            return;
        }
        this.adapter = adapter;
        adapter.setList(list);
        adapter.setOnItemClickListener(onItemClickListener);
        adapter.setSelectedPosition(selectIndex);
        adapter.setItemHeight(actualAdapterItemHeight);
        popupDataView.setAdapter(adapter);
    }

    public final <VH extends RecyclerView.ViewHolder> void setAdapter(@NonNull BaseSpinnerAdapter<T, VH> adapter, @NonNull List<T> list) {
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

    public final void setOnSelectedListener(OnSelectedListener onSelectedListener) {
        this.onSelectedListener = onSelectedListener;
    }

    /**
     * 返回SearchSpinner显示的文本的样式的控制器
     */
    @NonNull
    public final SearchSpinnerTextController getTextController() {
        return textController;
    }

    /**
     * 返回SearchSpinner三角形图标的控制器
     */
    @NonNull
    public final SearchSpinnerArrowController getArrowController() {
        return arrowController;
    }

    /**
     * 返回当搜索结果为空时的TextView的控制器
     */
    @NonNull
    public final SearchSpinnerTipController getTipController() {
        return tipController;
    }

    /**
     * 返回搜索框的控制器
     */
    @NonNull
    public final SearchSpinnerSearchController getSearchController() {
        return searchController;
    }

    //public method end===========================

    public interface OnSelectedListener {
        void onSelected(int position);
    }

    protected static final class PositionInfo {
        boolean isTop;
        float popupHeight;

        protected PositionInfo() {
        }

        protected PositionInfo setValue(boolean isTop, float popupHeight) {
            this.isTop = isTop;
            this.popupHeight = popupHeight;
            return this;
        }
    }
}