package com.mishaki.searchspinner2.adapter

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.jetbrains.anko.leftPadding
import org.jetbrains.anko.textColor

class StringSpinnerAdapter : BaseSpinnerAdapter<String>() {
    override fun getNormalView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        if (convertView == null) {
            view = TextView(parent.context)
        } else {
            view = convertView
        }
        (view as TextView).also {
            it.text = getItem(position)
            it.textColor = 0xff000000.toInt()
            it.textSize = 15f
            it.gravity = Gravity.CENTER_VERTICAL
            it.leftPadding = 10
        }
        return view
    }

    override fun getSearchView(position: Int, convertView: View?, parent: ViewGroup, searchText: String): View {
        return getNormalView(position, convertView, parent)
    }
}
