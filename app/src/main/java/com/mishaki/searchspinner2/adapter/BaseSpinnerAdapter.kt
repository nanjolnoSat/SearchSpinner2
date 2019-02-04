package com.mishaki.searchspinner2.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter

abstract class BaseSpinnerAdapter<T> : BaseAdapter() {
    internal var list: MutableList<T> = ArrayList<T>()
    private var searchText = ""

    //要显示PopupWindow需要知道item的高度,要知道item的高度需要显示PopupWindow,这样就产生了悖论了
    //所以只能规定item的高度
    internal var itemHeight = 0f
        set(itemHeight) {
            field = itemHeight
            notifyDataSetChanged()
        }

    var isShowSelectColor = true
        set(isShowSelectColor) {
            if (isShowSelectColor == field) {
                return
            }
            field = isShowSelectColor
            notifyDataSetChanged()
        }
    var normalItemColor = 0xffffffff.toInt()
        set(normalItemColor) {
            if (normalItemColor == field) {
                return
            }
            field = normalItemColor
            notifyDataSetChanged()
        }
    var selectedItemColor = 0xffaaaaaa.toInt()
        set(selectedItemColor) {
            if (selectedItemColor == field) {
                return
            }
            field = selectedItemColor
            notifyDataSetChanged()
        }
    var selectedPosition = -1
        set(selectedPosition) {
            if (selectedPosition == field) {
                return
            }
            field = selectedPosition
            notifyDataSetChanged()
        }

    /**
     * 在想显示通过getSearchView方法拿到的view的时候调用,如果不想,可以不调用
     */
    fun setItemInfo(searchText: String, list: MutableList<T>) {
        this.searchText = searchText
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): T = list[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = list.size

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rootView: View
        if (searchText.length == 0) {
            rootView = getNormalView(position, convertView, parent)
        } else {
            rootView = getSearchView(position, convertView, parent,searchText)
        }
        if (isShowSelectColor) {
            if (selectedPosition == position) {
                rootView.setBackgroundColor(selectedItemColor)
            } else {
                rootView.setBackgroundColor(normalItemColor)
            }
        }
        val param :AbsListView.LayoutParams
        if (rootView.layoutParams == null){
            param = AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,itemHeight.toInt(),0)
        }else{
            param = rootView.layoutParams as AbsListView.LayoutParams
            param.height = itemHeight.toInt()
        }
        rootView.layoutParams = param
        return rootView
    }

    protected abstract fun getNormalView(position: Int, convertView: View?, parent: ViewGroup): View
    protected abstract fun getSearchView(position: Int, convertView: View?, parent: ViewGroup,searchText:String): View
}