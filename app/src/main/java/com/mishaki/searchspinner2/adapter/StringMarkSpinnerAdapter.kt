package com.mishaki.searchspinner2.adapter

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.jetbrains.anko.leftPadding
import org.jetbrains.anko.textColor

class StringMarkSpinnerAdapter : BaseSpinnerAdapter<String>() {
    var matchedColor = 0xffff0000.toInt()
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
            it.leftPadding = 4
        }
        return view
    }

    override fun getSearchView(position: Int, convertView: View?, parent: ViewGroup, searchText: String): View {
        val view: View
        if (convertView == null) {
            view = TextView(parent.context)
        } else {
            view = convertView
        }
        (view as TextView).also {
            val index = getItem(position).indexOf(searchText)
            if (index != -1) {
                val sss = SpannableString(getItem(position))
                val color = ForegroundColorSpan(matchedColor)
                sss.setSpan(color, index, index + searchText.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                it.text = sss
            } else {
                it.text = getItem(position)
            }
            it.textColor = 0xff000000.toInt()
            it.textSize = 15f
            it.gravity = Gravity.CENTER_VERTICAL
            it.leftPadding = 4
        }
        return view
    }
}
