package com.mishaki.libsearchspinner.model;

/**
 * Created by 杜壁奇
 * on 2020/08/30 21:51
 */
public interface SpinnerFilterModel<T> {
    /**
     * 根据输入的内容和遍历到的对象设置是否过滤的
     *
     * @return true不过滤掉, false过滤掉
     */
    boolean filterModel(String searchContent, T t);
}
