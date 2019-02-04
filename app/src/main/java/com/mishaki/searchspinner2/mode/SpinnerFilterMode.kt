package com.mishaki.searchspinner2.mode

interface SpinnerFilterMode<T> {
    /**
     * 根据输入的内容和遍历到的对象设置是否过滤的
     * @return true不过滤掉,false过滤掉
     */
    fun filterMode(searchContent:String, t: T): Boolean
}