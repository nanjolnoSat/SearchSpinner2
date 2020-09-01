package com.mishaki.searchspinner2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 杜壁奇<br/>
 * on 2020/09/01 14:01
 */
public class ListUtil {
    public static List<Integer> range(int start, int length) {
        List<Integer> list = new ArrayList<>();
        for (int i = start; i < start + length + 1; i++) {
            list.add(i);
        }
        return list;
    }

    public static <T1, T2> void mapTo(List<T1> srcList, List<T2> destList, Action1<T1, T2> action) {
        for (T1 t1 : srcList) {
            destList.add(action.action(t1));
        }
    }

    public interface Action1<T, R> {
        R action(T t);
    }
}
