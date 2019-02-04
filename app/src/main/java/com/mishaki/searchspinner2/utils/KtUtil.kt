package com.mishaki.searchspinner2.utils

import android.view.View

fun String?.isEmpty(): Boolean {
    if (this == null) {
        return true
    }
    return trim().length == 0
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.setSize(width: Int, height: Int) {
    val param = layoutParams
    param.width = width
    param.height = height
    layoutParams = param
}