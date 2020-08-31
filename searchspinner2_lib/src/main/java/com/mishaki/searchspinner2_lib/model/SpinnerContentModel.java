package com.mishaki.searchspinner2_lib.model;

/**
 * Created by 杜壁奇
 * on 2020/08/30 21:52
 */
public interface SpinnerContentModel<T> {
    /**
     * 根据选择到的对象返回想要显示的内容
     */
    String getContent(T t);
}