package com.mishaki.searchspinner2.view

import android.content.Context
import android.util.AttributeSet
import com.mishaki.searchspinner2.mode.SpinnerContentMode
import com.mishaki.searchspinner2.mode.SpinnerFilterMode

class StringSearchSpinner : SearchSpinner<String> {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        filterMode = object : SpinnerFilterMode<String> {
            override fun filterMode(searchContent: String, t: String): Boolean {
                return t.contains(searchContent)
            }
        }
        contentMode = object : SpinnerContentMode<String> {
            override fun getContent(t: String): String {
                return t
            }
        }
    }
}