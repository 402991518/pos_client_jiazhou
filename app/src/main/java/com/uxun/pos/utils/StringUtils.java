package com.uxun.pos.utils;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {

    public static final String NULL = "";
    public static final char BLACK_SPACE = (char) 0x0020;
    public static final char CHINESE_ENGLISH_SEPARATOR = (char) 0x00FF;

    // 文本超过长度则折行并居中,剔除文本传中所有ASSIC控制字符,如果折行的数据小于length则两段平均填充空格
    public static List<String> center(String text, int length) {
        List<String> list = new ArrayList<>();
        if (text == null || NULL.equals(text.trim()) || length <= 0) {
            return list;
        }
        List<String> temp = new ArrayList<>();
        text = text.trim();
        char[] chars = text.toCharArray();
        StringBuilder stringBuilder = null;
        int count = 0;
        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];
            if (ch <= BLACK_SPACE) {
                continue;
            } else {
                if (ch >= CHINESE_ENGLISH_SEPARATOR) {
                    count += 2;
                } else {
                    count += 1;
                }
                if (stringBuilder == null) {
                    stringBuilder = new StringBuilder();
                }
                stringBuilder.append(ch);
                if (count >= length) {
                    temp.add(stringBuilder.toString());
                    stringBuilder = null;
                    count = 0;
                }
            }
        }
        if (stringBuilder != null) {
            temp.add(stringBuilder.toString());
        }

        for (int i = 0; i < temp.size(); i++) {
            String str = temp.get(i);
            int strLength = textLength(str);
            if (strLength > 0) {
                int avg = (length - strLength) / 2;
                for (int j = 0; j < avg; j++) {
                    str = (BLACK_SPACE) + str + (BLACK_SPACE);
                }
                strLength = textLength(str);
                int sub = length - strLength;
                for (int j = 0; j < sub; j++) {
                    str += BLACK_SPACE;
                }
            }
            list.add(str);
        }
        return list;
    }

    // 返回收银员:9999 店号:02 机号 205，返回数据的长度 等于length，不足部分填充空格
    private static String textCasherOrgcodePosno(String casher, String posno, int length) {
        casher = "收银员" + ":" + (casher == null ? "" : casher.trim());
        posno = "机号" + ":" + ((posno == null ? "" : posno.trim()));
        int casher_length = textLength(casher);
        int orgcode_length = textLength(posno);
        int delte = length - (casher_length + orgcode_length);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(casher);
        while (delte > 0) {
            stringBuilder.append(BLACK_SPACE);
            delte--;
        }
        stringBuilder.append(posno);
        return stringBuilder.toString();
    }



    public static String textCasherOrgcodePosno(String suffix_casher, String suffix_orgcode, String suffix_posno, int length) {
        return textCasherOrgcodePosno(suffix_casher, suffix_posno, length);
    }


    // 获取文本长度，ASSIC单个长度为1 否则长度为2
    public static int textLength(String text) {
        if (text == null) {
            return -1;
        }
        int length = 0;
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];
            if (ch >= CHINESE_ENGLISH_SEPARATOR) {
                length += 2;
            } else {
                length += 1;
            }
        }
        return length;
    }
}
