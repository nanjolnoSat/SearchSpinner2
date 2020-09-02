package com.mishaki.libsearchspinner.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by 杜壁奇<br/>
 * on 2020/09/02 11:06
 */
class Util {
    static int dip(Context context,float value) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics()) + 0.5f);
    }

    static boolean stringIsNotEmpty(CharSequence value) {
        return value != null && value.length() != 0;
    }

    static boolean isEquals(Object obj1, Object obj2) {
        return obj1 == obj2 || obj1.equals(obj2);
    }

    static int getStatusBarHeight() {
        int resourceId = Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return Resources.getSystem().getDimensionPixelSize(resourceId);
        }
        return 0;
    }
}
