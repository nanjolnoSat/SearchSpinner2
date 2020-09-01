package com.mishaki.libsearchspinner.view;

import java.util.List;

/**
 * Created by 杜壁奇<br/>
 * on 2020/08/31 14:30
 */
class ListUtil {
    static <T> void filterTo(List<T> srcList, List<T> destList, Condition<T, Boolean> condition) {
        for (T t : srcList) {
            if (condition.condition(t)) {
                destList.add(t);
            }
        }
    }

    interface Condition<T, R> {
        R condition(T t);
    }
}
