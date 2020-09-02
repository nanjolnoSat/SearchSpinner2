package com.mishaki.libsearchspinner.controller;

import android.view.View;

/**
 * Created by 杜壁奇<br/>
 * on 2020/09/02 10:11
 */

class ControllerUtil {
    static void checkView(View view, String viewName) {
        if (view == null) {
            throw new NullPointerException(viewName + " is can not be null");
        }
    }
}