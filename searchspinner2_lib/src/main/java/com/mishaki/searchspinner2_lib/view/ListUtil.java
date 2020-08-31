package com.mishaki.searchspinner2_lib.view;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 杜壁奇<br/>
 * on 2020/08/31 14:30
 */
class ListUtil {
    static <T> List<T> filter(List<T> list, Condition<T, Boolean> condition) {
        ArrayList<T> newList = new ArrayList<>(list.size());
        for (T t : list) {
            if (condition.condition(t)) {
                newList.add(t);
            }
        }
        newList.ensureCapacity(newList.size());
        return newList;
    }

    interface Condition<T, R> {
        R condition(T t);
    }

    interface Action<T> {
        void action(T t);
    }
}
