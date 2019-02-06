package com.mishaki.searchspinner2.view

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.*
import android.widget.*
import com.mishaki.searchspinner2.R
import com.mishaki.searchspinner2.adapter.BaseSpinnerAdapter
import com.mishaki.searchspinner2.mode.SpinnerContentMode
import com.mishaki.searchspinner2.mode.SpinnerFilterMode
import com.mishaki.searchspinner2.utils.gone
import com.mishaki.searchspinner2.utils.isEmpty
import com.mishaki.searchspinner2.utils.show
import org.jetbrains.anko.*

open class SearchSpinner<T> : LinearLayout, View.OnClickListener {
    lateinit var textView: TextView
        protected set
    lateinit var imageView: ImageView
        protected set

    protected var popupRootViewLayoutId = 0
    protected var popupSearchId = 0
    protected var popupDataId = 0
    protected var popupTipId = 0

    private lateinit var popupWindow: PopupWindow
    private lateinit var popupSearchView: EditText
    private lateinit var popupDataView: ListView
    private lateinit var popupTipView: TextView

    private val screenHeight: Int
    private val statusBarHeight: Int
    private var list: MutableList<T> = ArrayList<T>()
    private var isSearch = false
    private val searchList = ArrayList<T>()

    var topPopupAnim = -1
    var bottomPopupAnim = -1

    var filterMode: SpinnerFilterMode<T>? = null
    var contentMode: SpinnerContentMode<T>? = null

    private var adapter: BaseSpinnerAdapter<T>? = null
        set(adapter) {
            if (adapter == null) {
                return
            }
            field = adapter
            field!!.list = list
            field!!.selectedPosition = selectIndex
            field!!.itemHeight = actualAdapterItemHeight
            popupDataView.adapter = field
        }

    var adapterItemHeight = -1f
        set(adapterItemHeight) {
            field = adapterItemHeight
            if (field > 0) {
                actualAdapterItemHeight = field
            }
        }
    var searchViewHeight = -1f
        set(searchViewHeight) {
            field = searchViewHeight
            if (field > 0) {
                actualSearchViewHeight = field
            }
        }
    var emptyTipViewHeight = -1f
        set(emptyTipViewHeight) {
            field = emptyTipViewHeight
            if (field > 0) {
                actualEmptyTipViewHeight = field
            }
        }

    //由于需要show才能拿到searchView和item的高度,又因为必须在show之前拿到他们的高度,所以只能规定
    //他们的高度
    private var actualAdapterItemHeight = 0f
        set(value) {
            field = value
            adapter?.itemHeight = field
        }
    private var actualSearchViewHeight = 0f
        set(value) {
            field = value
            popupSearchView.height = value.toInt()
        }
    private var actualEmptyTipViewHeight = 0f
        set(value) {
            field = value
            popupTipView.height = value.toInt()
        }

    //阴影的大小
    private val DEFAULT_ELEVATION_SIZE = 16F
    var elevationSize = DEFAULT_ELEVATION_SIZE

    var selectIndex = -1
        private set
    private var searchSelectIndex = -1
    private var searchContent = ""

