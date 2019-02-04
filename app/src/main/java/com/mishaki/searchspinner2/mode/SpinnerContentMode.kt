package com.mishaki.searchspinner2.mode

interface SpinnerContentMode<T> {
    /**
     * 根据选择到的对象返回想要显示的内容
     */
    fun getContent(t: T): String
}