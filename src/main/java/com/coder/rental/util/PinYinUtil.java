package com.coder.rental.util;

import cn.hutool.extra.pinyin.PinyinUtil;

public class PinYinUtil {
    public static String getFirstLetter(String str) {
        return PinyinUtil.getFirstLetter(str, "").toUpperCase();
    }
}