    private var onSelectedListener: OnSelectedListener<T>? = null

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        var defaultText: String? = ""
        var textColor = 0xff000000.toInt()
        var textSize = -1f
        var showArrow = true
        var changeArrowColor = false
        var arrowColor = 0xffaaaaaa.toInt()
        var arrowImage: Drawable? = null
        var arrowWidth = -1f
        var arrowHeight = -1f
        var tipText: String? = ""
        var tipTextColor = 0xff000000.toInt()
        var tipTextSize = -1f
        var tipViewHeight = -1f
        var searchTextSize = -1f
        var searchTextColor = 0xff000000.toInt()
        var searchHint: String? = ""
        var searchHintColor = 0xfffafafa.toInt()
        var searchViewHeight = -1f
        if (attrs != null) {
            val array = context.obtainStyledAttributes(attrs, R.styleable.SearchSpinner)

            adapterItemHeight = array.getDimension(R.styleable.SearchSpinner_ss_adapterItemHeight, -1f)
            elevationSize = array.getDimension(R.styleable.SearchSpinner_ss_elevationSize, DEFAULT_ELEVATION_SIZE)
            defaultText = array.getString(R.styleable.SearchSpinner_ss_defaultText)
            textColor = array.getColor(R.styleable.SearchSpinner_ss_textColor, 0xff000000.toInt())
            textSize = array.getDimension(R.styleable.SearchSpinner_ss_textSize, -1f)
            showArrow = array.getBoolean(R.styleable.SearchSpinner_ss_showArrow, true)
            changeArrowColor = array.getBoolean(R.styleable.SearchSpinner_ss_changeArrowColor, false)
            arrowColor = array.getColor(R.styleable.SearchSpinner_ss_arrowColor, 0xffaaaaaa.toInt())
            arrowImage = array.getDrawable(R.styleable.SearchSpinner_ss_arrowImage)
            arrowWidth = array.getDimension(R.styleable.SearchSpinner_ss_arrowWidth, -1f)
            arrowHeight = array.getDimension(R.styleable.SearchSpinner_ss_arrowHeight, -1f)
            tipText = array.getString(R.styleable.SearchSpinner_ss_tipText)
            tipTextColor = array.getColor(R.styleable.SearchSpinner_ss_tipTextColor, 0xff000000.toInt())
            tipTextSize = array.getDimension(R.styleable.SearchSpinner_ss_tipTextSize, -1f)
            tipViewHeight = array.getDimension(R.styleable.SearchSpinner_ss_tipViewHeight, -1f)
            searchTextSize = array.getDimension(R.styleable.SearchSpinner_ss_searchTextSize, -1f)
            searchTextColor = array.getColor(R.styleable.SearchSpinner_ss_searchTextColor, 0xff000000.toInt())
            searchHint = array.getString(R.styleable.SearchSpinner_ss_searchHint)
            searchHintColor = array.getColor(R.styleable.SearchSpinner_ss_searchHintColor, 0xfffafafa.toInt())
            searchViewHeight = array.getDimension(R.styleable.SearchSpinner_ss_tipViewHeight, -1f)

            array.recycle()
        }

        super.setOrientation(LinearLayout.HORIZONTAL)
        initRootView(defaultText, textColor, textSize, showArrow, changeArrowColor, arrowColor, arrowImage, arrowWidth, arrowHeight)
        initResValue()
        initPopupWindow()
        initTipView(tipText, tipTextColor, tipTextSize, tipViewHeight)
        initSearchView(searchTextSize, searchTextColor, searchHint, searchHintColor, searchViewHeight)

