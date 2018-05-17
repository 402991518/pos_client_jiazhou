package com.uxun.pos.utils;

public class CamelUtils {
    /**
     * 驼峰法转下划线
     *
     * @param line 源字符串
     * @return 转换后的字符串
     */
    public static String camel2Underline(String line) {
        if (line == null || "".equals(line)) {
            return "";
        }
        char[] chs = line.toCharArray();
        char ch = chs[0];
        if (Character.isUpperCase(ch)) {
            ch = Character.toLowerCase(ch);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ch);
        for (int i = 1; i < chs.length; i++) {
            ch = chs[i];
            if (Character.isUpperCase(ch)) {
                ch = Character.toLowerCase(ch);
                stringBuilder.append('_');
            }
            stringBuilder.append(ch);
        }
        return stringBuilder.toString().toLowerCase();
    }
}
