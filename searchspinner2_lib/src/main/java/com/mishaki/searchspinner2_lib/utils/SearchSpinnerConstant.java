package com.mishaki.searchspinner2_lib.utils;

/**
 * Created by 杜壁奇<br/>
 * on 2020/08/30 22:19
 */
public interface SearchSpinnerConstant {
    interface Color {
        int WHITE = 0xFFFFFFFF;
        int GRAY = 0xFFAAAAAA;
        int TEXT_HINT = 0xFFFAFAFA;
        int RED = 0xFFFF0000;
        int BLACK = 0xFF000000;
    }

    interface Dimens {
        int INT_DEFAULT = -1;
        float FLOAT_DEFAULT = -1f;
        float DEFAULT_TEXT_SIZE = 15f;
        float DEFAULT_ELEVATION_SIZE = 16f;
        float DEFAULT_ARROW_SIZE = 10f;
        float ARROW_MARGIN_HORIZONTAL = 2.5f;
    }

    interface Other {
        String STRING_EMPTY_VALUE = "";
        int NO_INDEX = -1;
    }
}