        popupSearchView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                isSearch = s.isNotEmpty()
                searchContent = s.toString()
                val popupWindowHeight: Int
                if (isSearch) {
                    searchList.clear()
                    //filterMode没有赋值这个View没有存在的意义,所以直接空指针
                    searchList.addAll(list.filter { filterMode!!.filterMode(searchContent, it) })
                    if (searchList.contains(list[selectIndex])) {
                        val content = list[selectIndex]
                        var count1 = -1
                        for (i in 0 until list.size) {
                            if (list[i] == content) {
                                count1++
                            }
                            if (i == selectIndex) {
                                break
                            }
                        }
                        var count2 = -1
                        for (i in 0 until searchList.size) {
                            if (searchList[i] == content) {
                                count2++
                            }
                            if (count1 == count2) {
                                searchSelectIndex = i
                                break
                            }
                        }
                    } else {
                        searchSelectIndex = -1
                        adapter?.selectedPosition = -1
                    }
                    if (searchList.isEmpty()) {
                        popupDataView.gone()
                        popupTipView.show()
                        popupWindowHeight = (actualSearchViewHeight + dip(2f) + actualEmptyTipViewHeight).toInt()
                    } else {
                        popupTipView.gone()
                        popupDataView.show()
                        adapter?.setItemInfo(searchContent, searchList)
                        adapter?.notifyDataSetChanged()
                        popupWindowHeight = getPopupSearchHeight(searchList).toInt()
                        if (searchSelectIndex != -1) {
                            popupDataView.setSelection(searchSelectIndex)
                            adapter?.selectedPosition = searchSelectIndex
                        }
                    }
                } else {
                    popupTipView.gone()
                    popupDataView.show()
                    adapter?.setItemInfo(searchContent, list)
                    adapter?.notifyDataSetChanged()
                    if (selectIndex != -1) {
                        popupDataView.setSelection(selectIndex)
                        adapter?.selectedPosition = selectIndex
                    }
                    popupWindowHeight = getPopupSearchHeight(list).toInt()
                }
                if (isTop) {
                    popupWindow.update(left, y - popupWindowHeight, -1, popupWindowHeight)
                } else {
                    popupWindow.update(-1, popupWindowHeight)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
        })
        popupDataView.setOnItemClickListener { _, _, position, _ ->
            if (isSearch) {
                val text: String
                if (contentMode == null) {
                    text = searchList[position].toString()
                } else {
                    text = contentMode!!.getContent(searchList[position])
                }
                textView.text = text
                searchSelectIndex = position
                adapter?.selectedPosition = searchSelectIndex
                var index = -1
                for (i in 0 until list.size) {
                    //filterMode没有赋值这个View没有存在的意义,所以直接空指针
                    if (filterMode!!.filterMode(searchContent, list[i])) {
                        index++
                    }
                    if (index == searchSelectIndex) {
                        selectIndex = i
                        break
                    }
                }
                onSelectedListener?.onSelected(this, selectIndex)
            } else {
                searchSelectIndex = -1
                textView.text = contentMode?.getContent(list[position]) ?: list[position].toString()
                selectIndex = position
                adapter?.selectedPosition = selectIndex
                onSelectedListener?.onSelected(this, selectIndex)
            }
            popupWindow.dismiss()
        }

        statusBarHeight = getStatusBarHeight()
        screenHeight = context.resources.displayMetrics.heightPixels
        popupDataView.adapter = adapter
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.elevation = elevationSize
        }
        super.setOnClickListener(this)
    }

    protected open fun initRootView(defaultText: String?, textColor: Int, textSize: Float, showArrow: Boolean, changeArrowColor: Boolean, arrowColor: Int, arrowImage: Drawable?, arrowWidth: Float, arrowHeight: Float) {
        textView = TextView(context)
        textView.gravity = Gravity.CENTER_VERTICAL
        //最大行数必须只能为1行
        textView.maxLines = 1
        //当没有在layout设置文本大小的时候,就设置15sp
        if (textSize == -1f) {
            textView.textSize = 15f
        } else {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        }
        textView.textColor = textColor
        //设置结果 ...
        textView.ellipsize = TextUtils.TruncateAt.END
        //默认的文本,没有设置则为空
        textView.text = defaultText
        val param1 = generateDefaultLayoutParams() as LinearLayout.LayoutParams
        param1.gravity = Gravity.CENTER_VERTICAL
        //ImageView用剩下的都给TextView用
        param1.weight = 1f
        textView.layoutParams = param1
        super.addView(textView)

        imageView = ImageView(context)
        if (arrowImage != null) {
            imageView.setImageDrawable(arrowImage)
        } else {
            imageView.setImageResource(R.drawable.search_down)
        }
        //如果不喜欢三角形的颜色,可以在layout文件通过attr修改
        if (changeArrowColor) {
            imageView.colorFilter = PorterDuffColorFilter(arrowColor, PorterDuff.Mode.SRC_IN)
        }
        val param2: LinearLayout.LayoutParams
        //宽高都没有指定size的时候,为10dp,只有一个没有指定那一个用ViewGroup.LayoutParams.WRAP_CONTENT
        when {
            arrowWidth != -1f && arrowHeight != -1f -> param2 = LinearLayout.LayoutParams(arrowWidth.toInt(), arrowHeight.toInt())
            arrowWidth != -1f -> param2 = LinearLayout.LayoutParams(arrowWidth.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
            arrowHeight != -1f -> param2 = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, arrowHeight.toInt())
            else -> param2 = LinearLayout.LayoutParams(dip(10), dip(10))
        }
        param2.gravity = Gravity.CENTER_VERTICAL
        param2.marginStart = dip(2.5f)
        param2.marginEnd = dip(2.5f)
        imageView.layoutParams = param2
        imageView.adjustViewBounds = true
        super.addView(imageView)
        if (!showArrow) {
            imageView.gone()
        }
    }

    /**
     * 当对PopupWindow的布局不喜欢的话可以重写该方法指定
     */
    protected open fun initResValue() {
        popupRootViewLayoutId = R.layout.popup_search_spinner
        popupSearchId = R.id.popup_search_spinner_et
        popupDataId = R.id.popup_search_spinner_lv
        popupTipId = R.id.popup_search_spinner_tv
    }

    protected open fun initPopupWindow() {
        popupWindow = PopupWindow(context)
        popupWindow.isFocusable = true
        popupWindow.isOutsideTouchable = true
        //设置白色背景
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.popup_search_spinner))
        try {
            //禁止输入法影响屏幕的高度,否则会导致PopupWindow显示的位置不准确
            popupWindow.inputMethodMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING
            (context as Activity).window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val popupRootView = LayoutInflater.from(context).inflate(popupRootViewLayoutId, null)
        popupSearchView = popupRootView.findViewById(popupSearchId)
        popupDataView = popupRootView.findViewById(popupDataId)
        popupTipView = popupRootView.findViewById(popupTipId)
        popupWindow.contentView = popupRootView
        popupWindow.setOnDismissListener {
            //当PopupWindow关闭的时候,让三角形旋转回来
            animateArrow(true)
        }
    }

    protected open fun initTipView(tipText: String?, tipTextColor: Int, tipTextSize: Float, tipViewHeight: Float) {
        popupTipView.gravity = Gravity.CENTER
        if (!tipText.isEmpty()) {
            popupTipView.text = tipText
        }
        popupTipView.textColor = tipTextColor
        if (tipTextSize == -1f) {
            popupTipView.textSize = 15f
        } else {
            popupTipView.setTextSize(TypedValue.COMPLEX_UNIT_PX, tipTextSize)
        }
        if (tipViewHeight != -1f) {
            popupTipView.height = tipViewHeight.toInt()
        }
    }

    protected open fun initSearchView(searchTextSize: Float, searchTextColor: Int, searchHint: String?, searchHintColor: Int, searchViewHeight: Float) {
        popupSearchView.gravity = Gravity.CENTER_VERTICAL
        if (searchTextSize == -1f) {
            popupSearchView.textSize = 15f
        } else {
            popupSearchView.setTextSize(TypedValue.COMPLEX_UNIT_PX, searchTextSize)
        }
        popupSearchView.textColor = searchTextColor
        if (!searchHint.isEmpty()) {
            popupSearchView.hint = searchHint
        }
        popupSearchView.hintTextColor = searchHintColor
        if (searchViewHeight != -1f) {
            popupSearchView.height = searchViewHeight.toInt()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        popupWindow.width = w
        if (searchViewHeight == -1f) {
            actualSearchViewHeight = h.toFloat()
        } else {
            actualSearchViewHeight = searchViewHeight
        }
        if (adapterItemHeight == -1f) {
            actualAdapterItemHeight = h.toFloat()
        } else {
            actualAdapterItemHeight = adapterItemHeight
        }
        if (emptyTipViewHeight == -1f) {
            actualEmptyTipViewHeight = h.toFloat()
        } else {
            actualEmptyTipViewHeight = emptyTipViewHeight
        }
    }

    override fun addView(child: View?) = Unit

    private var y = 0
    private var isTop = false
    override fun onClick(v: View?) {
        if (adapter == null) {
            return
        }
        val point = IntArray(2)
        getLocationOnScreen(point)
        y = point[1]

        animateArrow(false)
        removeRule(popupDataView)
        removeRule(popupSearchView)
        removeRule(popupTipView)
        val positionInfo: PositionInfo
        if (isSearch) {
            positionInfo = getPositionInfo(searchList)
        } else {
            positionInfo = getPositionInfo(list)
        }
        popupWindow.height = positionInfo.height.toInt()
        //在弹出的时候就记录,弹出的位置
        isTop = positionInfo.isTop
        if (positionInfo.isTop) {
            if (topPopupAnim != -1) {
                popupWindow.animationStyle = topPopupAnim
            }
            val param1 = popupSearchView.layoutParams as RelativeLayout.LayoutParams
            param1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)

            val param2 = popupDataView.layoutParams as RelativeLayout.LayoutParams
            param2.addRule(RelativeLayout.ABOVE, popupSearchId)

            val param3 = popupTipView.layoutParams as RelativeLayout.LayoutParams
            param3.addRule(RelativeLayout.ABOVE, popupSearchId)

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                popupWindow.showAsDropDown(this, 0, 0, Gravity.BOTTOM or Gravity.START)
            } else {
                popupWindow.showAtLocation(this, Gravity.TOP or Gravity.START, 0, y - popupWindow.height)
            }
        } else {
            if (bottomPopupAnim != -1) {
                popupWindow.animationStyle = bottomPopupAnim
            }
            val param1 = popupDataView.layoutParams as RelativeLayout.LayoutParams
            param1.addRule(RelativeLayout.BELOW, popupSearchId)

            val param2 = popupTipView.layoutParams as RelativeLayout.LayoutParams
            param2.addRule(RelativeLayout.BELOW, popupSearchId)

            popupWindow.showAsDropDown(this)
        }
    }

    private fun removeRule(view: View) {
        val param = view.layoutParams as RelativeLayout.LayoutParams
        param.addRule(RelativeLayout.BELOW, 0)
        param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0)
        param.addRule(RelativeLayout.ABOVE, 0)
    }

    fun setAdapter(adapter: BaseSpinnerAdapter<T>, list: MutableList<T>) {
        if (list.isNotEmpty()) {
            selectIndex = 0
            textView.text = contentMode?.getContent(list[0]) ?: list[0].toString()
        }
        this.adapter = adapter
        this.list = list
        this.adapter!!.list = list
        this.adapter!!.selectedPosition = selectIndex
        this.adapter!!.itemHeight = actualAdapterItemHeight
        popupDataView.adapter = this.adapter!!
    }

    fun setOnSelectedListener(onSelectedListener: OnSelectedListener<T>) {
        this.onSelectedListener = onSelectedListener
    }

    fun setText(text: CharSequence) {
        textView.text = text
    }

    fun setText(resId: Int) {
        textView.setText(resId)
    }

    fun setTextColor(color: Int) {
        textView.textColor = color
    }

    fun setTextSize(sp: Float) {
        textView.textSize = sp
    }

    fun setTextSize(unit: Int, size: Float) {
        textView.setTextSize(unit, size)
    }

    fun setTextBackground(resId: Int) {
        textView.setBackgroundResource(resId)
    }

    fun setTextBackgroundColor(color: Int) {
        textView.setBackgroundColor(color)
    }

    fun setTextGravity(gravity: Int) {
        textView.gravity = gravity
    }

    fun setTextPaddingLeft(paddingLeft: Float) {
        textView.leftPadding = paddingLeft.toInt()
    }

    fun setTextPaddingTop(paddingTop: Float) {
        textView.topPadding = paddingTop.toInt()
    }

    fun setTextPaddingRight(paddingRight: Float) {
        textView.rightPadding = paddingRight.toInt()
    }

    fun setTextPaddingBottom(paddingBottom: Float) {
        textView.bottomPadding = paddingBottom.toInt()
    }

    fun setTextBackground(drawable: Drawable) {
        textView.setBackgroundDrawable(drawable)
    }

    fun showArrow() {
        imageView.show()
    }

    fun hideArrow() {
        imageView.gone()
    }

    fun setArrowColor(color: Int) {
        imageView.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
    }

    fun setArrowImage(drawable: Drawable) {
        imageView.setImageDrawable(drawable)
    }

    fun setArrowImage(resId: Int) {
        imageView.setImageResource(resId)
    }

    fun setArrowWidth(width: Float) {
        val param = imageView.layoutParams
        param.width = width.toInt()
        imageView.layoutParams = param
    }

    fun setArrowHeight(height: Float) {
        val param = imageView.layoutParams
        param.height = height.toInt()
        imageView.layoutParams = param
    }

    fun setArrowSize(width: Float, height: Float) {
        val param = imageView.layoutParams
        param.width = width.toInt()
        param.height = height.toInt()
        imageView.layoutParams = param
    }

    fun setTipText(text: CharSequence) {
        popupTipView.text = text
    }

    fun setTipText(resId: Int) {
        popupTipView.setText(resId)
    }

    fun setTipTextColor(textColor: Int) {
        popupTipView.textColor = textColor
    }

    fun setTipTextSize(sp: Float) {
        popupTipView.textSize = sp
    }

    fun setTipTextSize(unit: Int, size: Float) {
        popupTipView.setTextSize(unit, size)
    }

    fun setTipViewGravity(gravity: Int) {
        popupTipView.gravity = gravity
    }

    fun setTipViewPaddingLeft(paddingLeft: Float) {
        popupTipView.leftPadding = paddingLeft.toInt()
    }

    fun setTipViewPaddingTop(paddingTop: Float) {
        popupTipView.topPadding = paddingTop.toInt()
    }

    fun setTipViewPaddingRight(paddingRight: Float) {
        popupTipView.rightPadding = paddingRight.toInt()
    }

    fun setTipViewPaddingBottom(paddingBottom: Float) {
        popupTipView.bottomPadding = paddingBottom.toInt()
    }

    fun setTipViewBackground(resId: Int) {
        popupTipView.setBackgroundResource(resId)
    }

    fun setTipViewBackgroundDrawable(drawable: Drawable) {
        popupTipView.setBackgroundDrawable(drawable)
    }

    fun setTipViewBackgroundColor(color: Int) {
        popupTipView.setBackgroundColor(color)
    }

    fun setSearchViewTextSize(sp: Float) {
        popupSearchView.textSize = sp
    }

    fun setSearchViewTextSize(unit: Int, size: Float) {
        popupSearchView.setTextSize(unit, size)
    }

    fun setSearchViewTextColor(color: Int) {
        popupSearchView.textColor = color
    }

    fun setSearchViewHint(text: String) {
        popupSearchView.hint = text
    }

    fun setSearchViewHintColor(color: Int) {
        popupSearchView.hintTextColor = color
    }

    fun setSearchViewBackground(resId: Int) {
        popupSearchView.setBackgroundResource(resId)
    }

    fun setSearchViewBackground(drawable: Drawable) {
        popupSearchView.setBackgroundDrawable(drawable)
    }

    fun setSearchViewBackgroundColor(color: Int) {
        popupSearchView.setBackgroundColor(color)
    }

    //获取状态栏的高度
    private fun getStatusBarHeight(): Int {
        val resourceId = Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            return Resources.getSystem().getDimensionPixelSize(resourceId)
        }
        return 0
    }

    protected open fun getPositionInfo(list: MutableList<T>): PositionInfo {
        val popupMaxHeight = getPopupMaxHeight(list)
        val bottomHeight = screenHeight - y - height
        //如果下面够显示,显示在下面
        if (bottomHeight > popupMaxHeight + elevationSize) {
            return PositionInfo(false, popupMaxHeight)
        }
        //如果上面够显示,显示在上面
        val topHeight = y - statusBarHeight
        if (topHeight > popupMaxHeight + elevationSize) {
            return PositionInfo(true, popupMaxHeight)
        }
        //如果都不够
        val isTop = topHeight > bottomHeight
        val popupHeight: Float
        //如果上面大,上面的高度-阴影高度
        if (isTop) {
            popupHeight = topHeight - elevationSize
        } else {
            //如果下面大,下面的高度-阴影高度
            popupHeight = bottomHeight - elevationSize
        }
        return PositionInfo(isTop, popupHeight)
    }

    protected open fun getPopupMaxHeight(list: MutableList<T>): Float {
        return actualSearchViewHeight + list.size * actualAdapterItemHeight + dip(2f)
    }

    /**
     * 获取PopupWindow在search状态下的高度,不是使用searchList这个变量计算高度
     */
    protected open fun getPopupSearchHeight(list: MutableList<T>): Float {
        val height: Float
        val maxHeight = getPopupMaxHeight(list)
        if (isTop) {
            if (maxHeight < y - statusBarHeight) {
                height = maxHeight
            } else {
                height = y - statusBarHeight - elevationSize
            }
        } else {
            if (screenHeight - y > maxHeight) {
                height = maxHeight
            } else {
                height = screenHeight - y - this.height - elevationSize
            }
        }
        return height
    }

    protected class PositionInfo(val isTop: Boolean, val height: Float)

    private fun animateArrow(isRelease: Boolean) {
        if (isRelease) {
            imageView.animate().rotation(0f).start()
        } else {
            imageView.animate().rotation(180f).start()
        }
    }

    override fun setOrientation(orientation: Int) = Unit


    interface OnSelectedListener<T> {
        fun onSelected(spinner: SearchSpinner<T>, position: Int)
    }
}